package net.matt.modular_armory.items.tools.helpers;

import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.block.entity.custom.ScaffoldingAugmentBlockEntity;
import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

import static net.minecraft.world.level.block.Block.popResourceFromFace;

public class ModularGenericDiggerItem extends DiggerItem implements ModularItemInterface {
    private final TagKey<Block> mineableTag;

    public ModularGenericDiggerItem(Tier pTier, TagKey<Block> pBlocks, Properties pProperties) {
        super(pTier, pBlocks, pProperties);
        this.mineableTag = pBlocks;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (isBroken(stack)) {
            return false;
        }

        if (!level.isClientSide() && state.getDestroySpeed(level, pos) > 0.0F) {
            damageModularItem(stack, entity);
        }

        return true;
    }

    public ItemStack getSmeltingResult(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    public float getSmeltingExperience(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getExperience())
                .orElse(0.0f);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        Player player = pContext.getPlayer();
        Level level = player.level();
        BlockPos pos = pContext.getClickedPos();
        Direction facing = pContext.getClickedFace();

        if (isBroken(stack) || player == null) {
            return InteractionResult.PASS;
        }

        AugmentObject bonuses = getAugmentBonuses(stack);

        if (bonuses.MODULAR_CLAY_EXTRACTOR > 0 && level.getBlockState(pContext.getClickedPos()).getBlock() == Blocks.CLAY) {
            level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());

            int amount = 2 + new java.util.Random().nextInt(2 + bonuses.MODULAR_CLAY_EXTRACTOR);
            popResourceFromFace(level, pos, facing, new ItemStack(ModItems.MODULAR_CLAY.get(), amount));

            damageModularItem(stack, player, 5);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (bonuses.SCAFFOLDING > 0) {
            BlockPos placePos = pos.relative(facing);

            // Try to replace the block (like grass)
            if (level.getBlockState(pos).canBeReplaced()) {
                ItemStack scaffoldingStack = new ItemStack(ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get().asItem());
                BlockPlaceContext scaffoldingContext = new BlockPlaceContext(
                        level,
                        player,
                        pContext.getHand(),
                        scaffoldingStack,
                        new BlockHitResult(pContext.getClickLocation(), facing, pos, pContext.isInside())
                );
                InteractionResult result = ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get().asItem().useOn(scaffoldingContext);

                if (result.consumesAction()) {
                    if (!level.isClientSide) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof ScaffoldingAugmentBlockEntity scaffoldingBE) {
                            scaffoldingBE.setMaxLife(bonuses.SCAFFOLDING);
                        }
                    }
                    damageModularItem(stack, player, 2);

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            // Try to place the block relative to the clicked side
            else if (level.getBlockState(placePos).canBeReplaced()) {
                ItemStack scaffoldingStack = new ItemStack(ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get().asItem());
                BlockPlaceContext scaffoldingContext = new BlockPlaceContext(
                        level,
                        player,
                        pContext.getHand(),
                        scaffoldingStack,
                        new BlockHitResult(pContext.getClickLocation(), facing, placePos, pContext.isInside())
                );
                InteractionResult result = ModBlocks.SCAFFOLDING_AUGMENT_BLOCK.get().asItem().useOn(scaffoldingContext);

                if (result.consumesAction()) {
                    if (!level.isClientSide) {
                        BlockEntity be = level.getBlockEntity(placePos);
                        if (be instanceof ScaffoldingAugmentBlockEntity scaffoldingBE) {
                            scaffoldingBE.setMaxLife(bonuses.SCAFFOLDING);
                        }
                    }
                    damageModularItem(stack, player, 2);

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        } else if (bonuses.ILLUMINATOR) {
            BlockPos placePos = pos.relative(facing);

            if (level.getBlockState(placePos).canBeReplaced()) {
                ItemStack lightStack = new ItemStack(ModBlocks.ILLUMINATOR_AUGMENT_BLOCK.get());
                BlockPlaceContext lightContext = new BlockPlaceContext(
                        level,
                        player,
                        pContext.getHand(),
                        lightStack,
                        new BlockHitResult(pContext.getClickLocation(), facing, pos, pContext.isInside())
                );
                InteractionResult result = ModBlocks.ILLUMINATOR_AUGMENT_BLOCK.get().asItem().useOn(lightContext);

                if (result.consumesAction()) {
                    damageModularItem(stack, player, 5);

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return !isBroken(stack);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (isBroken(pStack)) {
            return;
        }

        damageModularItem(pStack, pAttacker, 2);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pIsSelected && !isBroken(pStack)) {
            handleHeldItem(pStack, pLevel, pEntity);
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (isBroken(stack)) {
            return ItemAttributeModifiers.EMPTY;
        }

        return super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
        if (isBroken(source.getWeaponItem())) {
            return -baseDamage + 1;
        }

        return super.getAttackDamageBonus(target, baseDamage, source);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return 0.1F;
        }

        float base = super.getDestroySpeed(stack, state);
        AugmentObject bonuses = getAugmentBonuses(stack);

        if (state.is(mineableTag)) {
            base += (float) (1.0f * (Math.pow(bonuses.EFFICIENCY, 2) + 1 + (bonuses.DIAMOND_SHARPENED ? 2 : 0)));
        }
        return base;

    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return false;
        }

        if (getAugmentBonuses(stack).DIAMOND_SHARPENED) {
            if (state.is(mineableTag) && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
                return true;
            }
        }

        return super.isCorrectToolForDrops(stack, state);
    }

    // Anvil repairing and enchanting
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    // Tooltips
    private static long lastHoverTime = 0;
    private static int sessionRandomOffset = 0;
    private static int lastStackId = 0;
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        boolean hasAugments = pStack.has(ModDataComponentTypes.AUGMENTS.get());
        int tier = 0;
        if (pStack.is(ModTags.Items.MODULAR_TIER_3)) {
            tier = 3;
        } else if (pStack.is(ModTags.Items.MODULAR_TIER_2)) {
            tier = 2;
        }

        // Randomizing flavor text
        if (tier > 1) {
            long currentTime = System.currentTimeMillis();
            int totalFlavors = AugmentObject.TOTAL_FLAVOR_TEXT_AMOUNT;
            int stackId = System.identityHashCode(pStack);

            if (currentTime - lastHoverTime > 100 || stackId != lastStackId) {
                lastStackId = stackId;
                sessionRandomOffset = new java.util.Random().nextInt(totalFlavors);
            }
            lastHoverTime = currentTime;
        }

        List<Component> toolTips = new AugmentObject(pStack).getTooltips(pTooltipComponents, hasAugments, isBroken(pStack), tier, sessionRandomOffset);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
