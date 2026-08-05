package net.matt.modular_armory.network;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.screen.custom.FilterMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetGhostSlotPayload(int slotId, ItemStack stack) implements CustomPacketPayload {
    public static final Type<SetGhostSlotPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "set_ghost_slot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetGhostSlotPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SetGhostSlotPayload::slotId,
            ItemStack.OPTIONAL_STREAM_CODEC, SetGhostSlotPayload::stack,
            SetGhostSlotPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(final SetGhostSlotPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof FilterMenu filterMenu) {
                if (data.slotId() >= 0 && data.slotId() < 9) {
                    ItemStack copy = data.stack().copy();
                    if (!copy.isEmpty()) {
                        copy.setCount(1);
                    }
                    filterMenu.slots.get(data.slotId()).set(copy);
                }
            }
        });
    }
}