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
                remainingXp = modularItem.repairFromXp(stack, event.getEntity().level(), remainingXp);
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

            // Custom augments handling
            if (bonuses.VOID) {
                level.removeBlock(pos, false);
                return;
            }
            else if (bonuses.AUTO_SMELT && !bonuses.SILK_TOUCH) {
                ItemStack simulationTool = itemStack.copy();

                // Get item drops
                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, simulationTool)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos))
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.BLOCK_STATE, state);

                List<ItemStack> drops = state.getDrops(builder);

                int totalExp = state.getExpDrop(serverLevel, pos, null, player, itemStack);

                // Auto smelting
                for (int i = 0; i < drops.size(); i++) {
                    ItemStack originalStack = drops.get(i);
                    ItemStack smeltedResult = modularTool.getSmeltingResult(serverLevel, originalStack);

                    if (!smeltedResult.isEmpty()) {
                        // Keep the count from the original drop
                        ItemStack finalStack = smeltedResult.copyWithCount(originalStack.getCount() * smeltedResult.getCount());
                        drops.set(i, finalStack);

                        // Spawn fire particles
                        serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
                        modularTool.damageModularItem(itemStack, player, 4);
                    }
                }

                // Break the block
                level.removeBlock(pos, false);
                modularTool.damageModularItem(itemStack, player);

                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }

                if (totalExp > 0) {
                    state.getBlock().popExperience(serverLevel, pos, totalExp);
                }

                event.setCanceled(true);
            }
        }
    }
}