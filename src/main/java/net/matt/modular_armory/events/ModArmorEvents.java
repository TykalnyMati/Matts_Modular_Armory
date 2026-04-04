package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModArmorEvents {

    // Protection augments handling
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        float currentDamage = event.getNewDamage();
        int totalProtectionLevel = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = entity.getItemBySlot(slot);

                // Check if augments should apply
                if (stack.getItem() instanceof ModularItemInterface armorItem && !armorItem.isBroken(stack)) {
                    AugmentObject bonuses = armorItem.getAugmentBonuses(stack);

                    totalProtectionLevel += bonuses.PROTECTION;
                }
            }
        }

        if (totalProtectionLevel > 0) {
            // Cap the levels at 22
            int cappedLevel = Math.min(totalProtectionLevel, 22);

            // Damage * (1 - (Level * 4%))
            float reductionMultiplier = 1.0f - (cappedLevel * 0.04f);
            float newDamage = currentDamage * reductionMultiplier;

            // Override the damage in the event
            event.setNewDamage(newDamage);
        }
    }
}
