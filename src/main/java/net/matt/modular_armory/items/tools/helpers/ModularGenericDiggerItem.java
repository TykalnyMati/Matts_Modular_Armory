package net.matt.modular_armory.items.tools.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModularGenericDiggerItem extends DiggerItem implements ModularDiggerToolItem {
    private final TagKey<Block> mineableTag;

    public ModularGenericDiggerItem(Tier pTier, TagKey<Block> pBlocks, Properties pProperties) {
        super(pTier, pBlocks, pProperties);
        this.mineableTag = pBlocks;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        AugumentObject bonuses = getAugmentBonuses(stack);

        if (isBroken(stack)) {
            return false;
        }

        if (!level.isClientSide() && state.getDestroySpeed(level, pos) > 0.0F) {
            // Silk touch check
            if (bonuses.SILK_TOUCH) {
                // if tool isn't correct
                if (!isCorrectToolForDrops(stack, state) && state.requiresCorrectToolForDrops()) {
                    return false;
                }

                BlockEntity blockEntity = level.getBlockEntity(pos);

                // Silk Holder test
                Holder<Enchantment> silkHolder =
                        level.registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT)
                                .getHolderOrThrow(Enchantments.SILK_TOUCH);

                // Create tool copy and inject silk enchant
                ItemStack silkTool = stack.copy();
                silkTool.enchant(silkHolder, 1);

                LootParams.Builder builder = new LootParams.Builder(((ServerLevel) level))
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, silkTool)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                        .withParameter(LootContextParams.THIS_ENTITY, entity);

                List<ItemStack> drops = state.getDrops(builder);

                // Remove block without vanilla drops
                level.removeBlock(pos, false);

                // Spawn custom drops
                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }
            } else // Fortune check
                if (bonuses.FORTUNE > 0) {
                    // if tool isn't correct
                    if (!isCorrectToolForDrops(stack, state) && state.requiresCorrectToolForDrops()) {
                        return false;
                    }

                    BlockEntity blockEntity = level.getBlockEntity(pos);

                    // Fortune Holder test
                    Holder<Enchantment> fortuneHolder =
                            level.registryAccess()
                                    .registryOrThrow(Registries.ENCHANTMENT)
                                    .getHolderOrThrow(Enchantments.FORTUNE);

                    // Create tool copy and inject fortune enchant
                    ItemStack fortuneTool = stack.copy();
                    fortuneTool.enchant(fortuneHolder, bonuses.FORTUNE);

                    LootParams.Builder builder = new LootParams.Builder(((ServerLevel) level))
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                            .withParameter(LootContextParams.TOOL, fortuneTool)
                            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                            .withParameter(LootContextParams.THIS_ENTITY, entity);

                    List<ItemStack> drops = state.getDrops(builder);

                    // Remove block without vanilla drops
                    level.removeBlock(pos, false);

                    // Spawn custom drops
                    for (ItemStack drop : drops) {
                        Block.popResource(level, pos, drop);
                    }
                }
            damageTool(stack, entity);
        }

        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return !isBroken(stack);
    }

    @Override
    public void postHurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
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
            base += getAugmentBonuses(stack).MINING_BONUS;
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
}
