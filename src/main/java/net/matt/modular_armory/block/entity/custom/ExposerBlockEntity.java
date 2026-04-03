package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.recipe.ExposerRecipe;
import net.matt.modular_armory.recipe.ModRecipes;
import net.matt.modular_armory.screen.custom.ExposerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExposerBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public ExposerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.EXPOSER_BE.get(), pPos, pBlockState);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ExposerBlockEntity.this.progress;
                    case 1 -> ExposerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> ExposerBlockEntity.this.progress = value;
                    case 1 -> ExposerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if (side == null) {
            return itemHandler;
        }

        return new IItemHandler() {
            @Override
            public int getSlots() {
                return itemHandler.getSlots();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return itemHandler.getStackInSlot(slot);
            }

            @Override
            public int getSlotLimit(int slot) {
                return itemHandler.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return slot == INPUT_SLOT && itemHandler.isItemValid(slot, stack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                // Can only push into slot 0
                return (slot == INPUT_SLOT) ? itemHandler.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                // Can only pull from slot 1
                return (slot == OUTPUT_SLOT) ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
            }
        };
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
        pTag.putInt("exposer.progress", progress);
        pTag.putInt("exposer.max_progress", maxProgress);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("exposer.progress");
        maxProgress = pTag.getInt("exposer.max_progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mattsmodulararmory.exposer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ExposerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    private void craftItem() {
        Optional<RecipeHolder<ExposerRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();
        Ingredient input = recipe.get().value().inputItem();
        if (input.getItems()[0].getItem() != ModItems.EXPOSER_SLOT_PLUG.get()) {
            itemHandler.extractItem(INPUT_SLOT, 1, false);
        }
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<ExposerRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();

        // Might cause problems, but I wouldn't worry about it for now
        if (this.progress <= 0) {
            this.maxProgress = recipe.get().value().time();
        }

        return canInsertItemIntoOutputSlot(output) && canInsertAmountIntoOutputSlot(output.getCount());
    }

    private Optional<RecipeHolder<ExposerRecipe>> getCurrentRecipe() {
        if (level == null) {
            return Optional.empty();
        }

        List<RecipeHolder<ExposerRecipe>> recipes =
                level.getRecipeManager().getAllRecipesFor(ModRecipes.EXPOSER_TYPE.get());

        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);

        for (RecipeHolder<ExposerRecipe> holder : recipes) {
            ExposerRecipe recipe = holder.value();

            if (matchesInput(recipe, inputStack) && matchesEnvironment(recipe)) {
                return Optional.of(holder);
            }
        }

        return Optional.empty();
    }

    private boolean matchesInput(ExposerRecipe recipe, ItemStack stack) {
        return recipe.inputItem().test(stack);
    }

    private boolean matchesEnvironment(ExposerRecipe recipe) {
        if (level == null) {
            return false;
        }

        List<BlockState> required = recipe.surroundings();
        if (required.isEmpty()) {
            return true;
        }

        BlockPos pos = getBlockPos();

        BlockPos[] around = new BlockPos[]{
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
        };

        int matched = 0;
        List<Boolean> alreadyMatched = new ArrayList<>();
        for (int i = 0; i < required.size(); i++) {
            alreadyMatched.add(false);
        }

        for (BlockPos testPos : around) {
            BlockState state = level.getBlockState(testPos);

            for (int i = 0; i < required.size(); i++) {
                BlockState requiredState = required.get(i);
                if (state.is(requiredState.getBlock()) && !alreadyMatched.get(i)) {
                    matched++;
                    alreadyMatched.set(i, true);
                    break;
                }
            }
        }

        return matched >= required.size();
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
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
