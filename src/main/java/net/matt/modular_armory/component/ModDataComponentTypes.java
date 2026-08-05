package net.matt.modular_armory.component;

import com.mojang.serialization.Codec;
import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE , MattsModularArmory.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> AUGMENTS = register("augments",
            builder -> builder.persistent(Codec.list(ItemStack.CODEC))
                    .networkSynchronized(ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC))
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> FILTERED_ITEMS = register("filtered_items",
            builder -> builder.persistent(Codec.list(ItemStack.OPTIONAL_CODEC))
                    .networkSynchronized(ByteBufCodecs.collection(ArrayList::new, ItemStack.OPTIONAL_STREAM_CODEC))
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
