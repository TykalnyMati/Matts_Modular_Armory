package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {

        // Modular Clay
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MODULAR_CLAY.get(), 2)
                .requires(Items.CLAY_BALL)
                .requires(Blocks.SAND)
                .unlockedBy("has_clay_and_sand", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLAY_BALL, Blocks.SAND))
                .group("modular_clay")
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MODULAR_CLAY.get(), 4)
                .requires(ModBlocks.MODULAR_CLAY_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.MODULAR_CLAY_BLOCK.get()), has(ModBlocks.MODULAR_CLAY_BLOCK.get()))
                .group("modular_clay")
                .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":modular_clay_from_block");

        // Modular Clay Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MODULAR_CLAY_BLOCK.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.MODULAR_CLAY.get())
                .unlockedBy(getHasName(ModItems.MODULAR_CLAY.get()), has(ModItems.MODULAR_CLAY.get()))
                .save(pRecipeOutput);

        // Basic Modular Plate
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASIC_MODULAR_PLATE.get())
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModItems.MODULAR_CLAY.get())
                .unlockedBy(getHasName(ModItems.MODULAR_CLAY.get()), has(ModItems.MODULAR_CLAY.get()))
                .save(pRecipeOutput);

        // Modular Clay Extractor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MODULAR_CLAY_EXTRACTOR.get())
                .pattern("A  ")
                .pattern(" B ")
                .pattern("  B")
                .define('A', ModItems.BASIC_MODULAR_PLATE.get())
                .define('B', Items.STICK)
                .unlockedBy("has_plate_and_stick", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BASIC_MODULAR_PLATE.get(), Items.STICK))
                .save(pRecipeOutput);

        // Flesh Meal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLESH_MEAL.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.ROTTEN_FLESH)
                .define('B', Items.BONE_MEAL)
                .unlockedBy("has_bone_meal_and_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL, Items.ROTTEN_FLESH))
                .save(pRecipeOutput);

        // Adaptive Flesh Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADAPTIVE_FLESH_BLOCK.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("DBD")
                .define('A', Blocks.NETHER_WART)
                .define('B', Items.ROTTEN_FLESH)
                .define('C', ModBlocks.MODULAR_CLAY_BLOCK.get())
                .define('D', Items.BLAZE_POWDER)
                .unlockedBy("has_items_for_adaptive_flesh_block", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.NETHER_WART, Items.ROTTEN_FLESH, ModBlocks.MODULAR_CLAY_BLOCK.get(), Items.BLAZE_POWDER))
                .save(pRecipeOutput);

        // Augment Table
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AUGMENT_TABLE.get())
                .pattern("AAA")
                .pattern("B B")
                .pattern("B B")
                .define('A', ModItems.BASIC_MODULAR_PLATE.get())
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_items_for_augment_table", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BASIC_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
                .save(pRecipeOutput);
    }
}
