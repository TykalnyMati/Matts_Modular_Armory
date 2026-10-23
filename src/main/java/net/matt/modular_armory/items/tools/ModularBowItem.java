package net.matt.modular_armory.items.tools;

import net.matt.modular_armory.component.ModDataComponentTypes;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.util.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ModularBowItem extends BowItem implements ModularItemInterface {
    private final float maxSpeed;

    public ModularBowItem(Properties properties, float maxSpeed) {
        super(properties);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        damageModularItem(stack, entity, amount);
        return 0;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!isBroken(stack)) {
            AugmentObject bonuses = getAugmentBonuses(stack);

            if (bonuses.RAPID_FIRE) {
                return;
            }

            if (entityLiving instanceof Player player) {
                ItemStack itemstack = player.getProjectile(stack);
                if (!itemstack.isEmpty()) {
                    int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                    i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                    if (i < 0) {
                        return;
                    }

                    float f = getPowerForTimeModular(i);
                    if (!((double)f < 0.1)) {
                        List<ItemStack> list = draw(stack, itemstack, player);
                        if (level instanceof ServerLevel) {
                            ServerLevel serverlevel = (ServerLevel)level;
                            if (!list.isEmpty()) {
                                this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == maxSpeed, (LivingEntity)null);
                            }
                        }

                        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {


        ItemStack itemstack = player.getItemInHand(hand);

        if (isBroken(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        }

        AugmentObject bonuses = getAugmentBonuses(itemstack);

        if (bonuses.RAPID_FIRE) {
            ItemStack stack = player.getProjectile(itemstack);
            List<ItemStack> list = draw(itemstack, stack, player);
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)level;
                if (!list.isEmpty()) {
                    this.shoot(serverlevel, player, player.getUsedItemHand(), itemstack, list, (maxSpeed * 0.75f) * 3.0F, 1.5F, false, (LivingEntity)null);
                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.75F * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }

            return InteractionResultHolder.fail(itemstack);
        } else {
            return super.use(level, player, hand);
        }
    }

    public float getPowerForTimeModular(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > maxSpeed) {
            f = maxSpeed;
        }

        return f;
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
