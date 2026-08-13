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
    private final int bonusPowerUsage;

    static private final int POWER_COLOR = 0xff0000;

    public ModularAugmentAddon(Properties properties, String addonId, int powerUsage, int maxStack, int bonusPowerUsage) {
        super(properties);

        this.addonId = addonId;
        this.tooltipKey = "tooltip.mattsmodulararmory.augment_addon." + addonId;
        this.powerUsage = powerUsage;
        this.maxStack = maxStack;
        this.bonusPowerUsage = bonusPowerUsage;
    }

    public ModularAugmentAddon(Properties properties, String addonId, int powerUsage, int maxStack) {
        this(properties, addonId, powerUsage, maxStack, 0);
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public int getBonusPowerUsage() {
        return bonusPowerUsage;
    }

    public String getAddonId() {
        return addonId;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable(tooltipKey).withStyle(ChatFormatting.GRAY));

        if (bonusPowerUsage == 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage1").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(Integer.toString(powerUsage)).withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage2").withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage3").withStyle(ChatFormatting.GRAY))
            );
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage1").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(Integer.toString(powerUsage)).withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage2").withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.literal(" + ").withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.literal(Integer.toString(bonusPowerUsage)).withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage2").withStyle(Style.EMPTY.withColor(POWER_COLOR)))
                    .append(Component.translatable("tooltip.mattsmodulararmory.augment_addon.power_usage3").withStyle(ChatFormatting.GRAY))
            );
        }

        if (maxStack == 1) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.addon_unstackable").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augment_addon.addon_stacks")
                    .append(Component.literal(Integer.toString(maxStack))).withStyle(ChatFormatting.GRAY));
        }
    }

    public static class ADDONS {
        // Values for addons
        static public final String SILK_TOUCH = "silk_touch_addon";
        static public final String FORTUNE = "fortune_addon";
        static public final String AUTO_SMELT = "auto_smelt_addon";
        static public final String AUTO_SMELTING_FORTUNE = "auto_smelting_fortune_addon";
        static public final String VOID = "void_addon";
    }
}
