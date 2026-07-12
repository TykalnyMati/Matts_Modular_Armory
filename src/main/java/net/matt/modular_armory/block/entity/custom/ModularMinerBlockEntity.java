package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.screen.custom.ModularMinerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModularMinerBlockEntity extends BlockEntity implements MenuProvider {
    // Inventory indexes
    public static final int OUTPUT_START = 0;
    public static final int FILTER_SLOT = 9;
    public static final int AUGMENT_START = 10;
    public static final int RANGE_LENGTH_SLOT = 12;
    public static final int RANGE_WIDTH_SLOT = 13;
    public static final int RANGE_HEIGHT_SLOT = 14;

    // Inventory slots
    public static final int OUTPUT_SLOTS = 9;
    public static final int AUGMENT_SLOTS = 2;

    private int tickCounter = 0;
    private int miningTime = 20;

    // Scan tracking
    private int currentDepth = 1;
    private int currentWidth = 0;
    private int currentHeight = 0;
    private boolean movingPositiveWidth = true;

    // Redstone mode 0 - Active on High, 1 - Active on Low, 2 - Ignore Signal
    private int redstoneMode = 0;
    protected final ContainerData data;

    public final ItemStackHandler itemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public ModularMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODULAR_MINER_BE.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ModularMinerBlockEntity.this.redstoneMode;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> ModularMinerBlockEntity.this.redstoneMode = value;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public int getRedstoneMode() {
        return this.redstoneMode;
    }

    public void setRedstoneMode(int mode) {
        this.redstoneMode = mode % 3;
        setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        // Checking if the block should be active
        switch (redstoneMode) {
            case 0 -> { if (!level.hasNeighborSignal(pos)) return; } // Active on High
            case 1 -> { if (level.hasNeighborSignal(pos)) return; }  // Active on Low
            default -> {} // Ignore Signal
        }

        this.tickCounter++;
        if (this.tickCounter >= this.miningTime) {
            this.tickCounter = 0;
            this.mineNextBlock((ServerLevel) level, pos, state);
        }
    }

    private void mineNextBlock(ServerLevel level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(ModularMinerBlock.FACING);

        int maxExtraWidth = getRangeBonus(RANGE_WIDTH_SLOT);
        int maxExtraHeight = getRangeBonus(RANGE_HEIGHT_SLOT);
        int maxLength = 1 + getRangeBonus(RANGE_LENGTH_SLOT);

        // Maximum number of blocks that can be checked in a tick
        int positionsChecked = 0;
        int maxChecksPerTick = 16;

        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
        while (positionsChecked < maxChecksPerTick) {
            calculateRelativeOffset(pos, facing, currentDepth, currentWidth, currentHeight, targetPos);
            BlockState targetState = level.getBlockState(targetPos);

            // Goes to the next position
            advanceScanPosition(maxExtraWidth, maxExtraHeight, maxLength);
            positionsChecked++;

            // Mines a block if possible, skips air, unbreakable blocks and fluids
            if (!targetState.isAir() && targetState.getDestroySpeed(level, targetPos) >= 0 && !(targetState.getBlock() instanceof LiquidBlock)) {
                mineBlock(level, targetPos, targetState);
                return;
            }
        }
    }

    private void advanceScanPosition(int maxExtraWidth, int maxExtraHeight, int maxLength) {
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

    private void resetScan() {
        int maxExtraWidth = getRangeBonus(RANGE_WIDTH_SLOT);
        int maxExtraHeight = getRangeBonus(RANGE_HEIGHT_SLOT);

        this.currentDepth = 1;
        this.currentWidth = -maxExtraWidth;
        this.currentHeight = maxExtraHeight;
        this.movingPositiveWidth = true;
        setChanged();
    }

    private void mineBlock(ServerLevel level, BlockPos targetPos, BlockState targetState) {
        // Helper tool for applying augments
        ItemStack helperTool = ItemStack.EMPTY;
        List<ItemStack> drops = Block.getDrops(targetState, level, targetPos, level.getBlockEntity(targetPos), null, helperTool);

        //TODO replace with inventory check to see if there is space for items
        if (hasRoomForDrops(drops)) {
            level.destroyBlock(targetPos, true);
//            for (ItemStack drop : drops) {
//                ModularMinerBlock.popResource(level, targetPos, drop);
//            }
            setChanged();
        }
    }

    // TODO add reading the amount of upgrades from inventory
    private int getRangeBonus(int slot) {
        return 3;
    }

    private void calculateRelativeOffset(BlockPos origin, Direction facing, int depth, int width, int height, BlockPos.MutableBlockPos target) {
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

    private boolean hasRoomForDrops(List<ItemStack> drops) {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mattsmodulararmory.modular_miner");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ModularMinerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("miner.tickCounter", tickCounter);
        pTag.putInt("miner.miningTime", miningTime);
        pTag.putInt("miner.currentDepth", currentDepth);
        pTag.putInt("miner.currentWidth", currentWidth);
        pTag.putInt("miner.currentHeight", currentHeight);
        pTag.putBoolean("miner.movingPositiveWidth", movingPositiveWidth);
        pTag.putInt("miner.redstoneMode", redstoneMode);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        this.tickCounter = pTag.getInt("miner.tickCounter");
        this.miningTime = pTag.getInt("miner.miningTime");
        this.currentDepth = pTag.getInt("miner.currentDepth");
        this.currentWidth = pTag.getInt("miner.currentWidth");
        this.currentHeight = pTag.getInt("miner.currentHeight");
        this.movingPositiveWidth = pTag.getBoolean("miner.movingPositiveWidth");
        this.redstoneMode = pTag.getInt("miner.redstoneMode");
    }
}
