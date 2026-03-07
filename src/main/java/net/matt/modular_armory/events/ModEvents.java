package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.ModularAxeItem;
import net.matt.modular_armory.items.tools.ModularPickaxeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offhandItem = player.getOffhandItem();

        int xp = event.getAmount();
        int remainingXp = xp;

        // First repairs in main hand
        if (mainHandItem.getItem() instanceof ModularPickaxeItem pickaxe) {
            remainingXp = pickaxe.repairFromXp(mainHandItem, remainingXp);
        } else if (mainHandItem.getItem() instanceof ModularAxeItem axe) {
            remainingXp = axe.repairFromXp(mainHandItem, remainingXp);
        }

        // Repairs in offhand
        if (offhandItem.getItem() instanceof ModularPickaxeItem pickaxe) {
            remainingXp = pickaxe.repairFromXp(offhandItem, remainingXp);
        } else if (offhandItem.getItem() instanceof ModularAxeItem axe) {
            remainingXp = axe.repairFromXp(offhandItem, remainingXp);
        }

        // Reduce the amount of XP player gets
        event.setAmount(remainingXp);
    }
}