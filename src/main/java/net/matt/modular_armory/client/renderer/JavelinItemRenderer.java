package net.matt.modular_armory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matt.modular_armory.client.model.ModularClayJavelinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JavelinItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final EntityModelSet entityModels;
    private ModularClayJavelinModel model;

    public JavelinItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet entityModels) {
        super(dispatcher, entityModels);
        this.entityModels = entityModels;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = new ModularClayJavelinModel(this.entityModels.bakeLayer(ModularClayJavelinModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new ModularClayJavelinModel(this.entityModels.bakeLayer(ModularClayJavelinModel.LAYER_LOCATION));
        }

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                this.model.renderType(ModularClayJavelinModel.TEXTURE),
                false,
                stack.hasFoil()
        );

        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
