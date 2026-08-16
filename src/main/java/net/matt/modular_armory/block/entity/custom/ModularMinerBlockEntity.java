package net.matt.modular_armory.block.entity.custom;

import com.mojang.authlib.GameProfile;
import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.custom.helper.ModularMachineBreakerBlockEntity;
import net.matt.modular_armory.screen.custom.ModularMinerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ModularMinerBlockEntity extends ModularMachineBreakerBlockEntity implements MenuProvider {
    // Container Data
    protected final ContainerData data;

    private static final GameProfile MINER_GAME_PROFILE = new GameProfile(UUID.fromString("41c82c87-da8e-4a84-9e11-92f33f114421"), "[ModularMiner]");

    public ModularMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODULAR_MINER_BE.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ModularMinerBlockEntity.this.redstoneMode;
                    case 1 -> ModularMinerBlockEntity.this.rangeDisplayState ? 1 : 0;
                    case 2 -> ModularMinerBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> ModularMinerBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> ModularMinerBlockEntity.this.redstoneMode = value;
                    case 1 -> ModularMinerBlockEntity.this.rangeDisplayState = (value == 1);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // Checking if the block should be active
        switch (getRedstoneMode()) {
            // Active on High
            case 0 -> {
                if (!pLevel.hasNeighborSignal(pPos)) {
                    if (pState.getValue(ModularMinerBlock.ACTIVE)) {
                        pLevel.setBlock(pPos, pState.setValue(ModularMinerBlock.ACTIVE, false), Block.UPDATE_ALL);
                    }
                    return;
                }
            }
            // Active on Low
            case 1 -> {
                if (pLevel.hasNeighborSignal(pPos)) {
                    if (pState.getValue(ModularMinerBlock.ACTIVE)) {
                        pLevel.setBlock(pPos, pState.setValue(ModularMinerBlock.ACTIVE, false), Block.UPDATE_ALL);
                    }
                    return;
                }
            }
            // Ignore Signal
            default -> {}
        }

        if (getAugmentPowerRequirements() > this.energyStorage.getEnergyStored() && pState.getValue(ModularMinerBlock.ACTIVE)) {
            pLevel.setBlock(pPos, pState.setValue(ModularMinerBlock.ACTIVE, false), Block.UPDATE_ALL);
        } else if (!pState.getValue(ModularMinerBlock.ACTIVE) && this.energyStorage.getEnergyStored() >= getAugmentPowerRequirements()) {
            pLevel.setBlock(pPos, pState.setValue(ModularMinerBlock.ACTIVE, true), Block.UPDATE_ALL);
        }

        if (this.energyStorage.getEnergyStored() < getAugmentPowerRequirements()) {
            return;
        }

        int currentRequiredTime = hasEnoughPower() ? POWERED_OPERATING_TIME : BASE_OPERATING_TIME;

        this.tickCounter++;
        if (this.tickCounter >= currentRequiredTime && !isInventoryFull()) {
            this.tickCounter = 0;
            this.mineNextBlock((ServerLevel) pLevel, pPos, pState, (currentRequiredTime == POWERED_OPERATING_TIME));
        } else if (this.tickCounter > 2 * BASE_OPERATING_TIME) {
            this.tickCounter = 2 * BASE_OPERATING_TIME;
        }
    }

    @Override
    public GameProfile getMinerGameProfile() {
        return MINER_GAME_PROFILE;
    }

    // Block Entity Specific Methods
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mattsmodulararmory.modular_miner");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ModularMinerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandler().getSlots());
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            inventory.setItem(i, getItemHandler().getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if (side == null) {
            return getItemHandler();
        }

        IItemHandler outputView = new RangedWrapper(getItemHandler(), OUTPUT_START, OUTPUT_START + OUTPUT_SLOTS);

        return new IItemHandler() {
            @Override
            public int getSlots() {
                return outputView.getSlots();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return outputView.getStackInSlot(slot);
            }

            @Override
            public int getSlotLimit(int slot) {
                return outputView.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return outputView.isItemValid(slot, stack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                // Cannot push into
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                // Can only pull from slots 0 - 8
                return outputView.extractItem(slot, amount, simulate);
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", getItemHandler().serializeNBT(pRegistries));
        pTag.putInt("energy", getEnergyStorage().getEnergyStored());

        pTag.putInt("miner.tickCounter", tickCounter);
        pTag.putInt("miner.currentDepth", currentDepth);
        pTag.putInt("miner.currentWidth", currentWidth);
        pTag.putInt("miner.currentHeight", currentHeight);
        pTag.putBoolean("miner.movingPositiveWidth", movingPositiveWidth);
        pTag.putInt("miner.redstoneMode", redstoneMode);
        pTag.putBoolean("miner.rangeDisplayState", rangeDisplayState);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        getItemHandler().deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        if (pTag.contains("energy")) {
            int savedEnergy = pTag.getInt("energy");
            this.energyStorage.setEnergyDirectly(savedEnergy - this.energyStorage.getEnergyStored());
        }

        this.tickCounter = pTag.getInt("miner.tickCounter");
        this.currentDepth = pTag.getInt("miner.currentDepth");
        this.currentWidth = pTag.getInt("miner.currentWidth");
        this.currentHeight = pTag.getInt("miner.currentHeight");
        this.movingPositiveWidth = pTag.getBoolean("miner.movingPositiveWidth");
        this.redstoneMode = pTag.getInt("miner.redstoneMode");
        this.rangeDisplayState = pTag.getBoolean("miner.rangeDisplayState");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
