package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AugmentObject {
    // Generic Augment values
    public int UNBREAKING;
    public boolean MENDING;

    // Tool Augment values
    public int EFFICIENCY;
    public boolean SILK_TOUCH;
    public int FORTUNE;

    // Weapon Augment values
    public int SHARPNESS;
    public int SMITE;
    public int BANE_OF_ARTHROPODS;
    public int IMPALING;
    public int LOOTING;
    public int KNOCKBACK;
    public int FIRE_ASPECT;

    // Armor Augment values
    public int PROTECTION;
    public int BLAST_PROTECTION;
    public int PROJECTILE_PROTECTION;
    public int FIRE_PROTECTION;

    // Custom Augment values
    public boolean AUTO_SMELT;
    public boolean DIAMOND_SHARPENED;
    public int MODULAR_CLAY_EXTRACTOR;
    public int SCAFFOLDING;
    public boolean ILLUMINATOR;
    public boolean MAGNET;

    public static int TOTAL_FLAVOR_TEXT_AMOUNT = 50;

    public AugmentObject(ItemStack stack) {
        for (ItemStack augment : getAugments(stack)) {
            switch (BuiltInRegistries.ITEM.getKey(augment.getItem()).toString()) {
                // Generic augments
                case "mattsmodulararmory:unbreaking_augment" -> this.UNBREAKING += augment.getCount();
                case "mattsmodulararmory:mending_augment" -> this.MENDING = true;

                // Tool specific augments
                case "mattsmodulararmory:efficiency_augment" -> this.EFFICIENCY += augment.getCount();
                case "mattsmodulararmory:silk_touch_augment" -> this.SILK_TOUCH = true;
                case "mattsmodulararmory:fortune_augment" -> this.FORTUNE = augment.getCount();

                case "mattsmodulararmory:auto_smelt_augment" -> this.AUTO_SMELT = true;
                case "mattsmodulararmory:diamond_sharpened_augment" -> this.DIAMOND_SHARPENED = true;
                case "mattsmodulararmory:modular_clay_extractor_augment" -> this.MODULAR_CLAY_EXTRACTOR = augment.getCount();
                case "mattsmodulararmory:scaffolding_augment" -> this.SCAFFOLDING = augment.getCount();
                case "mattsmodulararmory:illuminator_augment" -> this.ILLUMINATOR = true;
                case "mattsmodulararmory:magnet_augment" -> this.MAGNET = true;

                // Weapon specific augments
                case "mattsmodulararmory:sharpness_augment" -> this.SHARPNESS = augment.getCount();
                case "mattsmodulararmory:smite_augment" -> this.SMITE = augment.getCount();
                case "mattsmodulararmory:bane_of_arthropods_augment" -> this.BANE_OF_ARTHROPODS = augment.getCount();
                case "mattsmodulararmory:impaling_augment" -> this.IMPALING = augment.getCount();
                case "mattsmodulararmory:looting_augment" -> this.LOOTING = augment.getCount();
                case "mattsmodulararmory:knockback_augment" -> this.KNOCKBACK = augment.getCount();
                case "mattsmodulararmory:fire_aspect_augment" -> this.FIRE_ASPECT = augment.getCount();

                // Armor specific augments
                case "mattsmodulararmory:protection_augment" -> this.PROTECTION = augment.getCount();
                case "mattsmodulararmory:blast_protection_augment" -> this.BLAST_PROTECTION = augment.getCount();
                case "mattsmodulararmory:projectile_protection_augment" -> this.PROJECTILE_PROTECTION = augment.getCount();
                case "mattsmodulararmory:fire_protection_augment" -> this.FIRE_PROTECTION = augment.getCount();
            }
        }
    }

    private List<ItemStack> getAugments(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
            return List.of();
        }

        return stack.get(ModDataComponentTypes.AUGMENTS.get());
    }

    public List<Component> getTooltips(List<Component> pTooltipComponents, boolean displayOn, boolean isBroken, int tier, int randomIndex) {
        if (isBroken) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.broken").withStyle(ChatFormatting.DARK_RED));
        }

        if (displayOn) {
            if (tier > 1) {
                ChatFormatting[] colors = {ChatFormatting.GRAY, ChatFormatting.WHITE, ChatFormatting.DARK_RED, ChatFormatting.AQUA};
                String flavorText = "";

                if (tier == 3) {
                    flavorText = "tooltip.mattsmodulararmory.biorite_flavor." + randomIndex;
                } else if (tier == 2) {
                    flavorText = "tooltip.mattsmodulararmory.adaptive_flesh_flavor." + randomIndex;
                }

                pTooltipComponents.add(Component.literal("§k|abc")
                        .append(Component.translatable(flavorText))
                        .append(Component.literal("§k|abc"))
                        .withStyle(colors[tier])
                        .withStyle(ChatFormatting.ITALIC));
            }

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
                        .append(Component.literal(": " + toRoman((int) this.EFFICIENCY)).withStyle(ChatFormatting.YELLOW)));
            }

            if (this.SILK_TOUCH) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.silk_touch_augment").withStyle(ChatFormatting.LIGHT_PURPLE));
            } else if (this.FORTUNE > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fortune_augment").withStyle(ChatFormatting.BLUE)
                        .append(Component.literal(": " + toRoman(this.FORTUNE)).withStyle(ChatFormatting.BLUE)));
            }

            if (this.DIAMOND_SHARPENED) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.diamond_sharpened_augment").withStyle(ChatFormatting.AQUA));
            }

            if (this.AUTO_SMELT && !this.SILK_TOUCH) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.auto_smelt_augment").withStyle(ChatFormatting.DARK_RED));
            }

            if (this.MAGNET) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.magnet_augment").withStyle(ChatFormatting.WHITE));
            }

            if (this.MODULAR_CLAY_EXTRACTOR > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.modular_clay_extractor_augment")
                        .append(Component.literal(": " + toRoman(this.MODULAR_CLAY_EXTRACTOR))).withStyle(ChatFormatting.GRAY));
            }

            if (this.SCAFFOLDING > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.scaffolding_augment")
                        .append(Component.literal(": " + toRoman(this.SCAFFOLDING))).withStyle(ChatFormatting.YELLOW));
            } else if (this.ILLUMINATOR) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.illuminator_augment").withStyle(ChatFormatting.YELLOW));
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
            } else if (this.IMPALING > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.impaling_augment").withStyle(ChatFormatting.DARK_RED)
                        .append(Component.literal(": " + toRoman(this.IMPALING)).withStyle(ChatFormatting.DARK_RED)));
            }

            if (this.LOOTING > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.looting_augment").withStyle(ChatFormatting.BLUE)
                        .append(Component.literal(": " + toRoman(this.LOOTING)).withStyle(ChatFormatting.BLUE)));
            }

            if (this.KNOCKBACK > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.knockback_augment").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(": " + toRoman(this.KNOCKBACK)).withStyle(ChatFormatting.WHITE)));
            }

            if (this.FIRE_ASPECT > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fire_aspect_augment").withStyle(ChatFormatting.YELLOW)
                        .append(Component.literal(": " + toRoman(this.FIRE_ASPECT)).withStyle(ChatFormatting.YELLOW)));
            }

            if (this.PROTECTION > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.protection_augment")
                        .append(Component.literal(": " + toRoman(this.PROTECTION))).withStyle(ChatFormatting.DARK_AQUA));
            } else if (this.BLAST_PROTECTION > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.blast_protection_augment")
                        .append(Component.literal(": " + toRoman(this.BLAST_PROTECTION))).withStyle(ChatFormatting.DARK_AQUA));
            } else if (this.PROJECTILE_PROTECTION > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.projectile_protection_augment")
                        .append(Component.literal(": " + toRoman(this.PROJECTILE_PROTECTION))).withStyle(ChatFormatting.DARK_AQUA));
            } else if (this.FIRE_PROTECTION > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fire_protection_augment")
                        .append(Component.literal(": " + toRoman(this.FIRE_PROTECTION))).withStyle(ChatFormatting.DARK_AQUA));
            }
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
