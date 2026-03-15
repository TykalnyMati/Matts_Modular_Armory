package net.matt.modular_armory.items.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FleshMealItem extends Item {
    public FleshMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(pos);

        // Check if the clicked block is correct
        if (clickedBlockState.getBlock() == ModBlocks.ADAPTIVE_FLESH_BLOCK.get()) {
            // Play sound and spawn particles
            level.playSound(pContext.getPlayer(), pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (level.isClientSide()) {
                spawnEatingParticles(level, pos, clickedBlockState);
            }

            if (!level.isClientSide()) {
                BlockPos[] neighbors = {
                        pos.above().north().west(), pos.above().north(), pos.above().north().east(),
                        pos.above().west(), pos.above(), pos.above().east(),
                        pos.above().south().west(), pos.above().south(), pos.above().south().east(),

                        pos.north().west(), pos.north(), pos.north().east(),
                        pos.west(), pos.east(),
                        pos.south().west(), pos.south(), pos.south().east(),

                        pos.below().north().west(), pos.below().north(), pos.below().north().east(),
                        pos.below().west(), pos.below(), pos.below().east(),
                        pos.below().south().west(), pos.below().south(), pos.below().south().east()
                };

                double spreadChance = 0.25;

                for (BlockPos neighborPos : neighbors) {
                    if (level.random.nextDouble() <= spreadChance) {
                        if (level.getBlockState(neighborPos).is(ModBlocks.MODULAR_CLAY_BLOCK.get())) {
                            level.setBlockAndUpdate(neighborPos, ModBlocks.ADAPTIVE_FLESH_BLOCK.get().defaultBlockState());
                        }
                    }
                }

                // Consume the item
                if (!pContext.getPlayer().getAbilities().instabuild) {
                    pContext.getItemInHand().shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private void spawnEatingParticles(Level level, BlockPos pos, BlockState state) {
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 15; i++) {
            // Calculate offsets to center particles on the block face
            double d0 = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.8D;
            double d1 = pos.getY() + 1.0D; // Top of the block
            double d2 = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.8D;

            // Use BLOCK particles based on the Adaptive Flesh Block texture
            level.addParticle(new net.minecraft.core.particles.BlockParticleOption(
                            net.minecraft.core.particles.ParticleTypes.BLOCK, state),
                    d0, d1, d2, 0.0D, 0.1D, 0.0D);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.flesh_meal0").withStyle(ChatFormatting.GRAY)
                .append(Component.translatable("tooltip.mattsmodulararmory.flesh_meal1").withStyle(ChatFormatting.DARK_RED)));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
