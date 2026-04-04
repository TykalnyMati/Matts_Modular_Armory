package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.recipe.ModRecipes;
import net.matt.modular_armory.recipe.ModularAssemblerRecipe;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModularAssemblerMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;

    final Slot inputSlot;
    final Slot resultSlot;

    private List<RecipeHolder<ModularAssemblerRecipe>> availableRecipes = new ArrayList<>();
    private ItemStack inputStack = ItemStack.EMPTY;
    long lastSoundTime;

    public final Container container = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            ModularAssemblerMenu.this.slotsChanged(this);

            ModularAssemblerMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    public ModularAssemblerMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(ModMenuTypes.MODULAR_ASSEMBLER_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();

        // INPUT SLOT
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));

        // RESULT SLOT
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack pStack) { return false; }

            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                pStack.onCraftedBy(pPlayer.level(), pPlayer, pStack.getCount());

                // Get custom cost
                RecipeHolder<ModularAssemblerRecipe> recipe = getSelectedRecipe();
                if (recipe != null) {
                    int cost = recipe.value().amount();
                    ModularAssemblerMenu.this.inputSlot.remove(cost);
                }

                pAccess.execute((level, pos) -> {
                    long time = level.getGameTime();
                    if (ModularAssemblerMenu.this.lastSoundTime != time) {
                        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        ModularAssemblerMenu.this.lastSoundTime = time;
                    }
                });
                super.onTake(pPlayer, pStack);
            }
        });

        // Player Inventory Slots
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getCostAtIndex(int index) {
        if (index >= 0 && index < this.availableRecipes.size()) {
            return this.availableRecipes.get(index).value().amount();
        }
        return 1;
    }

    public ModularAssemblerMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    @Override
    public void slotsChanged(Container pInventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.inputStack.getItem())) {
            this.inputStack = itemstack.copy();
            this.setupRecipeList(itemstack);
        }
    }

    private void setupRecipeList(ItemStack pStack) {
        this.availableRecipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);

        if (!pStack.isEmpty()) {
            this.availableRecipes = level.getRecipeManager()
                    .getAllRecipesFor(ModRecipes.MODULAR_ASSEMBLER_TYPE.get())
                    .stream()
                    .filter(recipe -> recipe.value().inputItem().test(pStack))
                    .sorted((a, b) -> Integer.compare(
                            getToolPriority(a.value().output().getItem()),
                            getToolPriority(b.value().output().getItem())
                    ))
                    .collect(Collectors.toList());
        }
    }

    private int getToolPriority(Item item) {
        String name = item.toString();
        if (name.contains("sword")) return 0;
        if (name.contains("pickaxe")) return 1;
        if (name.contains("axe") && !name.contains("pickaxe")) return 2; // Avoid "pickaxe" matching "axe"
        if (name.contains("shovel")) return 3;
        if (name.contains("hoe")) return 4;
        if (name.contains("helmet")) return 5;
        if (name.contains("chestplate")) return 6;
        if (name.contains("leggings")) return 7;
        if (name.contains("boots")) return 8;
        return 9; // Everything else
    }

    void setupResultSlot() {
        RecipeHolder<ModularAssemblerRecipe> recipe = getSelectedRecipe();
        if (recipe != null) {
            int cost = recipe.value().amount();
            if (this.inputSlot.getItem().getCount() >= cost) {
                this.resultSlot.set(recipe.value().output().copy());
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    private RecipeHolder<ModularAssemblerRecipe> getSelectedRecipe() {
        int index = this.selectedRecipeIndex.get();
        if (index >= 0 && index < this.availableRecipes.size()) {
            return this.availableRecipes.get(index);
        }
        return null;
    }

    // Fills out the list of available tools to craft, not done by crafting recipes because I'm you know lazy and stuff
//    private void setupToolList(ItemStack pStack) {
//        this.availableTools.clear();
//        this.selectedRecipeIndex.set(-1);
//        this.resultSlot.set(ItemStack.EMPTY);
//
//        List<RecipeHolder<ModularAssemblerRecipe>> recipes =
//                level.getRecipeManager().getAllRecipesFor(ModRecipes.MODULAR_ASSEMBLER_TYPE.get());
//
//        if (!pStack.isEmpty()) {
//            Item plate = pStack.getItem();
//
//            // Tier 0: Modular Clay
//            if (plate == ModItems.BASIC_MODULAR_PLATE.get()) {
//                addItemsToTier(
//                        ModItems.MODULAR_CLAY_SWORD.get(),
//                        ModItems.MODULAR_CLAY_PICKAXE.get(),
//                        ModItems.MODULAR_CLAY_AXE.get(),
//                        ModItems.MODULAR_CLAY_SHOVEL.get(),
//                        ModItems.MODULAR_CLAY_HOE.get()
//                );
//            }
//
//            // Tier 1: Advanced Modular Clay
//            else if (plate == ModItems.ADVANCED_MODULAR_PLATE.get()) {
//                addItemsToTier(
//                        ModItems.ADVANCED_MODULAR_CLAY_SWORD.get(),
//                        ModItems.ADVANCED_MODULAR_CLAY_PICKAXE.get(),
//                        ModItems.ADVANCED_MODULAR_CLAY_AXE.get(),
//                        ModItems.ADVANCED_MODULAR_CLAY_SHOVEL.get(),
//                        ModItems.ADVANCED_MODULAR_CLAY_HOE.get()
//                );
//            }
//
//            // Tier 2: Adaptive Flesh
//            else if (plate == ModItems.ADAPTIVE_FLESH_PLATE.get()) {
//                addItemsToTier(
//                        ModItems.ADAPTIVE_FLESH_SWORD.get(),
//                        ModItems.ADAPTIVE_FLESH_PICKAXE.get(),
//                        ModItems.ADAPTIVE_FLESH_AXE.get(),
//                        ModItems.ADAPTIVE_FLESH_SHOVEL.get(),
//                        ModItems.ADAPTIVE_FLESH_HOE.get()
//                );
//            }
//
//            // Tier 3: Biorite
//            else if (plate == ModItems.BIORITE_PLATE.get()) {
//                addItemsToTier(
//                        ModItems.BIORITE_SWORD.get(),
//                        ModItems.BIORITE_PICKAXE.get(),
//                        ModItems.BIORITE_AXE.get(),
//                        ModItems.BIORITE_SHOVEL.get(),
//                        ModItems.BIORITE_HOE.get()
//                );
//            }
//        }
//    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (pId >= 0 && pId < this.availableRecipes.size()) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }
        return true;
    }

    // Necessary getters for the Screen/GUI
    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<ItemStack> getAvailableTools() {
        return this.availableRecipes.stream()
                .map(recipe -> recipe.value().output())
                .collect(Collectors.toList());
    }

    public int getNumRecipes() {
        return this.availableRecipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.MODULAR_ASSEMBLER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();

            // Moving from the Result Slot
            if (pIndex == 1) {
                item.onCraftedBy(itemstack1, pPlayer.level(), pPlayer);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            // Moving from the Input Slot
            else if (pIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Moving from player inventory
            else {
                // Check if the item is a valid plate for slot 0
                if (this.isValidPlate(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Swap between Inventory and Hotbar
                else if (pIndex >= 2 && pIndex < 29) {
                    if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 29 && pIndex < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    private boolean isIngredient(ItemStack stack) {
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.MODULAR_ASSEMBLER_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().inputItem().test(stack));
    }

    private Runnable slotUpdateListener = () -> {};

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    private boolean isValidPlate(ItemStack itemstack) {
        Item item = itemstack.getItem();

        if (item == ModItems.BASIC_MODULAR_PLATE.get() ||
                item == ModItems.ADVANCED_MODULAR_PLATE.get() ||
                item == ModItems.ADAPTIVE_FLESH_PLATE.get() ||
                item == ModItems.BIORITE_PLATE.get()) {
            return true;
        }
        return false;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, blockPos) -> this.clearContainer(pPlayer, this.container));
    }
}
