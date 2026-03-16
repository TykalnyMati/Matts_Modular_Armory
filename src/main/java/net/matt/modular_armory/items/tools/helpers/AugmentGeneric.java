package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AugmentGeneric extends Item {
    String descriptionLocation;
    String typeLocation;
    List<Integer> augmentLevels;
    public AugmentGeneric(Properties pProperties, String augmentName, String augmentType, List<Integer> augmentLevels) {
        super(pProperties);
        this.descriptionLocation = "tooltip." + MattsModularArmory.MOD_ID + ".description." + augmentName;
        this.typeLocation = "tooltip." + MattsModularArmory.MOD_ID + ".augment_type." + augmentType;
        this.augmentLevels = augmentLevels;
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
}
