package net.matt.modular_armory.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier MODULAR_CLAY = new ForgeTier(201, 4.0f, 1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ModItems.BASIC_MODULAR_PLATE.get()), BlockTags.INCORRECT_FOR_STONE_TOOL);

}
