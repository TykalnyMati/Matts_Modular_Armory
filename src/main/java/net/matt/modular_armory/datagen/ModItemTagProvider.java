package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, MattsModularArmory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // Modular Items tag
        tag(ModTags.Items.MODULAR_ITEMS)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.MODULAR_CLAY_HOE.get())
                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.MODULAR_CLAY_HELMET.get())
                .add(ModItems.MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.MODULAR_CLAY_BOOTS.get())
                .add(ModItems.MODULAR_CLAY_BOW.get())
                .add(ModItems.MODULAR_CLAY_JAVELIN.get())

                .add(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HOE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOW.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get())

                .add(ModItems.ADAPTIVE_FLESH_PICKAXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_SHOVEL.get())
                .add(ModItems.ADAPTIVE_FLESH_HOE.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_HELMET.get())
                .add(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get())
                .add(ModItems.ADAPTIVE_FLESH_LEGGINGS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOOTS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOW.get())

                .add(ModItems.BIORITE_PICKAXE.get())
                .add(ModItems.BIORITE_AXE.get())
                .add(ModItems.BIORITE_SHOVEL.get())
                .add(ModItems.BIORITE_HOE.get())
                .add(ModItems.BIORITE_SWORD.get())
                .add(ModItems.BIORITE_HELMET.get())
                .add(ModItems.BIORITE_CHESTPLATE.get())
                .add(ModItems.BIORITE_LEGGINGS.get())
                .add(ModItems.BIORITE_BOOTS.get())
                .add(ModItems.BIORITE_BOW.get())

                .add(ModItems.ADVANCED_MODULAR_CLAY_SHEARS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LIGHTER.get());

        // Modular Tools tag
        tag(ModTags.Items.MODULAR_TOOL)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.MODULAR_CLAY_HOE.get())

                .add(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HOE.get())

                .add(ModItems.ADAPTIVE_FLESH_PICKAXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_SHOVEL.get())
                .add(ModItems.ADAPTIVE_FLESH_HOE.get())

                .add(ModItems.BIORITE_PICKAXE.get())
                .add(ModItems.BIORITE_AXE.get())
                .add(ModItems.BIORITE_SHOVEL.get())
                .add(ModItems.BIORITE_HOE.get());

        // Modular Weapons tag
        tag(ModTags.Items.MODULAR_WEAPON)
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.BIORITE_AXE.get())

                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.BIORITE_SWORD.get())

                .add(ModItems.MODULAR_CLAY_JAVELIN.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get());

        tag(ModTags.Items.MODULAR_BOW)
                .add(ModItems.MODULAR_CLAY_BOW.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOW.get())
                .add(ModItems.ADAPTIVE_FLESH_BOW.get())
                .add(ModItems.BIORITE_BOW.get());

        tag(ModTags.Items.MODULAR_RANGED_WEAPON)
                .add(ModItems.MODULAR_CLAY_BOW.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOW.get())
                .add(ModItems.ADAPTIVE_FLESH_BOW.get())
                .add(ModItems.BIORITE_BOW.get());

        tag(ModTags.Items.MODULAR_SWORD)
                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.BIORITE_SWORD.get());

        tag(ModTags.Items.MODULAR_JAVELIN)
                .add(ModItems.MODULAR_CLAY_JAVELIN.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get());

        // Modular Armor tag
        tag(ModTags.Items.MODULAR_ARMOR)
                .add(ModItems.MODULAR_CLAY_HELMET.get())
                .add(ModItems.MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.MODULAR_CLAY_BOOTS.get())

                .add(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get())

                .add(ModItems.ADAPTIVE_FLESH_HELMET.get())
                .add(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get())
                .add(ModItems.ADAPTIVE_FLESH_LEGGINGS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOOTS.get())

                .add(ModItems.BIORITE_HELMET.get())
                .add(ModItems.BIORITE_CHESTPLATE.get())
                .add(ModItems.BIORITE_LEGGINGS.get())
                .add(ModItems.BIORITE_BOOTS.get());

        // Modular Items Tier 0
        tag(ModTags.Items.MODULAR_TIER_0)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.MODULAR_CLAY_HOE.get())
                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.MODULAR_CLAY_HELMET.get())
                .add(ModItems.MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.MODULAR_CLAY_BOOTS.get())
                .add(ModItems.MODULAR_CLAY_BOW.get())
                .add(ModItems.MODULAR_CLAY_JAVELIN.get());

        // Modular Items Tier 1
        tag(ModTags.Items.MODULAR_TIER_1)
                .add(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HOE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOW.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get());

        // Modular Items Tier 2
        tag(ModTags.Items.MODULAR_TIER_2)
                .add(ModItems.ADAPTIVE_FLESH_PICKAXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_SHOVEL.get())
                .add(ModItems.ADAPTIVE_FLESH_HOE.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_HELMET.get())
                .add(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get())
                .add(ModItems.ADAPTIVE_FLESH_LEGGINGS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOOTS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOW.get());

        // Modular Items Tier 3
        tag(ModTags.Items.MODULAR_TIER_3)
                .add(ModItems.BIORITE_PICKAXE.get())
                .add(ModItems.BIORITE_AXE.get())
                .add(ModItems.BIORITE_SHOVEL.get())
                .add(ModItems.BIORITE_HOE.get())
                .add(ModItems.BIORITE_SWORD.get())
                .add(ModItems.BIORITE_HELMET.get())
                .add(ModItems.BIORITE_CHESTPLATE.get())
                .add(ModItems.BIORITE_LEGGINGS.get())
                .add(ModItems.BIORITE_BOOTS.get())
                .add(ModItems.BIORITE_BOW.get());

        // Modular Items Untiered
        tag(ModTags.Items.MODULAR_UNTIERED)
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHEARS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LIGHTER.get());

        // Modular Shears
        tag(ModTags.Items.MODULAR_SHEARS)
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHEARS.get());

        // Modular Lighter
        tag(ModTags.Items.MODULAR_LIGHTER)
                .add(ModItems.ADVANCED_MODULAR_CLAY_LIGHTER.get());

        tag(ItemTags.CREEPER_IGNITERS)
                .add(ModItems.ADVANCED_MODULAR_CLAY_LIGHTER.get());


        // Augments
        tag(ModTags.Items.MODULAR_ITEMS_AUGMENT)
                .add(ModItems.UNBREAKING_AUGMENT.get())
                .add(ModItems.MENDING_AUGMENT.get())

                .add(ModItems.EFFICIENCY_AUGMENT.get())
                .add(ModItems.SILK_TOUCH_AUGMENT.get())
                .add(ModItems.FORTUNE_AUGMENT.get())

                .add(ModItems.SHARPNESS_AUGMENT.get())
                .add(ModItems.SMITE_AUGMENT.get())
                .add(ModItems.BANE_OF_ARTHROPODS_AUGMENT.get())
                .add(ModItems.IMPALING_AUGMENT.get())
                .add(ModItems.LOOTING_AUGMENT.get())
                .add(ModItems.KNOCKBACK_AUGMENT.get())
                .add(ModItems.FIRE_ASPECT_AUGMENT.get())
                .add(ModItems.SWEEPING_EDGE_AUGMENT.get())

                .add(ModItems.POWER_AUGMENT.get())
                .add(ModItems.PUNCH_AUGMENT.get())
                .add(ModItems.FLAME_AUGMENT.get())
                .add(ModItems.INFINITY_AUGMENT.get())

                .add(ModItems.PROTECTION_AUGMENT.get())
                .add(ModItems.BLAST_PROTECTION_AUGMENT.get())
                .add(ModItems.PROJECTILE_PROTECTION_AUGMENT.get())
                .add(ModItems.FIRE_PROTECTION_AUGMENT.get())
                .add(ModItems.THORNS_AUGMENT.get())

                .add(ModItems.RESPIRATION_AUGMENT.get())
                .add(ModItems.AQUA_AFFINITY_AUGMENT.get())

                .add(ModItems.SWIFT_SNEAK_AUGMENT.get())

                .add(ModItems.FEATHER_FALLING_AUGMENT.get())
                .add(ModItems.FROST_WALKER_AUGMENT.get())
                .add(ModItems.DEPTH_STRIDER_AUGMENT.get())
                .add(ModItems.SOUL_SPEED_AUGMENT.get())

                // Custom Augments
                .add(ModItems.AUTO_SMELT_AUGMENT.get())
                .add(ModItems.DIAMOND_SHARPENED_AUGMENT.get())
                .add(ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get())
                .add(ModItems.SCAFFOLDING_AUGMENT.get())
                .add(ModItems.ILLUMINATOR_AUGMENT.get())
                .add(ModItems.FIRE_STARTER_AUGMENT.get())
                .add(ModItems.OVERCLOCK_AUGMENT.get())
                .add(ModItems.VOID_AUGMENT.get())
                .add(ModItems.MAGNET_AUGMENT.get())
                .add(ModItems.XP_SIPHON_AUGMENT.get())
                .add(ModItems.EXILE_AUGMENT.get())
                .add(ModItems.POISON_AUGMENT.get())
                .add(ModItems.WITHER_AUGMENT.get())
                .add(ModItems.LIFE_SENSE_AUGMENT.get())
                .add(ModItems.OVERDRIVE_AUGMENT.get())
                .add(ModItems.RAPID_FIRE_AUGMENT.get())
                .add(ModItems.VITALITY_AUGMENT.get())
                .add(ModItems.GILDED_AUGMENT.get())
                .add(ModItems.SUPER_GILDED_AUGMENT.get())
                .add(ModItems.PUMPKIN_LENS_AUGMENT.get())
                .add(ModItems.STEP_UP_AUGMENT.get())
                .add(ModItems.VOID_TETHER_AUGMENT.get())

                .add(ModItems.ADVANCED_SHEARER_AUGMENT.get())
                .add(ModItems.JEBS_BLESSING_AUGMENT.get())

                .add(ModItems.AREA_LIGHTER_AUGMENT.get())
                .add(ModItems.FLAME_WAVE_AUGMENT.get())
                .add(ModItems.EXTINGUISHER_AUGMENT.get());

        // Armor Tags
        tag(ModTags.Items.MODULAR_HELMET)
                .add(ModItems.MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADAPTIVE_FLESH_HELMET.get())
                .add(ModItems.BIORITE_HELMET.get());

        tag(ModTags.Items.MODULAR_CHESTPLATE)
                .add(ModItems.MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get())
                .add(ModItems.BIORITE_CHESTPLATE.get());

        tag(ModTags.Items.MODULAR_LEGGINGS)
                .add(ModItems.MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADAPTIVE_FLESH_LEGGINGS.get())
                .add(ModItems.BIORITE_LEGGINGS.get());

        tag(ModTags.Items.MODULAR_BOOTS)
                .add(ModItems.MODULAR_CLAY_BOOTS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOOTS.get())
                .add(ModItems.BIORITE_BOOTS.get());

        // Range Addons
        tag(ModTags.Items.MODULAR_RANGE_ADDON)
                .add(ModItems.RANGE_ADDON_COAL.get())
                .add(ModItems.RANGE_ADDON_COPPER.get())
                .add(ModItems.RANGE_ADDON_IRON.get())
                .add(ModItems.RANGE_ADDON_QUARTZ.get())
                .add(ModItems.RANGE_ADDON_REDSTONE.get())
                .add(ModItems.RANGE_ADDON_GLOWSTONE.get())
                .add(ModItems.RANGE_ADDON_GOLD.get())
                .add(ModItems.RANGE_ADDON_AMETHYST.get())
                .add(ModItems.RANGE_ADDON_LAPIS.get())
                .add(ModItems.RANGE_ADDON_DIAMOND.get())
                .add(ModItems.RANGE_ADDON_EMERALD.get());

        // Filters
        tag(ModTags.Items.FILTER)
                .add(ModItems.BASIC_FILTER.get());

        // Augment Addons
        tag(ModTags.Items.MODULAR_AUGMENT_ADDON)
                .add(ModItems.SILK_TOUCH_AUGMENT_ADDON.get())
                .add(ModItems.FORTUNE_AUGMENT_ADDON.get())
                .add(ModItems.AUTO_SMELT_AUGMENT_ADDON.get())
                .add(ModItems.AUTO_SMELTING_FORTUNE_AUGMENT_ADDON.get())
                .add(ModItems.VOID_AUGMENT_ADDON.get())
                .add(ModItems.CLAY_EXTRACTOR_AUGMENT_ADDON.get());


        // NeoForge Tags
        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.MODULAR_CLAY_HELMET.get())
                .add(ModItems.MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.MODULAR_CLAY_BOOTS.get())

                .add(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get())

                .add(ModItems.ADAPTIVE_FLESH_HELMET.get())
                .add(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get())
                .add(ModItems.ADAPTIVE_FLESH_LEGGINGS.get())
                .add(ModItems.ADAPTIVE_FLESH_BOOTS.get())

                .add(ModItems.BIORITE_HELMET.get())
                .add(ModItems.BIORITE_CHESTPLATE.get())
                .add(ModItems.BIORITE_LEGGINGS.get())
                .add(ModItems.BIORITE_BOOTS.get());

        tag(ItemTags.SWORDS)
                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.BIORITE_SWORD.get());

        tag(ItemTags.PICKAXES)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADAPTIVE_FLESH_PICKAXE.get())
                .add(ModItems.BIORITE_PICKAXE.get());

        tag(ItemTags.AXES)
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.BIORITE_AXE.get());

        tag(ItemTags.SHOVELS)
                .add(ModItems.MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADAPTIVE_FLESH_SHOVEL.get())
                .add(ModItems.BIORITE_SHOVEL.get());

        tag(ItemTags.HOES)
                .add(ModItems.MODULAR_CLAY_HOE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HOE.get())
                .add(ModItems.ADAPTIVE_FLESH_HOE.get())
                .add(ModItems.BIORITE_HOE.get());

        tag(ItemTags.BREAKS_DECORATED_POTS)
                .add(ModItems.MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get())
                .add(ModItems.ADAPTIVE_FLESH_SWORD.get())
                .add(ModItems.BIORITE_SWORD.get())

                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.ADAPTIVE_FLESH_PICKAXE.get())
                .add(ModItems.BIORITE_PICKAXE.get())

                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_AXE.get())
                .add(ModItems.ADAPTIVE_FLESH_AXE.get())
                .add(ModItems.BIORITE_AXE.get())

                .add(ModItems.MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get())
                .add(ModItems.ADAPTIVE_FLESH_SHOVEL.get())
                .add(ModItems.BIORITE_SHOVEL.get())

                .add(ModItems.MODULAR_CLAY_HOE.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_HOE.get())
                .add(ModItems.ADAPTIVE_FLESH_HOE.get())
                .add(ModItems.BIORITE_HOE.get())

                .add(ModItems.MODULAR_CLAY_JAVELIN.get())
                .add(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get());

    }
}
