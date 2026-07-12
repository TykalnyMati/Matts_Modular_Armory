package net.matt.modular_armory.network;

import io.netty.buffer.ByteBuf;
import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RedstoneModePayload(BlockPos pos, int mode) implements CustomPacketPayload {
    public static final Type<RedstoneModePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "redstone_mode"));

    public static final StreamCodec<ByteBuf, RedstoneModePayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RedstoneModePayload::pos,
            ByteBufCodecs.VAR_INT, RedstoneModePayload::mode,
            RedstoneModePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
