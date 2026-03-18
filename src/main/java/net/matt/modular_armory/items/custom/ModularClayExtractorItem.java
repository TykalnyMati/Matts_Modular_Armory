package net.matt.modular_armory.items.custom;

import net.matt.modular_armory.items.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.minecraft.world.level.block.Block.popResource;
import static net.minecraft.world.level.block.Block.popResourceFromFace;

public class ModularClayExtractorItem extends Item {
    private static final Map<Block, Block> CHANGE_MAP = Map.of(
            Blocks.CLAY, Blocks.DIRT
    );

    public ModularClayExtractorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();

        if (CHANGE_MAP.containsKey(clickedBlock)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pContext.getClickedPos(), CHANGE_MAP.get(clickedBlock).defaultBlockState());

                // Usefull for droping XP
                //clickedBlock.popExperience(((ServerLevel) level), pContext.getClickedPos(), 10);

                // between 2/3
                int amount = 2 + new java.util.Random().nextInt(2);
                popResourceFromFace(level, pContext.getClickedPos(), pContext.getClickedFace(), new ItemStack(ModItems.MODULAR_CLAY.get(), amount));

                pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) pContext.getPlayer()), item -> {
                    assert pContext.getPlayer() != null;
                    pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND);
                });

                level.playSound(null, pContext.getClickedPos(), SoundEvents.GRAVEL_BREAK, SoundSource.BLOCKS);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
