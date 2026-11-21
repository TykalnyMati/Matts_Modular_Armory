package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AugmentObject {
    // List of augments
    public final List<ItemStack> augmentList;

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

    // Range Weapon Augment values
    public int POWER;
    public int PUNCH;
    public boolean FLAME;
    public boolean INFINITY;

    public int RIPTIDE;

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
    public boolean FIRE_STARTER;
    public int OVERCLOCK;
    public boolean VOID;
    public boolean MAGNET;
    public boolean XP_SIPHON;
    public int EXILE;
    public int POISON;
    public int WITHER;
    public int LIFE_SENSE;
    public int OVERDRIVE;
    public boolean RAPID_FIRE;
    public int VITALITY;
    public boolean GILDED;
    public boolean SUPER_GILDED;
    public boolean PUMPKIN_LENS;
    public int STEP_UP;
    public boolean VOID_TETHER;

    // Utility items
    public int ADVANCED_SHEARER;
    public boolean JEBS_BLESSING;

    public int AREA_LIGHTER;
    public int FLAME_WAVE;
    public int EXTINGUISHER;

    public static int TOTAL_FLAVOR_TEXT_AMOUNT = 50;

    public AugmentObject(ItemStack stack) {
        augmentList = getAugmentsWithCompatibility(stack);

        for (ItemStack augmentStack : augmentList) {
            if (augmentStack.getItem() instanceof AugmentGeneric augment) {
                switch (augment.getAugmentID()) {
                    // Generic augments
                    case "unbreaking_augment" -> this.UNBREAKING += augmentStack.getCount();
                    case "mending_augment" -> this.MENDING = true;
                    case "magnet_augment" -> this.MAGNET = true;
                    case "xp_siphon_augment" -> this.XP_SIPHON = true;

                    // Tool specific augments
                    case "efficiency_augment" -> this.EFFICIENCY += augmentStack.getCount();
                    case "silk_touch_augment" -> this.SILK_TOUCH = true;
                    case "fortune_augment" -> this.FORTUNE = augmentStack.getCount();

                    case "auto_smelt_augment" -> this.AUTO_SMELT = true;
                    case "diamond_sharpened_augment" -> this.DIAMOND_SHARPENED = true;
                    case "modular_clay_extractor_augment" -> this.MODULAR_CLAY_EXTRACTOR = augmentStack.getCount();
                    case "scaffolding_augment" -> this.SCAFFOLDING = augmentStack.getCount();
                    case "illuminator_augment" -> this.ILLUMINATOR = true;
                    case "fire_starter_augment" -> this.FIRE_STARTER = true;
                    case "overclock_augment" -> this.OVERCLOCK = augmentStack.getCount();
                    case "void_augment" -> this.VOID = true;

                    // Weapon specific augments
                    case "sharpness_augment" -> this.SHARPNESS = augmentStack.getCount();
                    case "smite_augment" -> this.SMITE = augmentStack.getCount();
                    case "bane_of_arthropods_augment" -> this.BANE_OF_ARTHROPODS = augmentStack.getCount();
                    case "impaling_augment" -> this.IMPALING = augmentStack.getCount();
                    case "looting_augment" -> this.LOOTING = augmentStack.getCount();
                    case "knockback_augment" -> this.KNOCKBACK = augmentStack.getCount();
                    case "fire_aspect_augment" -> this.FIRE_ASPECT = augmentStack.getCount();
                    case "sweeping_edge_augment" -> this.SWEEPING_EDGE = augmentStack.getCount();
                    case "exile_augment" -> this.EXILE = augmentStack.getCount();
                    case "poison_augment" -> this.POISON = augmentStack.getCount();
                    case "wither_augment" -> this.WITHER = augmentStack.getCount();
                    case "life_sense_augment" -> this.LIFE_SENSE = augmentStack.getCount();
                    case "overdrive_augment" -> this.OVERDRIVE = augmentStack.getCount();

                    // Ranged Weapons specific augments
                    case "power_augment" -> this.POWER = augmentStack.getCount();
                    case "punch_augment" -> this.PUNCH = augmentStack.getCount();
                    case "flame_augment" -> this.FLAME = true;
                    case "infinity_augment" -> this.INFINITY = true;
                    case "rapid_fire_augment" -> this.RAPID_FIRE = true;

                    // Javelin
                    case "riptide_augment" -> this.RIPTIDE = augmentStack.getCount();

                    // Armor specific augments
                    case "protection_augment" -> this.PROTECTION = augmentStack.getCount();
                    case "blast_protection_augment" -> this.BLAST_PROTECTION = augmentStack.getCount();
                    case "projectile_protection_augment" -> this.PROJECTILE_PROTECTION = augmentStack.getCount();
                    case "fire_protection_augment" -> this.FIRE_PROTECTION = augmentStack.getCount();
                    case "thorns_augment" -> this.THORNS = augmentStack.getCount();
                    case "vitality_augment" -> this.VITALITY = augmentStack.getCount();
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
                    case "void_tether_augment" -> this.VOID_TETHER = true;

                    case "advanced_shearer_augment" -> this.ADVANCED_SHEARER = augmentStack.getCount();
                    case "jebs_blessing_augment" -> this.JEBS_BLESSING = true;

                    case "area_lighter_augment" -> this.AREA_LIGHTER = augmentStack.getCount();
                    case "flame_wave_augment" -> this.FLAME_WAVE = augmentStack.getCount();
                    case "extinguisher_augment" -> this.EXTINGUISHER = augmentStack.getCount();
                }
            }
        }
    }

//    private List<ItemStack> getAugments(ItemStack stack) {
//        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
//            return List.of();
//        }
//
//        return stack.get(ModDataComponentTypes.AUGMENTS.get());
//    }

    // Returns a list of augments while checking compatibility
    private List<ItemStack> getAugmentsWithCompatibility(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
            return List.of();
        }

        List<ItemStack> compatibleAugmentList = new ArrayList<>();
        List<String> incompatibleAugments = new ArrayList<>();

        for (ItemStack augmentStack : Objects.requireNonNull(stack.get(ModDataComponentTypes.AUGMENTS.get()))) {
            if (augmentStack.getItem() instanceof AugmentGeneric augment) {
                // Checks if the augment should be processed or if it's in the incompatible list
                // If it isn't in the incompatible list populates it with other incompatible augments
                if (!augment.getIncompatibleWith().isEmpty()) {
                    if (incompatibleAugments.contains(augment.getAugmentID())) {
                        continue;
                    } else {
                        List<String> thisIncompatibleWith = augment.getIncompatibleWith();
                        incompatibleAugments.addAll(thisIncompatibleWith);
                    }
                }

                compatibleAugmentList.add(augmentStack);
            }
        }

        return compatibleAugmentList;
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

            // Renders the Augments
            for (ItemStack augmentStack : augmentList) {
                if (augmentStack.getItem() instanceof AugmentGeneric augment) {
                    if (augment.isLevelable()) {
                        // Exceptions (unbreaking for now)
                        if (augment.getAugmentID().equals("unbreaking_augment")) {
                            if (this.UNBREAKING >= 8){
                                pTooltipComponents.add(
                                        Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment_full")
                                            .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor()))
                                );
                            } else if (this.UNBREAKING > 0) {
                                pTooltipComponents.add(
                                        Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment")
                                                .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor()))
                                        .append(Component.literal(": " + toRoman(this.UNBREAKING))
                                            .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor())))
                                );
                            }
                        }
                        // Everything else
                        else {
                            pTooltipComponents.add(
                                    Component.translatable("tooltip.mattsmodulararmory." + augment.getAugmentID())
                                            .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor()))
                                    .append(Component.literal(": " + toRoman(augmentStack.getCount()))
                                            .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor())))
                            );
                        }
                    }
                    // Untiered augments
                    else {
                        pTooltipComponents.add(
                                Component.translatable("tooltip.mattsmodulararmory." + augment.getAugmentID())
                                        .withStyle(Style.EMPTY.withColor(augment.getTooltipHexColor())));
                    }
                }
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
