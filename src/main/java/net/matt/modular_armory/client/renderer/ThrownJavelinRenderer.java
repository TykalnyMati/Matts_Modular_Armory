package net.matt.modular_armory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.matt.modular_armory.client.model.ModularClayJavelinModel;
import net.matt.modular_armory.entity.ThrownJavelin;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownJavelinRenderer extends EntityRenderer<ThrownJavelin> {
    private final ModularClayJavelinModel model;

    public ThrownJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModularClayJavelinModel(context.bakeLayer(ModularClayJavelinModel.LAYER_LOCATION));
    }

    @Override
    public void render(ThrownJavelin entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                this.model.renderType(this.getTextureLocation(entity)),
                false,
                entity.isFoil()
        );

        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownJavelin entity) {
        return ModularClayJavelinModel.TEXTURE;
    }
}
