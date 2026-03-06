package net.matt.modular_armory.items.tools;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ModularAxeItem extends AxeItem {
    public ModularAxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (isBroken(stack)) {
            return false;
        }

        if (!level.isClientSide() && state.getDestroySpeed(level, pos) > 0.0F) {
            int fortuneLevel = getAugmentBonuses(stack).FORTUNE;

            // Silk touch check
            if (getAugmentBonuses(stack).SILK_TOUCH) {
                // if tool isn't correct
                if (!isCorrectToolForDrops(stack, state) && state.requiresCorrectToolForDrops()) {
                    return false;
                }

                BlockEntity blockEntity = level.getBlockEntity(pos);

                // Silk Holder test
                Holder<Enchantment> silkHolder =
                        level.registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT)
                                .getHolderOrThrow(Enchantments.SILK_TOUCH);

                // Create tool copy and inject silk enchant
                ItemStack silkTool = stack.copy();
                silkTool.enchant(silkHolder, 1);

                LootParams.Builder builder = new LootParams.Builder(((ServerLevel) level))
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, silkTool)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                        .withParameter(LootContextParams.THIS_ENTITY, entity);

                List<ItemStack> drops = state.getDrops(builder);

                // Remove block without vanilla drops
                level.removeBlock(pos, false);

                // Spawn custom drops
                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }
            } else // Fortune check
                if (fortuneLevel > 0) {
                    // if tool isn't correct
                    if (!isCorrectToolForDrops(stack, state) && state.requiresCorrectToolForDrops()) {
                        return false;
                    }

                    BlockEntity blockEntity = level.getBlockEntity(pos);

                    // Fortune Holder test
                    Holder<Enchantment> fortuneHolder =
                            level.registryAccess()
                                    .registryOrThrow(Registries.ENCHANTMENT)
                                    .getHolderOrThrow(Enchantments.FORTUNE);

                    // Create tool copy and inject fortune enchant
                    ItemStack fortuneTool = stack.copy();
                    fortuneTool.enchant(fortuneHolder, fortuneLevel);

                    LootParams.Builder builder = new LootParams.Builder(((ServerLevel) level))
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                            .withParameter(LootContextParams.TOOL, fortuneTool)
                            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                            .withParameter(LootContextParams.THIS_ENTITY, entity);

                    List<ItemStack> drops = state.getDrops(builder);

                    // Remove block without vanilla drops
                    level.removeBlock(pos, false);

                    // Spawn custom drops
                    for (ItemStack drop : drops) {
                        Block.popResource(level, pos, drop);
                    }
                }
            damageTool(stack, entity);
        }

        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return !isBroken(stack);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        damageTool(pStack, pAttacker);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (isBroken(stack)) {
            return ItemAttributeModifiers.EMPTY;
        }

        return super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        if (isBroken(source.getWeaponItem())) {
            return -baseDamage + 1;
        }

        return super.getAttackDamageBonus(target, baseDamage, source);
    }

    private void damageTool(ItemStack stack, LivingEntity entity) {
        // Unbreaking augment
        if (getAugmentBonuses(stack).UNBREAKING * 0.125f >= 1f || entity.getRandom().nextFloat() < getAugmentBonuses(stack).UNBREAKING * 0.125f) {
            return;
        }

        if (isBroken(stack)) {
            stack.setDamageValue(stack.getMaxDamage() - 1);
            return;
        }

        if (stack.getDamageValue() + 1 >= stack.getMaxDamage() - 1) {
            stack.setDamageValue(stack.getMaxDamage() - 1);

            if (!entity.level().isClientSide()) {
                entity.level().playSound(
                        null,
                        entity.blockPosition(),
                        SoundEvents.ITEM_BREAK,
                        entity.getSoundSource(),
                        1.0F,
                        1.0F
                );
            }

            return;
        }

        stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
    }

    public boolean isBroken(ItemStack item) {
        return item.getDamageValue() >= item.getMaxDamage() - 1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return 0.1F;
        }

        float base = super.getDestroySpeed(stack, state);

        if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            base += getAugmentBonuses(stack).MINING_BONUS;
        }
        return base;

    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return false;
        }

        return super.isCorrectToolForDrops(stack, state);
    }

    // For stripping
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        if (playerHasShieldUseIntent(pContext)) {
            return InteractionResult.PASS;
        } else {
            var modded = level.getBlockState(blockpos).getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.AXE_STRIP, false);
            Optional<BlockState> optional = modded != null ? Optional.of(modded) : this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos));
            if (optional.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                ItemStack itemstack = pContext.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                }

                level.setBlock(blockpos, optional.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, optional.get()));
                if (player != null) {
                    damageTool(itemstack, player);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    private static boolean playerHasShieldUseIntent(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        return pContext.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().is(Items.SHIELD) && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(Level pLevel, BlockPos pPos, @Nullable Player pPlayer, BlockState pState) {
        Optional<BlockState> optional = this.getStripped(pState);
        if (optional.isPresent()) {
            pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return optional;
        } else {
            Optional<BlockState> optional1 = WeatheringCopper.getPrevious(pState);
            if (optional1.isPresent()) {
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                pLevel.levelEvent(pPlayer, 3005, pPos, 0);
                return optional1;
            } else {
                Optional<BlockState> optional2 = Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(pState.getBlock()))
                        .map(p_150694_ -> p_150694_.withPropertiesOf(pState));
                if (optional2.isPresent()) {
                    pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.levelEvent(pPlayer, 3004, pPos, 0);
                    return optional2;
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private Optional<BlockState> getStripped(BlockState pUnstrippedState) {
        return Optional.ofNullable(STRIPPABLES.get(pUnstrippedState.getBlock()))
                .map(p_150689_ -> p_150689_.defaultBlockState().setValue(RotatedPillarBlock.AXIS, pUnstrippedState.getValue(RotatedPillarBlock.AXIS)));
    }

    private AugumentObject getAugmentBonuses(ItemStack stack) {
        AugumentObject Bonuses = new AugumentObject(stack);
        return Bonuses;
    }
}
