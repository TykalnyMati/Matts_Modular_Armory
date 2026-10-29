package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface ModularItemInterface {
    default void damageModularItem(ItemStack stack, LivingEntity entity) {
        if (entity.hasInfiniteMaterials()) {
            return;
        }

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
                removeVanillaEnchantments(stack);

                entity.level().playSound(
                        null,
                        entity.blockPosition(),
                        SoundEvents.ITEM_BREAK,
                        entity.getSoundSource(),
                        1.0F,
                        1.0F
                );

                if (entity.level() instanceof ServerLevel serverLevel) {
                    // Tool break particles
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

    // removes enchantments that were applied
    default void removeVanillaEnchantments(ItemStack itemStack) {
        EnchantmentHelper.updateEnchantments(itemStack, mutableEnchantments -> {
            mutableEnchantments.keySet().clear();
        });
    }

    default AugmentObject getAugmentBonuses(ItemStack stack) {
        AugmentObject Bonuses = new AugmentObject(stack);
        return Bonuses;
    }

    default void applyVanillaEnchantments(ItemStack itemStack, Level level) {
        var enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        AugmentObject bonuses = getAugmentBonuses(itemStack);

        ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(
                EnchantmentHelper.getEnchantmentsForCrafting(itemStack)
        );

        // Clear existing enchantments
        mutableEnchantments.keySet().clear();

        // Tools
        if (bonuses.EFFICIENCY > 0) {
            enchantmentRegistry.get(Enchantments.EFFICIENCY)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.EFFICIENCY));
        }

        if (bonuses.SILK_TOUCH) {
            enchantmentRegistry.get(Enchantments.SILK_TOUCH)
                    .ifPresent(holder -> mutableEnchantments.set(holder, 1));
        } else if(bonuses.FORTUNE > 0) {
            enchantmentRegistry.get(Enchantments.FORTUNE)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.FORTUNE));
        }

        // Weapons
        if (bonuses.SHARPNESS > 0) {
            enchantmentRegistry.get(Enchantments.SHARPNESS)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.SHARPNESS));
        } else if (bonuses.SMITE > 0) {
            enchantmentRegistry.get(Enchantments.SMITE)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.SMITE));
        } else if (bonuses.BANE_OF_ARTHROPODS > 0) {
            enchantmentRegistry.get(Enchantments.BANE_OF_ARTHROPODS)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.BANE_OF_ARTHROPODS));
        } else if (bonuses.IMPALING > 0) {
            enchantmentRegistry.get(Enchantments.IMPALING)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.IMPALING));
        }

        if (bonuses.LOOTING > 0) {
            enchantmentRegistry.get(Enchantments.LOOTING)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.LOOTING));
        }

        if (bonuses.KNOCKBACK > 0) {
            enchantmentRegistry.get(Enchantments.KNOCKBACK)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.KNOCKBACK));
        }

        if (bonuses.FIRE_ASPECT > 0) {
            enchantmentRegistry.get(Enchantments.FIRE_ASPECT)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.FIRE_ASPECT));
        }

        // Swords
        if (bonuses.SWEEPING_EDGE > 0) {
            enchantmentRegistry.get(Enchantments.SWEEPING_EDGE)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.SWEEPING_EDGE));
        }

        // Bow
        if (bonuses.POWER > 0) {
            enchantmentRegistry.get(Enchantments.POWER)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.POWER));
        }

        if (bonuses.PUNCH > 0) {
            enchantmentRegistry.get(Enchantments.PUNCH)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.PUNCH));
        }

        if (bonuses.FLAME) {
            enchantmentRegistry.get(Enchantments.FLAME)
                    .ifPresent(holder -> mutableEnchantments.set(holder, 1));
        }

        if (bonuses.INFINITY) {
            enchantmentRegistry.get(Enchantments.INFINITY)
                    .ifPresent(holder -> mutableEnchantments.set(holder, 1));
        }

        // Armor
        if (bonuses.PROTECTION > 0) {
            enchantmentRegistry.get(Enchantments.PROTECTION)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.PROTECTION));
        } else if (bonuses.BLAST_PROTECTION > 0) {
            enchantmentRegistry.get(Enchantments.BLAST_PROTECTION)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.BLAST_PROTECTION));
        } else if (bonuses.PROJECTILE_PROTECTION > 0) {
            enchantmentRegistry.get(Enchantments.PROJECTILE_PROTECTION)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.PROJECTILE_PROTECTION));
        } else if (bonuses.FIRE_PROTECTION > 0) {
            enchantmentRegistry.get(Enchantments.FIRE_PROTECTION)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.FIRE_PROTECTION));
        }

        if (bonuses.THORNS > 0) {
            enchantmentRegistry.get(Enchantments.THORNS)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.THORNS));
        }

        if (bonuses.VITALITY > 0) {
            enchantmentRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "vitality")))
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.VITALITY));
        }

        // Helmet
        if (bonuses.RESPIRATION > 0) {
            enchantmentRegistry.get(Enchantments.RESPIRATION)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.RESPIRATION));
        }

        if (bonuses.AQUA_AFFINITY > 0) {
            enchantmentRegistry.get(Enchantments.AQUA_AFFINITY)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.AQUA_AFFINITY));
        }

        // Leggings
        if (bonuses.SWIFT_SNEAK > 0) {
            enchantmentRegistry.get(Enchantments.SWIFT_SNEAK)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.SWIFT_SNEAK));
        }

        // Boots
        if (bonuses.FEATHER_FALLING > 0) {
            enchantmentRegistry.get(Enchantments.FEATHER_FALLING)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.FEATHER_FALLING));
        }

        if (bonuses.FROST_WALKER > 0) {
            enchantmentRegistry.get(Enchantments.FROST_WALKER)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.FROST_WALKER));
        } else if (bonuses.DEPTH_STRIDER > 0) {
            enchantmentRegistry.get(Enchantments.DEPTH_STRIDER)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.DEPTH_STRIDER));
        }

        if (bonuses.SOUL_SPEED > 0) {
            enchantmentRegistry.get(Enchantments.SOUL_SPEED)
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.SOUL_SPEED));
        }

        if (bonuses.STEP_UP > 0) {
            enchantmentRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "step_up")))
                    .ifPresent(holder -> mutableEnchantments.set(holder, bonuses.STEP_UP));
        }

        if (bonuses.VOID_TETHER) {
            enchantmentRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "void_tether")))
                    .ifPresent(holder -> mutableEnchantments.set(holder, 1));
        }

        ItemEnchantments enchantmentsComponent = mutableEnchantments.toImmutable().withTooltip(false);
        itemStack.set(DataComponents.ENCHANTMENTS, enchantmentsComponent);

        itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
    }

    default int repairFromXp(ItemStack stack, Level level, int xpAmount) {
        if (!getAugmentBonuses(stack).MENDING || stack.getDamageValue() <= 0) {
            return xpAmount;
        }

        boolean wasBroken = isBroken(stack);

        int repairAmount = xpAmount * 2;
        int newDamage = Math.max(0, stack.getDamageValue() - repairAmount);
        int remainingXp = xpAmount - repairAmount / 2;

        stack.setDamageValue(newDamage);

        // Reapply enchantments if the item was just fixed
        if (wasBroken && !isBroken(stack)) {
            applyVanillaEnchantments(stack, level);
        }

        return remainingXp;
    }

    default float getWeaponAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        ItemStack weapon = source.getWeaponItem();

        if (isBroken(weapon)) {
            return -baseDamage + 1;
        } else {
            return 0.0f;
        }
    }

    default void postWeaponHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (isBroken(pStack)) {
            return;
        }

        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);

        AugmentObject bonuses = getAugmentBonuses(pStack);

        int poison = bonuses.POISON;
        int wither = bonuses.WITHER;

        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            // Adding effects
            if (poison > 0) {
                int duration;
                int amplifier;

                if (poison == 1) {
                    duration = 20 * 6;
                    amplifier = 0;
                } else {
                    duration = 20 * 4;
                    amplifier = 1;
                }


                pTarget.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        duration,
                        amplifier
                ));
            }

            if (wither > 0) {
                int duration;
                int amplifier;

                if (wither == 1) {
                    duration = 20 * 9;
                    amplifier = 0;
                } else if (wither == 2) {
                    duration = 20 * 6;
                    amplifier = 1;
                } else {
                    duration = 20 * 4;
                    amplifier = 2;
                }


                pTarget.addEffect(new MobEffectInstance(
                        MobEffects.WITHER,
                        duration,
                        amplifier
                ));
            }
        }
    }

    default void handleHeldOrWornItem(ItemStack pStack, Level pLevel, Entity pEntity) {
        if (pEntity instanceof Player player) {
            AugmentObject bonuses = getAugmentBonuses(pStack);

            if (
                bonuses.XP_SIPHON &&
                pStack.getDamageValue() > 0 &&
                player.totalExperience > 0 &&
                pLevel.getGameTime() % 80 == 0
            ) {
                boolean wasBroken = isBroken(pStack);
                int xpAmount;

                if (player.totalExperience >= 10) {
                    xpAmount = 10;
                } else {
                    xpAmount = player.totalExperience;
                }

                int repairAmount = xpAmount * 2;
                int xpToRemove = xpAmount;

                if (pStack.getDamageValue() < 2 * repairAmount) {
                    xpToRemove = 2 * pStack.getDamageValue();
                }

                int newDamage = Math.max(0, pStack.getDamageValue() - repairAmount);

                player.giveExperiencePoints(-xpToRemove);
                pStack.setDamageValue(newDamage);

                // Reapply enchantments if the item was just fixed
                if (wasBroken && !isBroken(pStack)) {
                    applyVanillaEnchantments(pStack, pLevel);
                }
            }
        }

        if (isBroken(pStack)) {
            return;
        }

        if (pEntity instanceof Player player) {
            AugmentObject bonuses = getAugmentBonuses(pStack);

            if (bonuses.MAGNET) {
                double range = 4.0;

                // Find all Item Entities in range
                List<ItemEntity> items = pLevel.getEntitiesOfClass(
                        ItemEntity.class,
                        player.getBoundingBox().inflate(range)
                );

                for (ItemEntity item : items) {
                    // Ignore items that have pickup delay
                    if (item.hasPickUpDelay()) {
                        continue;
                    }

                    // Vector from the item to the player
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
