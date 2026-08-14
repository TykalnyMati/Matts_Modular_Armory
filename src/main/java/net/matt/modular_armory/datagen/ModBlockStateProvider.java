package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MattsModularArmory.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.MODULAR_CLAY_BLOCK);
        blockWithItem(ModBlocks.ADAPTIVE_FLESH_BLOCK);
        blockWithItem(ModBlocks.INFECTIOUS_ADAPTIVE_FLESH_BLOCK);
        blockWithItem(ModBlocks.EXPOSER);
        blockWithItem(ModBlocks.SCAFFOLDING_AUGMENT_BLOCK);
    }

    private void blockWithItem(DeferredHolder<Block, ? extends Block> blockHolder) {
        simpleBlockWithItem(blockHolder.get(), cubeAll(blockHolder.get()));
    }
}
