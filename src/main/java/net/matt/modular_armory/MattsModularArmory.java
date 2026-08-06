package net.matt.modular_armory;

import com.mojang.logging.LogUtils;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.renderer.AugmentTableBlockEntityRenderer;
import net.matt.modular_armory.block.entity.renderer.ModularMinerBlockEntityRenderer;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.datagen.DataGenerators;
import net.matt.modular_armory.items.ModCreativeModeTabs;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.recipe.ModRecipes;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.screen.custom.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.awt.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MattsModularArmory.MOD_ID)
public class MattsModularArmory {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mattsmodulararmory";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MattsModularArmory(IEventBus modEventBus, ModContainer container)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Adding Creative tab
        ModCreativeModeTabs.register(modEventBus);

        // Adding items
        ModItems.register(modEventBus);

        // Adding Blocks
        ModBlocks.register(modEventBus);

        // Adding Data Components
        ModDataComponentTypes.register(modEventBus);

        // Adding Block Entities
        ModBlockEntities.register(modEventBus);

        // Adding Menus
        ModMenuTypes.register(modEventBus);

        // Adding Custom Recipes
        ModRecipes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Registering inventories
        modEventBus.addListener(this::registerCapabilities);

        // Register our mod's NeoForgeConfigSpec so that NeoForge can create and load the config file for us
        modEventBus.addListener(Config::bake);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (net.neoforged.fml.loading.FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientModEvents::onClientSetup);
            modEventBus.addListener(ClientModEvents::registerScreens);
            modEventBus.addListener(ClientModEvents::registerRenderers);
        }

        modEventBus.addListener(DataGenerators::gatherData);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register Augment Table (Simple handler for all sides)
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.AUGMENT_TABLE_BE.get(),
                (be, side) -> be.inventory
        );

        // Register Exposer (Can insert and extract from any side)
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.EXPOSER_BE.get(),
                (be, side) -> be.getItemHandlerCapability(side)
        );

        // Register Modular Miner (Can only extract from any side)
        // Inventory
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MODULAR_MINER_BE.get(),
                (be, side) -> be.getItemHandlerCapability(side)
        );

        // Power
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.MODULAR_MINER_BE.get(),
                (be, direction) -> be.getEnergyStorage()
        );
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    public static class ClientModEvents {

        public static void onClientSetup(FMLClientSetupEvent event) {
            // General client setup if needed
        }

        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.AUGMENT_TABLE_MENU.get(), AugmentTableScreen::new);
            event.register(ModMenuTypes.EXPOSER_MENU.get(), ExposerScreen::new);
            event.register(ModMenuTypes.REPAIR_TABLE_MENU.get(), RepairTableScreen::new);
            event.register(ModMenuTypes.MODULAR_ASSEMBLER_MENU.get(), ModularAssemblerScreen::new);
            event.register(ModMenuTypes.MODULAR_MINER_MENU.get(), ModularMinerScreen::new);
            event.register(ModMenuTypes.FILTER_MENU.get(), FilterScreen::new);
        }

        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.AUGMENT_TABLE_BE.get(), AugmentTableBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.MODULAR_MINER_BE.get(), ModularMinerBlockEntityRenderer::new);
        }
    }
}
