package net.matt.modular_armory.datagen.recipe_builders;

import net.matt.modular_armory.recipe.ExposerRecipe;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExposerRecipeBuilder implements RecipeBuilder {
    private final Ingredient inputItem;
    private final List<BlockState> surroundings = new ArrayList<>();
    private final ItemStack result;
    private final int time;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public ExposerRecipeBuilder(Item inputItem, Item result, int count, int time) {
        this.inputItem = Ingredient.of(inputItem);
        this.result = new ItemStack(result, count);
        this.time = time;
    }

    public static ExposerRecipeBuilder exposer(Item inputItem, Item result, int count, int time) {
        return new ExposerRecipeBuilder(inputItem, result, count, time);
    }

    public static ExposerRecipeBuilder exposer(Item inputItem, Item result, int time) {
        return new ExposerRecipeBuilder(inputItem, result, 1, time);
    }

    public ExposerRecipeBuilder surrounding(Block block) {
        this.surroundings.add(block.defaultBlockState());
        return this;
    }

    public ExposerRecipeBuilder surrounding(BlockState state) {
        this.surroundings.add(state);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    public void save(RecipeOutput recipeOutput) {
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(this.result.getItem());
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                itemKey.getNamespace(),
                "exposer/" + itemKey.getPath() + "_from_exposer"
        );
        this.save(recipeOutput, recipeId);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        if (this.surroundings.isEmpty()) {
            throw new IllegalStateException("Exposer recipe " + id + " must have at least one surrounding block");
        }

        Advancement.Builder advancementBuilder = this.advancement
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        ExposerRecipe recipe = new ExposerRecipe(inputItem, List.copyOf(surroundings), result, time);
        recipeOutput.accept(id, recipe, advancementBuilder.build(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + id.getPath())));
    }
}
