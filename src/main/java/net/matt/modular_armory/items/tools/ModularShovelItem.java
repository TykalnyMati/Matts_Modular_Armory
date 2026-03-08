package net.matt.modular_armory.items.tools;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;

import net.matt.modular_armory.items.tools.helpers.ModularGenericDiggerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class ModularShovelItem extends ModularGenericDiggerItem {
    protected static final Map<Block, BlockState> FLATTENABLES = Map.of(
            Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState(),
            Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState(),
            Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState(),
            Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState(),
            Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState(),
            Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState()
    );

    public ModularShovelItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }

    // For making paths
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (pContext.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = pContext.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.isEmptyBlock(blockpos.above())) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent(null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(pContext.getPlayer(), level, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }

            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, blockstate2, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                    if (player != null) {
                        damageTool(pContext.getItemInHand(), player);
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @org.jetbrains.annotations.Nullable
    public static BlockState getShovelPathingState(BlockState originalState) {
        return FLATTENABLES.get(originalState.getBlock());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }
}
