package net.matt.modular_armory;

import com.mojang.logging.LogUtils;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.renderer.AugmentTableBlockEntityRenderer;
import net.matt.modular_armory.items.ModCreativeModeTabs;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.screen.custom.AugmentTableScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MattsModularArmory.MOD_ID)
public class MattsModularArmory {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mattsmodulararmory";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MattsModularArmory()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Adding Creative tab
        ModCreativeModeTabs.register(modEventBus);

        // Adding items
        ModItems.register(modEventBus);

        // Adding Blocks
        ModBlocks.register(modEventBus);

        // Adding Block Entities
        ModBlockEntities.register(modEventBus);

        // Adding Menus
        ModMenuTypes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Not needed, all the mod items are in its own tab
//        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
//            event.accept(ModItems.MODULAR_CLAY);
//            event.accept(ModItems.RAW_MODULAR_PLATE);
//            event.accept(ModBlocks.MODULAR_CLAY_BLOCK);
//        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            MenuScreens.register(ModMenuTypes.AUGMENT_TABLE_MENU.get(), AugmentTableScreen::new);
        }

        @SubscribeEvent
        public static void registerBBR(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.AUGMENT_TABLE_BE.get(), AugmentTableBlockEntityRenderer::new);
        }
    }
}
