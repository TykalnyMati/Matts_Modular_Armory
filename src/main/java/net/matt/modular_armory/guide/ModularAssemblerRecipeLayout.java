package net.matt.modular_armory.guide;

import guideme.document.block.LytBlock;
import guideme.document.block.recipes.LytStandardRecipeBox;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.recipe.ModularAssemblerRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;

public class ModularAssemblerRecipeLayout {
    public static LytBlock create(RecipeHolder<ModularAssemblerRecipe> holder) {
        ModularAssemblerRecipe recipe = holder.value();

        ItemStack[] inputsWithAmount = Arrays.stream(recipe.inputItem().getItems())
                .map(stack -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(recipe.amount());
                    copy.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
                    return copy;
                })
                .toArray(ItemStack[]::new);

        Ingredient inputIngredientWithCount = Ingredient.of(inputsWithAmount);

        return LytStandardRecipeBox.builder()
                .icon(ModBlocks.MODULAR_ASSEMBLER.get())
                .title(ModBlocks.MODULAR_ASSEMBLER.get().getName().getString())
                .input(inputIngredientWithCount)
                .outputFromResultOf(holder)
                .build(holder);
    }
}
