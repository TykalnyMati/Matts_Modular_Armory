package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.client.model.AdvancedModularClayJavelinModel;
import net.matt.modular_armory.client.model.ModularClayJavelinModel;
import net.matt.modular_armory.client.renderer.JavelinItemRenderer;
import net.matt.modular_armory.client.renderer.ThrownJavelinRenderer;
import net.matt.modular_armory.entity.ModEntities;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        if(event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().is(ModTags.Items.MODULAR_BOW)) {
            float fovModifier = 1f;
            int ticksUsingItem = event.getPlayer().getTicksUsingItem();
            float deltaTicks = (float)ticksUsingItem / 20f;
            if(deltaTicks > 1f) {
                deltaTicks = 1f;
            } else {
                deltaTicks *= deltaTicks;
            }
            fovModifier *= 1f - deltaTicks * 0.15f;
            event.setNewFovModifier(fovModifier);
        }
    }

    // Javelin Events
    private static JavelinItemRenderer javelinRenderer;

    private static JavelinItemRenderer getJavelinRenderer() {
        if (javelinRenderer == null) {
            Minecraft mc = Minecraft.getInstance();
            javelinRenderer = new JavelinItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
        }
        return javelinRenderer;
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModularClayJavelinModel.LAYER_LOCATION, ModularClayJavelinModel::createLayer);
        event.registerLayerDefinition(AdvancedModularClayJavelinModel.LAYER_LOCATION, AdvancedModularClayJavelinModel::createLayer);
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(getJavelinRenderer());
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return getJavelinRenderer();
            }
        }, ModItems.MODULAR_CLAY_JAVELIN.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return getJavelinRenderer();
            }
        }, ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.MODULAR_CLAY_JAVELIN.get(),
                    ResourceLocation.withDefaultNamespace("throwing"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );
        });

        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get(),
                    ResourceLocation.withDefaultNamespace("throwing"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_JAVELIN.get(), ThrownJavelinRenderer::new);
    }
}
