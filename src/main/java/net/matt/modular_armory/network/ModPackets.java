package net.matt.modular_armory.network;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.entity.custom.ModularMinerBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModPackets {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                RedstoneModePayload.TYPE,
                RedstoneModePayload.CODEC,
                ModPackets::handleRedstoneMode
        );
    }

    private static void handleRedstoneMode(final RedstoneModePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(payload.pos()) instanceof ModularMinerBlockEntity modularMinerBlockEntity) {
                modularMinerBlockEntity.setRedstoneMode(payload.mode());
            }
        });
    }
}
