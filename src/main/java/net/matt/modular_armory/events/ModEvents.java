package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.ModularAxeItem;
import net.matt.modular_armory.items.tools.ModularPickaxeItem;
import net.matt.modular_armory.items.tools.helpers.ModularDiggerToolItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModEvents {

    // Detecting Xp collection
    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offhandItem = player.getOffhandItem();

        int remainingXp = event.getAmount();

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

    // Canceling repairing in a grindstone
    @SubscribeEvent
    public static void onGrindstoneUpdate(GrindstoneEvent.OnPlaceItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (top.getItem() instanceof ModularDiggerToolItem ||
                bottom.getItem() instanceof ModularDiggerToolItem) {

            event.setCanceled(true);
        }
    }

    // Canceling repairing in an anvil
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ModularDiggerToolItem && !event.getRight().isEmpty()) {
            event.setCanceled(true);
        }
    }

    // Canceling repairing in a crafting grid and anvil (it's messy)
    // This checks the player's open container every tick. If it's a crafting grid
    // and the result is a modular tool repair attempt, it wipes the output slot.
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            AbstractContainerMenu menu = event.player.containerMenu;

            // --- FIX FOR CRAFTING GRID ---
            if (menu instanceof CraftingMenu || menu instanceof InventoryMenu) {
                Slot resultSlot = menu.getSlot(0);
                if (resultSlot.getItem().getItem() instanceof ModularDiggerToolItem) {
                    if (countModularTools(menu, menu instanceof InventoryMenu ? 1 : 1, menu instanceof InventoryMenu ? 5 : 10) >= 2) {
                        resultSlot.set(ItemStack.EMPTY);
                    }
                }
            }

            // --- FIX FOR ANVIL (The Safety Net) ---
            if (menu instanceof AnvilMenu anvil) {
                ItemStack left = anvil.getSlot(0).getItem();
                ItemStack right = anvil.getSlot(1).getItem();

                // If it's our tool and there is a "sacrifice" item in slot 1
                if (left.getItem() instanceof ModularDiggerToolItem && !right.isEmpty()) {
                    // Force the output slot (Slot 2) to stay empty
                    anvil.getSlot(2).set(ItemStack.EMPTY);
                }

                // If our tool is the sacrifice
                if (right.getItem() instanceof ModularDiggerToolItem) {
                    anvil.getSlot(2).set(ItemStack.EMPTY);
                }
            }
        }
    }

    private static int countModularTools(AbstractContainerMenu menu, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (menu.getSlot(i).getItem().getItem() instanceof ModularDiggerToolItem) count++;
        }
        return count;
    }
}