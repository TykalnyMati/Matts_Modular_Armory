package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugumentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

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

        return getWeaponAttackDamageBonus(target, baseDamage, source);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return !isBroken(stack);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        postWeaponHurtEnemy(pStack, pTarget, pAttacker);
    }

    // Disabling the sweeping action
    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (isBroken(stack) && toolAction == ToolActions.SWORD_SWEEP) {
            return false;
        }
        return super.canPerformAction(stack, toolAction);
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

    // Mining
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (isBroken(stack)) {
            return false;
        }

        damageTool(stack, entity, 2);

        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return 0.1F;
        }

        return super.getDestroySpeed(stack, state);
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
    private static long lastHoverTime = 0;
    private static int sessionRandomOffset = 0;
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        boolean hasAugments = pStack.has(ModDataComponentTypes.AUGMENTS.get());
        int tier = 0;
        if (pStack.is(ModTags.Items.MODULAR_TIER_3)) {
            tier = 3;
        } else if (pStack.is(ModTags.Items.MODULAR_TIER_2)) {
            tier = 2;
        }

        // Randomizing flavor text
        if (tier > 1) {
            long currentTime = System.currentTimeMillis();
            int totalFlavors = AugumentObject.TOTAL_FLAVOR_TEXT_AMOUNT;

            if (currentTime - lastHoverTime > 100) {
                sessionRandomOffset = new java.util.Random().nextInt(totalFlavors);
            }
            lastHoverTime = currentTime;
        }

        List<Component> toolTips = new AugumentObject(pStack).getTooltips(pTooltipComponents, hasAugments, tier, sessionRandomOffset);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
