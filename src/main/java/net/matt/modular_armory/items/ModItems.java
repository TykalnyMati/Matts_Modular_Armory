package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.custom.FleshMealItem;
import net.matt.modular_armory.items.custom.ModularClayExtractorItem;
import net.matt.modular_armory.items.tools.*;
import net.matt.modular_armory.items.tools.helpers.AugmentGeneric;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MattsModularArmory.MOD_ID);

    public static final RegistryObject<Item> MODULAR_CLAY = ITEMS.register("modular_clay", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_MODULAR_PLATE = ITEMS.register("basic_modular_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_MODULAR_PLATE = ITEMS.register("advanced_modular_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MODULAR_CLAY_EXTRACTOR = ITEMS.register("modular_clay_extractor", () -> new ModularClayExtractorItem(new Item.Properties().durability(16)));

    public static final RegistryObject<Item> FLESH_MEAL = ITEMS.register("flesh_meal", () -> new FleshMealItem(new Item.Properties()));

    public static final RegistryObject<Item> ADAPTIVE_FLESH = ITEMS.register("adaptive_flesh", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_PLATE = ITEMS.register("adaptive_flesh_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BIORITE_PLATE = ITEMS.register("biorite_plate", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> EXPOSER_SLOT_PLUG = ITEMS.register("exposer_slot_plug", () -> new Item(new Item.Properties().stacksTo(1)));

    // Tools & Weapons
    // Modular Clay
    public static final RegistryObject<Item> MODULAR_CLAY_SHOVEL = ITEMS.register("modular_clay_shovel",
            () -> new ModularShovelItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.5f, -3.0f))));

    public static final RegistryObject<Item> MODULAR_CLAY_PICKAXE = ITEMS.register("modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.0f, -2.8f))));

    public static final RegistryObject<Item> MODULAR_CLAY_AXE = ITEMS.register("modular_clay_axe",
            () -> new ModularAxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 7.0f, -3.2f))));

    public static final RegistryObject<Item> MODULAR_CLAY_HOE = ITEMS.register("modular_clay_hoe",
            () -> new ModularHoeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.MODULAR_CLAY, -1.0f, -2.0f))));

    public static final RegistryObject<Item> MODULAR_CLAY_SWORD = ITEMS.register("modular_clay_sword",
            () -> new ModularSwordItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ModularSwordItem.createAttributes(ModToolTiers.MODULAR_CLAY, 3.0f, -2.4f))));

    // Advanced Modular Clay
    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_SHOVEL = ITEMS.register("advanced_modular_clay_shovel",
            () -> new ModularShovelItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 2.0f, -2.8f))));

    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_PICKAXE = ITEMS.register("advanced_modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 1.5f, -2.6f))));

    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_AXE = ITEMS.register("advanced_modular_clay_axe",
            () -> new ModularAxeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 6.5f, -3.0f))));

    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_HOE = ITEMS.register("advanced_modular_clay_hoe",
            () -> new ModularHoeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, -1.5f, -0.8f))));

    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_SWORD = ITEMS.register("advanced_modular_clay_sword",
            () -> new ModularSwordItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(ModularSwordItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 3.5f, -2.2f))));


    // Adaptive Flesh
    public static final RegistryObject<Item> ADAPTIVE_FLESH_SHOVEL = ITEMS.register("adaptive_flesh_shovel",
            () -> new ModularShovelItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(ShovelItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 2.0f, -2.6f))));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_PICKAXE = ITEMS.register("adaptive_flesh_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(PickaxeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 1.5f, -2.4f))));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_AXE = ITEMS.register("adaptive_flesh_axe",
            () -> new ModularAxeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(AxeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 5.5f, -2.8f))));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_HOE = ITEMS.register("adaptive_flesh_hoe",
            () -> new ModularHoeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(HoeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, -2.5f, 0.4f))));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_SWORD = ITEMS.register("adaptive_flesh_sword",
            () -> new ModularSwordItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(ModularSwordItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 4f, -2.0f))));

    // Biorite
    public static final RegistryObject<Item> BIORITE_SHOVEL = ITEMS.register("biorite_shovel",
            () -> new ModularShovelItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(ShovelItem.createAttributes(ModToolTiers.BIORITE, 2.5f, -2.0f))));

    public static final RegistryObject<Item> BIORITE_PICKAXE = ITEMS.register("biorite_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(PickaxeItem.createAttributes(ModToolTiers.BIORITE, 2.0f, -2.2f))));

    public static final RegistryObject<Item> BIORITE_AXE = ITEMS.register("biorite_axe",
            () -> new ModularAxeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(AxeItem.createAttributes(ModToolTiers.BIORITE, 6.0f, -2.6f))));

    public static final RegistryObject<Item> BIORITE_HOE = ITEMS.register("biorite_hoe",
            () -> new ModularHoeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(HoeItem.createAttributes(ModToolTiers.BIORITE, -3.0f, 0.6f))));

    public static final RegistryObject<Item> BIORITE_SWORD = ITEMS.register("biorite_sword",
            () -> new ModularSwordItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(ModularSwordItem.createAttributes(ModToolTiers.BIORITE, 4.5f, -1.8f))));

    // Upgrades
    public static final RegistryObject<Item> ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("advanced_modular_clay_upgrade_smithing_template",
            () -> new SmithingTemplateItem(
                    Component.translatable("item.mattsmodulararmory.smithing_template.advanced_modular_clay_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
                    Component.translatable("item.mattsmodulararmory.smithing_template.advanced_modular_clay_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
                    Component.translatable("upgrade.mattsmodulararmory.advanced_modular_clay_upgrade").withStyle(ChatFormatting.GRAY),
                    Component.translatable("item.mattsmodulararmory.smithing_template.advanced_modular_clay_upgrade.base_slot_description"),
                    Component.translatable("item.mattsmodulararmory.smithing_template.advanced_modular_clay_upgrade.additions_slot_description"),
                    List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_hoe")),
                    List.of(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/empty_slot_plate"))
            ));

    public static final RegistryObject<Item> ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("adaptive_flesh_upgrade_smithing_template",
            () -> new SmithingTemplateItem(
                    Component.translatable("item.mattsmodulararmory.smithing_template.adaptive_flesh_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
                    Component.translatable("item.mattsmodulararmory.smithing_template.adaptive_flesh_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
                    Component.translatable("upgrade.mattsmodulararmory.adaptive_flesh_upgrade").withStyle(ChatFormatting.GRAY),
                    Component.translatable("item.mattsmodulararmory.smithing_template.adaptive_flesh_upgrade.base_slot_description"),
                    Component.translatable("item.mattsmodulararmory.smithing_template.adaptive_flesh_upgrade.additions_slot_description"),
                    List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_hoe")),
                    List.of(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/empty_slot_plate"))
            ));

    public static final RegistryObject<Item> BIORITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("biorite_upgrade_smithing_template",
            () -> new SmithingTemplateItem(
                    Component.translatable("item.mattsmodulararmory.smithing_template.biorite_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
                    Component.translatable("item.mattsmodulararmory.smithing_template.biorite_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
                    Component.translatable("upgrade.mattsmodulararmory.biorite_upgrade").withStyle(ChatFormatting.GRAY),
                    Component.translatable("item.mattsmodulararmory.smithing_template.biorite_upgrade.base_slot_description"),
                    Component.translatable("item.mattsmodulararmory.smithing_template.biorite_upgrade.additions_slot_description"),
                    List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                            ResourceLocation.withDefaultNamespace("item/empty_slot_hoe")),
                    List.of(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/empty_slot_plate"))
            ));

    // Augments
    public static final RegistryObject<Item> UNBREAKING_AUGMENT = ITEMS.register("unbreaking_augment", () -> new AugmentGeneric(new Item.Properties(), "unbreaking_augment", "generic", List.of(2, 4, 6, 8)));
    public static final RegistryObject<Item> MENDING_AUGMENT = ITEMS.register("mending_augment", () -> new AugmentGeneric(new Item.Properties(), "mending_augment", "generic", List.of(0, 1, 1, 1)));

    public static final RegistryObject<Item> EFFICIENCY_AUGMENT = ITEMS.register("efficiency_augment", () -> new AugmentGeneric(new Item.Properties(), "efficiency_augment", "tools", List.of(1, 3, 5, 7)));
    public static final RegistryObject<Item> SILK_TOUCH_AUGMENT = ITEMS.register("silk_touch_augment", () -> new AugmentGeneric(new Item.Properties(), "silk_touch_augment", "tools", List.of(1, 1, 1, 1)));
    public static final RegistryObject<Item> FORTUNE_AUGMENT = ITEMS.register("fortune_augment", () -> new AugmentGeneric(new Item.Properties(), "fortune_augment", "tools", List.of(1, 2, 3, 4)));

    public static final RegistryObject<Item> SHARPNESS_AUGMENT = ITEMS.register("sharpness_augment", () -> new AugmentGeneric(new Item.Properties(), "sharpness_augment", "weapons", List.of(1, 3, 5, 7)));
    public static final RegistryObject<Item> SMITE_AUGMENT = ITEMS.register("smite_augment", () -> new AugmentGeneric(new Item.Properties(), "smite_augment", "weapons", List.of(1, 3, 5, 7)));
    public static final RegistryObject<Item> BANE_OF_ARTHROPODS_AUGMENT = ITEMS.register("bane_of_arthropods_augment", () -> new AugmentGeneric(new Item.Properties(), "bane_of_arthropods_augment", "weapons", List.of(1, 3, 5, 7)));
    public static final RegistryObject<Item> LOOTING_AUGMENT = ITEMS.register("looting_augment", () -> new AugmentGeneric(new Item.Properties(), "looting_augment", "weapons", List.of(1, 2, 3, 4)));
    public static final RegistryObject<Item> KNOCKBACK_AUGMENT = ITEMS.register("knockback_augment", () -> new AugmentGeneric(new Item.Properties(), "knockback_augment", "weapons", List.of(1, 2, 2, 3)));
    public static final RegistryObject<Item> FIRE_ASPECT_AUGMENT = ITEMS.register("fire_aspect_augment", () -> new AugmentGeneric(new Item.Properties(), "fire_aspect_augment", "weapons", List.of(1, 1, 2, 3)));

    // Custom Augments
    public static final RegistryObject<Item> AUTO_SMELT_AUGMENT = ITEMS.register("auto_smelt_augment", () -> new AugmentGeneric(new Item.Properties(), "auto_smelt_augment", "tools", List.of(0, 1, 1, 1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
