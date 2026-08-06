package net.matt.modular_armory.block.entity.custom.helper;

import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void setEnergyDirectly(int energy) {
        this.energy = Math.clamp(energy, 0, this.capacity);
    }
}