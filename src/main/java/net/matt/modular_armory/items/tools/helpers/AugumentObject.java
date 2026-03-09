package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AugumentObject {
    public int UNBREAKING;
    public boolean MENDING;
    public float EFFICIENCY;
    public boolean SILK_TOUCH;
    public int FORTUNE;
    public int SHARPNESS;
    public int SMITE;
    public int BANE_OF_ARTHROPODS;

    public AugumentObject(ItemStack stack) {
        for (ItemStack augment : getAugments(stack)) {
            switch (BuiltInRegistries.ITEM.getKey(augment.getItem()).toString()) {
                // Generic augments
                case "mattsmodulararmory:unbreaking_augment" -> this.UNBREAKING += augment.getCount();
                case "mattsmodulararmory:mending_augment" -> this.MENDING = true;

                // Tool specific augments
                case "mattsmodulararmory:efficiency_augment" -> this.EFFICIENCY += augment.getCount();
                case "mattsmodulararmory:silk_touch_augment" -> this.SILK_TOUCH = true;
                case "mattsmodulararmory:fortune_augment" -> this.FORTUNE = augment.getCount();

                // Weapon specific augments
                case "mattsmodulararmory:sharpness_augment" -> this.SHARPNESS = augment.getCount();
                case "mattsmodulararmory:smite_augment" -> this.SMITE = augment.getCount();
                case "mattsmodulararmory:bane_of_arthropods_augment" -> this.BANE_OF_ARTHROPODS = augment.getCount();
            }
        }
    }

    private List<ItemStack> getAugments(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.AUGMENTS.get())) {
            return List.of();
        }

        return stack.get(ModDataComponentTypes.AUGMENTS.get());
    }
}
