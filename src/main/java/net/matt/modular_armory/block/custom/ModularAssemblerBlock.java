package net.matt.modular_armory.block.custom;

import net.matt.modular_armory.screen.custom.ModularAssemblerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ModularAssemblerBlock extends Block {

    private static final Component CONTAINER_TITLE = Component.translatable("container.mattsmodulararmory.modular_assembler");

    public ModularAssemblerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        pPlayer.openMenu(new SimpleMenuProvider((id, inventory, plyer) ->
                new ModularAssemblerMenu(id, inventory, ContainerLevelAccess.create(pLevel, pPos)), CONTAINER_TITLE));
        return InteractionResult.CONSUME;
    }
}
