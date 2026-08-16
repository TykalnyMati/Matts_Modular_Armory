package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.custom.ModularBuilderBlock;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.custom.helper.ModularMachineBlockEntity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ModularBuilderBlockEntity extends ModularMachineBlockEntity implements MenuProvider {
    public static final int RANGE_LENGTH_SLOT = 9;
    public static final int RANGE_WIDTH_SLOT = 10;
    public static final int RANGE_HEIGHT_SLOT = 11;

    // Container Data
    protected final ContainerData data;

    private ItemStackHandler itemHandler = new ItemStackHandler(12) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == RANGE_LENGTH_SLOT || slot == RANGE_WIDTH_SLOT || slot == RANGE_HEIGHT_SLOT) {
                return 1;
            } else {
                return super.getSlotLimit(slot);
            }
        }
    };

    public ModularBuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODULAR_BUILDER_BE.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ModularBuilderBlockEntity.this.redstoneMode;
                    case 1 -> ModularBuilderBlockEntity.this.rangeDisplayState ? 1 : 0;
                    case 2 -> ModularBuilderBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> ModularBuilderBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> ModularBuilderBlockEntity.this.redstoneMode = value;
                    case 1 -> ModularBuilderBlockEntity.this.rangeDisplayState = (value == 1);
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
                    if (pState.getValue(ModularBuilderBlock.ACTIVE)) {
                        pLevel.setBlock(pPos, pState.setValue(ModularBuilderBlock.ACTIVE, false), Block.UPDATE_ALL);
                    }
                    return;
                }
            }
            // Active on Low
            case 1 -> {
                if (pLevel.hasNeighborSignal(pPos)) {
                    if (pState.getValue(ModularBuilderBlock.ACTIVE)) {
                        pLevel.setBlock(pPos, pState.setValue(ModularBuilderBlock.ACTIVE, false), Block.UPDATE_ALL);
                    }
                    return;
                }
            }
            // Ignore Signal
            default -> {}
        }

        if (!pState.getValue(ModularBuilderBlock.ACTIVE)) {
            pLevel.setBlock(pPos, pState.setValue(ModularBuilderBlock.ACTIVE, true), Block.UPDATE_ALL);
        }

        int currentRequiredTime = hasEnoughPower() ? POWERED_OPERATING_TIME : BASE_OPERATING_TIME;

        this.tickCounter++;
        if (this.tickCounter >= currentRequiredTime) {
            this.tickCounter = 0;
            this.placeNextBlock((ServerLevel) pLevel, pPos, pState, (currentRequiredTime == POWERED_OPERATING_TIME));
        } else if (this.tickCounter > 2 * BASE_OPERATING_TIME) {
            this.tickCounter = 2 * BASE_OPERATING_TIME;
        }
    }

    private void placeNextBlock(ServerLevel level, BlockPos pos, BlockState state, boolean consumePower) {
        Direction facing = state.getValue(ModularBuilderBlock.FACING);

        int maxExtraWidth = getRangeBonus(RANGE_WIDTH_SLOT);
        int maxExtraHeight = getRangeBonus(RANGE_HEIGHT_SLOT);
        int maxLength = 1 + getRangeBonus(RANGE_LENGTH_SLOT);

        if (Math.abs(currentHeight) > maxExtraHeight || Math.abs(currentWidth) > maxExtraWidth || currentDepth > maxLength) {
            resetScan();
        }

        // Maximum number of blocks that can be checked in a tick
        int positionsChecked = 0;
        int maxChecksPerTick = Math.max(8, ((maxLength * ((2 * maxExtraWidth) + 1) * ((2 * maxExtraHeight) + 1)) / 8));

        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
        while (positionsChecked < maxChecksPerTick) {
            calculateRelativeOffset(pos, facing, currentDepth, currentWidth, currentHeight, targetPos);

            // Fallback when position unloaded
            if (!level.isLoaded(targetPos)) {
                advanceScanPosition(maxExtraWidth, maxExtraHeight, maxLength);
                positionsChecked++;
                continue;
            }

            BlockState targetState = level.getBlockState(targetPos);

            // Goes to the next position
            advanceScanPosition(maxExtraWidth, maxExtraHeight, maxLength);
            positionsChecked++;

            if (targetState.isAir() || (targetState.getBlock() instanceof LiquidBlock)) {
                placeBlock(level, targetPos, consumePower);
            }
        }
    }

    private void placeBlock(ServerLevel level, BlockPos targetPos, boolean consumePower) {
        if(level.setBlock(targetPos, Blocks.COBBLESTONE.defaultBlockState(), 3)) {
            if (consumePower) {
                this.energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
            }

            setChanged();
        }
    }

    @Override
    protected ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getRangeLengthSlot() {
        return RANGE_LENGTH_SLOT;
    }

    @Override
    public int getRangeWidthSlot() {
        return RANGE_WIDTH_SLOT;
    }

    @Override
    public int getRangeHeightSlot() {
        return RANGE_HEIGHT_SLOT;
    }

    // Block Entity Specific Methods
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mattsmodulararmory.modular_builder");
    }

    @Override //TODO
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandler().getSlots());
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            inventory.setItem(i, getItemHandler().getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    //TODO
//    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
//
//    }

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
