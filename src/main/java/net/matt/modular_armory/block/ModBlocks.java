package net.matt.modular_armory.block;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.custom.AdaptiveFleshBlock;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MattsModularArmory.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> MODULAR_CLAY_BLOCK = registerBlock("modular_clay_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.6f ).sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> ADAPTIVE_FLESH_BLOCK = registerBlock("adaptive_flesh_block",
            () -> new AdaptiveFleshBlock(BlockBehaviour.Properties.of().strength(0.6f ).sound(SoundType.MOSS)));


    // Helpfull methods
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
