package net.matt.modular_armory.items.addons;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ModularAugmentAddon extends Item {
    private final String addonId;
    private final String tooltipKey;
    private final int powerUsage;
    private final int maxStack;

    public ModularAugmentAddon(Properties properties, String addonId, int powerUsage, int maxStack) {
        super(properties);

        this.addonId = addonId;
        this.tooltipKey = "tooltip.mattsmodulararmory.augment_addon." + addonId;
        this.powerUsage = powerUsage;
        this.maxStack = maxStack;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public String getAddonId() {
        return addonId;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable(tooltipKey).withStyle(ChatFormatting.GRAY));

        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage1").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(Integer.toString(powerUsage)).withStyle(Style.EMPTY.withColor(0xff0000)))
                .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage2").withStyle(Style.EMPTY.withColor(0xff0000)))
                .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage3").withStyle(ChatFormatting.GRAY))
                );

        if (maxStack == 1) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.addon_unstackable").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.addon_stacks")
                    .append(Component.literal(Integer.toString(maxStack))).withStyle(ChatFormatting.GRAY));
        }
    }

    public static class ADDONS {
        // Values for addons
        static public String SILK_TOUCH = "silk_touch_addon";
        static public String FORTUNE = "fortune_addon";
    }
}
