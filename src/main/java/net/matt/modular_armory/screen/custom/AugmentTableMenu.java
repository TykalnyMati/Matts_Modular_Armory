package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.custom.AugmentTableBlockEntity;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class AugmentTableMenu extends AbstractContainerMenu {
    public final AugmentTableBlockEntity blockEntity;
    private final Level level;

    private final int[][] slotPositions = {
            {80, 35}, {62, 17}, {80, 17},
            {98, 17}, {62, 35}, {98, 35},
            {62, 53}, {80, 53}, {98, 53}};

    private boolean augmentSlotsActive = false;

    public AugmentTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public AugmentTableMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.AUGMENT_TABLE_MENU.get(), pContainerId);
        this.blockEntity = ((AugmentTableBlockEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Middle slot
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, slotPositions[0][0], slotPositions[0][1]));

        // 1st augment slot
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, slotPositions[1][0], slotPositions[1][1]) {
            @Override
            public boolean isActive() {
                ItemStack stack = ((AugmentTableBlockEntity) blockEntity).inventory.getStackInSlot(0);
                augmentSlotsActive = stack.is(ModTags.Items.MODULAR_ITEMS);
                return augmentSlotsActive;
            }
        });

        // Other augment slots
        for (int i = 2; i < 9; i++) {
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, i, slotPositions[i][0], slotPositions[i][1]) {
                @Override
                public boolean isActive() {
                    return augmentSlotsActive;
                }
            });
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 9;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copy = sourceStack.copy();

        int toolSlot = TE_INVENTORY_FIRST_SLOT_INDEX;
        int augmentStart = TE_INVENTORY_FIRST_SLOT_INDEX + 1;
        int augmentEnd = TE_INVENTORY_FIRST_SLOT_INDEX + 9;

        // TOOL SLOT → PLAYER
        if (index == toolSlot) {

            if (blockEntity instanceof AugmentTableBlockEntity table) {
                table.wrappedSaveAugmentsToItem(sourceStack);
                table.clearAugmentSlots();
            }

            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }

        // AUGMENT SLOT → PLAYER
        else if (index >= augmentStart && index < augmentEnd) {

            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }

        // PLAYER → MACHINE
        else if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {

            // TOOL
            if (sourceStack.is(ModTags.Items.MODULAR_ITEMS)) {

                if (!moveItemStackTo(sourceStack, toolSlot, toolSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // AUGMENTS
            else if (sourceStack.is(ModTags.Items.MODULAR_ITEMS_AUGMENT)) {
                // Checks if the slots are supposed to be open
                if (!this.getSlot(toolSlot).getItem().is(ModTags.Items.MODULAR_ITEMS)) {
                    return ItemStack.EMPTY;
                }

                // Check existing augment slots
                for (int i = augmentStart; i < augmentEnd; i++) {
                    Slot slot = this.getSlot(i);
                    ItemStack stack = slot.getItem();

                    if (!stack.isEmpty() && stack.is(sourceStack.getItem())) {
                        int max = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());

                        if (stack.getCount() < max) {
                            if (!moveItemStackTo(sourceStack, i, i + 1, false)) {
                                return ItemStack.EMPTY;
                            }
                            return sourceStack;
                        }
                    }
                }

                if (!moveItemStackTo(sourceStack, augmentStart, augmentEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // EVERYTHING ELSE
            else {
                return ItemStack.EMPTY;
            }
        }

        // FAILSAFE
        else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copy;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.AUGMENT_TABLE.get());
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
