package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MattsModularArmory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MODULAR_CLAY.get());
        basicItem(ModItems.BASIC_MODULAR_PLATE.get());
        basicItem(ModItems.MODULAR_CLAY_EXTRACTOR.get());
        basicItem(ModItems.ADVANCED_MODULAR_PLATE.get());
        basicItem(ModItems.FLESH_MEAL.get());
        basicItem(ModItems.ADAPTIVE_FLESH.get());
        basicItem(ModItems.ADAPTIVE_FLESH_PLATE.get());
        basicItem(ModItems.BIORITE_PLATE.get());
        basicItem(ModItems.EXPOSER_SLOT_PLUG.get());

        // Smithing Template Upgrades
        basicItem(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get());

        // Tier 0 modular items
        handHeldItem(ModItems.MODULAR_CLAY_PICKAXE);
        handHeldItem(ModItems.MODULAR_CLAY_AXE);
        handHeldItem(ModItems.MODULAR_CLAY_SHOVEL);
        handHeldItem(ModItems.MODULAR_CLAY_HOE);
        handHeldItem(ModItems.MODULAR_CLAY_SWORD);

        // Tier 1 modular items
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_AXE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_HOE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_SWORD);

        // Tier 2 modular items
        handHeldItem(ModItems.ADAPTIVE_FLESH_PICKAXE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_AXE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_SHOVEL);
        handHeldItem(ModItems.ADAPTIVE_FLESH_HOE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_SWORD);

        // Tier 3 modular items
        handHeldItem(ModItems.BIORITE_PICKAXE);
        handHeldItem(ModItems.BIORITE_AXE);
        handHeldItem(ModItems.BIORITE_SHOVEL);
        handHeldItem(ModItems.BIORITE_HOE);
        handHeldItem(ModItems.BIORITE_SWORD);

        // Augments
        basicItem(ModItems.UNBREAKING_AUGMENT.get());
        basicItem(ModItems.MENDING_AUGMENT.get());

        basicItem(ModItems.EFFICIENCY_AUGMENT.get());
        basicItem(ModItems.SILK_TOUCH_AUGMENT.get());
        basicItem(ModItems.FORTUNE_AUGMENT.get());
        basicItem(ModItems.DIAMOND_SHARPENED_AUGMENT.get());

        basicItem(ModItems.SHARPNESS_AUGMENT.get());
        basicItem(ModItems.SMITE_AUGMENT.get());
        basicItem(ModItems.BANE_OF_ARTHROPODS_AUGMENT.get());
        basicItem(ModItems.LOOTING_AUGMENT.get());
        basicItem(ModItems.KNOCKBACK_AUGMENT.get());
        basicItem(ModItems.FIRE_ASPECT_AUGMENT.get());

        // Custom Augments
        basicItem(ModItems.AUTO_SMELT_AUGMENT.get());
    }

    private ItemModelBuilder handHeldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/" + item.getId().getPath()));
    }
}
