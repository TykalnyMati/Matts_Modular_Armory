package net.matt.modular_armory.items.tools;

import com.mojang.datafixers.util.Pair;
import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModularHoeItem extends ModularGenericDiggerItem {
    public ModularHoeItem(Tier tier, Item.Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_HOE, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(pContext)) {
                Player player = pContext.getPlayer();
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide) {
                    consumer.accept(pContext);
                    if (player != null) {
                        pContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(pContext.getHand()));
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static Consumer<UseOnContext> changeIntoState(BlockState pState) {
        return p_327147_ -> {
            p_327147_.getLevel().setBlock(p_327147_.getClickedPos(), pState, 11);
            p_327147_.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, p_327147_.getClickedPos(), GameEvent.Context.of(p_327147_.getPlayer(), pState));
        };
    }

    public static Consumer<UseOnContext> changeIntoStateAndDropItem(BlockState pState, ItemLike pItemToDrop) {
        return p_327150_ -> {
            p_327150_.getLevel().setBlock(p_327150_.getClickedPos(), pState, 11);
            p_327150_.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, p_327150_.getClickedPos(), GameEvent.Context.of(p_327150_.getPlayer(), pState));
            Block.popResourceFromFace(p_327150_.getLevel(), p_327150_.getClickedPos(), p_327150_.getClickedFace(), new ItemStack(pItemToDrop));
        };
    }

    public static boolean onlyIfAirAbove(UseOnContext p_150857_) {
        return p_150857_.getClickedFace() != Direction.DOWN && p_150857_.getLevel().getBlockState(p_150857_.getClickedPos().above()).isAir();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }
}
