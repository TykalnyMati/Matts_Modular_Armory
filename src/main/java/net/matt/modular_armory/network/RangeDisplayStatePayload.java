package net.matt.modular_armory.network;

import io.netty.buffer.ByteBuf;
import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RangeDisplayStatePayload(BlockPos pos, boolean state) implements CustomPacketPayload {
    public static final Type<RangeDisplayStatePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "range_display_state"));

    public static final StreamCodec<ByteBuf, RangeDisplayStatePayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RangeDisplayStatePayload::pos,
            ByteBufCodecs.BOOL, RangeDisplayStatePayload::state,
            RangeDisplayStatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
