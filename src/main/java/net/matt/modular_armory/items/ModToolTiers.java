package net.matt.modular_armory.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier MODULAR_CLAY = new ForgeTier(201, 4.0f, 1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ModItems.BASIC_MODULAR_PLATE.get()), BlockTags.INCORRECT_FOR_STONE_TOOL);
    public static final Tier ADVANCED_MODULAR_CLAY = new ForgeTier(351, 6.0f, 2.0f, 0, BlockTags.NEEDS_IRON_TOOL, null, BlockTags.INCORRECT_FOR_IRON_TOOL);
    public static final Tier ADAPTIVE_FLESH = new ForgeTier(1051, 8.0f, 3.0f, 0, BlockTags.NEEDS_DIAMOND_TOOL, null, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

}
