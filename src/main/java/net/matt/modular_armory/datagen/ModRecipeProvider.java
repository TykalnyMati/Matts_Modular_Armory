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
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.IRON_NUGGET)
                .define('B', ModItems.BASIC_MODULAR_PLATE.get())
                .unlockedBy("has_iron_and_basic_plate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_NUGGET, ModItems.BASIC_MODULAR_PLATE.get()))
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLESH_MEAL.get(), 3)
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

        // TODO add modular miner recipe

        // Addon Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADDON_TEMPLATE.get(), 3)
                .pattern("A")
                .pattern("B")
                .pattern("B")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.PAPER)
                .unlockedBy("has_items_for_addon_template", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ADVANCED_MODULAR_PLATE.get(), Items.PAPER))
                .save(pRecipeOutput);

        // Blank Range Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLANK_RANGE_ADDON.get())
                .requires(ModItems.ADDON_TEMPLATE.get())
                .requires(Blocks.PISTON)
                .unlockedBy("has_items_for_blank_range_addon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ADDON_TEMPLATE.get(), Blocks.PISTON))
                .save(pRecipeOutput);

        // Range Addon Coal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_COAL.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', ItemTags.COALS)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_coal", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ItemTags.COALS).build(),
                        ItemPredicate.Builder.item().of(ModItems.BLANK_RANGE_ADDON.get()).build()))
                .save(pRecipeOutput);

        // Range Addon Copper
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_COPPER.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.COPPER_INGOT)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_copper", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Iron
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_IRON.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Quartz
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_QUARTZ.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.QUARTZ)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_quartz", InventoryChangeTrigger.TriggerInstance.hasItems(Items.QUARTZ, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Redstone
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_REDSTONE.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.REDSTONE)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_redstone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.REDSTONE, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Glowstone
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_GLOWSTONE.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.GLOWSTONE_DUST)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_glowstone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLOWSTONE_DUST, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Gold
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_GOLD.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.GOLD_INGOT)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Amethyst
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_AMETHYST.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_amethyst", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Lapis
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_LAPIS.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.LAPIS_LAZULI)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_lapis", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LAPIS_LAZULI, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Diamond
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_DIAMOND.get())
                .pattern("ABA")
                .define('A', Items.DIAMOND)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND, ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Emerald
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_EMERALD.get())
                .pattern("ABA")
                .define('A', Items.EMERALD)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_emerald", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EMERALD, ModItems.BLANK_RANGE_ADDON.get()))
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
                ModItems.MODULAR_CLAY_HOE.get(),
                ModItems.MODULAR_CLAY_HELMET.get(),
                ModItems.MODULAR_CLAY_CHESTPLATE.get(),
                ModItems.MODULAR_CLAY_LEGGINGS.get(),
                ModItems.MODULAR_CLAY_BOOTS.get()
        );

        List<Item> AdvancedModularClayItems = List.of(
                ModItems.ADVANCED_MODULAR_CLAY_SWORD.get(),
                ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_AXE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get(),
                ModItems.ADVANCED_MODULAR_CLAY_HOE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_HELMET.get(),
                ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get(),
                ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get(),
                ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get()
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
                ModItems.ADAPTIVE_FLESH_HOE.get(),
                ModItems.ADAPTIVE_FLESH_HELMET.get(),
                ModItems.ADAPTIVE_FLESH_CHESTPLATE.get(),
                ModItems.ADAPTIVE_FLESH_LEGGINGS.get(),
                ModItems.ADAPTIVE_FLESH_BOOTS.get()
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
                ModItems.BIORITE_HOE.get(),
                ModItems.BIORITE_HELMET.get(),
                ModItems.BIORITE_CHESTPLATE.get(),
                ModItems.BIORITE_LEGGINGS.get(),
                ModItems.BIORITE_BOOTS.get()
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

        // Augments
        // Unbreaking Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UNBREAKING_AUGMENT.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.OBSIDIAN)
                .define('C', Blocks.IRON_BLOCK)
                .unlockedBy("has_items_for_unbreaking_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.OBSIDIAN,
                        Blocks.IRON_BLOCK))
                .save(pRecipeOutput);

        // Mending Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MENDING_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.EXPERIENCE_BOTTLE)
                .define('C', Blocks.LAPIS_BLOCK)
                .unlockedBy("has_items_for_mending_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.EXPERIENCE_BOTTLE,
                        Blocks.LAPIS_BLOCK))
                .save(pRecipeOutput);

        // Efficiency Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EFFICIENCY_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.REDSTONE)
                .define('C', Blocks.REDSTONE_BLOCK)
                .unlockedBy("has_items_for_efficiency_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.REDSTONE,
                        Blocks.REDSTONE_BLOCK))
                .save(pRecipeOutput);

        // Silk Touch Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILK_TOUCH_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.STRING)
                .define('C', ItemTags.WOOL)
                .unlockedBy("has_items_for_silk_touch_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(Items.STRING).build(),
                        ItemPredicate.Builder.item().of(ItemTags.WOOL).build()))
                .save(pRecipeOutput);

        // Fortune Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FORTUNE_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.LAPIS_LAZULI)
                .define('C', Blocks.LAPIS_BLOCK)
                .unlockedBy("has_items_for_fortune_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.LAPIS_LAZULI,
                        Blocks.LAPIS_BLOCK))
                .save(pRecipeOutput);

        // Sharpness Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHARPNESS_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.QUARTZ)
                .define('C', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_items_for_sharpness_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.QUARTZ,
                        Blocks.QUARTZ_BLOCK))
                .save(pRecipeOutput);

        // Smite Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMITE_AUGMENT.get())
                .pattern("BDC")
                .pattern("DAD")
                .pattern("CDB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.ROTTEN_FLESH)
                .define('C', Items.BONE)
                .define('D', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_items_for_smite_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.ROTTEN_FLESH,
                        Items.BONE,
                        Blocks.QUARTZ_BLOCK))
                .save(pRecipeOutput);

        // Bane of Arthropods Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BANE_OF_ARTHROPODS_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.STRING)
                .define('C', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_items_for_bane_of_arthropods_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.STRING,
                        Blocks.QUARTZ_BLOCK))
                .save(pRecipeOutput);

        // Impaling Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IMPALING_AUGMENT.get())
                .pattern("BDC")
                .pattern("DAD")
                .pattern("CDB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ItemTags.FISHES)
                .define('C', Items.PRISMARINE_SHARD)
                .define('D', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_items_for_impaling_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.FISHES).build(),
                        ItemPredicate.Builder.item().of(Items.PRISMARINE_SHARD).build(),
                        ItemPredicate.Builder.item().of(Blocks.QUARTZ_BLOCK).build()))
                .save(pRecipeOutput);

        // Looting Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LOOTING_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.QUARTZ)
                .define('C', Blocks.LAPIS_BLOCK)
                .unlockedBy("has_items_for_looting_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.QUARTZ,
                        Blocks.LAPIS_BLOCK))
                .save(pRecipeOutput);

        // Knockback Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.KNOCKBACK_AUGMENT.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.PISTON)
                .define('C', Items.QUARTZ)
                .unlockedBy("has_items_for_knockback_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.PISTON,
                        Items.QUARTZ))
                .save(pRecipeOutput);

        // Fire Aspect Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRE_ASPECT_AUGMENT.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.BLAZE_POWDER)
                .unlockedBy("has_items_for_fire_aspect_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.BLAZE_POWDER))
                .save(pRecipeOutput);

        // Protection Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PROTECTION_AUGMENT.get())
                .pattern("BCB")
                .pattern("BAB")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.IRON_INGOT)
                .define('C', Blocks.IRON_BLOCK)
                .unlockedBy("has_items_for_protection_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.IRON_INGOT,
                        Blocks.IRON_BLOCK))
                .save(pRecipeOutput);

        // Blast Protection Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLAST_PROTECTION_AUGMENT.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.PROTECTION_AUGMENT.get())
                .define('B', Blocks.OBSIDIAN)
                .unlockedBy("has_items_for_blast_protection_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.PROTECTION_AUGMENT.get(),
                        Blocks.OBSIDIAN))
                .save(pRecipeOutput);

        // Projectile Protection Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PROJECTILE_PROTECTION_AUGMENT.get())
                .pattern("BCB")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.PROTECTION_AUGMENT.get())
                .define('B', ItemTags.PLANKS)
                .define('C', Items.IRON_INGOT)
                .unlockedBy("has_items_for_projectile_protection_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.PROTECTION_AUGMENT.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.PLANKS).build(),
                        ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(pRecipeOutput);

        // Fire Protection Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRE_PROTECTION_AUGMENT.get())
                .pattern(" B ")
                .pattern("CAC")
                .pattern(" B ")
                .define('A', ModItems.PROTECTION_AUGMENT.get())
                .define('B', Items.BLAZE_POWDER)
                .define('C', Blocks.MAGMA_BLOCK)
                .unlockedBy("has_items_for_fire_protection_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.PROTECTION_AUGMENT.get(),
                        Items.BLAZE_POWDER,
                        Blocks.MAGMA_BLOCK))
                .save(pRecipeOutput);

        // Thorns Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THORNS_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.CACTUS)
                .unlockedBy("has_items_for_thorns_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.CACTUS))
                .save(pRecipeOutput);

        // Respiration Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RESPIRATION_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.GLASS_BOTTLE)
                .unlockedBy("has_items_for_respiration_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.GLASS_BOTTLE))
                .save(pRecipeOutput);

        // Aqua Affinity Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AQUA_AFFINITY_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.PRISMARINE_SHARD)
                .define('C', Blocks.PRISMARINE)
                .unlockedBy("has_items_for_aqua_affinity_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.PRISMARINE_SHARD,
                        Blocks.PRISMARINE))
                .save(pRecipeOutput);

        // Swift Sneak Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SWIFT_SNEAK_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.SCULK)
                .define('C', Items.ECHO_SHARD)
                .unlockedBy("has_items_for_swift_sneak_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.SCULK,
                        Items.ECHO_SHARD))
                .save(pRecipeOutput);

        // Feather Falling Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FEATHER_FALLING_AUGMENT.get())
                .pattern("B B")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', ModItems.PROTECTION_AUGMENT.get())
                .define('B', Items.FEATHER)
                .define('C', ItemTags.WOOL)
                .unlockedBy("has_items_for_feather_falling_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.PROTECTION_AUGMENT.get()),
                        ItemPredicate.Builder.item().of(Items.FEATHER),
                        ItemPredicate.Builder.item().of(ItemTags.WOOL)))
                .save(pRecipeOutput);

        // Frost Walker Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FROST_WALKER_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.PACKED_ICE)
                .define('C', Blocks.BLUE_ICE)
                .unlockedBy("has_items_for_frost_walker_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.PACKED_ICE,
                        Blocks.BLUE_ICE))
                .save(pRecipeOutput);

        // Depth Strider Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DEPTH_STRIDER_AUGMENT.get())
                .pattern("B B")
                .pattern("BAB")
                .pattern("C C")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.PRISMARINE_SHARD)
                .define('C', ItemTags.FISHES)
                .unlockedBy("has_items_for_depth_strider_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()),
                        ItemPredicate.Builder.item().of(Items.PRISMARINE_SHARD),
                        ItemPredicate.Builder.item().of(ItemTags.FISHES)))
                .save(pRecipeOutput);

        // Soul Speed Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOUL_SPEED_AUGMENT.get())
                .pattern("B B")
                .pattern("BAB")
                .pattern("C C")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.GOLD_INGOT)
                .define('C', Blocks.SOUL_SAND)
                .unlockedBy("has_items_for_soul_speed_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.GOLD_INGOT,
                        Blocks.SOUL_SAND))
                .save(pRecipeOutput);

        // Auto Smelt Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_SMELT_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.BLAZE_POWDER)
                .define('C', Blocks.FURNACE)
                .unlockedBy("has_items_for_auto_smelt_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.BLAZE_POWDER,
                        Blocks.FURNACE))
                .save(pRecipeOutput);

        // Diamond Sharpened Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_SHARPENED_AUGMENT.get())
                .pattern(" B ")
                .pattern("CAC")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.IRON_INGOT)
                .define('C', Items.DIAMOND)
                .unlockedBy("has_items_for_diamond_sharpened_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.IRON_INGOT,
                        Items.DIAMOND))
                .save(pRecipeOutput);

        // Modular Clay Extractor Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get())
                .pattern("BBB")
                .pattern("CAC")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ModItems.MODULAR_CLAY.get())
                .define('C', ModItems.MODULAR_CLAY_EXTRACTOR.get())
                .unlockedBy("has_items_for_modular_clay_extractor_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        ModItems.MODULAR_CLAY.get(),
                        ModItems.MODULAR_CLAY_EXTRACTOR.get()))
                .save(pRecipeOutput);

        // Scaffolding Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SCAFFOLDING_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.SCAFFOLDING)
                .unlockedBy("has_items_for_scaffolding_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.SCAFFOLDING))
                .save(pRecipeOutput);

        // Illuminator Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ILLUMINATOR_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.GLOWSTONE_DUST)
                .define('C', Blocks.GLOWSTONE)
                .unlockedBy("has_items_for_illuminator_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.GLOWSTONE_DUST,
                        Blocks.GLOWSTONE))
                .save(pRecipeOutput);

        // Magnet Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGNET_AUGMENT.get())
                .pattern("B B")
                .pattern("CAC")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.REDSTONE_BLOCK)
                .define('C', Blocks.IRON_BLOCK)
                .unlockedBy("has_items_for_magnet_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.REDSTONE_BLOCK,
                        Blocks.IRON_BLOCK))
                .save(pRecipeOutput);
    }
}
