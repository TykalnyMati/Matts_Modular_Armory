package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, MattsModularArmory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.Items.MODULAR_ITEMS)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.MODULAR_CLAY_SHOVEL.get());

        tag(ModTags.Items.MODULAR_TIER_0)
                .add(ModItems.MODULAR_CLAY_PICKAXE.get())
                .add(ModItems.MODULAR_CLAY_AXE.get())
                .add(ModItems.MODULAR_CLAY_SHOVEL.get());



        tag(ModTags.Items.MODULAR_ITEMS_AUGMENT)
                .add(ModItems.EFFICIENCY_AUGMENT.get())
                .add(ModItems.UNBREAKING_AUGMENT.get())
                .add(ModItems.SILK_TOUCH_AUGMENT.get())
                .add(ModItems.FORTUNE_AUGMENT.get());
    }
}
