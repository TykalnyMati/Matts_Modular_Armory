package net.matt.modular_armory.block.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfectiousAdaptiveFleshBlock extends Block {
    public InfectiousAdaptiveFleshBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        double spreadChance = 0.025;
        boolean convertedAny = false;

        // Iterate through all 26 surrounding neighbors (3x3x3 cube excluding self)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    BlockPos neighborPos = pos.offset(x, y, z);

                    if (level.getBlockState(neighborPos).is(ModBlocks.MODULAR_CLAY_BLOCK.get())) {
                        if (random.nextDouble() <= spreadChance) {
                            level.setBlockAndUpdate(neighborPos, ModBlocks.ADAPTIVE_FLESH_BLOCK.get().defaultBlockState());
                            convertedAny = true;

                            level.sendParticles(
                                    ParticleTypes.CRIMSON_SPORE,
                                    neighborPos.getX() + 0.5D,
                                    neighborPos.getY() + 0.5D,
                                    neighborPos.getZ() + 0.5D,
                                    5,
                                    0.3D,
                                    0.3D,
                                    0.3D,
                                    0.05D
                            );
                        }
                    }
                }
            }
        }

        // Play sound if at least one block was infected
        if (convertedAny) {
            level.playSound(
                    null,
                    pos,
                    SoundEvents.GENERIC_EAT,
                    SoundSource.BLOCKS,
                    0.5F,
                    1.0F
            );
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull Item.TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block0").withStyle(ChatFormatting.DARK_RED)
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block1").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block2").withStyle(ChatFormatting.WHITE))
        );
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
