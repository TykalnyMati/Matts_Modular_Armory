package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentGeneric;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.screen.custom.AugmentTableMenu;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AugmentTableBlockEntity extends BlockEntity implements MenuProvider {
    boolean isClearing = false;

    public int getDynamicLimit(int slot, ItemStack stack) {
        int slotSize = 1;
        ItemStack modularItemStack = inventory.getStackInSlot(0);

        if (slot == 0) {
            return 1;
        }

        if (!modularItemStack.isEmpty()) {
            slotSize = 0;
        }

        int tier = 0;
        if (modularItemStack.is(ModTags.Items.MODULAR_TIER_0)) {
            tier = 0;
        } else if (modularItemStack.is(ModTags.Items.MODULAR_TIER_1)) {
            tier = 1;
        } else if (modularItemStack.is(ModTags.Items.MODULAR_TIER_2)) {
            tier = 2;
        } else if (modularItemStack.is(ModTags.Items.MODULAR_TIER_3)) {
            tier = 3;
        }

        if (stack.getItem() instanceof AugmentGeneric augmentItem) {

            // Generic Augments
            if (augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_ITEMS)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }

            // Tool Augments
            if (modularItemStack.is(ModTags.Items.MODULAR_TOOL) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_TOOL)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }

            // Weapon Augments
            if (modularItemStack.is(ModTags.Items.MODULAR_WEAPON) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_WEAPON)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }

            // Armor Augments
            if (modularItemStack.is(ModTags.Items.MODULAR_ARMOR) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_ARMOR)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }

            // Sword Augments
            if (modularItemStack.is(ModTags.Items.MODULAR_SWORD) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_SWORD)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }

            // Specific Armor Piece Augments
            if (modularItemStack.is(ModTags.Items.MODULAR_HELMET) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_HELMET)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            } else if (modularItemStack.is(ModTags.Items.MODULAR_CHESTPLATE) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_CHESTPLATE)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            } else if (modularItemStack.is(ModTags.Items.MODULAR_LEGGINGS) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_LEGGINGS)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            } else if (modularItemStack.is(ModTags.Items.MODULAR_BOOTS) && augmentItem.augmentTypes.contains(ModTags.Items.MODULAR_BOOTS)) {
                slotSize = augmentItem.augmentLevels.get(tier);
            }
        }

        return slotSize;
    }

    public boolean hasItemInAnotherSlot(int currentSlot, ItemStack stack) {
        for (int i = 1; i < 9; i++) {
            if (i != currentSlot) {
                ItemStack slotStack = inventory.getStackInSlot(i);
                if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(slotStack, stack)) {
                    // Duplicate found
                    return true;
                }
            }
        }
        return false;
    }

    public final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return getDynamicLimit(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();

            if (!level.isClientSide()) {
                if (slot == 0) {
                    ItemStack current = inventory.getStackInSlot(0);
                    // Item inserted
                    if (!current.isEmpty()) {
                        loadAugmentsFromItem(current);
                    }else {
                        isClearing = true;
                        clearAugmentSlots();
                        isClearing = false;
                    }
                } else if(!isClearing) {
                    ItemStack item = inventory.getStackInSlot(0);
                    if (!item.isEmpty()) {
                        saveAugmentsToItem(item);
                    }
                }

                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 && !simulate) {
                ItemStack stack = getStackInSlot(slot);
                wrappedSaveAugmentsToItem(stack);
            }

            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            // Find if this item type already exists in another slot
            for (int i = 0; i < getSlots(); i++) {
                if (i != slot && !getStackInSlot(i).isEmpty() && ItemStack.isSameItemSameComponents(getStackInSlot(i), stack)) {
                    // If it exists elsewhere, reject insertion
                    return stack;
                }
            }
            // Otherwise, use default behavior
            return super.insertItem(slot, stack, simulate);
        }
    };

    public void wrappedSaveAugmentsToItem(ItemStack stack) {
        if (!stack.isEmpty() && stack.is(ModTags.Items.MODULAR_ITEMS)) {
            saveAugmentsToItem(stack);
            level.playSound(null, getBlockPos(), SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS);
            //clearAugmentSlots();
        }
    }

    // Saving augments into the item
    private void saveAugmentsToItem(ItemStack stack) {
        List<ItemStack> augments = new ArrayList<>();

        for (int i = 1; i < 9; i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                augments.add(itemStack.copy());
            }
        }

        stack.set(ModDataComponentTypes.AUGMENTS.get(), augments);

        applyEnchantmentAugmentsToItem(stack);
    }

    private void applyEnchantmentAugmentsToItem(ItemStack stack) {
        if (stack.getItem() instanceof ModularItemInterface modularItem) {
            if (!modularItem.isBroken(stack)) {
                modularItem.applyVanillaEnchantments(stack, level);
            }
        }
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

        clearAugmentSlots();

        int listIndex = 0;
        for (int i = 1; i < 9 && listIndex < augments.size(); i++) {
            inventory.setStackInSlot(i, augments.get(listIndex).copy());
            listIndex++;
        }
    }

    // Clearing after the items have been transferred
    public void clearAugmentSlots() {
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
        wrappedSaveAugmentsToItem(inventory.getStackInSlot(0));
        SimpleContainer inv = new SimpleContainer(inventory.getStackInSlot(0));
//        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
//        for (int i = 0; i < inventory.getSlots(); i++) {
//            inv.setItem(i, inventory.getStackInSlot(i));
//        }

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
