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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
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
        // Guide Book
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GUIDE_BOOK.get())
                .requires(Items.BOOK)
                .requires(ModItems.BASIC_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_guide_book", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.BOOK,
                        ModItems.BASIC_MODULAR_PLATE.get()))
                .save(pRecipeOutput);

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

        // Adaptive Flesh Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFECTIOUS_ADAPTIVE_FLESH_BLOCK.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.ADAPTIVE_FLESH_BLOCK.get())
                .define('B', ModItems.ADAPTIVE_FLESH.get())
                .unlockedBy("has_items_for_infectious_adaptive_flesh_block", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.ADAPTIVE_FLESH_BLOCK.get(), ModItems.ADAPTIVE_FLESH.get()))
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

        // Modular Processing Unit
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MODULAR_PROCESSING_UNIT.get())
                .pattern("ABA")
                .pattern("CCC")
                .pattern("D D")
                .define('A', Items.QUARTZ)
                .define('B', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('C', Items.GOLD_INGOT)
                .define('D', Items.COPPER_INGOT)
                .unlockedBy("has_items_for_modular_processing_unit", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.QUARTZ,
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.GOLD_INGOT,
                        Items.COPPER_INGOT
                ))
                .save(pRecipeOutput);

        // Modular Capacitor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MODULAR_CAPACITOR.get(), 2)
                .pattern(" A ")
                .pattern("B B")
                .pattern("B B")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.COPPER_INGOT)
                .unlockedBy("has_items_for_modular_capacitor", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.COPPER_INGOT
                ))
                .save(pRecipeOutput);

        // Modular Miner
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MODULAR_MINER.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("AEA")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ModItems.MODULAR_PROCESSING_UNIT.get())
                .define('C', Items.REDSTONE)
                .define('D', ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .define('E', ModItems.MODULAR_CAPACITOR.get())
                .unlockedBy("has_items_for_modular_miner", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        ModItems.MODULAR_PROCESSING_UNIT.get(),
                        Items.REDSTONE,
                        ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get(),
                        ModItems.MODULAR_CAPACITOR.get()
                ))
                .save(pRecipeOutput);

        // Modular Builder
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MODULAR_BUILDER.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("AEA")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ModItems.MODULAR_PROCESSING_UNIT.get())
                .define('C', Items.REDSTONE)
                .define('D', Blocks.DISPENSER)
                .define('E', ModItems.MODULAR_CAPACITOR.get())
                .unlockedBy("has_items_for_modular_builder", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        ModItems.MODULAR_PROCESSING_UNIT.get(),
                        Items.REDSTONE,
                        Blocks.DISPENSER,
                        ModItems.MODULAR_CAPACITOR.get()
                ))
                .save(pRecipeOutput);

        // Basic Filter
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASIC_FILTER.get())
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.STRING)
                .define('B', ModItems.ADDON_TEMPLATE.get())
                .define('C', Blocks.HOPPER)
                .unlockedBy("has_items_for_basic_filter", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.STRING,
                        ModItems.ADDON_TEMPLATE.get(),
                        Blocks.HOPPER))
                .save(pRecipeOutput);

        // Addon Template
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADDON_TEMPLATE.get(), 3)
                .pattern("A")
                .pattern("B")
                .pattern("B")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.PAPER)
                .unlockedBy("has_items_for_addon_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.PAPER))
                .save(pRecipeOutput);

        // Blank Range Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLANK_RANGE_ADDON.get())
                .requires(ModItems.ADDON_TEMPLATE.get())
                .requires(Blocks.PISTON)
                .unlockedBy("has_items_for_blank_range_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADDON_TEMPLATE.get(),
                        Blocks.PISTON))
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
                .unlockedBy("has_items_for_range_addon_copper", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.COPPER_INGOT,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Iron
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_IRON.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_iron", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.IRON_INGOT,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Quartz
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_QUARTZ.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.QUARTZ)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_quartz", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.QUARTZ,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Redstone
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_REDSTONE.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.REDSTONE)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_redstone", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.REDSTONE,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Glowstone
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_GLOWSTONE.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.GLOWSTONE_DUST)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_glowstone", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.GLOWSTONE_DUST,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Gold
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_GOLD.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.GOLD_INGOT)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_gold", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.GOLD_INGOT,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Amethyst
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_AMETHYST.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_amethyst", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.AMETHYST_SHARD,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Lapis
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_LAPIS.get())
                .pattern("A A")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.LAPIS_LAZULI)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_lapis", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.LAPIS_LAZULI,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Diamond
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_DIAMOND.get())
                .pattern("ABA")
                .define('A', Items.DIAMOND)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.DIAMOND,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Range Addon Emerald
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RANGE_ADDON_EMERALD.get())
                .pattern("ABA")
                .define('A', Items.EMERALD)
                .define('B', ModItems.BLANK_RANGE_ADDON.get())
                .unlockedBy("has_items_for_range_addon_emerald", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.EMERALD,
                        ModItems.BLANK_RANGE_ADDON.get()))
                .save(pRecipeOutput);

        // Blank Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.ADDON_TEMPLATE.get())
                .requires(ModItems.ADVANCED_MODULAR_PLATE.get())
                .unlockedBy("has_items_for_blank_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADDON_TEMPLATE.get(),
                        ModItems.ADVANCED_MODULAR_PLATE.get()))
                .save(pRecipeOutput);

        // Silk Touch Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILK_TOUCH_AUGMENT_ADDON.get())
                .requires(ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.SILK_TOUCH_AUGMENT.get())
                .unlockedBy("has_items_for_silk_touch_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.BLANK_AUGMENT_ADDON.get(),
                        ModItems.SILK_TOUCH_AUGMENT.get()))
                .save(pRecipeOutput);

        // Fortune Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FORTUNE_AUGMENT_ADDON.get())
                .requires(ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.FORTUNE_AUGMENT.get())
                .unlockedBy("has_items_for_fortune_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.BLANK_AUGMENT_ADDON.get(),
                        ModItems.FORTUNE_AUGMENT.get()))
                .save(pRecipeOutput);

        // Auto Smelt Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AUTO_SMELT_AUGMENT_ADDON.get())
                .requires(ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.AUTO_SMELT_AUGMENT.get())
                .unlockedBy("has_items_for_auto_smelt_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.BLANK_AUGMENT_ADDON.get(),
                        ModItems.AUTO_SMELT_AUGMENT.get()))
                .save(pRecipeOutput);

        // Auto Smelting Fortune Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AUTO_SMELTING_FORTUNE_AUGMENT_ADDON.get())
                .requires(ModItems.AUTO_SMELT_AUGMENT_ADDON.get())
                .requires(ModItems.ADAPTIVE_FLESH_PLATE.get())
                .requires(ModItems.FORTUNE_AUGMENT_ADDON.get())
                .unlockedBy("has_items_for_auto_smelting_fortune_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.AUTO_SMELT_AUGMENT_ADDON.get(),
                        ModItems.ADAPTIVE_FLESH_PLATE.get(),
                        ModItems.FORTUNE_AUGMENT_ADDON.get()))
                .save(pRecipeOutput);

        // Void Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VOID_AUGMENT_ADDON.get())
                .requires(ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.VOID_AUGMENT.get())
                .unlockedBy("has_items_for_void_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.BLANK_AUGMENT_ADDON.get(),
                        ModItems.VOID_AUGMENT.get()))
                .save(pRecipeOutput);

        // Clay Extractor Augment Addon
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CLAY_EXTRACTOR_AUGMENT_ADDON.get())
                .requires(ModItems.BLANK_AUGMENT_ADDON.get())
                .requires(ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get())
                .unlockedBy("has_items_for_clay_extractor_augment_addon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.BLANK_AUGMENT_ADDON.get(),
                        ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get()))
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
                ModItems.MODULAR_CLAY_BOOTS.get(),
                ModItems.MODULAR_CLAY_BOW.get()
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
                ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get(),
                ModItems.ADVANCED_MODULAR_CLAY_BOW.get()
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
                ModItems.ADAPTIVE_FLESH_BOOTS.get(),
                ModItems.ADAPTIVE_FLESH_BOW.get()
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
                ModItems.BIORITE_BOOTS.get(),
                ModItems.BIORITE_BOW.get()
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
                .pattern("BBB")
                .pattern("CAC")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', ItemTags.WOOL)
                .define('C', Items.EMERALD)
                .unlockedBy("has_items_for_silk_touch_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModItems.ADVANCED_MODULAR_PLATE.get()).build(),
                        ItemPredicate.Builder.item().of(ItemTags.WOOL).build(),
                        ItemPredicate.Builder.item().of(Items.EMERALD).build()))
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
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.PISTON)
                .unlockedBy("has_items_for_knockback_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.PISTON))
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

        // Power Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.POWER_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.ARROW)
                .define('C', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_items_for_power_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.ARROW,
                        Blocks.QUARTZ_BLOCK))
                .save(pRecipeOutput);

        // Punch Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PUNCH_AUGMENT.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.PISTON)
                .define('C', Items.ARROW)
                .unlockedBy("has_items_for_punch_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.PISTON,
                        Items.ARROW))
                .save(pRecipeOutput);

        // Flame Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLAME_AUGMENT.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.BLAZE_POWDER)
                .define('C', Items.ARROW)
                .unlockedBy("has_items_for_flame_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.BLAZE_POWDER,
                        Items.ARROW))
                .save(pRecipeOutput);

        // Infinity Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFINITY_AUGMENT.get())
                .pattern("BCB")
                .pattern("BAB")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.ARROW)
                .define('C', Items.EXPERIENCE_BOTTLE)
                .unlockedBy("has_items_for_infinity_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.ARROW,
                        Items.EXPERIENCE_BOTTLE))
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

        // Void Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOID_AUGMENT.get())
                .pattern("BBB")
                .pattern("CAC")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.OBSIDIAN)
                .define('C', Items.ENDER_PEARL)
                .unlockedBy("has_items_for_void_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.OBSIDIAN,
                        Items.ENDER_PEARL))
                .save(pRecipeOutput);

        // Magnet Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGNET_AUGMENT.get())
                .pattern("B B")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.REDSTONE_BLOCK)
                .define('C', Blocks.IRON_BLOCK)
                .unlockedBy("has_items_for_magnet_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.REDSTONE_BLOCK,
                        Blocks.IRON_BLOCK))
                .save(pRecipeOutput);

        // Sweeping Edge Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SWEEPING_EDGE_AUGMENT.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.WIND_CHARGE)
                .unlockedBy("has_items_for_sweeping_edge_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.WIND_CHARGE))
                .save(pRecipeOutput);

        // Poison Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.POISON_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("DCD")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.NETHER_WART)
                .define('C', Items.SPIDER_EYE)
                .define('D', Items.BLAZE_POWDER)
                .unlockedBy("has_items_for_poison_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.NETHER_WART,
                        Items.SPIDER_EYE,
                        Items.BLAZE_POWDER))
                .save(pRecipeOutput);

        // Wither Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WITHER_AUGMENT.get())
                .pattern("BCB")
                .pattern("DAD")
                .pattern("BDB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.COAL)
                .define('C', Blocks.WITHER_SKELETON_SKULL)
                .define('D', Blocks.WITHER_ROSE)
                .unlockedBy("has_items_for_wither_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.COAL,
                        Blocks.WITHER_SKELETON_SKULL,
                        Blocks.WITHER_ROSE))
                .save(pRecipeOutput);

        // Rapid Fire Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RAPID_FIRE_AUGMENT.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BCB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.STRING)
                .define('C', Items.NETHER_STAR)
                .unlockedBy("has_items_for_rapid_fire_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.STRING,
                        Items.NETHER_STAR))
                .save(pRecipeOutput);

        // Vitality Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VITALITY_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("DED")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.NETHER_WART)
                .define('C', Items.GLISTERING_MELON_SLICE)
                .define('D', Items.BLAZE_POWDER)
                .define('E', Items.NETHER_STAR)
                .unlockedBy("has_items_for_vitality_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.NETHER_WART,
                        Items.GLISTERING_MELON_SLICE,
                        Items.BLAZE_POWDER,
                        Items.NETHER_STAR))
                .save(pRecipeOutput);

        // Gilded Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GILDED_AUGMENT.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.GOLD_INGOT)
                .unlockedBy("has_items_for_gilded_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.GOLD_INGOT))
                .save(pRecipeOutput);

        // Super Gilded Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUPER_GILDED_AUGMENT.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', ModItems.GILDED_AUGMENT.get())
                .define('B', Items.NETHERITE_SCRAP)
                .define('C', Blocks.GOLD_BLOCK)
                .unlockedBy("has_items_for_super_gilded_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.NETHERITE_SCRAP,
                        Blocks.GOLD_BLOCK))
                .save(pRecipeOutput);

        // Pumpkin Lens Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PUMPKIN_LENS_AUGMENT.get())
                .pattern("BBB")
                .pattern("CAC")
                .pattern("BBB")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.ORANGE_STAINED_GLASS_PANE)
                .define('C', Blocks.CARVED_PUMPKIN)
                .unlockedBy("has_items_for_pumpkin_lens_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.ORANGE_STAINED_GLASS_PANE,
                        Blocks.CARVED_PUMPKIN))
                .save(pRecipeOutput);

        // Step Up Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STEP_UP_AUGMENT.get())
                .pattern("BAB")
                .pattern("B B")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Blocks.PISTON)
                .unlockedBy("has_items_for_step_up_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Blocks.PISTON))
                .save(pRecipeOutput);

        // Void Tether Augment
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOID_TETHER_AUGMENT.get())
                .pattern("BBB")
                .pattern("CAC")
                .pattern("C C")
                .define('A', ModItems.ADVANCED_MODULAR_PLATE.get())
                .define('B', Items.SHULKER_SHELL)
                .define('C', Items.WIND_CHARGE)
                .unlockedBy("has_items_for_void_tether_augment", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.ADVANCED_MODULAR_PLATE.get(),
                        Items.SHULKER_SHELL,
                        Items.WIND_CHARGE))
                .save(pRecipeOutput);
    }
}
