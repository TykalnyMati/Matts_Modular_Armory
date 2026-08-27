package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class ModArmorEvents {

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        if(event.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.level().isClientSide()) {
                return;
            }

            ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);

            if (boots.getItem() instanceof ModularItemInterface modularItem && !modularItem.isBroken(boots)) {
                AugmentObject bonuses = modularItem.getAugmentBonuses(boots);

                if (bonuses.FROST_WALKER > 0 && livingEntity.onGround()) {
                    Vec3 delta = livingEntity.getKnownMovement();
                    boolean isPhysicallyMoving = delta.horizontalDistance() > 0.001f;

                    if (isPhysicallyMoving) {
                        int radius = Math.min(6, 2 + bonuses.FROST_WALKER);
                        BlockState frostState = Blocks.FROSTED_ICE.defaultBlockState();
                        BlockPos pos = livingEntity.blockPosition();

                        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
                            if (nearbyPos.closerToCenterThan(livingEntity.position(), radius) && !livingEntity.level().isClientSide()) {
                                BlockState currentState = livingEntity.level().getBlockState(nearbyPos);
                                BlockState aboveState = livingEntity.level().getBlockState(nearbyPos.above());

                                if (currentState.is(Blocks.WATER) && currentState.getValue(LiquidBlock.LEVEL) == 0 && aboveState.isAir() && frostState.canSurvive(livingEntity.level(), nearbyPos)) {
                                    livingEntity.level().setBlockAndUpdate(nearbyPos, frostState);
                                    livingEntity.level().scheduleTick(nearbyPos, Blocks.FROSTED_ICE, livingEntity.getRandom().nextInt(20, 40));
                                }
                            }
                        }
                    }
                }

                if (bonuses.SOUL_SPEED > 0) {
                    handleSoulSpeed(livingEntity, boots, modularItem, bonuses);
                } else {
                    removeSoulSpeedModifiers(livingEntity);
                }
            } else {
                removeSoulSpeedModifiers(livingEntity);
            }
        }
    }

    // Handle Soul Speed
    private static final ResourceLocation SOUL_SPEED_BOOST_ID = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "soul_speed_boost");
    private static void handleSoulSpeed(LivingEntity livingEntity, ItemStack boots, ModularItemInterface armorItem, AugmentObject bonuses) {
        var speedAttribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        var efficiencyAttribute = livingEntity.getAttribute(Attributes.MOVEMENT_EFFICIENCY);

        if (speedAttribute == null || efficiencyAttribute == null) {
            return;
        }

        boolean isOnSoulBlock = livingEntity.level().getBlockState(livingEntity.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SOUL_SPEED_BLOCKS);

        if (isOnSoulBlock && !livingEntity.isFallFlying()) {
            double speedAmount = 0.0405 + (bonuses.SOUL_SPEED * 0.0105);

            if (speedAttribute.getModifier(SOUL_SPEED_BOOST_ID) == null) {
                speedAttribute.addTransientModifier(new AttributeModifier(SOUL_SPEED_BOOST_ID, speedAmount, AttributeModifier.Operation.ADD_VALUE));
            }

            if (efficiencyAttribute.getModifier(SOUL_SPEED_BOOST_ID) == null) {
                efficiencyAttribute.addTransientModifier(new AttributeModifier(SOUL_SPEED_BOOST_ID, 1.0, AttributeModifier.Operation.ADD_VALUE));
            }

            if (!livingEntity.level().isClientSide()) {
                Vec3 delta = livingEntity.getKnownMovement();
                boolean isPhysicallyMoving = delta.horizontalDistance() > 0.1f;

                if (isPhysicallyMoving) {

                    // 4% to damage boots
                    if (livingEntity.getRandom().nextFloat() < 0.02f) {
                        armorItem.damageModularItem(boots, livingEntity);
                    }

                    if (livingEntity.getRandom().nextFloat() > 0.30f) {
                        livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.SOUL_ESCAPE, livingEntity.getSoundSource(), 0.15f, 1.0f);
                    }

                    ((ServerLevel) livingEntity.level()).sendParticles(ParticleTypes.SOUL, livingEntity.getX(), livingEntity.getY() + 0.1, livingEntity.getZ(), 1, 0.2, 0, 0.2, 0.075);

                }
            }
        } else if (livingEntity.onGround()) {
            removeSoulSpeedModifiers(livingEntity);
        }
    }

    private static void removeSoulSpeedModifiers(LivingEntity entity) {
        if (entity.level().isClientSide()) {
            return;
        }

        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && speed.getModifier(SOUL_SPEED_BOOST_ID) != null) {
            speed.removeModifier(SOUL_SPEED_BOOST_ID);
        }

        var efficiency = entity.getAttribute(Attributes.MOVEMENT_EFFICIENCY);
        if (efficiency != null && efficiency.getModifier(SOUL_SPEED_BOOST_ID) != null) {
            efficiency.removeModifier(SOUL_SPEED_BOOST_ID);
        }
    }
}
