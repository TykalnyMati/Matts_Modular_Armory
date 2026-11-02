package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ModularPickaxeItem extends ModularGenericDiggerItem {
    public ModularPickaxeItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        if (!ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility)) {
            return super.canPerformAction(stack, itemAbility);
        }
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (super.use(level, player, usedHand).getResult().consumesAction()) {
            return super.use(level, player, usedHand);
        } else {
            return modularItemUse(level, player, usedHand);
        }
    }
}