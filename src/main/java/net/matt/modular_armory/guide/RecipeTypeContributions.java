package net.matt.modular_armory.guide;

import guideme.compiler.tags.RecipeTypeMappingSupplier;
import net.matt.modular_armory.recipe.ModRecipes;

public class RecipeTypeContributions implements RecipeTypeMappingSupplier {
    @Override
    public void collect(RecipeTypeMappings mappings) {
        mappings.add(ModRecipes.MODULAR_ASSEMBLER_TYPE.get(), ModularAssemblerRecipeLayout::create);
    }
}
