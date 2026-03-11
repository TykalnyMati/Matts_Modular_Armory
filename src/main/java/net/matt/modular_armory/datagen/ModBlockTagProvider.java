package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MattsModularArmory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.EXPOSER.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.ADAPTIVE_FLESH_BLOCK.get())
                .add(ModBlocks.AUGMENT_TABLE.get())
                .add(ModBlocks.REPAIR_TABLE.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.MODULAR_CLAY_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.ADAPTIVE_FLESH_BLOCK.get());
    }
}
