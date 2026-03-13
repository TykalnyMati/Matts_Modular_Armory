package net.matt.modular_armory.items.tools.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
            damageTool(stack, entity);
        }

        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (player.isCreative()) {
            return false;
        }

        if (isBroken(itemstack)) {
            return false;
        }

        Level level = player.level();
        if (level.isClientSide) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if (!isCorrectToolForDrops(itemstack, state) && state.requiresCorrectToolForDrops()) {
            return false;
        }

        AugumentObject bonuses = getAugmentBonuses(itemstack);
        ServerLevel serverLevel = (ServerLevel) level;

        // Only intercept if we have Silk or Fortune and the tool is correct
        if ((bonuses.SILK_TOUCH || bonuses.FORTUNE > 0 || bonuses.AUTO_SMELT)) {

            // Create Simulation Tool
            ItemStack simulationTool = itemstack.copy();
            if (bonuses.SILK_TOUCH) {
                simulationTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
            } else {
                simulationTool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), bonuses.FORTUNE);
            }

            // Calculate Item Drops
            LootParams.Builder builder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, simulationTool)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos))
                    .withParameter(LootContextParams.THIS_ENTITY, player);

            List<ItemStack> drops = state.getDrops(builder);

            // Handle Experience (XP)
            // If it's Silk Touch, we skip this entirely (XP = 0)
            // If it's Fortune, we manually spawn the XP since we are bypassing vanilla
            if (!bonuses.SILK_TOUCH) {
                int xp = state.getExpDrop(level, level.random, pos, bonuses.FORTUNE, 0);
                if (xp > 0) {
                    state.getBlock().popExperience(serverLevel, pos, xp);
                }
            }

            // Auto smelting
            if (bonuses.AUTO_SMELT && !bonuses.SILK_TOUCH) {
                for (int i = 0; i < drops.size(); i++) {
                    ItemStack originalStack = drops.get(i);
                    ItemStack smeltedResult = getSmeltingResult(serverLevel, originalStack);

                    if (!smeltedResult.isEmpty()) {
                        // Keep the count from the original drop (handles Fortune synergy!)
                        ItemStack finalStack = smeltedResult.copy();
                        finalStack.setCount(originalStack.getCount() * smeltedResult.getCount());
                        drops.set(i, finalStack);

                        // Optional: Spawn some "smelting" XP for the player
                        float smeltingXp = getSmeltingExperience(serverLevel, originalStack);
                        if (smeltingXp > 0) {
                            // Cast to int, but handle the remainder randomly (vanilla style)
                            int xpToDrop = (int) smeltingXp + (serverLevel.random.nextFloat() < (smeltingXp % 1) ? 1 : 0);

                            // Apply fortune for xp
                            if (bonuses.FORTUNE > 0) {
                                xpToDrop *= bonuses.FORTUNE;
                            }

                            if (xpToDrop > 0) {
                                state.getBlock().popExperience(serverLevel, pos, xpToDrop);
                            }
                        }

                        // Optional: Spawn fire particles to show smelting happened
                        serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.05);
                        damageTool(itemstack, player);
                    }
                }
            }

            // Finalize the break
            level.removeBlock(pos, false);
            for (ItemStack drop : drops) {
                Block.popResource(level, pos, drop);
            }

            damageTool(itemstack, player);
            return true;
        }

        return false;
    }

    private ItemStack getSmeltingResult(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getResultItem(level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    private float getSmeltingExperience(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getExperience())
                .orElse(0.0f);
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

        damageTool(pStack, pAttacker);
        damageTool(pStack, pAttacker);
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

        if (state.is(mineableTag)) {
            base += (float) (1.0f * (Math.pow(getAugmentBonuses(stack).EFFICIENCY, 2) + 1));
        }
        return base;

    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return false;
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
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        List<Component> toolTips = new AugumentObject(pStack).getTooltips(pTooltipComponents);

        super.appendHoverText(pStack, pContext, toolTips, pTooltipFlag);
    }
}
