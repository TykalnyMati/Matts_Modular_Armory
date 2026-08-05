package net.matt.modular_armory.screen.custom.helpers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class GhostSlot extends SlotItemHandler {
    public GhostSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    // Handles player clicking the slot
    public void handleGhostClick(ItemStack carriedStack) {
        // Clicking with an empty hand, erases the filter
        if (carriedStack.isEmpty()) {
            set(ItemStack.EMPTY);
        }
        // Otherwise copies the item and forces it to 1 count
        else {
            ItemStack ghostCopy = carriedStack.copy();
            ghostCopy.setCount(1);
            set(ghostCopy);
        }
    }
}
