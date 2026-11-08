package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModularLighterItem extends FlintAndSteelItem implements ModularItemInterface {
    public ModularLighterItem(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        damageModularItem(stack, entity, amount);
        return 0;
    }

    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (isBroken(itemstack)) {
            return InteractionResult.PASS;
        }

        AugmentObject bonuses = getAugmentBonuses(itemstack);
        if (bonuses.EXTINGUISHER > 0) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        // TNT
        if (blockstate.getBlock() instanceof TntBlock tntBlock) {
            if (!level.isClientSide) {
                tntBlock.onCaughtFire(blockstate, level, blockpos, context.getClickedFace(), player);
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 11);

                damageModularItem(itemstack, player, 1);
            }

            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        BlockState blockstate2 = blockstate.getToolModifiedState(context, ItemAbilities.FIRESTARTER_LIGHT, false);
        if (blockstate2 == null) {
            BlockPos blockpos1 = blockpos.relative(context.getClickedFace());

            Direction clickedFace = context.getClickedFace();

            if (bonuses.AREA_LIGHTER > 0) {
                List<BlockPos> toLight = getAreaBlocksToLight(clickedFace, bonuses, blockpos1);
                boolean lightedAtLeastOne = false;

                for (BlockPos blockToLight : toLight) {
                    if (BaseFireBlock.canBePlacedAt(level, blockToLight, context.getHorizontalDirection())) {
                        lightedAtLeastOne = true;
                        BlockState blockstate1 = BaseFireBlock.getState(level, blockToLight);
                        level.setBlock(blockToLight, blockstate1, 11);
                        level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockToLight, itemstack);
                        }
                    }
                }

                if (lightedAtLeastOne) {
                    level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    itemstack.hurtAndBreak(1 + bonuses.AREA_LIGHTER, player, LivingEntity.getSlotForHand(context.getHand()));
                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    return InteractionResult.FAIL;
                }

            } else if (bonuses.FLAME_WAVE > 0) {
                Direction lookingDirection = player.getNearestViewDirection();
                List<BlockPos> toLight = new ArrayList<>();
                toLight.add(blockpos1);

                int range = 5 * bonuses.FLAME_WAVE;
                for (int i = 1; i <= range; i++) {
                    toLight.add(blockpos1.relative(lookingDirection, i));
                }

                boolean lightedAtLeastOne = false;
                boolean placeFire = false;
                for (BlockPos blockToLight : toLight) {
                    if (BaseFireBlock.canBePlacedAt(level, blockToLight, context.getHorizontalDirection())) {
                        placeFire = true;
                    } else if (clickedFace == Direction.UP) {
                        if (BaseFireBlock.canBePlacedAt(level, blockToLight.above(), context.getHorizontalDirection())) {
                            placeFire = true;
                            blockToLight = blockToLight.above();
                        } else if (BaseFireBlock.canBePlacedAt(level, blockToLight.below(), context.getHorizontalDirection())) {
                            placeFire = true;
                            blockToLight = blockToLight.below();
                        }
                    }

                    if (placeFire) {
                        lightedAtLeastOne = true;
                        BlockState blockstate1 = BaseFireBlock.getState(level, blockToLight);
                        level.setBlock(blockToLight, blockstate1, 11);
                        level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockToLight, itemstack);
                        }
                    }
                    placeFire = false;
                }

                if (lightedAtLeastOne) {
                    level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.playSound(
                            null,
                            player.blockPosition(),
                            SoundEvents.WIND_CHARGE_BURST.value(),
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F
                    );
                    itemstack.hurtAndBreak(1 + (2 * bonuses.FLAME_WAVE), player, LivingEntity.getSlotForHand(context.getHand()));
                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                if (BaseFireBlock.canBePlacedAt(level, blockpos1, context.getHorizontalDirection())) {
                    level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                    level.setBlock(blockpos1, blockstate1, 11);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
                        itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    return InteractionResult.FAIL;
                }
            }
        } else {
            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(blockpos, blockstate2, 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    private static List<BlockPos> getAreaBlocksToLight(Direction clickedFace, AugmentObject bonuses, BlockPos blockpos1) {
        List<BlockPos> toLight;
        // Floor / Ceiling
        if (clickedFace == Direction.UP || clickedFace == Direction.DOWN) {
            if (bonuses.AREA_LIGHTER == 1) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.north(),
                        blockpos1.east(),
                        blockpos1.south(),
                        blockpos1.west()
                );
            } else if (bonuses.AREA_LIGHTER == 2) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.north(),
                        blockpos1.north().east(),
                        blockpos1.east(),
                        blockpos1.east().south(),
                        blockpos1.south(),
                        blockpos1.south().west(),
                        blockpos1.west(),
                        blockpos1.west().north()
                );
            } else {
                toLight = List.of(
                        blockpos1,
                        blockpos1.north(),
                        blockpos1.north().east(),
                        blockpos1.north(2),
                        blockpos1.east(),
                        blockpos1.east().south(),
                        blockpos1.east(2),
                        blockpos1.south(),
                        blockpos1.south().west(),
                        blockpos1.south(2),
                        blockpos1.west(),
                        blockpos1.west().north(),
                        blockpos1.west(2)
                );
            }
        }
        // North / South Walls
        else if (clickedFace == Direction.NORTH || clickedFace == Direction.SOUTH) {
            if (bonuses.AREA_LIGHTER == 1) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.east(),
                        blockpos1.below(),
                        blockpos1.west()
                );
            } else if (bonuses.AREA_LIGHTER == 2) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.above().east(),
                        blockpos1.east(),
                        blockpos1.east().below(),
                        blockpos1.below(),
                        blockpos1.below().west(),
                        blockpos1.west(),
                        blockpos1.west().above()
                );
            } else {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.above().east(),
                        blockpos1.above(2),
                        blockpos1.east(),
                        blockpos1.east().below(),
                        blockpos1.east(2),
                        blockpos1.below(),
                        blockpos1.below().west(),
                        blockpos1.below(2),
                        blockpos1.west(),
                        blockpos1.west().above(),
                        blockpos1.west(2)
                );
            }
        }
        // East / West Walls
        else {
            if (bonuses.AREA_LIGHTER == 1) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.south(),
                        blockpos1.below(),
                        blockpos1.north()
                );
            } else if (bonuses.AREA_LIGHTER == 2) {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.above().south(),
                        blockpos1.south(),
                        blockpos1.south().below(),
                        blockpos1.below(),
                        blockpos1.below().north(),
                        blockpos1.north(),
                        blockpos1.north().above()
                );
            } else {
                toLight = List.of(
                        blockpos1,
                        blockpos1.above(),
                        blockpos1.above().south(),
                        blockpos1.above(2),
                        blockpos1.south(),
                        blockpos1.south().below(),
                        blockpos1.south(2),
                        blockpos1.below(),
                        blockpos1.below().north(),
                        blockpos1.below(2),
                        blockpos1.north(),
                        blockpos1.north().above(),
                        blockpos1.north(2)
                );
            }
        }
        return toLight;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (super.use(level, player, usedHand).getResult().consumesAction()) {
            return super.use(level, player, usedHand);
        } else {
            return modularItemUse(level, player, usedHand);
        }
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        if (isBroken(stack)) {
            return false;
        }

        return ItemAbilities.DEFAULT_FLINT_ACTIONS.contains(itemAbility);
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
