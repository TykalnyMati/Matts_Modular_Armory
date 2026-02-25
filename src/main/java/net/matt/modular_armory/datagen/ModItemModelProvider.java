package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

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

        basicItem(ModItems.MODULAR_CLAY_PICKAXE.get());
        basicItem(ModItems.MODULAR_CLAY_AXE.get());
        basicItem(ModItems.MODULAR_CLAY_SHOVEL.get());
    }
}
