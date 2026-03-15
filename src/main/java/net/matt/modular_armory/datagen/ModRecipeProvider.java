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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASIC_MODULAR_PLATE.get(), 2)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModItems.MODULAR_CLAY.get())
                .unlockedBy(getHasName(ModItems.MODULAR_CLAY.get()), has(ModItems.MODULAR_CLAY.get()))
                .save(pRecipeOutput);

        // Advanced Modular Plate
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_MODULAR_PLATE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.BASIC_MODULAR_PLATE.get())
                .unlockedBy("has_iron_and_basic_plate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT, ModItems.BASIC_MODULAR_PLATE.get()))
                .save(pRecipeOutput);

        // Adaptive Flesh Block - from adaptive flesh
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADAPTIVE_FLESH_BLOCK.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.ADAPTIVE_FLESH.get())
                .unlockedBy(getHasName(ModItems.ADAPTIVE_FLESH.get()), has(ModItems.ADAPTIVE_FLESH.get()))
                .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":adaptive_flesh_block_from_flesh");

        // Adaptive Flesh Plate
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADAPTIVE_FLESH_PLATE.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ModItems.ADAPTIVE_FLESH.get())
                .define('B', ModItems.ADVANCED_MODULAR_PLATE.get())
                .unlockedBy("has_adaptive_flesh_and_advanced_modular_plate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ADAPTIVE_FLESH.get(), ModItems.ADVANCED_MODULAR_PLATE.get()))
                .save(pRecipeOutput);

        // Biorite Plate
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BIORITE_PLATE.get())
                .pattern("ABD")
                .pattern("BCB")
                .pattern("DBA")
                .define('A', Blocks.SCULK)
                .define('B', Items.NETHERITE_SCRAP)
                .define('C', ModItems.ADAPTIVE_FLESH_PLATE.get())
                .define('D', Items.DIAMOND)
                .unlockedBy("has_items_for_biorite_plate", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SCULK, Items.NETHERITE_SCRAP, ModItems.ADAPTIVE_FLESH_PLATE.get(), Items.DIAMOND))
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

        // Repair Table
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REPAIR_TABLE.get())
                .pattern("AA ")
                .pattern("BB ")
                .pattern("BB ")
                .define('A', ModItems.BASIC_MODULAR_PLATE.get())
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_items_for_repair_table", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BASIC_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
                .save(pRecipeOutput);

        // Modular Assembler
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MODULAR_ASSEMBLER.get())
                .pattern("AAA")
                .pattern("BBB")
                .pattern("AAA")
                .define('A', ItemTags.PLANKS)
                .define('B', ModItems.BASIC_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_modular_assembler", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BASIC_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
                .save(pRecipeOutput);

        // Exposer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EXPOSER.get())
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', ItemTags.STONE_BRICKS)
                .define('B', ModItems.ADVANCED_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_exposer", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.STONE_BRICKS).build()))
                .save(pRecipeOutput);

        // Exposer Slot Plug
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXPOSER_SLOT_PLUG.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ItemTags.STONE_BRICKS)
                .define('B', ModItems.ADVANCED_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_exposer_slot_plug", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.STONE_BRICKS).build()))
                .save(pRecipeOutput);

        // Advanced Modular Clay Upgrade Smithing Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ModItems.BASIC_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_advanced_modular_clay_upgrade_smithing_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BASIC_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .requires(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get())
                .requires(ModItems.ADVANCED_MODULAR_PLATE.get(), 2)
                .unlockedBy("has_items_for_advanced_modular_clay_upgrade_smithing_template_duplicate", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build()))
                .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":advanced_modular_clay_upgrade_smithing_template_duplicate");

        // Advanced Modular Clay Item Upgrades
        // Transforms Modular Clay -> Advanced Modular Clay using the Template and an Advanced Modular Clay Plate
        List<Item> ModularClayItems = List.of(
                ModItems.MODULAR_CLAY_SWORD.get(),
                ModItems.MODULAR_CLAY_PICKAXE.get(),
                ModItems.MODULAR_CLAY_AXE.get(),
                ModItems.MODULAR_CLAY_SHOVEL.get(),
                ModItems.MODULAR_CLAY_HOE.get()
        );

        List<Item> AdvancedModularClayItems = List.of(
                ModItems.ADVANCED_MODULAR_CLAY_SWORD.get(),
                ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_AXE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get(),
                ModItems.ADVANCED_MODULAR_CLAY_HOE.get()
        );

        for (int i = 0; i < ModularClayItems.size(); i++) {
            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get()), // Template
                            Ingredient.of(ModularClayItems.get(i)),                                        // Base Item
                            Ingredient.of(ModItems.ADVANCED_MODULAR_PLATE.get()),                          // Addition
                            RecipeCategory.TOOLS,
                            AdvancedModularClayItems.get(i))                                               // Result
                    .unlocks(getHasName(ModItems.ADVANCED_MODULAR_PLATE.get()), has(ModItems.ADVANCED_MODULAR_PLATE.get()))
                    .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":" + getConversionRecipeName(AdvancedModularClayItems.get(i), ModularClayItems.get(i)));
        }

        // Adaptive Flesh Upgrade Smithing Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ModItems.ADAPTIVE_FLESH_PLATE.get())
                .define('B', ModItems.ADVANCED_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_adaptive_flesh_upgrade_smithing_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.ADAPTIVE_FLESH_PLATE.get()).build()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .requires(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get())
                .requires(ModItems.ADAPTIVE_FLESH_PLATE.get(), 2)
                .unlockedBy("has_items_for_adaptive_flesh_upgrade_smithing_template_duplicate", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.ADAPTIVE_FLESH_PLATE.get()).build()))
                .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":adaptive_flesh_upgrade_smithing_template_duplicate");

        // Adaptive Flesh Item Upgrades
        // Transforms Advanced Modular Clay -> Adaptive Flesh using the Template and an Adaptive Flesh Plate
        List<Item> AdaptiveFleshItems = List.of(
                ModItems.ADAPTIVE_FLESH_SWORD.get(),
                ModItems.ADAPTIVE_FLESH_PICKAXE.get(),
                ModItems.ADAPTIVE_FLESH_AXE.get(),
                ModItems.ADAPTIVE_FLESH_SHOVEL.get(),
                ModItems.ADAPTIVE_FLESH_HOE.get()
        );

        for (int i = 0; i < AdvancedModularClayItems.size(); i++) {
            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get()), // Template
                            Ingredient.of(AdvancedModularClayItems.get(i)),                                        // Base Item
                            Ingredient.of(ModItems.ADAPTIVE_FLESH_PLATE.get()),                          // Addition
                            RecipeCategory.TOOLS,
                            AdaptiveFleshItems.get(i))                                               // Result
                    .unlocks(getHasName(ModItems.ADAPTIVE_FLESH_PLATE.get()), has(ModItems.ADAPTIVE_FLESH_PLATE.get()))
                    .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":" + getConversionRecipeName(AdaptiveFleshItems.get(i), AdvancedModularClayItems.get(i)));
        }

        // Biorite Upgrade Smithing Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', ModItems.BIORITE_PLATE.get())
                .define('B', ModItems.ADAPTIVE_FLESH_PLATE.get())
                .unlockedBy("has_items_for_biorite_upgrade_smithing_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BIORITE_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.ADAPTIVE_FLESH_PLATE.get()).build()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .requires(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get())
                .requires(ModItems.BIORITE_PLATE.get(), 2)
                .unlockedBy("has_items_for_biorite_upgrade_smithing_template_duplicate", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ModItems.BIORITE_PLATE.get()).build()))
                .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":biorite_upgrade_smithing_template_duplicate");

        // Biorite Item Upgrades
        // Transforms Adaptive Flesh -> Biorite using the Template and a Biorite Plate
        List<Item> BioriteItems = List.of(
                ModItems.BIORITE_SWORD.get(),
                ModItems.BIORITE_PICKAXE.get(),
                ModItems.BIORITE_AXE.get(),
                ModItems.BIORITE_SHOVEL.get(),
                ModItems.BIORITE_HOE.get()
        );

        for (int i = 0; i < AdaptiveFleshItems.size(); i++) {
            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get()), // Template
                            Ingredient.of(AdaptiveFleshItems.get(i)),                                        // Base Item
                            Ingredient.of(ModItems.BIORITE_PLATE.get()),                          // Addition
                            RecipeCategory.TOOLS,
                            BioriteItems.get(i))                                               // Result
                    .unlocks(getHasName(ModItems.BIORITE_PLATE.get()), has(ModItems.BIORITE_PLATE.get()))
                    .save(pRecipeOutput, MattsModularArmory.MOD_ID + ":" + getConversionRecipeName(BioriteItems.get(i), AdaptiveFleshItems.get(i)));
        }
    }
}
