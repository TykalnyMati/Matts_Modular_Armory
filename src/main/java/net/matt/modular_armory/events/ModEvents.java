package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.ModularSwordItem;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModEvents {

    // Detecting Xp collection
    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offhandItem = player.getOffhandItem();
        Iterable<ItemStack> equippedItems = player.getArmorSlots();

        int remainingXp = event.getAmount();

        // First repairs in main hand
        if (mainHandItem.getItem() instanceof ModularItemInterface modularItem) {
            remainingXp = modularItem.repairFromXp(mainHandItem, remainingXp);
        }

        // Repairs in offhand
        if (offhandItem.getItem() instanceof ModularItemInterface modularItem) {
            remainingXp = modularItem.repairFromXp(offhandItem, remainingXp);
        }

        // Repairs worn armor
        for (ItemStack equippedItem : equippedItems) {
            if (equippedItem.getItem() instanceof  ModularItemInterface modularItem) {
                remainingXp = modularItem.repairFromXp(equippedItem, remainingXp);
            }
        }


        // Reduce the amount of XP player gets
        event.setAmount(remainingXp);
    }

    // Canceling repairing in a grindstone
    @SubscribeEvent
    public static void onGrindstoneUpdate(GrindstoneEvent.OnPlaceItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (top.getItem() instanceof ModularItemInterface ||
                bottom.getItem() instanceof ModularItemInterface) {

            event.setCanceled(true);
        }
    }

    // Canceling repairing in an anvil
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ModularItemInterface && !event.getRight().isEmpty()) {
            event.setCanceled(true);
        }
    }

    // Canceling repairing in a crafting grid and anvil (it's messy)
    // This checks the player's open container every tick. If it's a crafting grid
    // and the result is a modular tool repair attempt, it wipes the output slot.
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            AbstractContainerMenu menu = event.player.containerMenu;

            // --- FIX FOR CRAFTING GRID ---
            if (menu instanceof CraftingMenu || menu instanceof InventoryMenu) {
                Slot resultSlot = menu.getSlot(0);
                if (resultSlot.getItem().getItem() instanceof ModularItemInterface) {
                    if (countModularTools(menu, menu instanceof InventoryMenu ? 1 : 1, menu instanceof InventoryMenu ? 5 : 10) >= 2) {
                        resultSlot.set(ItemStack.EMPTY);
                    }
                }
            }

            // --- FIX FOR ANVIL (The Safety Net) ---
            if (menu instanceof AnvilMenu anvil) {
                ItemStack left = anvil.getSlot(0).getItem();
                ItemStack right = anvil.getSlot(1).getItem();

                // If it's our tool and there is a "sacrifice" item in slot 1
                if (left.getItem() instanceof ModularItemInterface && !right.isEmpty()) {
                    // Force the output slot (Slot 2) to stay empty
                    anvil.getSlot(2).set(ItemStack.EMPTY);
                }

                // If our tool is the sacrifice
                if (right.getItem() instanceof ModularItemInterface) {
                    anvil.getSlot(2).set(ItemStack.EMPTY);
                }
            }
        }
    }

    private static int countModularTools(AbstractContainerMenu menu, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (menu.getSlot(i).getItem().getItem() instanceof ModularItemInterface) count++;
        }
        return count;
    }

    // Mobs drop Cooked items when a weapon has the Fire Aspect Augment
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        // Check if the killer is a living entity holding our modular sword
        if (attacker instanceof LivingEntity livingAttacker) {
            ItemStack weapon = livingAttacker.getMainHandItem();

            if (weapon.getItem() instanceof ModularSwordItem sword && !sword.isBroken(weapon)) {
                AugmentObject bonuses = sword.getAugmentBonuses(weapon);

                // If the weapon has Fire Aspect, convert meat drops to cooked versions
                if (bonuses.FIRE_ASPECT > 0) {
                    for (ItemEntity drop : event.getDrops()) {
                        ItemStack stack = drop.getItem();
                        ItemStack cooked = getCookedVariant(livingAttacker.level(), stack);

                        if (!cooked.isEmpty()) {
                            cooked.setCount(stack.getCount());
                            drop.setItem(cooked);
                        }
                    }
                }
            }
        }
    }

    // Helper to find the cooked version of a food item using the furnace recipes.
    private static ItemStack getCookedVariant(Level level, ItemStack rawStack) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(rawStack), level)
                .map(RecipeHolder::value)
                .map(recipe -> recipe.getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    // Looting augment handling
    @SubscribeEvent
    public static void onLootingLevel(LootingLevelEvent event) {
        // Verify the damage came from a living entity (the player)
        if (event.getDamageSource() != null && event.getDamageSource().getDirectEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            // Check if it's one of your modular tools and not broken
            if (weapon.getItem() instanceof ModularItemInterface modularTool && !modularTool.isBroken(weapon)) {
                AugmentObject bonuses = modularTool.getAugmentBonuses(weapon);

                // Add the augment level to the existing looting level
                if (bonuses.LOOTING > 0) {
                    event.setLootingLevel(event.getLootingLevel() + bonuses.LOOTING);
                }
            }
        }
    }

    // Fire Aspect augment handling
    @SubscribeEvent
    public static void applyModularFireAspect(LivingDamageEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player) {
            ItemStack weapon = player.getWeaponItem();

            // Check if it's a modular item
            if (weapon.getItem() instanceof ModularItemInterface modularItem && !modularItem.isBroken(weapon)) {
                AugmentObject bonuses = modularItem.getAugmentBonuses(weapon);

                // If it has Fire Aspect, ignite the entity(ies) being damaged
                if (bonuses.FIRE_ASPECT > 0) {
                    event.getEntity().igniteForSeconds(bonuses.FIRE_ASPECT * 4);
                }
            }
        }
    }
}