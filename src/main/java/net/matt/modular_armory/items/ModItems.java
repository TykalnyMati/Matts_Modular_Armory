package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.custom.FleshMealItem;
import net.matt.modular_armory.items.custom.ModularClayExtractorItem;
import net.matt.modular_armory.items.tools.ModularAxeItem;
import net.matt.modular_armory.items.tools.ModularHoeItem;
import net.matt.modular_armory.items.tools.ModularPickaxeItem;
import net.matt.modular_armory.items.tools.ModularShovelItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MattsModularArmory.MOD_ID);

    public static final RegistryObject<Item> MODULAR_CLAY = ITEMS.register("modular_clay", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_MODULAR_PLATE = ITEMS.register("basic_modular_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MODULAR_CLAY_EXTRACTOR = ITEMS.register("modular_clay_extractor", () -> new ModularClayExtractorItem(new Item.Properties().durability(16)));

    public static final RegistryObject<Item> FLESH_MEAL = ITEMS.register("flesh_meal", () -> new FleshMealItem(new Item.Properties()));

    public static final RegistryObject<Item> EXPOSER_SLOT_PLUG = ITEMS.register("exposer_slot_plug", () -> new Item(new Item.Properties().stacksTo(1)));

    // Tools
    // Modular Clay
    public static final RegistryObject<Item> MODULAR_CLAY_PICKAXE = ITEMS.register("modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.0f, -2.8f))));

    public static final RegistryObject<Item> MODULAR_CLAY_AXE = ITEMS.register("modular_clay_axe",
            () -> new ModularAxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 7.0f, -3.2f))));

    public static final RegistryObject<Item> MODULAR_CLAY_SHOVEL = ITEMS.register("modular_clay_shovel",
            () -> new ModularShovelItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.5f, -3.0f))));

    public static final RegistryObject<Item> MODULAR_CLAY_HOE = ITEMS.register("modular_clay_hoe",
            () -> new ModularHoeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.MODULAR_CLAY, -1.0f, -2.0f))));


    // Advanced Modular Clay
    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_PICKAXE = ITEMS.register("advanced_modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 1.0f, -2.8f))));


    // Adaptive Flesh
    public static final RegistryObject<Item> ADAPTIVE_FLESH_PICKAXE = ITEMS.register("adaptive_flesh_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 1.5f, -2.4f))));


    // Augments
    public static final RegistryObject<Item> EFFICIENCY_AUGMENT = ITEMS.register("efficiency_augment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> UNBREAKING_AUGMENT = ITEMS.register("unbreaking_augment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK_TOUCH_AUGMENT = ITEMS.register("silk_touch_augment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FORTUNE_AUGMENT = ITEMS.register("fortune_augment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MENDING_AUGMENT = ITEMS.register("mending_augment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHARPNESS_AUGMENT = ITEMS.register("sharpness_augment", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
