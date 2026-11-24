package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.addons.FilterItem;
import net.matt.modular_armory.items.addons.ModularAugmentAddon;
import net.matt.modular_armory.items.addons.ModularRangeAddon;
import net.matt.modular_armory.items.armor.ModularArmorItem;
import net.matt.modular_armory.items.custom.FleshMealItem;
import net.matt.modular_armory.items.custom.GuideBookItem;
import net.matt.modular_armory.items.custom.ModularClayExtractorItem;
import net.matt.modular_armory.items.tools.*;
import net.matt.modular_armory.items.tools.helpers.AugmentGeneric;
import net.matt.modular_armory.items.tools.ModularLighterItem;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
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

    public static final DeferredHolder<Item, Item> GUIDE_BOOK = ITEMS.register("guide_book",
            () -> new GuideBookItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

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

    // Modular Processing Unit
    public static final DeferredHolder<Item, Item> MODULAR_PROCESSING_UNIT = ITEMS.register("modular_processing_unit", () -> new Item(new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.used_for_crafting").withStyle(ChatFormatting.GRAY));

            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        }
    });

    // Modular Capacitor
    public static final DeferredHolder<Item, Item> MODULAR_CAPACITOR = ITEMS.register("modular_capacitor", () -> new Item(new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.used_for_crafting").withStyle(ChatFormatting.GRAY));

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
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.SILK_TOUCH, 50, 1));

    public static final DeferredHolder<Item, Item> FORTUNE_AUGMENT_ADDON = ITEMS.register("fortune_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.FORTUNE, 250, 4));

    public static final DeferredHolder<Item, Item> AUTO_SMELT_AUGMENT_ADDON = ITEMS.register("auto_smelt_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.AUTO_SMELT, 400, 1));

    public static final DeferredHolder<Item, Item> AUTO_SMELTING_FORTUNE_AUGMENT_ADDON = ITEMS.register("auto_smelting_fortune_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.AUTO_SMELTING_FORTUNE, 275, 4, 450));

    public static final DeferredHolder<Item, Item> VOID_AUGMENT_ADDON = ITEMS.register("void_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.VOID, 10, 1));

    public static final DeferredHolder<Item, Item> CLAY_EXTRACTOR_AUGMENT_ADDON = ITEMS.register("clay_extractor_augment_addon",
            () -> new ModularAugmentAddon(new Item.Properties(), ModularAugmentAddon.ADDONS.CLAY_EXTRACTOR, 50, 4));

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

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_BOW = ITEMS.register("modular_clay_bow",
            () -> new ModularBowItem(new Item.Properties().durability(193), 0.8F));

    public static final DeferredHolder<Item, Item> MODULAR_CLAY_JAVELIN = ITEMS.register("modular_clay_javelin",
            () -> new ModularJavelinItem(new Item.Properties().durability(193).attributes(ModularJavelinItem.createAttributes(5.0f, -3.1f)), 6.0f));

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

        public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_BOW = ITEMS.register("advanced_modular_clay_bow",
                () -> new ModularBowItem(new Item.Properties().durability(385), 1.0F));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_JAVELIN = ITEMS.register("advanced_modular_clay_javelin",
            () -> new ModularJavelinItem(new Item.Properties().durability(385).attributes(ModularJavelinItem.createAttributes(7.0f, -3.0f)), 8.0f));

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

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_BOW = ITEMS.register("adaptive_flesh_bow",
            () -> new ModularBowItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(577), 1.2F));

    public static final DeferredHolder<Item, Item> ADAPTIVE_FLESH_JAVELIN = ITEMS.register("adaptive_flesh_javelin",
            () -> new ModularJavelinItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(577).attributes(ModularJavelinItem.createAttributes(7.5f, -3.0f)), 8.5f));

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

    public static final DeferredHolder<Item, Item> BIORITE_BOW = ITEMS.register("biorite_bow",
            () -> new ModularBowItem(new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(769), 1.4F));

    public static final DeferredHolder<Item, Item> BIORITE_JAVELIN = ITEMS.register("biorite_javelin",
            () -> new ModularJavelinItem(new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(769).attributes(ModularJavelinItem.createAttributes(9.0f, -2.8f)), 10.0f));

    // Untiered
    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_SHEARS = ITEMS.register("advanced_modular_clay_shears",
            () -> new ModularShearsItem(new Item.Properties().durability(576).component(DataComponents.TOOL, ShearsItem.createToolProperties())));

    public static final DeferredHolder<Item, Item> ADVANCED_MODULAR_CLAY_LIGHTER = ITEMS.register("advanced_modular_clay_lighter",
            () -> new ModularLighterItem(new Item.Properties().fireResistant().durability(168)));

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
    public static final DeferredHolder<Item, Item> UNBREAKING_AUGMENT = ITEMS.register("unbreaking_augment", () -> new AugmentGeneric(new Item.Properties(), "unbreaking_augment", AugmentGeneric.Types.GENERIC, List.of(2, 4, 6, 8), 0x5f3e87));
    public static final DeferredHolder<Item, Item> MENDING_AUGMENT = ITEMS.register("mending_augment", () -> new AugmentGeneric(new Item.Properties(), "mending_augment", AugmentGeneric.Types.GENERIC, List.of(0, 1, 1, 1), 0xb5e45a, List.of("xp_siphon_augment")));

    // Tools
    public static final DeferredHolder<Item, Item> EFFICIENCY_AUGMENT = ITEMS.register("efficiency_augment", () -> new AugmentGeneric(new Item.Properties(), "efficiency_augment", AugmentGeneric.Types.TOOLS, List.of(1, 3, 5, 7), 0xe62008).bonusType(AugmentGeneric.Types.SHEARS));
    public static final DeferredHolder<Item, Item> SILK_TOUCH_AUGMENT = ITEMS.register("silk_touch_augment", () -> new AugmentGeneric(new Item.Properties(), "silk_touch_augment", AugmentGeneric.Types.TOOLS, List.of(1, 1, 1, 1), 0x82f6ad, List.of("void_augment", "fortune_augment", "auto_smelt_augment")));
    public static final DeferredHolder<Item, Item> FORTUNE_AUGMENT = ITEMS.register("fortune_augment", () -> new AugmentGeneric(new Item.Properties(), "fortune_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4), 0x2b568d, List.of("void_augment", "silk_touch_augment")));

    // Weapons
    public static final DeferredHolder<Item, Item> SHARPNESS_AUGMENT = ITEMS.register("sharpness_augment", () -> new AugmentGeneric(new Item.Properties(), "sharpness_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7), 0xf7f5f2, List.of("smite_augment", "bane_of_arthropods_augment", "impaling_augment", "exile_augment")));
    public static final DeferredHolder<Item, Item> SMITE_AUGMENT = ITEMS.register("smite_augment", () -> new AugmentGeneric(new Item.Properties(), "smite_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7), 0xc5815a, List.of("sharpness_augment", "bane_of_arthropods_augment", "impaling_augment", "exile_augment")));
    public static final DeferredHolder<Item, Item> BANE_OF_ARTHROPODS_AUGMENT = ITEMS.register("bane_of_arthropods_augment", () -> new AugmentGeneric(new Item.Properties(), "bane_of_arthropods_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7), 0x9d1e2d, List.of("sharpness_augment", "smite_augment", "impaling_augment", "exile_augment")));
    public static final DeferredHolder<Item, Item> IMPALING_AUGMENT = ITEMS.register("impaling_augment", () -> new AugmentGeneric(new Item.Properties(), "impaling_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7), 0x589d8e, List.of("sharpness_augment", "smite_augment", "bane_of_arthropods_augment", "exile_augment")));
    public static final DeferredHolder<Item, Item> LOOTING_AUGMENT = ITEMS.register("looting_augment", () -> new AugmentGeneric(new Item.Properties(), "looting_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 2, 3, 4), 0x2b568d));
    public static final DeferredHolder<Item, Item> KNOCKBACK_AUGMENT = ITEMS.register("knockback_augment", () -> new AugmentGeneric(new Item.Properties(), "knockback_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 2, 2, 3), 0xa5a5a5));
    public static final DeferredHolder<Item, Item> FIRE_ASPECT_AUGMENT = ITEMS.register("fire_aspect_augment", () -> new AugmentGeneric(new Item.Properties(), "fire_aspect_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 1, 2, 3), 0xffcb00));
    public static final DeferredHolder<Item, Item> SWEEPING_EDGE_AUGMENT = ITEMS.register("sweeping_edge_augment", () -> new AugmentGeneric(new Item.Properties(), "sweeping_edge_augment", AugmentGeneric.Types.SWORD, List.of(1, 2, 3, 4), 0xd3dbff));

    // Bow
    public static final DeferredHolder<Item, Item> POWER_AUGMENT = ITEMS.register("power_augment", () -> new AugmentGeneric(new Item.Properties(), "power_augment", AugmentGeneric.Types.RANGED_WEAPONS, List.of(1, 3, 5, 7), 0xf7f5f2));
    public static final DeferredHolder<Item, Item> PUNCH_AUGMENT = ITEMS.register("punch_augment", () -> new AugmentGeneric(new Item.Properties(), "punch_augment", AugmentGeneric.Types.RANGED_WEAPONS, List.of(1, 1, 2, 3), 0xa5a5a5));
    public static final DeferredHolder<Item, Item> FLAME_AUGMENT = ITEMS.register("flame_augment", () -> new AugmentGeneric(new Item.Properties(), "flame_augment", AugmentGeneric.Types.RANGED_WEAPONS, List.of(1, 1, 1, 1), 0xffcb00));
    public static final DeferredHolder<Item, Item> INFINITY_AUGMENT = ITEMS.register("infinity_augment", () -> new AugmentGeneric(new Item.Properties(), "infinity_augment", AugmentGeneric.Types.RANGED_WEAPONS, List.of(1, 1, 1, 1), 0x8f36db));
    public static final DeferredHolder<Item, Item> PIERCING_AUGMENT = ITEMS.register("piercing_augment", () -> new AugmentGeneric(new Item.Properties(), "piercing_augment", AugmentGeneric.Types.RANGED_AND_THROWN_WEAPONS, List.of(2, 3, 4, 5), 0x927965, List.of("riptide_augment")));

    // Javelin
    public static final DeferredHolder<Item, Item> LOYALTY_AUGMENT = ITEMS.register("loyalty_augment", () -> new AugmentGeneric(new Item.Properties(), "loyalty_augment", AugmentGeneric.Types.JAVELIN, List.of(1, 2, 3, 4), 0x589d8e, List.of("riptide_augment")));
    public static final DeferredHolder<Item, Item> CHANNELING_AUGMENT = ITEMS.register("channeling_augment", () -> new AugmentGeneric(new Item.Properties(), "channeling_augment", AugmentGeneric.Types.RANGED_AND_THROWN_WEAPONS, List.of(0, 1, 1, 1), 0xf2ff00, List.of("riptide_augment")));
    public static final DeferredHolder<Item, Item> RIPTIDE_AUGMENT = ITEMS.register("riptide_augment", () -> new AugmentGeneric(new Item.Properties(), "riptide_augment", AugmentGeneric.Types.JAVELIN, List.of(2, 3, 4, 5), 0xd3ebea, List.of("loyalty_augment", "channeling_augment")));

    // Armor
    public static final DeferredHolder<Item, Item> PROTECTION_AUGMENT = ITEMS.register("protection_augment", () -> new AugmentGeneric(new Item.Properties(), "protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7), 0xd8d8d8, List.of("blast_protection_augment", "projectile_protection_augment", "fire_protection_augment")));
    public static final DeferredHolder<Item, Item> BLAST_PROTECTION_AUGMENT = ITEMS.register("blast_protection_augment", () -> new AugmentGeneric(new Item.Properties(), "blast_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7), 0xb11527, List.of("protection_augment", "projectile_protection_augment", "fire_protection_augment")));
    public static final DeferredHolder<Item, Item> PROJECTILE_PROTECTION_AUGMENT = ITEMS.register("projectile_protection_augment", () -> new AugmentGeneric(new Item.Properties(), "projectile_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7), 0xd8d8d8, List.of("protection_augment", "blast_protection_augment", "fire_protection_augment")));
    public static final DeferredHolder<Item, Item> FIRE_PROTECTION_AUGMENT = ITEMS.register("fire_protection_augment", () -> new AugmentGeneric(new Item.Properties().fireResistant(), "fire_protection_augment", AugmentGeneric.Types.ARMOR, List.of(1, 3, 5, 7), 0xfb8c00, List.of("protection_augment", "blast_protection_augment", "projectile_protection_augment")));
    public static final DeferredHolder<Item, Item> THORNS_AUGMENT = ITEMS.register("thorns_augment", () -> new AugmentGeneric(new Item.Properties(), "thorns_augment", AugmentGeneric.Types.ARMOR, List.of(1, 2, 3, 4), 0x527d26));

    // Helmet
    public static final DeferredHolder<Item, Item> RESPIRATION_AUGMENT = ITEMS.register("respiration_augment", () -> new AugmentGeneric(new Item.Properties(), "respiration_augment", AugmentGeneric.Types.ARMOR_HEAD, List.of(1, 2, 3, 4), 0x82cce8));
    public static final DeferredHolder<Item, Item> AQUA_AFFINITY_AUGMENT = ITEMS.register("aqua_affinity_augment", () -> new AugmentGeneric(new Item.Properties(), "aqua_affinity_augment", AugmentGeneric.Types.ARMOR_HEAD, List.of(1, 1, 2, 2), 0x0b49fb));

    // Leggings
    public static final DeferredHolder<Item, Item> SWIFT_SNEAK_AUGMENT = ITEMS.register("swift_sneak_augment", () -> new AugmentGeneric(new Item.Properties(), "swift_sneak_augment", AugmentGeneric.Types.ARMOR_LEGS, List.of(1, 2, 3, 4), 0x463aa5));

    // Boots
    public static final DeferredHolder<Item, Item> FEATHER_FALLING_AUGMENT = ITEMS.register("feather_falling_augment", () -> new AugmentGeneric(new Item.Properties(), "feather_falling_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 3, 5, 7), 0xd3d3d3));
    public static final DeferredHolder<Item, Item> FROST_WALKER_AUGMENT = ITEMS.register("frost_walker_augment", () -> new AugmentGeneric(new Item.Properties(), "frost_walker_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 3), 0xa1c2fd, List.of("depth_strider_augment")));
    public static final DeferredHolder<Item, Item> DEPTH_STRIDER_AUGMENT = ITEMS.register("depth_strider_augment", () -> new AugmentGeneric(new Item.Properties(), "depth_strider_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 3), 0x0b49fb, List.of("frost_walker_augment")));
    public static final DeferredHolder<Item, Item> SOUL_SPEED_AUGMENT = ITEMS.register("soul_speed_augment", () -> new AugmentGeneric(new Item.Properties(), "soul_speed_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 2, 3, 4), 0x60f5fa));

    // Custom Augments
    // Generic
    public static final DeferredHolder<Item, Item> MAGNET_AUGMENT = ITEMS.register("magnet_augment", () -> new AugmentGeneric(new Item.Properties(), "magnet_augment", AugmentGeneric.Types.GENERIC, List.of(0, 1, 1, 1), 0xd8d8d8));
    public static final DeferredHolder<Item, Item> XP_SIPHON_AUGMENT = ITEMS.register("xp_siphon_augment", () -> new AugmentGeneric(new Item.Properties(), "xp_siphon_augment", AugmentGeneric.Types.GENERIC, List.of(0, 1, 1, 1), 0xb5e45a, List.of("mending_augment")));

    // Tools
    public static final DeferredHolder<Item, Item> AUTO_SMELT_AUGMENT = ITEMS.register("auto_smelt_augment", () -> new AugmentGeneric(new Item.Properties(), "auto_smelt_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1), 0xff8f00, List.of("void_augment", "silk_touch_augment")));
    public static final DeferredHolder<Item, Item> DIAMOND_SHARPENED_AUGMENT = ITEMS.register("diamond_sharpened_augment", () -> new AugmentGeneric(new Item.Properties(), "diamond_sharpened_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1), 0x4aedd9));
    public static final DeferredHolder<Item, Item> MODULAR_CLAY_EXTRACTOR_AUGMENT = ITEMS.register("modular_clay_extractor_augment", () -> new AugmentGeneric(new Item.Properties(), "modular_clay_extractor_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4), 0xcccccc, AugmentGeneric.Incompatibilities.RIGHT_CLICK));
    public static final DeferredHolder<Item, Item> SCAFFOLDING_AUGMENT = ITEMS.register("scaffolding_augment", () -> new AugmentGeneric(new Item.Properties(), "scaffolding_augment", AugmentGeneric.Types.TOOLS, List.of(1, 2, 3, 4), 0xd3bb50, AugmentGeneric.Incompatibilities.RIGHT_CLICK));
    public static final DeferredHolder<Item, Item> ILLUMINATOR_AUGMENT = ITEMS.register("illuminator_augment", () -> new AugmentGeneric(new Item.Properties(), "illuminator_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1), 0xffbc5e, AugmentGeneric.Incompatibilities.RIGHT_CLICK));
    public static final DeferredHolder<Item, Item> FIRE_STARTER_AUGMENT = ITEMS.register("fire_starter_augment", () -> new AugmentGeneric(new Item.Properties(), "fire_starter_augment", AugmentGeneric.Types.TOOLS, List.of(1, 1, 1, 1), 0xffcb00, AugmentGeneric.Incompatibilities.RIGHT_CLICK));
    public static final DeferredHolder<Item, Item> OVERCLOCK_AUGMENT = ITEMS.register("overclock_augment", () -> new AugmentGeneric(new Item.Properties(), "overclock_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 2, 3), 0xe9b115, AugmentGeneric.Incompatibilities.RIGHT_CLICK));
    public static final DeferredHolder<Item, Item> VOID_AUGMENT = ITEMS.register("void_augment", () -> new AugmentGeneric(new Item.Properties(), "void_augment", AugmentGeneric.Types.TOOLS, List.of(0, 1, 1, 1), 0x8f36db, List.of("silk_touch_augment", "fortune_augment", "auto_smelt_augment")));

    // Weapons
    public static final DeferredHolder<Item, Item> EXILE_AUGMENT = ITEMS.register("exile_augment", () -> new AugmentGeneric(new Item.Properties(), "exile_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 3, 5, 7), 0x17dd62, List.of("sharpness_augment", "smite_augment", "bane_of_arthropods_augment", "impaling_augment")));
    public static final DeferredHolder<Item, Item> POISON_AUGMENT = ITEMS.register("poison_augment", () -> new AugmentGeneric(new Item.Properties(), "poison_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 1, 2, 2), 0x76db4c));
    public static final DeferredHolder<Item, Item> WITHER_AUGMENT = ITEMS.register("wither_augment", () -> new AugmentGeneric(new Item.Properties(), "wither_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 1, 2, 3), 0x764857));
    public static final DeferredHolder<Item, Item> LIFE_SENSE_AUGMENT = ITEMS.register("life_sense_augment", () -> new AugmentGeneric(new Item.Properties(), "life_sense_augment", AugmentGeneric.Types.WEAPONS, List.of(1, 2, 3, 4), 0xf9fbcb, AugmentGeneric.Incompatibilities.RIGHT_CLICK).incompatibleItemType(ModTags.Items.MODULAR_JAVELIN));
    public static final DeferredHolder<Item, Item> OVERDRIVE_AUGMENT = ITEMS.register("overdrive_augment", () -> new AugmentGeneric(new Item.Properties(), "overdrive_augment", AugmentGeneric.Types.WEAPONS, List.of(0, 1, 2, 3), 0xfcc500, AugmentGeneric.Incompatibilities.RIGHT_CLICK).incompatibleItemType(ModTags.Items.MODULAR_JAVELIN));

    // Ranged and Thrown Weapons
    public static final DeferredHolder<Item, Item> RAPID_FIRE_AUGMENT = ITEMS.register("rapid_fire_augment", () -> new AugmentGeneric(new Item.Properties(), "rapid_fire_augment", AugmentGeneric.Types.RANGED_AND_THROWN_WEAPONS, List.of(0, 1, 1, 1), 0xcc2929));

    // Armor
    public static final DeferredHolder<Item, Item> VITALITY_AUGMENT = ITEMS.register("vitality_augment", () -> new AugmentGeneric(new Item.Properties(), "vitality_augment", AugmentGeneric.Types.ARMOR, List.of(0, 0, 1, 2), 0xff1313));
    public static final DeferredHolder<Item, Item> GILDED_AUGMENT = ITEMS.register("gilded_augment", () -> new AugmentGeneric(new Item.Properties(), "gilded_augment", AugmentGeneric.Types.ARMOR, List.of(1, 1, 1, 1), 0xfdf55f, List.of("super_gilded_augment")));
    public static final DeferredHolder<Item, Item> SUPER_GILDED_AUGMENT = ITEMS.register("super_gilded_augment", () -> new AugmentGeneric(new Item.Properties(), "super_gilded_augment", AugmentGeneric.Types.ARMOR, List.of(0, 0, 1, 1), 0xf5da2a, List.of("gilded_augment")));

    public static final DeferredHolder<Item, Item> PUMPKIN_LENS_AUGMENT = ITEMS.register("pumpkin_lens_augment", () -> new AugmentGeneric(new Item.Properties(), "pumpkin_lens_augment", AugmentGeneric.Types.ARMOR_HEAD, List.of(1, 1, 1, 1), 0xe3901d));

    public static final DeferredHolder<Item, Item> STEP_UP_AUGMENT = ITEMS.register("step_up_augment", () -> new AugmentGeneric(new Item.Properties(), "step_up_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 1, 2, 2), 0x66a63c));
    public static final DeferredHolder<Item, Item> VOID_TETHER_AUGMENT = ITEMS.register("void_tether_augment", () -> new AugmentGeneric(new Item.Properties(), "void_tether_augment", AugmentGeneric.Types.ARMOR_FEET, List.of(1, 1, 1, 1), 0x875b87));

    // Utility
    // Shears
    public static final DeferredHolder<Item, Item> ADVANCED_SHEARER_AUGMENT = ITEMS.register("advanced_shearer_augment", () -> new AugmentGeneric(new Item.Properties(), "advanced_shearer_augment", AugmentGeneric.Types.SHEARS, List.of(1, 2, 3, 4), 0x7fcc19));
    public static final DeferredHolder<Item, Item> JEBS_BLESSING_AUGMENT = ITEMS.register("jebs_blessing_augment", () -> new AugmentGeneric(new Item.Properties(), "jebs_blessing_augment", AugmentGeneric.Types.SHEARS, List.of(1, 1, 1, 1), 0xe5e533));

    // Lighter
    public static final DeferredHolder<Item, Item> AREA_LIGHTER_AUGMENT = ITEMS.register("area_lighter_augment", () -> new AugmentGeneric(new Item.Properties(), "area_lighter_augment", AugmentGeneric.Types.LIGHTER, List.of(1, 2, 3, 3), 0xffcb00, List.of("flame_wave_augment", "extinguisher_augment")));
    public static final DeferredHolder<Item, Item> FLAME_WAVE_AUGMENT = ITEMS.register("flame_wave_augment", () -> new AugmentGeneric(new Item.Properties(), "flame_wave_augment", AugmentGeneric.Types.LIGHTER, List.of(1, 2, 3, 3), 0xffcb00, List.of("area_lighter_augment", "extinguisher_augment")));
    public static final DeferredHolder<Item, Item> EXTINGUISHER_AUGMENT = ITEMS.register("extinguisher_augment", () -> new AugmentGeneric(new Item.Properties(), "extinguisher_augment", AugmentGeneric.Types.LIGHTER, List.of(1, 2, 3, 4), 0xffcb00, List.of("area_lighter_augment", "flame_wave_augment")));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
