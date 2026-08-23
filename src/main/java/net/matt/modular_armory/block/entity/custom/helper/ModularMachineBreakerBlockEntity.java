package net.matt.modular_armory.block.entity.custom.helper;

import com.mojang.authlib.GameProfile;
import net.matt.modular_armory.block.custom.ModularMinerBlock;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.items.addons.ModularAugmentAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ModularMachineBreakerBlockEntity extends ModularMachineBlockEntity {
    // Inventory indexes
    protected static final int OUTPUT_START = 0;
    protected static final int OUTPUT_SLOTS = 9;

    public static final int FILTER_SLOT = 9;
    public static final int AUGMENT_SLOT = 10;
    public static final int RANGE_LENGTH_SLOT = 11;
    public static final int RANGE_WIDTH_SLOT = 12;
    public static final int RANGE_HEIGHT_SLOT = 13;

    private ItemStackHandler itemHandler = new ItemStackHandler(14) {
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

    @Override
    public ItemStackHandler getItemHandler() {
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

    public abstract GameProfile getMinerGameProfile();

    public ModularMachineBreakerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected int getAugmentPowerRequirements() {
        ItemStack augmentAddon = getItemHandler().getStackInSlot(AUGMENT_SLOT);
        if (augmentAddon.getItem() instanceof ModularAugmentAddon modularAugmentAddon) {
            return ((modularAugmentAddon.getPowerUsage() * augmentAddon.getCount()) + modularAugmentAddon.getBonusPowerUsage());
        } else {
            return 0;
        }
    }

    @Override
    protected boolean hasEnoughPower() {
        return this.energyStorage.getEnergyStored() >= ENERGY_PER_OPERATION + getAugmentPowerRequirements();
    }

    protected boolean hasRoomForDrops(List<ItemStack> drops) {
        // Temporary handler mirroring the output slots
        ItemStackHandler tempHandler = new ItemStackHandler(OUTPUT_SLOTS);

        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            tempHandler.setStackInSlot(i, getItemHandler().getStackInSlot(OUTPUT_START + i).copy());
        }

        for (ItemStack drop : drops) {
            if (drop.isEmpty()) {
                continue;
            }

            ItemStack remainder = drop.copy();

            for (int i = 0; i < OUTPUT_SLOTS; i++) {
                // Simulate insertion to check if ALL items fit
                remainder = tempHandler.insertItem(i, remainder, false);
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

    protected boolean isInventoryFull() {
        for (int i = OUTPUT_START; i < OUTPUT_START + OUTPUT_SLOTS; i++) {
            ItemStack stack = getItemHandler().getStackInSlot(i);

            if (stack.isEmpty()) {
                return false;
            }

            if (stack.getCount() < stack.getMaxStackSize() && stack.getCount() < getItemHandler().getSlotLimit(i)) {
                return false;
            }
        }

        return true;
    }

    protected void mineNextBlock(ServerLevel level, BlockPos pos, BlockState state, boolean consumePower) {
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
        ItemStack filterStack = getItemHandler().getStackInSlot(FILTER_SLOT);

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

        ItemStack augmentAddon = getItemHandler().getStackInSlot(AUGMENT_SLOT);
        boolean skipDrops = false;
        boolean smeltDrops = false;
        int clayExtract = 0;
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
                case ModularAugmentAddon.ADDONS.CLAY_EXTRACTOR -> {
                    clayExtract = augmentAddon.getCount();
                }
            }
        }

        FakePlayer minerPlayer = FakePlayerFactory.get(level, getMinerGameProfile());
        minerPlayer.setItemInHand(InteractionHand.MAIN_HAND, helperTool);

        List<ItemStack> drops = new ArrayList<>();

        if (clayExtract > 0) {
            drops.add(new ItemStack(Items.DIRT, 1));
            drops.add(new ItemStack(ModItems.MODULAR_CLAY, 2 + new java.util.Random().nextInt(2 + clayExtract)));
        } else {
            drops = Block.getDrops(
                    targetState,
                    level,
                    targetPos,
                    level.getBlockEntity(targetPos),
                    minerPlayer,
                    helperTool);
        }

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
                            remainder = getItemHandler().insertItem(i, remainder, false);
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
}
