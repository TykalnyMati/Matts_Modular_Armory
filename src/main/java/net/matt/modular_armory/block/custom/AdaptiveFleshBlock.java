package net.matt.modular_armory.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptiveFleshBlock extends Block {
    public AdaptiveFleshBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull Item.TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block0").withStyle(ChatFormatting.DARK_RED)
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block1").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block2").withStyle(ChatFormatting.WHITE))
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block3").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_block4").withStyle(ChatFormatting.DARK_RED))
                );
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
