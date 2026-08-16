package net.matt.modular_armory.block.entity.custom.helper;

import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.items.addons.ModularRangeAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class ModularMachineBlockEntity extends BlockEntity {
    // Mining
    protected static final int BASE_OPERATING_TIME = 20;
    protected static final int POWERED_OPERATING_TIME = 5;
    protected static final int MAX_ENERGY_CAPACITY = 50000;
    protected static final int MAX_ENERGY_TRANSFER = 2500;
    protected static final int ENERGY_PER_OPERATION = 100;

    // Scan tracking
    protected int currentDepth = 1;
    protected int currentWidth = 0;
    protected int currentHeight = 0;
    protected boolean movingPositiveWidth = true;

    // Redstone mode 0 - Active on High, 1 - Active on Low, 2 - Ignore Signal
    protected int redstoneMode = 0;

    // Display false - off, true - on
    protected boolean rangeDisplayState = false;

    protected int tickCounter = 0;

    protected final CustomEnergyStorage energyStorage = new CustomEnergyStorage(MAX_ENERGY_CAPACITY, MAX_ENERGY_TRANSFER, MAX_ENERGY_TRANSFER) {
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int energyReceived = super.receiveEnergy(toReceive, simulate);

            if (energyReceived > 0 && !simulate) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
            return energyReceived;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int energyExtracted = super.extractEnergy(toExtract, simulate);

            if (energyExtracted > 0 && !simulate) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
            return energyExtracted;
        }
    };

    protected abstract ItemStackHandler getItemHandler();

    public abstract int getRangeLengthSlot();

    public abstract int getRangeWidthSlot();

    public abstract int getRangeHeightSlot();

    protected boolean hasEnoughPower() {
        return this.energyStorage.getEnergyStored() >= ENERGY_PER_OPERATION;
    }

    public ModularMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public int getRedstoneMode() {
        return this.redstoneMode;
    }

    public boolean getRangeDisplayState() {
        return this.rangeDisplayState;
    }

    public void setRedstoneMode(int mode) {
        this.redstoneMode = mode % 3;
        setChanged();
    }

    public void setRangeDisplayState(boolean state) {
        if (this.rangeDisplayState != state) {
            this.rangeDisplayState = state;
            this.setChanged();
        }
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public int getRangeBonus(int slot) {
        Item upgrade = getItemHandler().getStackInSlot(slot).getItem();

        if (upgrade instanceof ModularRangeAddon modularRangeAddon) {
            return modularRangeAddon.getRangeIncrease();
        } else {
            return 0;
        }
    }

    public Direction getFacing() {
        BlockState state = this.getBlockState();
        if (state.hasProperty(ModularMinerBlock.FACING)) {
            return state.getValue(ModularMinerBlock.FACING);
        }
        return Direction.NORTH;
    }

    protected void calculateRelativeOffset(BlockPos origin, Direction facing, int depth, int width, int height, BlockPos.MutableBlockPos target) {
        int forwardX = facing.getStepX() * depth;
        int forwardY = facing.getStepY() * depth;
        int forwardZ = facing.getStepZ() * depth;

        int lateralX = 0;
        int lateralY = 0;
        int lateralZ = 0;

        int verticalX = 0;
        int verticalY = 0;
        int verticalZ = 0;


        if (facing.getAxis() == Direction.Axis.Y) {
            lateralX = width;
            verticalZ = height;
        } else {
            Direction right = facing.getClockWise();
            lateralX = right.getStepX() * width;
            lateralZ = right.getStepZ() * width;
            verticalY = height;
        }

        target.set(
                origin.getX() + forwardX + lateralX + verticalX,
                origin.getY() + forwardY + lateralY + verticalY,
                origin.getZ() + forwardZ + lateralZ + verticalZ
        );
    }

    protected void advanceScanPosition(int maxExtraWidth, int maxExtraHeight, int maxLength) {
        // Sweeps left to right, or right to left
        if (movingPositiveWidth) {
            if (currentWidth < maxExtraWidth) {
                currentWidth++;
                return;
            } else {
                movingPositiveWidth = false;
            }
        } else {
            if (currentWidth > -maxExtraWidth) {
                currentWidth--;
                return;
            } else {
                movingPositiveWidth = true;
            }
        }

        // Move forward to the next column
        if (currentDepth < maxLength) {
            currentDepth++;
            return;
        } else {
            currentDepth = 1;
        }

        // Move to next row
        if (currentHeight > -maxExtraHeight) {
            currentHeight--;
            currentWidth = movingPositiveWidth ? -maxExtraWidth : maxExtraWidth;
            return;
        } else {
            currentHeight = maxExtraHeight;
            currentWidth = movingPositiveWidth ? -maxExtraWidth : maxExtraWidth;
        }

        resetScan();
    }

    protected void resetScan() {
        int maxExtraWidth = getRangeBonus(getRangeLengthSlot());
        int maxExtraHeight = getRangeBonus(getRangeHeightSlot());

        this.currentDepth = 1;
        this.currentWidth = -maxExtraWidth;
        this.currentHeight = maxExtraHeight;
        this.movingPositiveWidth = true;
        setChanged();
    }
}
