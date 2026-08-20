package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.custom.ModularBuilderBlockEntity;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.screen.custom.helpers.ModularMachineMenu;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModularBuilderMenu extends ModularMachineMenu {
    public final ModularBuilderBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ModularBuilderMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public ModularBuilderMenu(int pContainerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.MODULAR_BUILDER_MENU.get(), pContainerId);
        this.blockEntity = ((ModularBuilderBlockEntity) entity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addDataSlots(data);

        // Input Slots
        int[][] inputSlotsPositions = new int[][] {
                {26, 17}, {44, 17}, {62, 17},
                {26, 35}, {44, 35}, {62, 35},
                {26, 53}, {44, 53}, {62, 53}
        };
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), i, inputSlotsPositions[i][0], inputSlotsPositions[i][1]) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return true;
                }
            });
        }

        // Range Slots
        int [][] rangeSlotsPositions = new int[][] {
                {152, 53},
                {152, 35},
                {152, 17}
        };
        for (int i = 0; i < 3; i++) {
            this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), i + ModularBuilderBlockEntity.RANGE_LENGTH_SLOT, rangeSlotsPositions[i][0], rangeSlotsPositions[i][1]) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (stack.is(ModTags.Items.MODULAR_RANGE_ADDON)) {
                        return super.mayPlace(stack);
                    }
                    return false;
                }
            });
        }
    }

    public int getEnergyStored() {
        return data.get(2);
    }

    public int getMaxEnergyStored() {
        return data.get(3);
    }

    public boolean hasPower() {
        return data.get(2) > 0;
    }

    public int getEnergyDisplayProgress() {
        int energyStored = data.get(2);
        int maxEnergy = data.get(3);
        int energyProgress = 54;

        return maxEnergy != 0 && energyStored != 0 ? energyStored * energyProgress / maxEnergy : 0;
    }

    // Slot indexes
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int INPUT_SLOT_START = 36;
    private static final int INPUT_SLOT_COUNT = 9;
    private static final int RANGE_SLOT_START = 45;
    private static final int RANGE_SLOT_COUNT = 3;
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            // from container to player
            if (index >= VANILLA_SLOT_COUNT) {
                if (!this.moveItemStackTo(itemStack1, 0, VANILLA_SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemStack);
            }
            // from player to container
            else {
                // Range Addons -> Range Slots
                if (itemStack1.is(ModTags.Items.MODULAR_RANGE_ADDON)) {
                    if (!this.moveItemStackTo(itemStack1, RANGE_SLOT_START, RANGE_SLOT_START + RANGE_SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Other items
                else if (!this.moveItemStackTo(itemStack1, INPUT_SLOT_START, INPUT_SLOT_START + INPUT_SLOT_COUNT, false)){
                    // Move between Player Inventory and Hotbar if the item doesn't fit
                    if (index < 27) {
                        if (!this.moveItemStackTo(itemStack1, 27, 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 36) {
                        if (!this.moveItemStackTo(itemStack1, 0, 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (itemStack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.MODULAR_BUILDER.get());
    }
}
