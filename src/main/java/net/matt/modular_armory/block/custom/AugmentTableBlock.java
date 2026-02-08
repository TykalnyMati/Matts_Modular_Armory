package net.matt.modular_armory.block.custom;

import com.mojang.serialization.MapCodec;
import net.matt.modular_armory.block.entity.custom.AugmentTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AugmentTableBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    // Custom hitbox shape
//    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 4, 12, 4);
//    public static final VoxelShape leg_2 = Block.box(8, 0, 0, 4, 12, 4);
//    public static final VoxelShape leg_3 = Block.box(0, 0, 8, 4, 12, 4);
//    public static final VoxelShape leg_4 = Block.box(8, 0, 8, 4, 12, 4);
//    public static final VoxelShape top = Block.box(0, 12, 0, 16, 4, 16);

    public static final MapCodec<AugmentTableBlock> CODEC = simpleCodec(AugmentTableBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AugmentTableBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    // Rotates the block to face the player
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    // Overall EntityBlock Handling
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AugmentTableBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if(pLevel.getBlockEntity(pPos) instanceof AugmentTableBlockEntity augumentTableBlockEntity) {
                augumentTableBlockEntity.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    // Does stuff
    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof AugmentTableBlockEntity augumentTableBlockEntity) {
            if(!pLevel.isClientSide()) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(augumentTableBlockEntity, Component.literal("Augment Table")), pPos);
                return ItemInteractionResult.SUCCESS;
            }

            if(pLevel.isClientSide()) {
                return ItemInteractionResult.SUCCESS;
            }

//            if(augumentTableBlockEntity.inventory.getStackInSlot(0).isEmpty() && !pStack.isEmpty()) {
//                augumentTableBlockEntity.inventory.insertItem(0, pStack.copy(), false);
//                pStack.shrink(1);
//                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
//                return ItemInteractionResult.SUCCESS;
//            } else if(pStack.isEmpty()) {
//                ItemStack stackOnTable = augumentTableBlockEntity.inventory.extractItem(0, 1, false);
//                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnTable);
//                augumentTableBlockEntity.clearContents();
//                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
//                return ItemInteractionResult.SUCCESS;
//            }
        }
        return ItemInteractionResult.FAIL;
    }
}
