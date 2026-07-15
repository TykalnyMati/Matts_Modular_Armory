package net.matt.modular_armory.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matt.modular_armory.block.entity.custom.ModularMinerBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class ModularMinerBlockEntityRenderer implements BlockEntityRenderer<ModularMinerBlockEntity> {

    public ModularMinerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ModularMinerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction facing = pBlockEntity.getFacing();

        int maxExtraWidth = pBlockEntity.getRangeBonus(ModularMinerBlockEntity.RANGE_WIDTH_SLOT);
        int maxExtraHeight = pBlockEntity.getRangeBonus(ModularMinerBlockEntity.RANGE_HEIGHT_SLOT);
        int maxLength = 1 + pBlockEntity.getRangeBonus(ModularMinerBlockEntity.RANGE_LENGTH_SLOT);

        double minX = 0, minY = 0, minZ = 0;
        double maxX = 1, maxY = 1, maxZ = 1;

        int stepX = facing.getStepX();
        int stepY = facing.getStepY();
        int stepZ = facing.getStepZ();

        if (stepX != 0) {
            minX = stepX > 0 ? 1 : -maxLength;
            maxX = stepX > 0 ? 1 + maxLength : 0;
        } else if (stepY != 0) {
            minY = stepY > 0 ? 1 : -maxLength;
            maxY = stepY > 0 ? 1 + maxLength : 0;
        } else if (stepZ != 0) {
            minZ = stepZ > 0 ? 1 : -maxLength;
            maxZ = stepZ > 0 ? 1 + maxLength : 0;
        }

        // When oriented Up/Down
        if (facing.getAxis() == Direction.Axis.Y) {
            minX -= maxExtraWidth;
            maxX += maxExtraWidth;
            minZ -= maxExtraHeight;
            maxZ += maxExtraHeight;
        }
        // When oriented North/South/East/West
        else {
            Direction right = facing.getClockWise();
            int rx = Math.abs(right.getStepX());
            int rz = Math.abs(right.getStepZ());

            minX -= rx * maxExtraWidth;
            maxX += rx * maxExtraWidth;
            minZ -= rz * maxExtraWidth;
            maxZ += rz * maxExtraWidth;

            minY -= maxExtraHeight;
            maxY += maxExtraHeight;
        }

        // Bounding box
        AABB area = new AABB(minX, minY, minZ, maxX, maxY, maxZ);

        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                pPoseStack,
                vertexConsumer,
                area,
                0.352f, 0.976f, 0.913f, 0.75f
        );
    }

    @Override
    public boolean shouldRenderOffScreen(ModularMinerBlockEntity pBlockEntity) {
        return true;
    }
}