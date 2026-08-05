package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.block.entity.custom.ExposerBlockEntity;
import net.matt.modular_armory.block.entity.custom.ModularMinerBlockEntity;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModularMinerMenu extends AbstractContainerMenu {
    public final ModularMinerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ModularMinerMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public ModularMinerMenu(int pContainerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.MODULAR_MINER_MENU.get(), pContainerId);
        this.blockEntity = ((ModularMinerBlockEntity) entity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        // Output Slots
        int[][] outputSlotsPositions = new int[][] {
                {26, 17}, {44, 17}, {62, 17},
                {26, 35}, {44, 35}, {62, 35},
                {26, 53}, {44, 53}, {62, 53}
        };
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, i, outputSlotsPositions[i][0], outputSlotsPositions[i][1]) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
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
            this.addSlot(new SlotItemHandler(blockEntity.itemHandler, i + ModularMinerBlockEntity.RANGE_LENGTH_SLOT, rangeSlotsPositions[i][0], rangeSlotsPositions[i][1]) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (stack.is(ModTags.Items.MODULAR_RANGE_ADDON)) {
                        return super.mayPlace(stack);
                    }
                    return false;
                }
            });
        }

        // Filter Slot

        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, ModularMinerBlockEntity.FILTER_SLOT, 116, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.is(ModTags.Items.FILTER)) {
                    return super.mayPlace(stack);
                }
                return false;
            }
        });
    }

    // TODO implement quick moving to filter/upgrade/range slots
    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.MODULAR_MINER.get());
    }

    // Helper methods for adding player inventory
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 97 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 155));
        }
    }
}
