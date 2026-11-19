package net.matt.modular_armory.client.model;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class AdaptiveFleshJavelinModel extends ModularClayJavelinModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "adaptive_flesh_javelin"),
            "main"
    );

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/entity/adaptive_flesh_javelin.png");

    public AdaptiveFleshJavelinModel(ModelPart root) {
        super(root);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
