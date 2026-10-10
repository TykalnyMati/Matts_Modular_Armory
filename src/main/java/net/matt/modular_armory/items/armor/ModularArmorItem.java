package net.matt.modular_armory.items.armor;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
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

        return super.getDefaultAttributeModifiers(stack);
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
