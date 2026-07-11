package net.matt.modular_armory.block;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.custom.*;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, MattsModularArmory.MOD_ID);

    // Blocks
    public static final DeferredHolder<Block, Block> MODULAR_CLAY_BLOCK = registerBlock("modular_clay_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.6f ).sound(SoundType.GRAVEL)));

    public static final DeferredHolder<Block, Block> ADAPTIVE_FLESH_BLOCK = registerBlock("adaptive_flesh_block",
            () -> new AdaptiveFleshBlock(BlockBehaviour.Properties.of().strength(1f ).sound(SoundType.MUD)));

    public static final DeferredHolder<Block, Block> AUGMENT_TABLE = registerBlock("augment_table",
            () -> new AugmentTableBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredHolder<Block, Block> EXPOSER = registerBlock("exposer",
            () -> new ExposerBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> REPAIR_TABLE = registerBlock("repair_table",
            () -> new RepairTableBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, Block> MODULAR_ASSEMBLER = registerBlock("modular_assembler",
            () -> new ModularAssemblerBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, Block> MODULAR_MINER = registerBlock("modular_miner",
            () -> new ModularMinerBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> SCAFFOLDING_AUGMENT_BLOCK = registerBlock("scaffolding_augment_block",
            () -> new ScaffoldingAugmentBlock(BlockBehaviour.Properties.of().noLootTable().strength(2f).sound(SoundType.SCAFFOLDING)));

    public static final DeferredHolder<Block, Block> ILLUMINATOR_AUGMENT_BLOCK = registerBlock("illuminator_augment_block",
            () -> new IlluminatorAugmentBlock(BlockBehaviour.Properties.of().replaceable().noLootTable().instabreak().sound(SoundType.AMETHYST_CLUSTER)));

    // Helpfull methods
    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
