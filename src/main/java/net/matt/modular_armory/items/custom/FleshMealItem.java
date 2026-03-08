package net.matt.modular_armory.items.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class FleshMealItem extends Item {
    public FleshMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();

        if (clickedBlock == ModBlocks.ADAPTIVE_FLESH_BLOCK.get()) {
            if (!level.isClientSide()) {
                // Map of all the blocks that will change state
                Map<Integer, BlockPos> GROW_MAP = Map.of(
                        0, pContext.getClickedPos().north().west(),
                        1, pContext.getClickedPos().north(),
                        2, pContext.getClickedPos().north().east(),
                        3, pContext.getClickedPos().west(),
                        4, pContext.getClickedPos().east(),
                        5, pContext.getClickedPos().south().west(),
                        6, pContext.getClickedPos().south(),
                        7, pContext.getClickedPos().south().east()
                );

                // Removes the item
                pContext.getItemInHand().shrink(1);

                // Iterating through all the blocks and changing their state if possible
                for (int i = 0; i < 8; i++) {
                    if (level.getBlockState(GROW_MAP.get(i)).getBlock() == ModBlocks.MODULAR_CLAY_BLOCK.get()) {
                        level.setBlockAndUpdate(GROW_MAP.get(i), ModBlocks.ADAPTIVE_FLESH_BLOCK.get().defaultBlockState());
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.flesh_meal0")
                .append(Component.translatable("tooltip.mattsmodulararmory.flesh_meal1").withStyle(ChatFormatting.RED)));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
