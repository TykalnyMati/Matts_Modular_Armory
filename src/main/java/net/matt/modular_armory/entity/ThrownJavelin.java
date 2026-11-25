package net.matt.modular_armory.entity;

import net.matt.modular_armory.items.ModItems;
import net.matt.modular_armory.items.tools.helpers.AugmentObject;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ThrownJavelin extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> ID_ITEM_STACK =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Boolean> ID_WATER_INERTIA =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BOOLEAN);

    private float baseDamage = 6.0F;
    private boolean dealtDamage;
    public int clientSideReturnJavelinTickCount;

    private final Set<UUID> hitEntityIds = new HashSet<>();
    private int maxEntitiesHit = 1;

    public ThrownJavelin(EntityType<? extends ThrownJavelin> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownJavelin(Level level, LivingEntity shooter, ItemStack pickupItemStack, float baseDamage) {
        super(ModEntities.THROWN_JAVELIN.get(), shooter, level, pickupItemStack, null);
        this.baseDamage = baseDamage;
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
        this.entityData.set(ID_ITEM_STACK, pickupItemStack.copy());

        if (pickupItemStack.getItem() instanceof ModularItemInterface modularItem) {
            AugmentObject bonuses = modularItem.getAugmentBonuses(pickupItemStack);

            if (bonuses.PIERCING > 0) {
                this.maxEntitiesHit = bonuses.PIERCING + 1;
            }

            if (bonuses.WATER_INERTIA) {
                this.entityData.set(ID_WATER_INERTIA, true);
            }
        }

    }

    // Unused for now
//    public ThrownJavelin(Level level, double x, double y, double z, ItemStack pickupItemStack) {
//        super(ModEntities.THROWN_JAVELIN.get(), x, y, z, level, pickupItemStack, pickupItemStack);
//        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
//        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
//    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
        builder.define(ID_ITEM_STACK, new ItemStack(ModItems.MODULAR_CLAY_JAVELIN.get()));
        builder.define(ID_WATER_INERTIA, false);
    }

    public ItemStack getSyncedItemStack() {
        return this.entityData.get(ID_ITEM_STACK);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = (Byte)this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05 * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnJavelinTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnJavelinTickCount;
            }
        }

        super.tick();
    }


    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        return entity != null && entity.isAlive() ? !(entity instanceof ServerPlayer) || !entity.isSpectator() : false;
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.hitEntityIds.contains(entity.getUUID())) {
            return false;
        }
        return super.canHitEntity(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        float damage = this.baseDamage;
        ItemStack weaponStack = this.getWeaponItem();
        Entity owner = this.getOwner();
        // This doesn't apply enchantments that require is_direct = true
        // e.g. fire aspect
        DamageSource damageSource = this.damageSources().trident(this, owner == null ? this : owner);

        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, weaponStack, target, damageSource, damage);
        }

        this.hitEntityIds.add(target.getUUID());

        if (target.hurt(damageSource, damage)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                // Changes the damage source so that it will apply effects that require is_direct = true
                DamageSource damageEffectSource = new DamageSource(
                        this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.TRIDENT),
                        owner == null ? this : owner,
                        owner == null ? this : owner
                );

                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, target, damageEffectSource, weaponStack);

                // Fix for channeling
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, target, damageSource, weaponStack);
            }

            if (target instanceof LivingEntity livingTarget) {
                this.doKnockback(livingTarget, damageSource);
                this.doPostHurtEffects(livingTarget);

                if (owner instanceof LivingEntity livingOwner && weaponStack.getItem() instanceof ModularItemInterface modularItem) {
                    modularItem.postWeaponHurtEnemy(weaponStack, livingTarget, livingOwner);
                }
            }
        }
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);

        if (hitEntityIds.size() >= maxEntitiesHit) {
            this.dealtDamage = true;
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        }
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        Entity var6 = this.getOwner();
        LivingEntity var10002;
        if (var6 instanceof LivingEntity livingentity) {
            var10002 = livingentity;
        } else {
            var10002 = null;
        }

        EnchantmentHelper.onHitBlock(level, stack, var10002, this, (EquipmentSlot)null, vec3, level.getBlockState(hitResult.getBlockPos()), (p_348680_) -> this.kill());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.MODULAR_CLAY_JAVELIN);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player entity) {
        if (this.ownedBy(entity) || this.getOwner() == null) {
            super.playerTouch(entity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        if (compound.contains("BaseDamage")) {
            this.baseDamage = compound.getFloat("BaseDamage");
        }
        ItemStack origin = this.getPickupItemStackOrigin();
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(origin));
        this.entityData.set(ID_ITEM_STACK, origin.copy());

        if (origin.getItem() instanceof ModularItemInterface modularItem) {
            this.entityData.set(ID_WATER_INERTIA, modularItem.getAugmentBonuses(origin).WATER_INERTIA);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", this.dealtDamage);
        compound.putFloat("BaseDamage", this.baseDamage);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        Level var3 = this.level();
        byte var10000;
        if (var3 instanceof ServerLevel serverlevel) {
            var10000 = (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127);
        } else {
            var10000 = 0;
        }

        return var10000;
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return this.entityData.get(ID_WATER_INERTIA) ? 0.99F : 0.6F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
