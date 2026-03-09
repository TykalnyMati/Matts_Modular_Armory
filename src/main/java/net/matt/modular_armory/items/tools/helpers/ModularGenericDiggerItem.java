package net.matt.modular_armory.items.tools.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
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

    // Tooltips
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        AugumentObject bonuses = new AugumentObject(pStack);

        pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.augments").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(":").withStyle(ChatFormatting.GRAY)));

        if (bonuses.UNBREAKING >= 8) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment_full").withStyle(ChatFormatting.DARK_AQUA));
        } else if (bonuses.UNBREAKING > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.unbreaking_augment").withStyle(ChatFormatting.DARK_AQUA)
                    .append(Component.literal(": " + toRoman(bonuses.UNBREAKING)).withStyle(ChatFormatting.DARK_AQUA)));
        }

        if (bonuses.MENDING) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.mending_augment").withStyle(ChatFormatting.GREEN));
        }

        if (bonuses.EFFICIENCY > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.efficiency_augment").withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(": " + toRoman((int)bonuses.EFFICIENCY)).withStyle(ChatFormatting.YELLOW)));
        }

        if (bonuses.SILK_TOUCH) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.silk_touch_augment").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else if (bonuses.FORTUNE > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.fortune_augment").withStyle(ChatFormatting.BLUE)
                    .append(Component.literal(": " + toRoman(bonuses.FORTUNE)).withStyle(ChatFormatting.BLUE)));
        }

        if (bonuses.SHARPNESS > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.sharpness_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(bonuses.SHARPNESS)).withStyle(ChatFormatting.DARK_RED)));
        } else if (bonuses.SMITE > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.smite_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(bonuses.SMITE)).withStyle(ChatFormatting.DARK_RED)));
        } else if (bonuses.BANE_OF_ARTHROPODS > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.mattsmodulararmory.bane_of_arthropods_augment").withStyle(ChatFormatting.DARK_RED)
                    .append(Component.literal(": " + toRoman(bonuses.BANE_OF_ARTHROPODS)).withStyle(ChatFormatting.DARK_RED)));
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}
