package net.matt.modular_armory.items.tools;

import com.google.common.collect.ImmutableMap;
import net.matt.modular_armory.items.tools.helpers.AugumentObject;
import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class ModularAxeItem extends ModularGenericDiggerItem {
    protected static final Map<Block, Block> STRIPPABLES = new ImmutableMap.Builder<Block, Block>()
            .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
            .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
            .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
            .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
            .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
            .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
            .put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
            .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
            .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
            .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
            .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
            .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
            .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
            .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
            .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
            .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
            .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
            .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
            .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
            .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
            .put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
            .build();

    public ModularAxeItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_AXE, properties);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        damageTool(pStack, pAttacker);

        AugumentObject bonuses = getAugmentBonuses(pStack);

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;

        boolean DO_ENCHANTED_PARTICLES = false;

        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            if (sharpness > 0) {
                DO_ENCHANTED_PARTICLES = true;
            } else if (smite > 0 && pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
                DO_ENCHANTED_PARTICLES = true;
            } else if (bane_of_arthropods > 0 && pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
                DO_ENCHANTED_PARTICLES = true;
            }

            if (DO_ENCHANTED_PARTICLES) {
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        pTarget.getRandomX(0.5D),
                        pTarget.getRandomY(),
                        pTarget.getRandomZ(0.5D),
                        24,
                        0.0D,
                        0.5D,
                        0.0D,
                        0.5D
                );
            }
        }
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

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
    }

    @org.jetbrains.annotations.Nullable
    public static BlockState getAxeStrippingState(BlockState originalState) {
        Block block = STRIPPABLES.get(originalState.getBlock());
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    // Damage calculations
    @Override
    public float getAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        ItemStack weapon = source.getWeaponItem();
        if (weapon == null) {
            return super.getAttackDamageBonus(target, baseDamage, source);
        }

        if (isBroken(source.getWeaponItem())) {
            return -baseDamage + 1;
        }

        AugumentObject bonuses = getAugmentBonuses(weapon);

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;

        if (sharpness > 0) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 0.5f * sharpness  + 0.5f;
        } else if (smite > 0 && target.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 2.5f * smite;
        } else if (bane_of_arthropods > 0 && target.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 2.5f * bane_of_arthropods;
        }

        return super.getAttackDamageBonus(target, baseDamage, source);
    }
}
