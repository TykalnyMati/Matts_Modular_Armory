package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ModularShearsItem extends ShearsItem implements ModularItemInterface {
    public ModularShearsItem(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        damageModularItem(stack, entity, amount);
        return 0;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (isBroken(stack)) {
            return false;
        }

        if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
            stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
        }

        return state.is(BlockTags.LEAVES)
                || state.is(Blocks.COBWEB)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.DEAD_BUSH)
                || state.is(Blocks.HANGING_ROOTS)
                || state.is(Blocks.VINE)
                || state.is(Blocks.TRIPWIRE)
                || state.is(BlockTags.WOOL);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (isBroken(stack)) {
            return InteractionResult.PASS;
        }

        if (entity instanceof IShearable target) {
            BlockPos pos = entity.blockPosition();
            boolean isClient = entity.level().isClientSide();
            // Check isShearable on both sides (mirrors vanilla readyForShearing())
            if (target.isShearable(player, stack, entity.level(), pos)) {
                // Call onSheared on both sides (mirrors vanilla shear())
                List<ItemStack> drops = target.onSheared(player, stack, entity.level(), pos);
                // Spawn drops on the server side using spawnShearedDrop to retain vanilla mob-specific behavior
                if (!isClient) {
                    for(ItemStack drop : drops) {
                        target.spawnShearedDrop(entity.level(), pos, drop);
                    }
                }
                // Call GameEvent.SHEAR on both sides
                entity.gameEvent(GameEvent.SHEAR, player);
                // Damage the shear item stack by 1 on the server side
                if (!isClient) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                }
                // Return sided success if the entity was shearable
                return InteractionResult.sidedSuccess(isClient);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity livingEntity) {
            boolean isHeld = pIsSelected;

            if (!isHeld) {
                isHeld = (livingEntity.getOffhandItem() == pStack);
            }

            if (isHeld) {
                handleHeldOrWornItem(pStack, pLevel, pEntity);
            }
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return 0.1F;
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return false;
        }

        return super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        if (isBroken(stack)) {
            return false;
        }

        return ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (isBroken(itemstack)) {
            return InteractionResult.PASS;
        } else {
            return super.useOn(context);
        }
    }

    // Anvil and grindstone repairing, enchanting
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        boolean hasAugments = pStack.has(ModDataComponentTypes.AUGMENTS.get());

        List<Component> toolTips = new AugmentObject(pStack).getTooltips(pTooltipComponents, hasAugments, isBroken(pStack), 1, 0);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
