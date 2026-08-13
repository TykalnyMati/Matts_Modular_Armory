package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModEvents {

    // Detecting Xp collection
    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        int remainingXp = event.getAmount();

        if (remainingXp <= 0) {
            return;
        }

        List<ItemStack> repairCandidates = new ArrayList<>();

        repairCandidates.add(player.getMainHandItem());
        repairCandidates.add(player.getOffhandItem());
        for (ItemStack armorStack : player.getArmorSlots()) {
            repairCandidates.add(armorStack);
        }

        Collections.shuffle(repairCandidates);

        for (ItemStack stack : repairCandidates) {
            if (remainingXp <= 0) {
                break;
            }

            if (stack.getItem() instanceof ModularItemInterface modularItem) {
                remainingXp = modularItem.repairFromXp(stack, remainingXp);
            }
        }

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
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        AbstractContainerMenu menu = player.containerMenu;

        // --- FIX FOR CRAFTING GRID ---
        if (menu instanceof CraftingMenu || menu instanceof InventoryMenu) {
            Slot resultSlot = menu.getSlot(0);
            if (resultSlot.getItem().getItem() instanceof ModularItemInterface) {
                if (countModularTools(menu, menu instanceof InventoryMenu ? 1 : 1, menu instanceof InventoryMenu ? 5 : 10) >= 2) {
                    resultSlot.set(ItemStack.EMPTY);
                }
            }
        }

        // --- FIX FOR ANVIL ---
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

    private static int countModularTools(AbstractContainerMenu menu, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (menu.getSlot(i).getItem().getItem() instanceof ModularItemInterface) count++;
        }
        return count;
    }

    // Mobs drop more and cooked items when a weapon has the Fire Aspect Augment
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        // Check if the killer is a living entity holding our modular sword
        if (attacker instanceof LivingEntity livingAttacker) {
            ItemStack weapon = livingAttacker.getMainHandItem();

            if (weapon.getItem() instanceof ModularItemInterface sword && !sword.isBroken(weapon) && livingAttacker.level() instanceof ServerLevel serverLevel) {
                AugmentObject bonuses = sword.getAugmentBonuses(weapon);
                LivingEntity victim = event.getEntity();

                // Looting augment handling (kinda scuffed)
                if (bonuses.LOOTING > 0) {
                    // Saving the weapon's original enchantments
                    var originalEnchants = weapon.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

                    // Temporarily apply Looting to the REAL weapon
                    weapon.enchant(serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), bonuses.LOOTING);

                    // Build params. We DO NOT need the TOOL parameter for entities.
                    LootParams lootParams = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.THIS_ENTITY, victim)
                            .withParameter(LootContextParams.ORIGIN, victim.position())
                            .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, livingAttacker)
                            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, livingAttacker)
                            .create(LootContextParamSets.ENTITY);

                    // Roll the loot table
                    LootTable loottable = serverLevel.getServer().reloadableRegistries().getLootTable(victim.getLootTable());
                    List<ItemStack> newDrops = loottable.getRandomItems(lootParams);

                    // Restore the weapon to its original state IMMEDIATELY
                    weapon.set(DataComponents.ENCHANTMENTS, originalEnchants);

                    // Replace the drops
                    event.getDrops().clear();
                    for (ItemStack stack : newDrops) {
                        event.getDrops().add(new ItemEntity(serverLevel, victim.getX(), victim.getY(), victim.getZ(), stack));
                    }
                }

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

    // Fire Aspect augment handling
    @SubscribeEvent
    public static void applyModularFireAspect(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity entity) {
            ItemStack weapon = entity.getWeaponItem();

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

    // Mining augments handling
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player.isCreative()) {
            return;
        }

        ItemStack itemStack = player.getMainHandItem();

        if (itemStack.getItem() instanceof ModularGenericDiggerItem modularTool) {
            if (modularTool.isBroken(itemStack)) {
                return;
            }

            Level level = (Level) event.getLevel();
            if (level.isClientSide()) {
                return;
            }

            BlockPos pos = event.getPos();
            BlockState state = event.getState();
            ServerLevel serverLevel = (ServerLevel) level;

            if (!modularTool.isCorrectToolForDrops(itemStack, state) && state.requiresCorrectToolForDrops()) {
                return;
            }

            AugmentObject bonuses = modularTool.getAugmentBonuses(itemStack);

            // Only intercept if the tool has Silk touch, Fortune, Auto Smelt and the tool is correct
            if ((bonuses.SILK_TOUCH || bonuses.FORTUNE > 0 || bonuses.AUTO_SMELT) && !bonuses.VOID) {
                // Create Simulation Tool
                ItemStack simulationTool = itemStack.copy();
                if (bonuses.SILK_TOUCH) {
                    simulationTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
                } else {
                    simulationTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), bonuses.FORTUNE);
                }

                // Calculate Item Drops
                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, simulationTool)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos))
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.BLOCK_STATE, state);

                List<ItemStack> drops = state.getDrops(builder);

                // Handle Experience
                // If it's Silk Touch, skip entirely
                // If it's Fortune, manually spawn the XP
                if (!bonuses.SILK_TOUCH) {
                    int xp = state.getExpDrop(serverLevel, pos, level.getBlockEntity(pos), player, simulationTool);
                    if (xp > 0) {
                        state.getBlock().popExperience(serverLevel, pos, xp);
                    }
                }

                // Auto smelting
                if (bonuses.AUTO_SMELT && !bonuses.SILK_TOUCH) {
                    for (int i = 0; i < drops.size(); i++) {
                        ItemStack originalStack = drops.get(i);
                        ItemStack smeltedResult = modularTool.getSmeltingResult(serverLevel, originalStack);

                        if (!smeltedResult.isEmpty()) {
                            // Keep the count from the original drop
                            ItemStack finalStack = smeltedResult.copy();
                            finalStack.setCount(originalStack.getCount() * smeltedResult.getCount());
                            drops.set(i, finalStack);

                            // Spawning XP for the player
                            float smeltingXp = modularTool.getSmeltingExperience(serverLevel, originalStack);
                            if (smeltingXp > 0) {
                                int xpToDrop = (int) smeltingXp + (serverLevel.random.nextFloat() < (smeltingXp % 1) ? 1 : 0);

                                // Apply fortune for xp
                                if (bonuses.FORTUNE > 0) {
                                    xpToDrop *= bonuses.FORTUNE;
                                }

                                if (xpToDrop > 0) {
                                    state.getBlock().popExperience(serverLevel, pos, xpToDrop);
                                }
                            }

                            // Spawn fire particles to show smelting happened
                            serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
                            modularTool.damageModularItem(itemStack, player);
                        }
                    }
                }
                // Break the block
                level.removeBlock(pos, false);
                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }

                modularTool.damageModularItem(itemStack, player);
            } else if (bonuses.VOID) {
                level.removeBlock(pos, false);
            }
        }
    }
}