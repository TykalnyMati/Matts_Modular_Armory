package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AugmentGeneric extends Item {
    private final String descriptionLocation;
    private final String typeLocation;
    public final List<Integer> augmentLevels;
    public List<TagKey<Item>> augmentTypes = new ArrayList<>();

    public AugmentGeneric(Properties pProperties, String augmentName, String augmentType, List<Integer> augmentLevels) {
        super(pProperties);
        this.descriptionLocation = "tooltip." + MattsModularArmory.MOD_ID + ".description." + augmentName;
        this.typeLocation = "tooltip." + MattsModularArmory.MOD_ID + ".augment_type." + augmentType;
        this.augmentLevels = augmentLevels;

        switch (augmentType) {
            case Types.GENERIC -> this.augmentTypes.add(ModTags.Items.MODULAR_ITEMS);
            case Types.TOOLS -> this.augmentTypes.add(ModTags.Items.MODULAR_TOOL);
            case Types.WEAPONS -> this.augmentTypes.add(ModTags.Items.MODULAR_WEAPON);
            case Types.TOOLS_AND_WEAPONS -> {
                this.augmentTypes.add(ModTags.Items.MODULAR_TOOL);
                this.augmentTypes.add(ModTags.Items.MODULAR_WEAPON);
            }
            case Types.ARMOR -> this.augmentTypes.add(ModTags.Items.MODULAR_ARMOR);
            case Types.ARMOR_HEAD -> this.augmentTypes.add(ModTags.Items.MODULAR_HELMET);
            case Types.ARMOR_CHEST -> this.augmentTypes.add(ModTags.Items.MODULAR_CHESTPLATE);
            case Types.ARMOR_LEGS -> this.augmentTypes.add(ModTags.Items.MODULAR_LEGGINGS);
            case Types.ARMOR_FEET -> this.augmentTypes.add(ModTags.Items.MODULAR_BOOTS);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
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

    public static class Types {
        // Values for selecting type
        static public final String GENERIC = "generic";
        static public final String TOOLS = "tools";
        static public final String WEAPONS = "weapons";
        static public final String TOOLS_AND_WEAPONS = "tools_and_weapons";
        static public final String ARMOR = "armor";
        static public final String ARMOR_HEAD = "armor_head";
        static public final String ARMOR_CHEST = "armor_chest";
        static public final String ARMOR_LEGS = "armor_legs";
        static public final String ARMOR_FEET = "armor_feet";
    }
}
