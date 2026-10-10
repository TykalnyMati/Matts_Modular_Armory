package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.EnderManAngerEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

import java.util.UUID;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ArmorEvents {
    @SubscribeEvent
    public static void onEndermanAnger(EnderManAngerEvent event) {
        if (event.getPlayer() instanceof Player player) {
            ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);

            if (headSlot.getItem() instanceof ModularItemInterface modularItem) {
                if (!modularItem.isBroken(headSlot) && modularItem.getAugmentBonuses(headSlot).PUMPKIN_LENS) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Piglin || event.getEntity() instanceof PiglinBrute) {
            LivingEntity newTarget = event.getNewAboutToBeSetTarget();

            if (newTarget instanceof Player player) {
                UUID angryAtUUID = event.getEntity().getBrain().getMemory(MemoryModuleType.ANGRY_AT).orElse(null);

                if (player.getUUID().equals(angryAtUUID)) {
                    return;
                }

                int gildedLevel = getGildedLevel(player);

                if (event.getEntity() instanceof PiglinBrute) {
                    if (gildedLevel == 2) {
                        event.setCanceled(true);
                    }
                } else {
                    if (gildedLevel >= 1) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static int getGildedLevel(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof ModularItemInterface modularItem) {
                if (!modularItem.isBroken(stack)) {
                    AugmentObject bonuses = modularItem.getAugmentBonuses(stack);
                    if (bonuses.SUPER_GILDED) {
                        return 2;
                    } else if (bonuses.GILDED) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}
