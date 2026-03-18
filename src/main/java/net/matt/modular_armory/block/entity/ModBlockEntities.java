package net.matt.modular_armory.block.entity;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.custom.AugmentTableBlockEntity;
import net.matt.modular_armory.block.entity.custom.ExposerBlockEntity;
import net.matt.modular_armory.block.entity.custom.ScaffoldingAugmentBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MattsModularArmory.MOD_ID);

    public static final RegistryObject<BlockEntityType<AugmentTableBlockEntity>> AUGMENT_TABLE_BE = BLOCK_ENTITIES.register("augment_table_be",
            () -> BlockEntityType.Builder.of(AugmentTableBlockEntity::new, ModBlocks.AUGMENT_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExposerBlockEntity>> EXPOSER_BE = BLOCK_ENTITIES.register("exposer_be",
            () -> BlockEntityType.Builder.of(ExposerBlockEntity::new, ModBlocks.EXPOSER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ScaffoldingAugmentBlockEntity>> SCAFFOLDING_AUGMENT_BE = BLOCK_ENTITIES.register("scaffolding_augment_be",
            () -> BlockEntityType.Builder.of(ScaffoldingAugmentBlockEntity::new, ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
