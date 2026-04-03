package net.matt.modular_armory.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
    public static final Tier MODULAR_CLAY = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            201, 4.0f, 1.0f, 0,
            () -> Ingredient.EMPTY
    );

    public static final Tier ADVANCED_MODULAR_CLAY = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            351, 6.0f, 2.0f, 0,
            () -> Ingredient.EMPTY
    );

    public static final Tier ADAPTIVE_FLESH = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1051, 8.0f, 3.0f, 0,
            () -> Ingredient.EMPTY
    );

    public static final Tier BIORITE = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2031, 10.0f, 4.0f, 0,
            () -> Ingredient.EMPTY
    );
}
