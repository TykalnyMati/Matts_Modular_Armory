package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.addons.FilterItem;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.screen.custom.helpers.GhostSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class FilterMenu extends AbstractContainerMenu {
    private final ItemStack filterItem;
    private boolean isUpdating = false;

    private final ItemStackHandler filterInventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            if (isUpdating) {
                return;
            }

            isUpdating = true;
            try {
                filterItem.set(ModDataComponentTypes.FILTERED_ITEMS.get(), getItemsInFilter());
            } finally {
                isUpdating = false;
            }
        }
    };

    public FilterMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, inventory.player.getMainHandItem());
    }

    public FilterMenu(int containerId, Inventory playerInventory, ItemStack filterItem) {
        super(ModMenuTypes.FILTER_MENU.get(), containerId);
        this.filterItem = filterItem;

        // Unloading items from data component into the menu
        if (filterItem.has(ModDataComponentTypes.FILTERED_ITEMS.get())) {
            List<ItemStack> filteredItems = filterItem.get(ModDataComponentTypes.FILTERED_ITEMS.get());

            if (filteredItems != null && !filteredItems.isEmpty()) {
                isUpdating = true;
                for (int i = 0; i < Math.min(filteredItems.size(), 9); i++) {
                    filterInventory.setStackInSlot(i, filteredItems.get(i).copy());
                }
                isUpdating = false;
            }
        }

        // Slots
        int[][] slotPositions = new int[][] {
                {62, 17}, {80, 17}, {98, 17},
                {62, 35}, {80, 35}, {98, 35},
                {62, 53}, {80, 53}, {98, 53}
        };

        for (int i = 0; i < 9; i++) {
            this.addSlot(new GhostSlot(filterInventory, i, slotPositions[i][0], slotPositions[i][1]));
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private List<ItemStack> getItemsInFilter() {
        List<ItemStack> itemsInInventory = new ArrayList<>();

        for (int i = 0; i < filterInventory.getSlots(); i++) {
            itemsInInventory.add(filterInventory.getStackInSlot(i).copy());
        }

        return itemsInInventory;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem() && index >= 9) {
            // If clicking player inventory
            ItemStack stackInSlot = slot.getItem();

            // Look for an empty ghost slot to populate
            for (int i = 0; i < 9; i++) {
                GhostSlot ghostSlot = (GhostSlot) this.slots.get(i);
                if (!ghostSlot.hasItem()) {
                    ItemStack ghostCopy = stackInSlot.copy();
                    ghostCopy.setCount(1);
                    ghostSlot.set(ghostCopy);
                    break;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < 9) {
            Slot slot = this.slots.get(slotId);
            if (slot instanceof GhostSlot ghostSlot) {
                ItemStack carried = getCarried();
                ghostSlot.handleGhostClick(carried);
                return;
            }
        }

        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof FilterItem;
    }

    // Helper methods for adding player inventory
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
