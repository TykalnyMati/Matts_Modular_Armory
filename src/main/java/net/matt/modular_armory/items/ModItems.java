package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MattsModularArmory.MOD_ID);

    public static final RegistryObject<Item> MODULAR_CLAY = ITEMS.register("modular_clay", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_MODULAR_PLATE = ITEMS.register("raw_modular_plate", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
