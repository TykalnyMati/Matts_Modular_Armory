package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AugumentObject {
    public int UNBREAKING;
    public boolean MENDING;
    public float EFFICIENCY;
    public boolean SILK_TOUCH;
    public int FORTUNE;
    public int SHARPNESS;
    public int SMITE;
    public int BANE_OF_ARTHROPODS;
    public int FIRE_ASPECT;

    public AugumentObject(ItemStack stack) {
        for (ItemStack augment : getAugments(stack)) {
            switch (BuiltInRegistries.ITEM.getKey(augment.getItem()).toString()) {
                // Generic augments
                case "mattsmodulararmory:unbreaking_augment" -> this.UNBREAKING += augment.getCount();
                case "mattsmodulararmory:mending_augment" -> this.MENDING = true;

                // Tool specific augments
                case "mattsmodulararmory:efficiency_augment" -> this.EFFICIENCY += augment.getCount();
                case "mattsmodulararmory:silk_touch_augment" -> this.SILK_TOUCH = true;
                case "mattsmodulararmory:fortune_augment" -> this.FORTUNE = augment.getCount();

                // Weapon specific augments
                case "mattsmodulararmory:sharpness_augment" -> this.SHARPNESS = augment.getCount();
                case "mattsmodulararmory:smite_augment" -> this.SMITE = augment.getCount();
                case "mattsmodulararmory:bane_of_arthropods_augment" -> this.BANE_OF_ARTHROPODS = augment.getCount();
                case "mattsmodulararmory:fire_aspect_augment" -> this.FIRE_ASPECT = augment.getCount();
            }
        }
    }

    private List<ItemStack> getAugments(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
            return List.of();
        }

        return stack.get(ModDataComponentTypes.AUGMENTS.get());
    }

    public List<Component> getTooltips(List<Component> pTooltipComponents) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augments").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(":").withStyle(ChatFormatting.GRAY)));

        if (this.UNBREAKING >= 8) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment_full").withStyle(ChatFormatting.DARK_AQUA));
        } else if (this.UNBREAKING > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment").withStyle(ChatFormatting.DARK_AQUA)
                    .append(Component.literal(": " + toRoman(this.UNBREAKING)).withStyle(ChatFormatting.DARK_AQUA)));
        }

        if (this.MENDING) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.mending_augment").withStyle(ChatFormatting.GREEN));
        }

        if (this.EFFICIENCY > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.efficiency_augment").withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(": " + toRoman((int)this.EFFICIENCY)).withStyle(ChatFormatting.YELLOW)));
        }

        if (this.SILK_TOUCH) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.silk_touch_augment").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else if (this.FORTUNE > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fortune_augment").withStyle(ChatFormatting.BLUE)
                    .append(Component.literal(": " + toRoman(this.FORTUNE)).withStyle(ChatFormatting.BLUE)));
        }

        if (this.SHARPNESS > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.sharpness_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(this.SHARPNESS)).withStyle(ChatFormatting.DARK_RED)));
        } else if (this.SMITE > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.smite_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(this.SMITE)).withStyle(ChatFormatting.DARK_RED)));
        } else if (this.BANE_OF_ARTHROPODS > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.bane_of_arthropods_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(this.BANE_OF_ARTHROPODS)).withStyle(ChatFormatting.DARK_RED)));
        }

        if (this.FIRE_ASPECT > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fire_aspect_augment").withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(": " + toRoman(this.FIRE_ASPECT)).withStyle(ChatFormatting.YELLOW)));
        }

        return pTooltipComponents;
    }

    public static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}
