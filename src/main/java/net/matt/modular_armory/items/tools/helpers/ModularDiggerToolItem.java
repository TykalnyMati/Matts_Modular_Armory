package net.matt.modular_armory.items.tools.helpers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ModularDiggerToolItem {
    default void damageTool(ItemStack stack, LivingEntity entity) {
        int bonusesUnbreaking = getAugmentBonuses(stack).UNBREAKING;
        // Unbreaking augment
        if (bonusesUnbreaking * 0.125f >= 1f || entity.getRandom().nextFloat() < bonusesUnbreaking * 0.125f) {
            return;
        }

        if (isBroken(stack)) {
            stack.setDamageValue(stack.getMaxDamage() - 1);
            return;
        }

        if (stack.getDamageValue() + 1 >= stack.getMaxDamage() - 1) {
            stack.setDamageValue(stack.getMaxDamage() - 1);

            if (!entity.level().isClientSide()) {
                entity.level().playSound(
                        null,
                        entity.blockPosition(),
                        SoundEvents.ITEM_BREAK,
                        entity.getSoundSource(),
                        1.0F,
                        1.0F
                );
            }

            return;
        }

        stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
    }

    default boolean isBroken(ItemStack item) {
        return item.getDamageValue() >= item.getMaxDamage() - 1;
    }

    default AugumentObject getAugmentBonuses(ItemStack stack) {
        AugumentObject Bonuses = new AugumentObject(stack);
        return Bonuses;
    }

    default int repairFromXp(ItemStack stack, int xpAmount) {
        if (!getAugmentBonuses(stack).MENDING) {
            return xpAmount;
        }

        if (stack.getDamageValue() <= 0) {
            return xpAmount;
        }

        int repairAmount = xpAmount * 2;
        int newDamage = Math.max(0, stack.getDamageValue() - repairAmount);
        int remainingXp = xpAmount - repairAmount / 2;

        stack.setDamageValue(newDamage);
        return remainingXp;
    }
}
