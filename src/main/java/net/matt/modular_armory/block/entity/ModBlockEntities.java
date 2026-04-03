package net.matt.modular_armory.block.entity;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.custom.AugmentTableBlockEntity;
import net.matt.modular_armory.block.entity.custom.ExposerBlockEntity;
import net.matt.modular_armory.block.entity.custom.ScaffoldingAugmentBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MattsModularArmory.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AugmentTableBlockEntity>> AUGMENT_TABLE_BE = BLOCK_ENTITIES.register("augment_table_be",
            () -> BlockEntityType.Builder.of(AugmentTableBlockEntity::new, ModBlocks.AUGMENT_TABLE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExposerBlockEntity>> EXPOSER_BE = BLOCK_ENTITIES.register("exposer_be",
            () -> BlockEntityType.Builder.of(ExposerBlockEntity::new, ModBlocks.EXPOSER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ScaffoldingAugmentBlockEntity>> SCAFFOLDING_AUGMENT_BE = BLOCK_ENTITIES.register("scaffolding_augment_be",
            () -> BlockEntityType.Builder.of(ScaffoldingAugmentBlockEntity::new, ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
