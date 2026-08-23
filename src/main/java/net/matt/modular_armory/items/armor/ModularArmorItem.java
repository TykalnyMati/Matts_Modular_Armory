package net.matt.modular_armory.items.armor;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ModularArmorItem extends ArmorItem implements ModularItemInterface {
    public ModularArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        damageModularItem(stack, entity, amount);
        return 0;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (isBroken(stack)) {
            return ItemAttributeModifiers.EMPTY;
        }

        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
        AugmentObject bonuses = new AugmentObject(stack);

        // Protections
        if (bonuses.PROTECTION > 0) {

        } else if (bonuses.BLAST_PROTECTION > 0) {
            double resistanceAmount = bonuses.BLAST_PROTECTION * 0.15;
            resistanceAmount = Math.min(1.0, resistanceAmount);

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "blast_protection_knockback_resistance_" + this.getType().getName());

            modifiers = modifiers.withModifierAdded(
                    Attributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                    new AttributeModifier(modifierId, resistanceAmount, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        } else if (bonuses.PROJECTILE_PROTECTION > 0) {

        } else if (bonuses.FIRE_PROTECTION > 0) {
            double burnTimeReduction = bonuses.FIRE_PROTECTION * -0.15;
            burnTimeReduction = Math.max(-1.0, burnTimeReduction);

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "fire_protection_burn_time_" + this.getType().getName());

            modifiers = modifiers.withModifierAdded(
                    Attributes.BURNING_TIME,
                    new AttributeModifier(modifierId, burnTimeReduction, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        }

        // Respiration
        if (bonuses.RESPIRATION > 0) {
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "respiration_" + this.getType().getName());

            modifiers = modifiers.withModifierAdded(
                    Attributes.OXYGEN_BONUS,
                    new AttributeModifier(modifierId, bonuses.RESPIRATION, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        }

        // Aqua Affinity
        if (bonuses.AQUA_AFFINITY > 0) {
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "aqua_affinity_" + this.getType().getName());

            double aquaBonus = 0;

            if (bonuses.AQUA_AFFINITY == 1) {
                aquaBonus = 0.8;
            } else if (bonuses.AQUA_AFFINITY >= 2) {
                aquaBonus = 1.3;
            }

            modifiers = modifiers.withModifierAdded(
                    Attributes.SUBMERGED_MINING_SPEED,
                    new AttributeModifier(modifierId, aquaBonus, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        }

        // Swift Sneak
        if (bonuses.SWIFT_SNEAK > 0) {
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "swift_sneak_" + this.getType().getName());

            double sneakBonus = bonuses.SWIFT_SNEAK * 0.15;

            modifiers = modifiers.withModifierAdded(
                    Attributes.SNEAKING_SPEED,
                    new AttributeModifier(modifierId, sneakBonus, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        }

        // Depth Strider
        if (bonuses.DEPTH_STRIDER > 0 && bonuses.FROST_WALKER <= 0) {
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID,
                    "depth_strider_" + this.getType().getName());

            double depthBonus = bonuses.DEPTH_STRIDER * 0.3333;

            modifiers = modifiers.withModifierAdded(
                    Attributes.WATER_MOVEMENT_EFFICIENCY,
                    new AttributeModifier(modifierId, depthBonus, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(this.getType().getSlot())
            );
        }

        return modifiers;
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
    private static int lastStackId = 0;
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
            int totalFlavors = AugmentObject.TOTAL_FLAVOR_TEXT_AMOUNT;
            int stackId = System.identityHashCode(pStack);

            if (currentTime - lastHoverTime > 100 || stackId != lastStackId) {
                lastStackId = stackId;
                sessionRandomOffset = new java.util.Random().nextInt(totalFlavors);
            }
            lastHoverTime = currentTime;
        }

        List<Component> toolTips = new AugmentObject(pStack).getTooltips(pTooltipComponents, hasAugments, isBroken(pStack), tier, sessionRandomOffset);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
