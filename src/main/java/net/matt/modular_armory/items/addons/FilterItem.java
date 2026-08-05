package net.matt.modular_armory.items.addons;

import net.matt.modular_armory.screen.custom.FilterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FilterItem extends Item {
    public FilterItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) -> new FilterMenu(containerId, playerInventory, stack),
                    Component.translatable("item.mattsmodulararmory.basic_filter")
            ));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
