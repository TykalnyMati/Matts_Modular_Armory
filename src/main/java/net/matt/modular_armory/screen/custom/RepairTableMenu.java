package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.screen.ModMenuTypes;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RepairTableMenu extends ItemCombinerMenu {
    private final ContainerLevelAccess access;

    // This constructor is used by the Registry/Client
    public RepairTableMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, ContainerLevelAccess.NULL);
    }

    // This constructor is used by the Block (Server)
    public RepairTableMenu(int pContainerId, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenuTypes.REPAIR_TABLE_MENU.get(), pContainerId, inventory, access);
        this.access = access;
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, (pStack) -> true) // Tool Slot
                .withSlot(1, 76, 47, (pStack) -> true) // Material Slot
                .withResultSlot(2, 134, 47)            // Result Slot
                .build();
    }

    // Define which slots are valid
    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return !this.resultSlots.getItem(0).isEmpty();
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        this.inputSlots.getItem(0).shrink(1);
        this.inputSlots.getItem(1).shrink(1);
        this.access.execute((level, pos) -> {
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        });
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.REPAIR_TABLE.get());
    }

    @Override
    public void createResult() {
        ItemStack tool = this.inputSlots.getItem(0);
        ItemStack material = this.inputSlots.getItem(1);

        if (tool.isEmpty() || material.isEmpty() || !tool.is(ModTags.Items.MODULAR_ITEMS)) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        if (isValidRepair(tool, material) && tool.isDamaged()) {
            ItemStack result = tool.copy();
            // Repair 50% of durability
            int repairAmount = result.getMaxDamage() / 2;
            result.setDamageValue(Math.max(0, result.getDamageValue() - repairAmount));

            this.resultSlots.setItem(0, result);
        } else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    private boolean isValidRepair(ItemStack tool, ItemStack material) {
        if (tool.is(ModTags.Items.MODULAR_TIER_0) && material.is(ModItems.BASIC_MODULAR_PLATE.get())) {
            return true;
        }
        if (tool.is(ModTags.Items.MODULAR_TIER_1) && material.is(ModItems.ADVANCED_MODULAR_PLATE.get())) {
            return true;
        }
        if (tool.is(ModTags.Items.MODULAR_TIER_2) && material.is(ModItems.ADAPTIVE_FLESH_PLATE.get())) {
            return true;
        }
        if (tool.is(ModTags.Items.MODULAR_TIER_3) && material.is(ModItems.BIORITE_PLATE.get())) {
            return true;
        }
        return false;
    }
}
