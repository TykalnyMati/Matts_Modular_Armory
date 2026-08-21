package net.matt.modular_armory.items.tools.helpers;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface ModularItemInterface {
    default void damageModularItem(ItemStack stack, LivingEntity entity) {
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

                if (entity.level() instanceof ServerLevel serverLevel) {
                    // This spawns the "explosion of tool bits" particles
                    serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
                            entity.getX(), entity.getY() + 1.0, entity.getZ(),
                            5, 0.1, 0.1, 0.1, 0.05);
                }
            }
        } else {
            stack.setDamageValue(stack.getDamageValue() + 1);
        }
    }

    default void damageModularItem(ItemStack stack, LivingEntity entity, int amount) {
        for (int i = 0; i < amount; i++) {
            damageModularItem(stack, entity);
        }
    }

    default boolean isBroken(ItemStack item) {
        return item.getDamageValue() >= item.getMaxDamage() - 1;
    }

    default AugmentObject getAugmentBonuses(ItemStack stack) {
        AugmentObject Bonuses = new AugmentObject(stack);
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

    default float getWeaponAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        ItemStack weapon = source.getWeaponItem();

        if (isBroken(weapon)) {
            return -baseDamage + 1;
        }

        AugmentObject bonuses = getAugmentBonuses(weapon);
        float bonusDamage = 0.0f;

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;
        int impaling = bonuses.IMPALING;

        if (sharpness > 0) {
            bonusDamage = 0.5f * sharpness  + 0.5f;
        } else if (smite > 0) {
            if (target.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
                bonusDamage = 2.5f * smite;
            }
        } else if (bane_of_arthropods > 0) {
            if (target.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
                bonusDamage = 2.5f * bane_of_arthropods;
            }
        } else if (impaling > 0) {
            if (target.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING)) {
                bonusDamage = 2.5f * impaling;
            }
        }

        return bonusDamage;
    }

    default void postWeaponHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (isBroken(pStack)) {
            return;
        }

        damageModularItem(pStack, pAttacker);

        AugmentObject bonuses = getAugmentBonuses(pStack);

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;
        int impaling = bonuses.IMPALING;
        int knockback = bonuses.KNOCKBACK;
        int poison = bonuses.POISON;

        boolean DO_ENCHANTED_PARTICLES = false;

        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            // Damaging effects
            if (sharpness > 0) {
                DO_ENCHANTED_PARTICLES = true;
            } else if (smite > 0) {
                if (pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
                    DO_ENCHANTED_PARTICLES = true;
                }
            } else if (bane_of_arthropods > 0) {
                if (pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
                    DO_ENCHANTED_PARTICLES = true;

                    // Random value between 1 and 1 + 0.5 seconds per level
                    int duration = 20 + pAttacker.getRandom().nextInt(10 * bonuses.BANE_OF_ARTHROPODS);

                    pTarget.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN,
                            duration,
                            3
                    ));
                }
            } else if (impaling > 0) {
                if (pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING)) {
                    DO_ENCHANTED_PARTICLES = true;
                }
            }

            // Spawn particles
            if (DO_ENCHANTED_PARTICLES) {
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        pTarget.getRandomX(0.5D),
                        pTarget.getRandomY(),
                        pTarget.getRandomZ(0.5D),
                        24,
                        0.0D,
                        0.5D,
                        0.0D,
                        0.5D
                );
            }

            // Knockback augment
            if (knockback > 0) {
                float strength = 0.5F * knockback;

                double dx = Math.sin(pAttacker.getYRot() * Math.PI / 180.0);
                double dz = -Math.cos(pAttacker.getYRot() * Math.PI / 180.0);

                pTarget.knockback(strength, dx, dz);

                pAttacker.setDeltaMovement(pAttacker.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            // Adding effects
            if (poison > 0) {
                int duration;
                int amplifier;

                if (poison == 1) {
                    duration = 20 * 6;
                    amplifier = 0;
                } else if (poison == 2) {
                    duration = 20 * 3;
                    amplifier = 1;
                } else {
                    duration = 20 * 3;
                    amplifier = 3;
                }


                pTarget.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        duration,
                        amplifier
                ));
            }
        }
    }

    default void handleHeldItem(ItemStack pStack, Level pLevel, Entity pEntity) {
        if (pEntity instanceof Player player) {
            AugmentObject bonuses = getAugmentBonuses(pStack);

            if (bonuses.MAGNET) {
                double range = 4.0;

                // Find all Item Entities in that range
                List<ItemEntity> items = pLevel.getEntitiesOfClass(
                        net.minecraft.world.entity.item.ItemEntity.class,
                        player.getBoundingBox().inflate(range)
                );

                for (net.minecraft.world.entity.item.ItemEntity item : items) {
                    // Ignore items that have a pickup delay
                    if (item.hasPickUpDelay()) {
                        continue;
                    }

                    // Calculate the vector from the item to the player
                    Vec3 moveVec = player.position().add(0, 0.5, 0).subtract(item.position());

                    // Only pull if the item is not too close
                    if (moveVec.length() > 0.5) {
                        Vec3 pull = moveVec.normalize().scale(0.2);

                        // Apply the velocity to the item
                        item.setDeltaMovement(pull);

                        // Syncs the server and the player
                        item.hasImpulse = true;
                    }
                }
            }
        }
    }
}
