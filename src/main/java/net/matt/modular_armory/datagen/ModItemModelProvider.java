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
        basicItem(ModItems.FLESH_MEAL.get());
        basicItem(ModItems.EXPOSER_SLOT_PLUG.get());

        // Tier 0 modular items
        handHeldItem(ModItems.MODULAR_CLAY_PICKAXE);
        handHeldItem(ModItems.MODULAR_CLAY_AXE);
        handHeldItem(ModItems.MODULAR_CLAY_SHOVEL);
        handHeldItem(ModItems.MODULAR_CLAY_HOE);

        // Tier 1 modular items
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE);

        // Tier 2 modular items
        handHeldItem(ModItems.ADAPTIVE_FLESH_PICKAXE);

        basicItem(ModItems.EFFICIENCY_AUGMENT.get());
        basicItem(ModItems.UNBREAKING_AUGMENT.get());
        basicItem(ModItems.SILK_TOUCH_AUGMENT.get());
        basicItem(ModItems.FORTUNE_AUGMENT.get());
        basicItem(ModItems.MENDING_AUGMENT.get());
    }

    private ItemModelBuilder handHeldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/" + item.getId().getPath()));
    }
}
