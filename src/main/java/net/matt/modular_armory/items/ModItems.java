package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.addons.FilterItem;
import net.matt.modular_armory.items.addons.ModularAugmentAddon;
import net.matt.modular_armory.items.addons.ModularRangeAddon;
import net.matt.modular_armory.items.armor.ModularArmorItem;
import net.matt.modular_armory.items.custom.FleshMealItem;
import net.matt.modular_armory.items.custom.ModularClayExtractorItem;
import net.matt.modular_armory.items.tools.*;
import net.matt.modular_armory.items.tools.helpers.AugmentGeneric;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MattsModularArmory.MOD_ID);

    public static final DeferredHolder<Item, Item> MODULAR_CLAY = ITEMS.register("modular_clay", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BASIC_MODULAR_PLATE = ITEMS.register("basic_modular_plate", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.generic_plate").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("tooltip.mattsmodulararmory.basic_modular_plate").withStyle(ChatFormatting.GRAY)));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_PLATE = ITEMS.register("advanced_modular_plate", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.generic_plate").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("tooltip.mattsmodulararmory.advanced_modular_plate").withStyle(ChatFormatting.WHITE)));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_EXTRACTOR = ITEMS.register("modular_clay_extractor", () -> new ModularClayExtractorItem(new Item.Properties().durability(16)));

    public static final DeferredHolder<Item, Item> FLESH_MEAL = ITEMS.register("flesh_meal", () -> new FleshMealItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH = ITEMS.register("adaptive_flesh", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationModifier(0.3f)
                    .fast()
                    // 40% chance of getting Hunger
                    .effect(new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.4f)
                    // 60% chance of getting Nausea
                    .effect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 0.6f)
                    // 20% change of getting Regeneration
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1), 0.2f)
                    .build())
    ));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_PLATE = ITEMS.register("adaptive_flesh_plate", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.generic_plate").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("tooltip.mattsmodulararmory.adaptive_flesh_plate").withStyle(ChatFormatting.DARK_RED)));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    public static final DeferredHolder<Item, Item> BIORITE_PLATE = ITEMS.register("biorite_plate", () -> new Item(new Item.Properties().fireResistant()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.generic_plate").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("tooltip.mattsmodulararmory.biorite_plate").withStyle(ChatFormatting.AQUA)));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    public static final DeferredHolder<Item, Item> EXPOSER_SLOT_PLUG = ITEMS.register("exposer_slot_plug", () -> new Item(new Item.Properties().stacksTo(1)){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.exposer_slot_plug").withStyle(ChatFormatting.GRAY));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    // Addon Template
    public static final DeferredHolder<Item, Item> ADDON_TEMPLATE = ITEMS.register("addon_template", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.addon_template").withStyle(ChatFormatting.GRAY));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    // Blank Range Addon
    public static final DeferredHolder<Item, Item> BLANK_RANGE_ADDON = ITEMS.register("blank_range_addon", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.blank_range_addon").withStyle(ChatFormatting.GRAY));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    // Range Addons
    public static final DeferredHolder<Item, Item> RANGE_ADDON_COAL = ITEMS.register("range_addon_coal",
            () -> new ModularRangeAddon(new Item.Properties(), "coal", 0x5c5c5c, 1));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_COPPER = ITEMS.register("range_addon_copper",
            () -> new ModularRangeAddon(new Item.Properties(), "copper", 0xfc9982, 2));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_IRON = ITEMS.register("range_addon_iron",
            () -> new ModularRangeAddon(new Item.Properties(), "iron", 0xffffff, 3));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_QUARTZ = ITEMS.register("range_addon_quartz",
            () -> new ModularRangeAddon(new Item.Properties(), "quartz", 0xf7f5f2, 4));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_REDSTONE = ITEMS.register("range_addon_redstone",
            () -> new ModularRangeAddon(new Item.Properties(), "redstone", 0xff0000, 5));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_GLOWSTONE = ITEMS.register("range_addon_glowstone",
            () -> new ModularRangeAddon(new Item.Properties(), "glowstone", 0xf9d49c, 6));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_GOLD = ITEMS.register("range_addon_gold",
            () -> new ModularRangeAddon(new Item.Properties(), "gold", 0xfdf55f, 7));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_AMETHYST = ITEMS.register("range_addon_amethyst",
            () -> new ModularRangeAddon(new Item.Properties(), "amethyst", 0xfecbe6, 8));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_LAPIS = ITEMS.register("range_addon_lapis",
            () -> new ModularRangeAddon(new Item.Properties(), "lapis", 0x5a82e2, 9));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_DIAMOND = ITEMS.register("range_addon_diamond",
            () -> new ModularRangeAddon(new Item.Properties(), "diamond", 0xa1fbe8, 10));

    public static final DeferredHolder<Item, Item> RANGE_ADDON_EMERALD = ITEMS.register("range_addon_emerald",
            () -> new ModularRangeAddon(new Item.Properties(), "emerald", 0x17dd62, 11));

    // Basic Filter
    public static final DeferredHolder<Item, Item> BASIC_FILTER = ITEMS.register("basic_filter",
            () -> new FilterItem(new Item.Properties()));

    // Blank Augment Addon
    public static final DeferredHolder<Item, Item> BLANK_AUGMENT_ADDON = ITEMS.register("blank_augment_addon", () -> new Item(new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.blank_augment_addon").withStyle(ChatFormatting.GRAY));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    // Augment Addons
    public static final DeferredHolder<Item, Item> SILK_TOUCH_AUGMENT_ADDON = ITEMS.register("silk_touch_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.SILK_TOUCH, 10, 1));

    public static final DeferredHolder<Item, Item> FORTUNE_AUGMENT_ADDON = ITEMS.register("fortune_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.FORTUNE, 25, 4));

    // Tools, Weapons & Armor
    // Modular Clay
    public static final DeferredHolder<Item, Item> MODULAR_CLAY_SHOVEL = ITEMS.register("modular_clay_shovel",
            () -> new ModularShovelItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.5f, -3.0f))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_PICKAXE = ITEMS.register("modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 1.0f, -2.8f))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_AXE = ITEMS.register("modular_clay_axe",
            () -> new ModularAxeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.MODULAR_CLAY, 7.0f, -3.2f))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_HOE = ITEMS.register("modular_clay_hoe",
            () -> new ModularHoeItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.MODULAR_CLAY, -1.0f, -2.0f))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_SWORD = ITEMS.register("modular_clay_sword",
            () -> new ModularSwordItem(ModToolTiers.MODULAR_CLAY, new Item.Properties().attributes(ModularSwordItem.createAttributes(ModToolTiers.MODULAR_CLAY, 3.0f, -2.4f))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_HELMET = ITEMS.register("modular_clay_helmet",
            () -> new ModularArmorItem(ModArmorMaterials.MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(10))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_CHESTPLATE = ITEMS.register("modular_clay_chestplate",
            () -> new ModularArmorItem(ModArmorMaterials.MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(10))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_LEGGINGS = ITEMS.register("modular_clay_leggings",
            () -> new ModularArmorItem(ModArmorMaterials.MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(10))));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_BOOTS = ITEMS.register("modular_clay_boots",
            () -> new ModularArmorItem(ModArmorMaterials.MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(10))));

    // Advanced Modular Clay
    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_SHOVEL = ITEMS.register("advanced_modular_clay_shovel",
            () -> new ModularShovelItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 2.0f, -2.8f))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_PICKAXE = ITEMS.register("advanced_modular_clay_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 1.5f, -2.6f))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_AXE = ITEMS.register("advanced_modular_clay_axe",
            () -> new ModularAxeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 6.5f, -3.0f))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_HOE = ITEMS.register("advanced_modular_clay_hoe",
            () -> new ModularHoeItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, -1.5f, -0.8f))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_SWORD = ITEMS.register("advanced_modular_clay_sword",
            () -> new ModularSwordItem(ModToolTiers.ADVANCED_MODULAR_CLAY, new Item.Properties().attributes(ModularSwordItem.createAttributes(ModToolTiers.ADVANCED_MODULAR_CLAY, 3.5f, -2.2f))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_HELMET = ITEMS.register("advanced_modular_clay_helmet",
            () -> new ModularArmorItem(ModArmorMaterials.ADVANCED_MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(17))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_CHESTPLATE = ITEMS.register("advanced_modular_clay_chestplate",
            () -> new ModularArmorItem(ModArmorMaterials.ADVANCED_MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(17))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_LEGGINGS = ITEMS.register("advanced_modular_clay_leggings",
            () -> new ModularArmorItem(ModArmorMaterials.ADVANCED_MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(17))));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_BOOTS = ITEMS.register("advanced_modular_clay_boots",
            () -> new ModularArmorItem(ModArmorMaterials.ADVANCED_MODULAR_CLAY_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(17))));

    // Adaptive Flesh
    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_SHOVEL = ITEMS.register("adaptive_flesh_shovel",
            () -> new ModularShovelItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(ShovelItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 2.0f, -2.6f))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_PICKAXE = ITEMS.register("adaptive_flesh_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(PickaxeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 1.5f, -2.4f))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_AXE = ITEMS.register("adaptive_flesh_axe",
            () -> new ModularAxeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(AxeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 5.5f, -2.8f))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_HOE = ITEMS.register("adaptive_flesh_hoe",
            () -> new ModularHoeItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(HoeItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, -2.5f, 0.4f))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_SWORD = ITEMS.register("adaptive_flesh_sword",
            () -> new ModularSwordItem(ModToolTiers.ADAPTIVE_FLESH, new Item.Properties().rarity(Rarity.UNCOMMON).attributes(ModularSwordItem.createAttributes(ModToolTiers.ADAPTIVE_FLESH, 4f, -2.0f))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_HELMET = ITEMS.register("adaptive_flesh_helmet",
            () -> new ModularArmorItem(ModArmorMaterials.ADAPTIVE_FLESH_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.UNCOMMON).durability(ArmorItem.Type.HELMET.getDurability(30))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_CHESTPLATE = ITEMS.register("adaptive_flesh_chestplate",
            () -> new ModularArmorItem(ModArmorMaterials.ADAPTIVE_FLESH_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.UNCOMMON).durability(ArmorItem.Type.CHESTPLATE.getDurability(30))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_LEGGINGS = ITEMS.register("adaptive_flesh_leggings",
            () -> new ModularArmorItem(ModArmorMaterials.ADAPTIVE_FLESH_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.UNCOMMON).durability(ArmorItem.Type.LEGGINGS.getDurability(30))));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_BOOTS = ITEMS.register("adaptive_flesh_boots",
            () -> new ModularArmorItem(ModArmorMaterials.ADAPTIVE_FLESH_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.UNCOMMON).durability(ArmorItem.Type.BOOTS.getDurability(30))));

    // Biorite
    public static final DeferredHolder<Item, Item> BIORITE_SHOVEL = ITEMS.register("biorite_shovel",
            () -> new ModularShovelItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(ShovelItem.createAttributes(ModToolTiers.BIORITE, 2.5f, -2.0f))));

    public static final DeferredHolder<Item, Item> BIORITE_PICKAXE = ITEMS.register("biorite_pickaxe",
            () -> new ModularPickaxeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(PickaxeItem.createAttributes(ModToolTiers.BIORITE, 2.0f, -2.2f))));

    public static final DeferredHolder<Item, Item> BIORITE_AXE = ITEMS.register("biorite_axe",
            () -> new ModularAxeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(AxeItem.createAttributes(ModToolTiers.BIORITE, 6.0f, -2.6f))));

    public static final DeferredHolder<Item, Item> BIORITE_HOE = ITEMS.register("biorite_hoe",
            () -> new ModularHoeItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(HoeItem.createAttributes(ModToolTiers.BIORITE, -3.0f, 0.6f))));

    public static final DeferredHolder<Item, Item> BIORITE_SWORD = ITEMS.register("biorite_sword",
            () -> new ModularSwordItem(ModToolTiers.BIORITE, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(ModularSwordItem.createAttributes(ModToolTiers.BIORITE, 4.5f, -1.8f))));

    public static final DeferredHolder<Item, Item> BIORITE_HELMET = ITEMS.register("biorite_helmet",
            () -> new ModularArmorItem(ModArmorMaterials.BIORITE_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));

    public static final DeferredHolder<Item, Item> BIORITE_CHESTPLATE = ITEMS.register("biorite_chestplate",
            () -> new ModularArmorItem(ModArmorMaterials.BIORITE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));

    public static final DeferredHolder<Item, Item> BIORITE_LEGGINGS = ITEMS.register("biorite_leggings",
            () -> new ModularArmorItem(ModArmorMaterials.BIORITE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));

    public static final DeferredHolder<Item, Item> BIORITE_BOOTS = ITEMS.register("biorite_boots",
            () -> new ModularArmorItem(ModArmorMaterials.BIORITE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));

    // Upgrades
    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("advanced_modular_clay_upgrade_smithing_template",
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

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("adaptive_flesh_upgrade_smithing_template",
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

    public static final DeferredHolder<Item, Item> BIORITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("biorite_upgrade_smithing_template",
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
    // Generic
    public static final DeferredHolder<Item, Item> UNBREAKING_AUGMENT = ITEMS.register("unbreaking_augment", () -> new AugmentGeneric(new Item.Properties(), "unbreaking_augment", AugmentGeneric.Types.GENERIC, List.of(2, 4, 6, 8)));
    public static final DeferredHolder<Item, Item> MENDING_AUGMENT = ITEMS.register("mending_augment", () -> new AugmentGeneric(new Item.Properties(), "mending_augment", AugmentGeneric.Types.GENERIC, List.of(0, 1, 1, 1)));

    // Tools
    public static final DeferredHolder<Item, Item> EFFICIENCY_AUGMENT = ITEMS.register("efficiency_augment", () -> new AugmentGeneric(new Item.Properties(), "efficiency_augment", AugmentGeneric.Types.TOOLS, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> SILK_TOUCH_AUGMENT = ITEMS.register("silk_touch_augment", () -> new AugmentGeneric(new Item.Properties(), "silk_touch_augment", AugmentGeneric.Types.TOOLS, List.of(1, 1, 1, 1)));
    public static final DeferredHolder<Item, Item> FORTUNE_AUGMENT = ITEMS.register("fortune_augment", () -> new AugmentGeneric(new Item.Properties(), "fortune_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4)));

    // Weapons
    public static final DeferredHolder<Item, Item> SHARPNESS_AUGMENT = ITEMS.register("sharpness_augment", () -> new AugmentGeneric(new Item.Properties(), "sharpness_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> SMITE_AUGMENT = ITEMS.register("smite_augment", () -> new AugmentGeneric(new Item.Properties(), "smite_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> BANE_OF_ARTHROPODS_AUGMENT = ITEMS.register("bane_of_arthropods_augment", () -> new AugmentGeneric(new Item.Properties(), "bane_of_arthropods_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> IMPALING_AUGMENT = ITEMS.register("impaling_augment", () -> new AugmentGeneric(new Item.Properties(), "impaling_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> LOOTING_AUGMENT = ITEMS.register("looting_augment", () -> new AugmentGeneric(new Item.Properties(), "looting_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 2, 3, 4)));
    public static final DeferredHolder<Item, Item> KNOCKBACK_AUGMENT = ITEMS.register("knockback_augment", () -> new AugmentGeneric(new Item.Properties(), "knockback_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 2, 2, 3)));
    public static final DeferredHolder<Item, Item> FIRE_ASPECT_AUGMENT = ITEMS.register("fire_aspect_augment", () -> new AugmentGeneric(new Item.Properties(), "fire_aspect_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 1, 2, 3)));

    // Armor
    public static final DeferredHolder<Item, Item> PROTECTION_AUGMENT = ITEMS.register("protection_augment", () -> new AugmentGeneric(new Item.Properties(), "protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> BLAST_PROTECTION_AUGMENT = ITEMS.register("blast_protection_augment", () -> new AugmentGeneric(new Item.Properties(), "blast_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> PROJECTILE_PROTECTION_AUGMENT = ITEMS.register("projectile_protection_augment", () -> new AugmentGeneric(new Item.Properties(), "projectile_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> FIRE_PROTECTION_AUGMENT = ITEMS.register("fire_protection_augment", () -> new AugmentGeneric(new Item.Properties().fireResistant(), "fire_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> THORNS_AUGMENT = ITEMS.register("thorns_augment", () -> new AugmentGeneric(new Item.Properties(), "thorns_augment", AugmentGeneric.Types.ARMOR, List.of(1, 2, 3, 4)));

    // Helmet
    public static final DeferredHolder<Item, Item> RESPIRATION_AUGMENT = ITEMS.register("respiration_augment", () -> new AugmentGeneric(new Item.Properties(), "respiration_augment", AugmentGeneric.Types.ARMOR_HEAD, List.of(1, 2, 3, 4)));
    public static final DeferredHolder<Item, Item> AQUA_AFFINITY_AUGMENT = ITEMS.register("aqua_affinity_augment", () -> new AugmentGeneric(new Item.Properties(), "aqua_affinity_augment", AugmentGeneric.Types.ARMOR_HEAD, List.of(1, 1, 2, 2)));

    // Leggings
    public static final DeferredHolder<Item, Item> SWIFT_SNEAK_AUGMENT = ITEMS.register("swift_sneak_augment", () -> new AugmentGeneric(new Item.Properties(), "swift_sneak_augment", AugmentGeneric.Types.ARMOR_LEGS, List.of(1, 2, 3, 4)));

    // Boots
    public static final DeferredHolder<Item, Item> FEATHER_FALLING_AUGMENT = ITEMS.register("feather_falling_augment", () -> new AugmentGeneric(new Item.Properties(), "feather_falling_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 3, 5, 7)));
    public static final DeferredHolder<Item, Item> FROST_WALKER_AUGMENT = ITEMS.register("frost_walker_augment", () -> new AugmentGeneric(new Item.Properties(), "frost_walker_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 3)));
    public static final DeferredHolder<Item, Item> DEPTH_STRIDER_AUGMENT = ITEMS.register("depth_strider_augment", () -> new AugmentGeneric(new Item.Properties(), "depth_strider_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 3)));
    public static final DeferredHolder<Item, Item> SOUL_SPEED_AUGMENT = ITEMS.register("soul_speed_augment", () -> new AugmentGeneric(new Item.Properties(), "soul_speed_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 4)));

    // Custom Augments
    public static final DeferredHolder<Item, Item> AUTO_SMELT_AUGMENT = ITEMS.register("auto_smelt_augment", () -> new AugmentGeneric(new Item.Properties(), "auto_smelt_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1)));
    public static final DeferredHolder<Item, Item> DIAMOND_SHARPENED_AUGMENT = ITEMS.register("diamond_sharpened_augment", () -> new AugmentGeneric(new Item.Properties(), "diamond_sharpened_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1)));
    public static final DeferredHolder<Item, Item> MODULAR_CLAY_EXTRACTOR_AUGMENT = ITEMS.register("modular_clay_extractor_augment", () -> new AugmentGeneric(new Item.Properties(), "modular_clay_extractor_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4)));
    public static final DeferredHolder<Item, Item> SCAFFOLDING_AUGMENT = ITEMS.register("scaffolding_augment", () -> new AugmentGeneric(new Item.Properties(), "scaffolding_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4)));
    public static final DeferredHolder<Item, Item> ILLUMINATOR_AUGMENT = ITEMS.register("illuminator_augment", () -> new AugmentGeneric(new Item.Properties(), "illuminator_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1)));
    public static final DeferredHolder<Item, Item> MAGNET_AUGMENT = ITEMS.register("magnet_augment", () -> new AugmentGeneric(new Item.Properties(), "magnet_augment", AugmentGeneric.Types.TOOLS_AND_WEAPONS, List.of(0, 1, 1, 1)));
    public static final DeferredHolder<Item, Item> VOID_AUGMENT = ITEMS.register("void_augment", () -> new AugmentGeneric(new Item.Properties(), "void_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
