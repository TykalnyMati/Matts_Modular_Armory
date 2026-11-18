package net.matt.modular_armory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.matt.modular_armory.client.model.AdvancedModularClayJavelinModel;
import net.matt.modular_armory.client.model.ModularClayJavelinModel;
import net.matt.modular_armory.entity.ThrownJavelin;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ThrownJavelinRenderer extends EntityRenderer<ThrownJavelin> {
    private final Map<Item, ModularClayJavelinModel> models = new HashMap<>();

    public ThrownJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.models.put(ModItems.MODULAR_CLAY_JAVELIN.get(),
                new ModularClayJavelinModel(context.bakeLayer(ModularClayJavelinModel.LAYER_LOCATION)));
        this.models.put(ModItems.ADVANCED_MODULAR_CLAY_JAVELIN.get(),
                new AdvancedModularClayJavelinModel(context.bakeLayer(AdvancedModularClayJavelinModel.LAYER_LOCATION)));
    }

    @Override
    public void render(ThrownJavelin entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getSyncedItemStack();
        ModularClayJavelinModel activeModel = this.models.getOrDefault(
                stack.getItem(),
                this.models.get(ModItems.MODULAR_CLAY_JAVELIN.get())
        );

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                activeModel.renderType(activeModel.getTexture()),
                false,
                entity.isFoil()
        );

        activeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownJavelin entity) {
        ItemStack stack = entity.getSyncedItemStack();
        ModularClayJavelinModel activeModel = this.models.getOrDefault(
                stack.getItem(),
                this.models.get(ModItems.MODULAR_CLAY_JAVELIN.get())
        );
        return activeModel.getTexture();
    }
}
