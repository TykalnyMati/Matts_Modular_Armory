package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.screen.custom.AugmentTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AugmentTableBlockEntity extends BlockEntity implements MenuProvider {
    private ItemStack lastMainStack = ItemStack.EMPTY;

    public final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            int slotSize = 1;

            if (slot != 0) {
                if (stack.is(Items.SUGAR_CANE)) {
                    slotSize = 12;
                }
            }

            return slotSize;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();

            if (!level.isClientSide()) {
                if (slot == 0) {
                    ItemStack current = inventory.getStackInSlot(0);

                    // Item removed
                    if (!lastMainStack.isEmpty() && current.isEmpty()) {
                        saveAugmentsToItem(lastMainStack);
                        clearAugmentSlots();
                    }

                    // Item inserted
                    if (!current.isEmpty()) {
                        loadAugmentsFromItem(current);
                    }

                    lastMainStack = current.copy();
                }

                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    // Saving augments into the item
    private void saveAugmentsToItem(ItemStack stack) {
        List<ItemStack> augments = new ArrayList<>();

        for (int i = 1; i < 9; i++) {
            augments.add(inventory.getStackInSlot(i).copy());
        }

        stack.set(ModDataComponentTypes.AUGMENTS.get(), augments);
    }

    // Loading augments from the item
    private void loadAugmentsFromItem(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
            return;
        }

        List<ItemStack> augments = stack.get(ModDataComponentTypes.AUGMENTS.get());

        if (augments.isEmpty()) {
            return;
        }

        int listIndex = 0;
        for (int i = 1; i < 9; i++) {
            inventory.setStackInSlot(i, augments.get(listIndex).copy());
            listIndex++;
        }
    }

    // Clearing after the items have been transferred
    private void clearAugmentSlots() {
        for (int i = 1; i < 9; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public AugmentTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.AUGMENT_TABLE_BE.get(), pPos, pBlockState);
    }

    // Rotation of item
    private float rotation;
    public float getRenderingRotation() {
        rotation += 0.25f;

        if (rotation >= 360) {
            rotation = 0;
        }

        return rotation;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", inventory.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }

    // Menu
    @Override
    public Component getDisplayName() {
        return Component.literal("Augment Table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AugmentTableMenu(pContainerId, pPlayerInventory, this);
    }

    // Packets
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
