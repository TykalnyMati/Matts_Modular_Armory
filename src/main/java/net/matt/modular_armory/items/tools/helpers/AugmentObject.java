package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
    public int SWEEPING_EDGE;

    // Armor Augment values
    public int PROTECTION;
    public int BLAST_PROTECTION;
    public int PROJECTILE_PROTECTION;
    public int FIRE_PROTECTION;
    public int THORNS;

    public int RESPIRATION;
    public int AQUA_AFFINITY;

    public int SWIFT_SNEAK;

    public int FEATHER_FALLING;
    public int FROST_WALKER;
    public int DEPTH_STRIDER;
    public int SOUL_SPEED;

    // Custom Augment values
    public boolean AUTO_SMELT;
    public boolean DIAMOND_SHARPENED;
    public int MODULAR_CLAY_EXTRACTOR;
    public int SCAFFOLDING;
    public boolean ILLUMINATOR;
    public boolean VOID;
    public boolean MAGNET;
    public int POISON;
    public int WITHER;
    public boolean GILDED;
    public boolean SUPER_GILDED;
    public boolean PUMPKIN_LENS;
    public int STEP_UP;

    public static int TOTAL_FLAVOR_TEXT_AMOUNT = 50;

    public AugmentObject(ItemStack stack) {
        for (ItemStack augmentStack : getAugments(stack)) {
            if (augmentStack.getItem() instanceof AugmentGeneric augment) {
                switch (augment.getAugmentID()) {
                    // Generic augments
                    case "unbreaking_augment" -> this.UNBREAKING += augmentStack.getCount();
                    case "mending_augment" -> this.MENDING = true;

                    // Tool specific augments
                    case "efficiency_augment" -> this.EFFICIENCY += augmentStack.getCount();
                    case "silk_touch_augment" -> this.SILK_TOUCH = true;
                    case "fortune_augment" -> this.FORTUNE = augmentStack.getCount();

                    case "auto_smelt_augment" -> this.AUTO_SMELT = true;
                    case "diamond_sharpened_augment" -> this.DIAMOND_SHARPENED = true;
                    case "modular_clay_extractor_augment" -> this.MODULAR_CLAY_EXTRACTOR = augmentStack.getCount();
                    case "scaffolding_augment" -> this.SCAFFOLDING = augmentStack.getCount();
                    case "illuminator_augment" -> this.ILLUMINATOR = true;
                    case "void_augment" -> this.VOID = true;
                    case "magnet_augment" -> this.MAGNET = true;

                    // Weapon specific augments
                    case "sharpness_augment" -> this.SHARPNESS = augmentStack.getCount();
                    case "smite_augment" -> this.SMITE = augmentStack.getCount();
                    case "bane_of_arthropods_augment" -> this.BANE_OF_ARTHROPODS = augmentStack.getCount();
                    case "impaling_augment" -> this.IMPALING = augmentStack.getCount();
                    case "looting_augment" -> this.LOOTING = augmentStack.getCount();
                    case "knockback_augment" -> this.KNOCKBACK = augmentStack.getCount();
                    case "fire_aspect_augment" -> this.FIRE_ASPECT = augmentStack.getCount();
                    case "sweeping_edge_augment" -> this.SWEEPING_EDGE = augmentStack.getCount();
                    case "poison_augment" -> this.POISON = augmentStack.getCount();
                    case "wither_augment" -> this.WITHER = augmentStack.getCount();

                    // Armor specific augments
                    case "protection_augment" -> this.PROTECTION = augmentStack.getCount();
                    case "blast_protection_augment" -> this.BLAST_PROTECTION = augmentStack.getCount();
                    case "projectile_protection_augment" -> this.PROJECTILE_PROTECTION = augmentStack.getCount();
                    case "fire_protection_augment" -> this.FIRE_PROTECTION = augmentStack.getCount();
                    case "thorns_augment" -> this.THORNS = augmentStack.getCount();
                    case "gilded_augment" -> this.GILDED = true;
                    case "super_gilded_augment" -> this.SUPER_GILDED = true;

                    case "respiration_augment" -> this.RESPIRATION = augmentStack.getCount();
                    case "aqua_affinity_augment" -> this.AQUA_AFFINITY = augmentStack.getCount();
                    case "pumpkin_lens_augment" -> this.PUMPKIN_LENS = true;

                    case "swift_sneak_augment" -> this.SWIFT_SNEAK = augmentStack.getCount();

                    case "feather_falling_augment" -> this.FEATHER_FALLING = augmentStack.getCount();
                    case "frost_walker_augment" -> this.FROST_WALKER = augmentStack.getCount();
                    case "depth_strider_augment" -> this.DEPTH_STRIDER = augmentStack.getCount();
                    case "soul_speed_augment" -> this.SOUL_SPEED = augmentStack.getCount();
                    case "step_up_augment" -> this.STEP_UP = augmentStack.getCount();
                }
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
                        .append(Component.literal("§kabc|"))
                        .withStyle(colors[tier])
                        .withStyle(ChatFormatting.ITALIC));
            }

            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augments").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(":").withStyle(ChatFormatting.GRAY)));

            // GENERIC
            if (this.UNBREAKING >= 8) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment_full").withStyle(ChatFormatting.DARK_AQUA));
            } else if (this.UNBREAKING > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.literal(": " + toRoman(this.UNBREAKING)).withStyle(ChatFormatting.DARK_AQUA)));
            }

            if (this.MENDING) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.mending_augment").withStyle(ChatFormatting.GREEN));
            }

            // TOOLS
            if (this.EFFICIENCY > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.efficiency_augment").withStyle(ChatFormatting.YELLOW)
                        .append(Component.literal(": " + toRoman((int) this.EFFICIENCY)).withStyle(ChatFormatting.YELLOW)));
            }

            if (this.VOID) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.void_augment").withStyle(Style.EMPTY.withColor(0x8f36db)));
            } else {
                if (this.SILK_TOUCH) {
                    pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.silk_touch_augment").withStyle(ChatFormatting.LIGHT_PURPLE));
                } else if (this.FORTUNE > 0) {
                    pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fortune_augment").withStyle(ChatFormatting.BLUE)
                            .append(Component.literal(": " + toRoman(this.FORTUNE)).withStyle(ChatFormatting.BLUE)));
                }

                if (this.AUTO_SMELT && !this.SILK_TOUCH) {
                    pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.auto_smelt_augment").withStyle(ChatFormatting.DARK_RED));
                }
            }

            if (this.DIAMOND_SHARPENED) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.diamond_sharpened_augment").withStyle(ChatFormatting.AQUA));
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

            // WEAPONS
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

            if (this.POISON > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.poison_augment").withStyle(Style.EMPTY.withColor(0x76db4c))
                        .append(Component.literal(": " + toRoman(this.POISON)).withStyle(Style.EMPTY.withColor(0x76db4c))));
            }

            if (this.WITHER > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.wither_augment").withStyle(Style.EMPTY.withColor(0x764857))
                        .append(Component.literal(": " + toRoman(this.WITHER)).withStyle(Style.EMPTY.withColor(0x764857))));
            }

            // SWORD
            if (this.SWEEPING_EDGE > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.sweeping_edge_augment").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(": " + toRoman(this.SWEEPING_EDGE)).withStyle(ChatFormatting.WHITE)));
            }

            // ARMOR
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

            if (this.THORNS > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.thorns_augment")
                        .append(Component.literal(": " + toRoman(this.THORNS))).withStyle(ChatFormatting.DARK_RED));
            }

            if (this.SUPER_GILDED) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.super_gilded_augment").withStyle(ChatFormatting.GOLD));
            }
            else if (this.GILDED) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.gilded_augment").withStyle(ChatFormatting.GOLD));
            }

            // Helmet
            if (this.RESPIRATION > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.respiration_augment")
                        .append(Component.literal(": " + toRoman(this.RESPIRATION))).withStyle(ChatFormatting.AQUA));
            }

            if (this.AQUA_AFFINITY > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.aqua_affinity_augment")
                        .append(Component.literal(": " + toRoman(this.AQUA_AFFINITY))).withStyle(ChatFormatting.AQUA));
            }

            if (this.PUMPKIN_LENS) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.pumpkin_lens_augment").withStyle(ChatFormatting.GOLD));
            }

            // Leggings
            if (this.SWIFT_SNEAK > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.swift_sneak_augment")
                        .append(Component.literal(": " + toRoman(this.SWIFT_SNEAK))).withStyle(ChatFormatting.AQUA));
            }

            // Boots
            if (this.FEATHER_FALLING > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.feather_falling_augment")
                        .append(Component.literal(": " + toRoman(this.FEATHER_FALLING))).withStyle(ChatFormatting.DARK_AQUA));
            }

            if (this.FROST_WALKER > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.frost_walker_augment")
                        .append(Component.literal(": " + toRoman(this.FROST_WALKER))).withStyle(ChatFormatting.AQUA));
            } else if (this.DEPTH_STRIDER > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.depth_strider_augment")
                        .append(Component.literal(": " + toRoman(this.DEPTH_STRIDER))).withStyle(ChatFormatting.AQUA));
            }

            if (this.SOUL_SPEED > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.soul_speed_augment")
                        .append(Component.literal(": " + toRoman(this.SOUL_SPEED))).withStyle(ChatFormatting.BLUE));
            }

            if (this.STEP_UP > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.step_up_augment")
                        .append(Component.literal(": " + toRoman(this.STEP_UP))).withStyle(ChatFormatting.YELLOW));
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
