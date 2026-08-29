package net.matt.modular_armory.items.custom;

import guideme.GuidesCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.matt.modular_armory.MattsModularArmory.MOD_ID;

public class GuideBookItem extends Item {
    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            ResourceLocation guideId = ResourceLocation.fromNamespaceAndPath(MOD_ID, "guide");

            GuidesCommon.openGuide(player, guideId);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
