package net.matt.modular_armory.items.addons;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ModularRangeAddon extends Item {
    private final String tooltipKey;
    private final int hexColor;
    private final int rangeIncrease;

    public ModularRangeAddon(Properties properties, String tooltipKey, int hexColor, int rangeIncrease) {
        super(properties);

        this.tooltipKey = "tooltip.mattsmodulararmory.addon_tier." + tooltipKey;
        this.hexColor = hexColor;
        this.rangeIncrease = rangeIncrease;
    }

    public int getRangeIncrease() {
        return this.rangeIncrease;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.range_addon_tier").withStyle(ChatFormatting.GRAY)
                .append(Component.translatable(tooltipKey).withStyle(Style.EMPTY.withColor(hexColor))));

        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.range_addon_text")
                .append(Integer.toString(getRangeIncrease())).withStyle(ChatFormatting.GRAY));

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
