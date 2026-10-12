package net.matt.modular_armory.util;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> MODULAR_ITEMS = createTag("modular_items");

        public static final TagKey<Item> MODULAR_TIER_0 = createTag("modular_tier_0");
        public static final TagKey<Item> MODULAR_TIER_1 = createTag("modular_tier_1");
        public static final TagKey<Item> MODULAR_TIER_2 = createTag("modular_tier_2");
        public static final TagKey<Item> MODULAR_TIER_3 = createTag("modular_tier_3");

        public static final TagKey<Item> MODULAR_TOOL = createTag("modular_tool");
        public static final TagKey<Item> MODULAR_WEAPON = createTag("modular_weapon");
        public static final TagKey<Item> MODULAR_ARMOR = createTag("modular_armor");

        public static final TagKey<Item> MODULAR_HELMET = createTag("modular_helmet");
        public static final TagKey<Item> MODULAR_CHESTPLATE = createTag("modular_chestplate");
        public static final TagKey<Item> MODULAR_LEGGINGS = createTag("modular_leggings");
        public static final TagKey<Item> MODULAR_BOOTS = createTag("modular_boots");

        public static final TagKey<Item> MODULAR_SWORD = createTag("modular_sword");

        public static final TagKey<Item> MODULAR_ITEMS_AUGMENT = createTag("modular_items_augment");

        public static final TagKey<Item> MODULAR_RANGE_ADDON = createTag("modular_range_addon");

        public static final TagKey<Item> MODULAR_AUGMENT_ADDON = createTag("modular_augment_addon");

        public static final TagKey<Item> FILTER = createTag("filter");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, name));
        }
    }
}
