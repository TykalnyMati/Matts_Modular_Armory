package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class AugmentGeneric extends Item {
    private final String augmentID;
    private final String descriptionLocation;
    private final String typeLocation;
    public final List<Integer> augmentLevels;
    public List<TagKey<Item>> augmentTypes = new ArrayList<>();
    public List<TagKey<Item>> incompatibleItemTypes = new ArrayList<>();
    private boolean levelable = false;
    private final int tooltipHexColor;

    private final List<String> incompatibleWithAugments;

    public AugmentGeneric(Properties pProperties, String augmentName, Types augmentType, List<Integer> augmentLevels, int tooltipHexColor, List<String> incompatibleWithAugments) {
        super(pProperties);
        this.augmentID = augmentName;
        this.descriptionLocation = "tooltip." + MattsModularArmory.MOD_ID + ".description." + augmentName;
        this.typeLocation = "tooltip." + MattsModularArmory.MOD_ID + ".augment_type." + augmentType.toString().toLowerCase();
        this.augmentLevels = augmentLevels;
        this.tooltipHexColor = tooltipHexColor;

        this.incompatibleWithAugments = incompatibleWithAugments;

        for (int level : augmentLevels) {
            if (level > 1) {
                this.levelable = true;
                break;
            }
        }

        addType(augmentType);
    }

    private void addType(Types augmentType) {
        switch (augmentType) {
            case Types.GENERIC -> this.augmentTypes.add(ModTags.Items.MODULAR_ITEMS);
            case Types.TOOLS -> this.augmentTypes.add(ModTags.Items.MODULAR_TOOL);
            case Types.WEAPONS -> this.augmentTypes.add(ModTags.Items.MODULAR_WEAPON);
            case Types.TOOLS_AND_WEAPONS -> {
                this.augmentTypes.add(ModTags.Items.MODULAR_TOOL);
                this.augmentTypes.add(ModTags.Items.MODULAR_WEAPON);
            }
            case Types.ARMOR -> this.augmentTypes.add(ModTags.Items.MODULAR_ARMOR);
            case Types.RANGED_WEAPONS -> this.augmentTypes.add(ModTags.Items.MODULAR_RANGED_WEAPON);
            case Types.RANGED_AND_THROWN_WEAPONS -> {
                this.augmentTypes.add(ModTags.Items.MODULAR_RANGED_WEAPON);
                this.augmentTypes.add(ModTags.Items.MODULAR_THROWN_WEAPON);
            }

            case Types.ARMOR_HEAD -> this.augmentTypes.add(ModTags.Items.MODULAR_HELMET);
            case Types.ARMOR_CHEST -> this.augmentTypes.add(ModTags.Items.MODULAR_CHESTPLATE);
            case Types.ARMOR_LEGS -> this.augmentTypes.add(ModTags.Items.MODULAR_LEGGINGS);
            case Types.ARMOR_FEET -> this.augmentTypes.add(ModTags.Items.MODULAR_BOOTS);
            case Types.SWORD -> this.augmentTypes.add(ModTags.Items.MODULAR_SWORD);
            case Types.JAVELIN -> this.augmentTypes.add(ModTags.Items.MODULAR_JAVELIN);
            case Types.SHEARS -> this.augmentTypes.add(ModTags.Items.MODULAR_SHEARS);
            case Types.LIGHTER -> this.augmentTypes.add(ModTags.Items.MODULAR_LIGHTER);
        }
    }

    public AugmentGeneric bonusType(Types augmentType) {
        addType(augmentType);
        return this;
    }

    public AugmentGeneric incompatibleItemType(TagKey<Item> incompatibleItemType) {
        this.incompatibleItemTypes.add(incompatibleItemType);
        return this;
    }

    public AugmentGeneric(Properties pProperties, String augmentName, Types augmentType, List<Integer> augmentLevels, int tooltipHexColor) {
        this(pProperties, augmentName, augmentType, augmentLevels, tooltipHexColor, List.of());
    }

    public String getAugmentID() {
        return augmentID;
    }

    public boolean isLevelable() {
        return this.levelable;
    }

    public int getTooltipHexColor() {
        return this.tooltipHexColor;
    }

    public List<String> getIncompatibleWith() {
        return this.incompatibleWithAugments;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(
                Component.translatable("tooltip.mattsmodulararmory." + getAugmentID())
                        .withStyle(Style.EMPTY.withColor(getTooltipHexColor())));

        // Augment description
        String description = Component.translatable(descriptionLocation).getString();
        for (String line : description.split("\n")) {
            pTooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }

        // Augment type
        pTooltipComponents.add(Component.translatable(typeLocation).withStyle(ChatFormatting.GRAY));

        // Augment levels
        if (augmentLevels.isEmpty()) {
            pTooltipComponents.add(Component.literal("1").withStyle(ChatFormatting.GRAY));
        } else {
            ChatFormatting[] colors = {ChatFormatting.GRAY, ChatFormatting.WHITE, ChatFormatting.DARK_RED, ChatFormatting.AQUA};
            MutableComponent levelsLine = Component.empty();

            for (int i = 0; i < augmentLevels.size(); i++) {
                // Add the number with its specific color
                levelsLine.append(Component.literal(augmentLevels.get(i).toString()).withStyle(colors[i]));

                // Add the separator " | " in gray, but only IF it's not the last number
                if (i < augmentLevels.size() - 1) {
                    levelsLine.append(Component.literal(" | ").withStyle(ChatFormatting.GRAY));
                }
            }

            pTooltipComponents.add(levelsLine);
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public enum Types {
        // Values for selecting type
        GENERIC,
        TOOLS,
        WEAPONS,
        TOOLS_AND_WEAPONS,
        ARMOR,

        RANGED_WEAPONS,
        RANGED_AND_THROWN_WEAPONS,

        SWORD,
        JAVELIN,
        SHEARS,
        LIGHTER,

        ARMOR_HEAD,
        ARMOR_CHEST,
        ARMOR_LEGS,
        ARMOR_FEET
    }

    public static class Incompatibilities {
        static public final List<String> RIGHT_CLICK = List.of("modular_clay_extractor_augment", "scaffolding_augment", "illuminator_augment", "fire_starter_augment", "life_sense_augment", "overdrive_augment", "overclock_augment");
    }
}
