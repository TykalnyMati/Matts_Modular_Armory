package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MattsModularArmory.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATTS_MODULAR_ARMORY_TAB = CREATIVE_MODE_TABS.register("matts_modular_armory_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MODULAR_CLAY.get()))
                    .title(Component.translatable("creativetab.mattsmodulararmory.mattsmodulararmory"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MODULAR_CLAY.get());
                        output.accept(ModBlocks.MODULAR_CLAY_BLOCK.get());
                        output.accept(ModItems.BASIC_MODULAR_PLATE.get());
                        output.accept(ModItems.MODULAR_CLAY_EXTRACTOR.get());
                        output.accept(ModItems.ADVANCED_MODULAR_PLATE.get());
                        output.accept(ModItems.FLESH_MEAL.get());
                        output.accept(ModBlocks.ADAPTIVE_FLESH_BLOCK.get());
                        output.accept(ModItems.ADAPTIVE_FLESH.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_PLATE.get());
                        output.accept(ModItems.BIORITE_PLATE.get());
                        output.accept(ModBlocks.MODULAR_ASSEMBLER.get());
                        output.accept(ModBlocks.AUGMENT_TABLE.get());
                        output.accept(ModBlocks.REPAIR_TABLE.get());
                        output.accept(ModBlocks.EXPOSER.get());
                        output.accept(ModItems.EXPOSER_SLOT_PLUG.get());

                        // Machines
                        output.accept(ModBlocks.MODULAR_MINER.get());

                        // Addons
                        output.accept(ModItems.ADDON_TEMPLATE.get());
                        output.accept(ModItems.BLANK_RANGE_ADDON.get());
                        output.accept(ModItems.RANGE_ADDON_COAL.get());
                        output.accept(ModItems.RANGE_ADDON_COPPER.get());
                        output.accept(ModItems.RANGE_ADDON_IRON.get());
                        output.accept(ModItems.RANGE_ADDON_QUARTZ.get());
                        output.accept(ModItems.RANGE_ADDON_REDSTONE.get());
                        output.accept(ModItems.RANGE_ADDON_GLOWSTONE.get());
                        output.accept(ModItems.RANGE_ADDON_GOLD.get());
                        output.accept(ModItems.RANGE_ADDON_AMETHYST.get());
                        output.accept(ModItems.RANGE_ADDON_LAPIS.get());
                        output.accept(ModItems.RANGE_ADDON_DIAMOND.get());
                        output.accept(ModItems.RANGE_ADDON_EMERALD.get());

                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get());
                        output.accept(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get());

                        // Tier 0 modular items
                        output.accept(ModItems.MODULAR_CLAY_SWORD.get());
                        output.accept(ModItems.MODULAR_CLAY_SHOVEL.get());
                        output.accept(ModItems.MODULAR_CLAY_PICKAXE.get());
                        output.accept(ModItems.MODULAR_CLAY_AXE.get());
                        output.accept(ModItems.MODULAR_CLAY_HOE.get());
                        output.accept(ModItems.MODULAR_CLAY_HELMET.get());
                        output.accept(ModItems.MODULAR_CLAY_CHESTPLATE.get());
                        output.accept(ModItems.MODULAR_CLAY_LEGGINGS.get());
                        output.accept(ModItems.MODULAR_CLAY_BOOTS.get());

                        // Tier 1 modular items
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_SWORD.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_AXE.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_HOE.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_HELMET.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS.get());
                        output.accept(ModItems.ADVANCED_MODULAR_CLAY_BOOTS.get());

                        // Tier 2 modular items
                        output.accept(ModItems.ADAPTIVE_FLESH_SWORD.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_SHOVEL.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_PICKAXE.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_AXE.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_HOE.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_HELMET.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_CHESTPLATE.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_LEGGINGS.get());
                        output.accept(ModItems.ADAPTIVE_FLESH_BOOTS.get());

                        // Tier 3 modular items
                        output.accept(ModItems.BIORITE_SWORD.get());
                        output.accept(ModItems.BIORITE_SHOVEL.get());
                        output.accept(ModItems.BIORITE_PICKAXE.get());
                        output.accept(ModItems.BIORITE_AXE.get());
                        output.accept(ModItems.BIORITE_HOE.get());
                        output.accept(ModItems.BIORITE_HELMET.get());
                        output.accept(ModItems.BIORITE_CHESTPLATE.get());
                        output.accept(ModItems.BIORITE_LEGGINGS.get());
                        output.accept(ModItems.BIORITE_BOOTS.get());

                        // Augments
                        output.accept(ModItems.UNBREAKING_AUGMENT.get());
                        output.accept(ModItems.MENDING_AUGMENT.get());

                        output.accept(ModItems.EFFICIENCY_AUGMENT.get());
                        output.accept(ModItems.SILK_TOUCH_AUGMENT.get());
                        output.accept(ModItems.FORTUNE_AUGMENT.get());

                        output.accept(ModItems.SHARPNESS_AUGMENT.get());
                        output.accept(ModItems.SMITE_AUGMENT.get());
                        output.accept(ModItems.BANE_OF_ARTHROPODS_AUGMENT.get());
                        output.accept(ModItems.IMPALING_AUGMENT.get());
                        output.accept(ModItems.LOOTING_AUGMENT.get());
                        output.accept(ModItems.KNOCKBACK_AUGMENT.get());
                        output.accept(ModItems.FIRE_ASPECT_AUGMENT.get());

                        output.accept(ModItems.PROTECTION_AUGMENT.get());
                        output.accept(ModItems.BLAST_PROTECTION_AUGMENT.get());
                        output.accept(ModItems.PROJECTILE_PROTECTION_AUGMENT.get());
                        output.accept(ModItems.FIRE_PROTECTION_AUGMENT.get());
                        output.accept(ModItems.THORNS_AUGMENT.get());

                        output.accept(ModItems.RESPIRATION_AUGMENT.get());
                        output.accept(ModItems.AQUA_AFFINITY_AUGMENT.get());

                        output.accept(ModItems.SWIFT_SNEAK_AUGMENT.get());

                        output.accept(ModItems.FEATHER_FALLING_AUGMENT.get());
                        output.accept(ModItems.FROST_WALKER_AUGMENT.get());
                        output.accept(ModItems.DEPTH_STRIDER_AUGMENT.get());
                        output.accept(ModItems.SOUL_SPEED_AUGMENT.get());

                        // Custom Augments
                        output.accept(ModItems.AUTO_SMELT_AUGMENT.get());
                        output.accept(ModItems.DIAMOND_SHARPENED_AUGMENT.get());
                        output.accept(ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get());
                        output.accept(ModItems.SCAFFOLDING_AUGMENT.get());
                        output.accept(ModItems.ILLUMINATOR_AUGMENT.get());
                        output.accept(ModItems.MAGNET_AUGMENT.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
