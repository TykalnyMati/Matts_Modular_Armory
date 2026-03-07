package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AugumentObject {
    public float MINING_BONUS;
    public int UNBREAKING;
    public boolean SILK_TOUCH;
    public int FORTUNE;
    public boolean MENDING;

    public AugumentObject(ItemStack stack) {
        for (ItemStack augment : getAugments(stack)) {
            if (augment.is(ModItems.EFFICIENCY_AUGMENT.get())) {
                this.MINING_BONUS += (float) (1.0f * (Math.pow(augment.getCount(), 2) + 1));
            }
            if (augment.is(ModItems.UNBREAKING_AUGMENT.get())) {
                this.UNBREAKING += (augment.getCount());
            }
            if (augment.is(ModItems.SILK_TOUCH_AUGMENT.get())) {
                this.SILK_TOUCH = true;
            }
            if (augment.is(ModItems.FORTUNE_AUGMENT.get())) {
                this.FORTUNE = (augment.getCount());
            }
            if (augment.is(ModItems.MENDING_AUGMENT.get())) {
                this.MENDING = true;
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
