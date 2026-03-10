package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.items.tools.helpers.AugumentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class ModularSwordItem extends SwordItem implements ModularItemInterface {
    public ModularSwordItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    // Damage calculations
    @Override
    public float getAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        ItemStack weapon = source.getWeaponItem();
        if (weapon == null) {
            return super.getAttackDamageBonus(target, baseDamage, source);
        }

        if (isBroken(source.getWeaponItem())) {
            return -baseDamage + 1;
        }

        AugumentObject bonuses = getAugmentBonuses(weapon);

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;

        if (sharpness > 0) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 0.5f * sharpness  + 0.5f;
        } else if (smite > 0 && target.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 2.5f * smite;
        } else if (bane_of_arthropods > 0 && target.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
            return super.getAttackDamageBonus(target, baseDamage, source) + 2.5f * bane_of_arthropods;
        }

        return super.getAttackDamageBonus(target, baseDamage, source);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return !isBroken(stack);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        damageTool(pStack, pAttacker);

        AugumentObject bonuses = getAugmentBonuses(pStack);

        int sharpness = bonuses.SHARPNESS;
        int smite = bonuses.SMITE;
        int bane_of_arthropods = bonuses.BANE_OF_ARTHROPODS;

        boolean DO_ENCHANTED_PARTICLES = false;

        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            if (sharpness > 0) {
                DO_ENCHANTED_PARTICLES = true;
            } else if (smite > 0 && pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
                DO_ENCHANTED_PARTICLES = true;
            } else if (bane_of_arthropods > 0 && pTarget.getType().is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)) {
                DO_ENCHANTED_PARTICLES = true;
            }

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
        }
    }

    public static ItemAttributeModifiers createAttributes(Tier pTier, float pAttackDamage, float pAttackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)((float)pAttackDamage + pTier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)pAttackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // Anvil repairing and enchanting
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    // Tooltips
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        List<Component> toolTips = new AugumentObject(pStack).getTooltips(pTooltipComponents);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
