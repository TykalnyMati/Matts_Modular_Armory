package net.matt.modular_armory.block.entity.custom;

import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ScaffoldingAugmentBlockEntity extends BlockEntity {
    private int damageTick = 0;
    private int MAX_LIFE = 40;

    public ScaffoldingAugmentBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCAFFOLDING_AUGMENT_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ScaffoldingAugmentBlockEntity be) {
        if (level.isClientSide) return;

        // Check for entities standing on it
        boolean hasEntity = !level.getEntities(null, new AABB(pos).move(0, 0.5, 0)).isEmpty();

        if (hasEntity) {
            // Reset if stepped on
            if (be.damageTick > 0) {
                be.damageTick = 0;
                level.destroyBlockProgress(be.hashCode(), pos, -1); // Clears cracks
            }
        } else {
            be.damageTick++;

            // Map max life to 0-9 cracking stages
            int stage = (int) ((be.damageTick / (float) be.MAX_LIFE) * 10);

            if (stage < 10) {
                // Send the "cracking" animation to all nearby players
                level.destroyBlockProgress(be.hashCode(), pos, stage);
            } else {
                level.destroyBlock(pos, false);
            }
        }
    }

    public void setMaxLife(int tier) {
        this.MAX_LIFE = 40 + tier * 20;
        this.setChanged();
    }

    @Override
    public void setRemoved() {
        // Remove cracks if the block is broken early
        if (level != null) level.destroyBlockProgress(this.hashCode(), worldPosition, -1);
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("MaxLife", this.MAX_LIFE);
        tag.putInt("DamageTick", this.damageTick);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.MAX_LIFE = tag.getInt("MaxLife");
        this.damageTick = tag.getInt("DamageTick");
    }
}
