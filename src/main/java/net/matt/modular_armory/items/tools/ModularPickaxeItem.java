package net.matt.modular_armory.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
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

public class ModularPickaxeItem extends PickaxeItem {
    public ModularPickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
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

    private void damageTool(ItemStack stack, LivingEntity entity) {
        int bonusesUnbreaking = getAugmentBonuses(stack).UNBREAKING;
        // Unbreaking augment
        if (bonusesUnbreaking * 0.125f >= 1f || entity.getRandom().nextFloat() < bonusesUnbreaking * 0.125f) {
            return;
        }

        if (isBroken(stack)) {
            stack.setDamageValue(stack.getMaxDamage() - 1);
            return;
        }

        if (stack.getDamageValue() + 1 >= stack.getMaxDamage() - 1) {
            stack.setDamageValue(stack.getMaxDamage() - 1);

            if (!entity.level().isClientSide()) {
                entity.level().playSound(
                        null,
                        entity.blockPosition(),
                        SoundEvents.ITEM_BREAK,
                        entity.getSoundSource(),
                        1.0F,
                        1.0F
                );
            }

            return;
        }

        stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
    }

    public int repairFromXp (ItemStack stack, int xpAmount) {
        if (!getAugmentBonuses(stack).MENDING) {
            return xpAmount;
        }

        if (stack.getDamageValue() <= 0) {
            return xpAmount;
        }

        int repairAmount = xpAmount * 2;
        int newDamage = Math.max(0, stack.getDamageValue() - repairAmount);
        int remainingXp = xpAmount - repairAmount / 2;

        stack.setDamageValue(newDamage);
        return remainingXp;
    }

    public boolean isBroken(ItemStack item) {
        return item.getDamageValue() >= item.getMaxDamage() - 1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isBroken(stack)) {
            return 0.1F;
        }

        float base = super.getDestroySpeed(stack, state);

        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
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

    private AugumentObject getAugmentBonuses(ItemStack stack) {
        AugumentObject Bonuses = new AugumentObject(stack);
        return Bonuses;
    }
}