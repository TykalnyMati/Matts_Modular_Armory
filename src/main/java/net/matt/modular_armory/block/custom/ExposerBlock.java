package net.matt.modular_armory.block.custom;

import com.mojang.serialization.MapCodec;
import net.matt.modular_armory.block.entity.ModBlockEntities;
import net.matt.modular_armory.block.entity.custom.ExposerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExposerBlock extends BaseEntityBlock {
    public static final MapCodec<ExposerBlock> CODEC = simpleCodec(ExposerBlock::new);

    public ExposerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ExposerBlockEntity(pPos, pState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ExposerBlockEntity exposerBlockEntity) {
                exposerBlockEntity.drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);

            if (entity instanceof ExposerBlockEntity exposerBlockEntity) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(exposerBlockEntity, Component.literal("Exposer")), pPos);
            } else {
                throw new IllegalStateException("Container provider is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    // Disable menu for spectator mode
    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.EXPOSER_BE.get(),
                (level, blockPos, blockState, exposerBlockEntity) -> exposerBlockEntity.tick(level, blockPos, blockState));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.exposer").withStyle(ChatFormatting.GRAY));

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
