package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
        DamageSource source = event.getSource();
        float currentDamage = event.getNewDamage();

        if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                source.is(DamageTypes.STARVE) ||
                source.is(DamageTypes.SONIC_BOOM) ||
                source.is(DamageTypes.OUTSIDE_BORDER) ||
                source.is(DamageTypes.GENERIC_KILL) ||
                source.is(DamageTypes.FREEZE)) {
            return;
        }

        int totalEPF = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = entity.getItemBySlot(slot);

                // Check if augments should apply
                if (stack.getItem() instanceof ModularItemInterface armorItem && !armorItem.isBroken(stack)) {
                    AugmentObject bonuses = armorItem.getAugmentBonuses(stack);

                    if (bonuses.PROTECTION > 0) {
                        totalEPF += bonuses.PROTECTION;
                    } else if (bonuses.BLAST_PROTECTION > 0) {
                        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                            totalEPF += bonuses.BLAST_PROTECTION * 2;
                        }
                    } else if (bonuses.PROJECTILE_PROTECTION > 0) {
                        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                            totalEPF += bonuses.PROJECTILE_PROTECTION * 2;
                        }
                    } else if (bonuses.FIRE_PROTECTION > 0) {
                        if (source.is(DamageTypeTags.IS_FIRE)) {
                            totalEPF += bonuses.FIRE_PROTECTION * 2;
                        }
                    }
                }
            }
        }

        if (totalEPF > 0) {
            // Cap the levels at 22 (so 88% reduction at max)
            int cappedLevel = Math.min(totalEPF, 22);

            // Damage * (1 - (Level * 4%))
            float reductionMultiplier = 1.0f - (cappedLevel * 0.04f);
            float newDamage = currentDamage * reductionMultiplier;

            // Override the damage in the event
            event.setNewDamage(newDamage);
        }
    }

    // Thorns augment handling
    @SubscribeEvent
    public static void onLivingDamageThorns(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor()) {
                    ItemStack stack = victim.getItemBySlot(slot);

                    if (stack.getItem() instanceof ModularItemInterface armorItem && !armorItem.isBroken(stack)) {
                        AugmentObject bonuses = armorItem.getAugmentBonuses(stack);

                        if (bonuses.THORNS > 0) {
                            if (victim.getRandom().nextFloat() < (bonuses.THORNS * 0.15f)) {
                                float damageAmount = bonuses.THORNS + victim.getRandom().nextInt(2);
                                attacker.hurt(victim.damageSources().thorns(victim), damageAmount);
                                armorItem.damageModularItem(stack, victim, 2);
                            }
                        }
                    }
                }
            }
        }
    }
}
