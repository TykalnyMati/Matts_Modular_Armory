package net.matt.modular_armory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matt.modular_armory.client.model.AdvancedModularClayJavelinModel;
import net.matt.modular_armory.client.model.ModularClayJavelinModel;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class JavelinItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final EntityModelSet entityModels;
    private final Map<Item, ModularClayJavelinModel> models = new HashMap<>();

    public JavelinItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet entityModels) {
        super(dispatcher, entityModels);
        this.entityModels = entityModels;
    }

    private void bakeModels() {
        this.models.clear();
        this.models.put(ModItems.MODULAR_CLAY_JAVELIN.get(),
                new ModularClayJavelinModel(this.entityModels.bakeLayer(ModularClayJavelinModel.LAYER_LOCATION)));
        this.models.put(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get(),
                new AdvancedModularClayJavelinModel(this.entityModels.bakeLayer(AdvancedModularClayJavelinModel.LAYER_LOCATION)));
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        bakeModels();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.models.isEmpty()) {
            bakeModels();
        }

        ModularClayJavelinModel activeModel = this.models.getOrDefault(
                stack.getItem(),
                this.models.get(ModItems.MODULAR_CLAY_JAVELIN.get())
        );

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                activeModel.renderType(activeModel.getTexture()),
                false,
                stack.hasFoil()
        );

        activeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
