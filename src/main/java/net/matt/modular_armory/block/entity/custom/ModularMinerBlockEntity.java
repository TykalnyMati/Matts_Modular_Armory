package net.matt.modular_armory.block.entity.custom;

import com.mojang.authlib.GameProfile;
import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.custom.helper.CustomEnergyStorage;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.addons.ModularAugmentAddon;
import net.matt.modular_armory.items.addons.ModularRangeAddon;
import net.matt.modular_armory.screen.custom.ModularMinerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ModularMinerBlockEntity extends BlockEntity implements MenuProvider {
    // Inventory indexes
    public static final int OUTPUT_START = 0;
    public static final int FILTER_SLOT = 9;
    public static final int AUGMENT_SLOT = 10;
    public static final int RANGE_LENGTH_SLOT = 11;
    public static final int RANGE_WIDTH_SLOT = 12;
    public static final int RANGE_HEIGHT_SLOT = 13;

    // Inventory slots
    public static final int OUTPUT_SLOTS = 9;

    // Scan tracking
    private int currentDepth = 1;
    private int currentWidth = 0;
    private int currentHeight = 0;
    private boolean movingPositiveWidth = true;

    // Redstone mode 0 - Active on High, 1 - Active on Low, 2 - Ignore Signal
    private int redstoneMode = 0;

    // Display false - off, true - on
    private boolean rangeDisplayState = false;
    protected final ContainerData data;

    // Mining
    private static final int BASE_MINING_TIME = 20;
    private static final int POWERED_MINING_TIME = 5;
    private static final int MAX_ENERGY_CAPACITY = 50000;
    private static final int MAX_ENERGY_TRANSFER = 2500;
    private static final int ENERGY_PER_OPERATION = 100;
    private static final GameProfile MINER_GAME_PROFILE = new GameProfile(UUID.fromString("41c82c87-da8e-4a84-9e11-92f33f114421"), "[ModularMiner]");

    private int tickCounter = 0;

    public final ItemStackHandler itemHandler = new ItemStackHandler(14) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == RANGE_LENGTH_SLOT || slot == RANGE_WIDTH_SLOT || slot == RANGE_HEIGHT_SLOT || slot == FILTER_SLOT) {
                return 1;
            } else {
                return super.getSlotLimit(slot);
            }
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            if (stack.getItem() instanceof ModularAugmentAddon modularAugmentAddon) {
                return modularAugmentAddon.getMaxStack();
            } else {
                return super.getStackLimit(slot, stack);
            }
        }
    };

    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(MAX_ENERGY_CAPACITY, MAX_ENERGY_TRANSFER, MAX_ENERGY_TRANSFER) {
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

    public boolean hasEnoughPower() {
        return this.energyStorage.getEnergyStored() >= ENERGY_PER_OPERATION + getAugmentPowerRequirements();
    }

    public int getAugmentPowerRequirements() {
        ItemStack augmentAddon = itemHandler.getStackInSlot(AUGMENT_SLOT);
        if (augmentAddon.getItem() instanceof ModularAugmentAddon modularAugmentAddon) {
            return ((modularAugmentAddon.getPowerUsage() * augmentAddon.getCount()) + modularAugmentAddon.getBonusPowerUsage());
        } else {
            return 0;
        }
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

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // Checking if the block should be active
        switch (redstoneMode) {
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

        int currentRequiredTime = hasEnoughPower() ? POWERED_MINING_TIME : BASE_MINING_TIME;

        this.tickCounter++;
        if (this.tickCounter >= currentRequiredTime && !isInventoryFull()) {
            this.tickCounter = 0;
            this.mineNextBlock((ServerLevel) pLevel, pPos, pState, (currentRequiredTime == POWERED_MINING_TIME));
        } else if (this.tickCounter > 2 * BASE_MINING_TIME) {
            this.tickCounter = 2 * BASE_MINING_TIME;
        }
    }

    private void mineNextBlock(ServerLevel level, BlockPos pos, BlockState state, boolean consumePower) {
        Direction facing = state.getValue(ModularMinerBlock.FACING);

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

            if (!level.isLoaded(targetPos)) {
                advanceScanPosition(maxExtraWidth, maxExtraHeight, maxLength);
                positionsChecked++;
                continue;
            }

            BlockState targetState = level.getBlockState(targetPos);

            // Goes to the next position
            advanceScanPosition(maxExtraWidth, maxExtraHeight, maxLength);
            positionsChecked++;

            // Mines a block if possible, skips air, unbreakable blocks and fluids
            if (!targetState.isAir()
                    && targetState.getDestroySpeed(level, targetPos) >= 0
                    && !(targetState.getBlock() instanceof LiquidBlock)
                    && passesFilter(targetState, level, targetPos) ) {
                mineBlock(level, targetPos, targetState, consumePower);
                return;
            }
        }
    }

    private boolean passesFilter(BlockState targetState, ServerLevel serverLevel, BlockPos targetPos) {
        ItemStack filterStack = itemHandler.getStackInSlot(FILTER_SLOT);

        if (filterStack.isEmpty()) {
            return true;
        }

        List<ItemStack> filteredItems = filterStack.getOrDefault(
                ModDataComponentTypes.FILTERED_ITEMS.get(),
                List.of()
        );

        if (filteredItems.isEmpty()) {
            return true;
        }

        boolean isWhitelist = filterStack.getOrDefault(
                ModDataComponentTypes.FILTER_IS_WHITELIST.get(),
                true
        );

        // Converts the targeted block into an item
        ItemStack targetBlockItem = new ItemStack(targetState.getBlock().asItem());
        if (targetBlockItem.isEmpty()) {
            return !isWhitelist;
        }

        boolean matchesFilter = false;
        for (ItemStack filterItem : filteredItems) {
            if (!filterItem.isEmpty() && ItemStack.isSameItem(filterItem, targetBlockItem)) {
                matchesFilter = true;
                break;
            }
        }

        return isWhitelist == matchesFilter;
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

    private void mineBlock(ServerLevel level, BlockPos targetPos, BlockState targetState, boolean consumePower) {
        // Helper tool for applying augments
        ItemStack helperTool;

        if (targetState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            helperTool = new ItemStack(Items.NETHERITE_SHOVEL);
        } else if (targetState.is(BlockTags.MINEABLE_WITH_AXE)) {
            helperTool = new ItemStack(Items.NETHERITE_AXE);
        } else if (targetState.is(BlockTags.MINEABLE_WITH_HOE)) {
            helperTool = new ItemStack(Items.NETHERITE_HOE);
        } else {
            helperTool = new ItemStack(Items.NETHERITE_PICKAXE);
        }

        ItemStack augmentAddon = itemHandler.getStackInSlot(AUGMENT_SLOT);
        boolean skipDrops = false;
        boolean smeltDrops = false;
        if (augmentAddon.getItem() instanceof ModularAugmentAddon modularAugmentAddon) {
            switch (modularAugmentAddon.getAddonId()) {
                case ModularAugmentAddon.ADDONS.SILK_TOUCH -> {
                    helperTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
                }
                case ModularAugmentAddon.ADDONS.FORTUNE -> {
                    helperTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), augmentAddon.getCount());
                }
                case ModularAugmentAddon.ADDONS.AUTO_SMELT -> {
                    smeltDrops = true;
                }
                case ModularAugmentAddon.ADDONS.AUTO_SMELTING_FORTUNE -> {
                    helperTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), augmentAddon.getCount());
                    smeltDrops = true;
                }
                case ModularAugmentAddon.ADDONS.VOID -> {
                    skipDrops = true;
                }
            }
        }

        FakePlayer minerPlayer = FakePlayerFactory.get(level, MINER_GAME_PROFILE);
        minerPlayer.setItemInHand(InteractionHand.MAIN_HAND, helperTool);

        List<ItemStack> drops = Block.getDrops(
                targetState,
                level,
                targetPos,
                level.getBlockEntity(targetPos),
                minerPlayer,
                helperTool);

        if (smeltDrops) {
            for (int i = 0; i < drops.size(); i++) {
                ItemStack originalStack = drops.get(i);
                ItemStack smeltedResult = getSmeltingResult(level, originalStack);

                if (!smeltedResult.isEmpty()) {
                    ItemStack finalStack = smeltedResult.copy();
                    finalStack.setCount(originalStack.getCount() * smeltedResult.getCount());
                    drops.set(i, finalStack);
                }
            }
        }

        if (hasRoomForDrops(drops) || skipDrops) {
            if (level.destroyBlock(targetPos, false, minerPlayer)) {
                if (!skipDrops) {
                    for (ItemStack drop : drops) {
                        ItemStack remainder = drop.copy();
                        for (int i = OUTPUT_START; i < OUTPUT_START + OUTPUT_SLOTS; i++) {
                            remainder = itemHandler.insertItem(i, remainder, false);
                            if (remainder.isEmpty()) {
                                break;
                            }
                        }
                    }
                }

                if (smeltDrops) {
                    level.sendParticles(ParticleTypes.FLAME, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
                }

                if (getAugmentPowerRequirements() > 0) {
                    this.energyStorage.extractEnergy(getAugmentPowerRequirements(), false);
                }

                if (consumePower) {
                    this.energyStorage.extractEnergy(ENERGY_PER_OPERATION, false);
                }

                setChanged();
            }
        }
    }

    public ItemStack getSmeltingResult(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    public int getRangeBonus(int slot) {
        Item upgrade = itemHandler.getStackInSlot(slot).getItem();

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
        for (ItemStack drop : drops) {
            // Copy used only for checking if items will fit
            ItemStack remainder = drop.copy();

            for (int i = OUTPUT_START; i < OUTPUT_START + OUTPUT_SLOTS; i++) {
                remainder = itemHandler.insertItem(i, remainder, true);
                if (remainder.isEmpty()) {
                    break;
                }
            }

            // If the inventory can't fit all drops
            if (!remainder.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isInventoryFull() {
        for (int i = OUTPUT_START; i < OUTPUT_START + OUTPUT_SLOTS; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);

            if (stack.isEmpty()) {
                return false;
            }

            if (stack.getCount() < stack.getMaxStackSize() && stack.getCount() < itemHandler.getSlotLimit(i)) {
                return false;
            }
        }

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

    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if (side == null) {
            return itemHandler;
        }

        IItemHandler outputView = new RangedWrapper(itemHandler, OUTPUT_START, OUTPUT_START + OUTPUT_SLOTS);

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

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("energy", energyStorage.getEnergyStored());

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

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

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
