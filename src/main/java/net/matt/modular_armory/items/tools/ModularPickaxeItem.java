package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;

public class ModularPickaxeItem extends ModularGenericDiggerItem {
    public ModularPickaxeItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}