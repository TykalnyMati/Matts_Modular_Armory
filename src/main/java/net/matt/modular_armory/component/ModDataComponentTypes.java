package net.matt.modular_armory.component;

import com.mojang.serialization.Codec;
import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE , MattsModularArmory.MOD_ID);

    //
    public static final RegistryObject<DataComponentType<List<ItemStack>>> AUGMENTS = register("augments",
            builder -> builder.persistent(Codec.list(ItemStack.CODEC))
                    .networkSynchronized(ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC))
    );

    private static <T>RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
