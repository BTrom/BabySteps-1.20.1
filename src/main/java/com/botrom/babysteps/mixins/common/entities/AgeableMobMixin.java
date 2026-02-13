package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.utils.BabyConfig;
import com.botrom.babysteps.utils.IAgeableMob;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob implements IAgeableMob {

    @Unique private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);
    @Unique private int ageLockParticleTimer = 0;
    @Unique private boolean isLockingAction = true;

    @Shadow public abstract void setAge(int pAge);
    @Shadow public abstract int getAge();
    @Shadow public abstract boolean isBaby();

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    @Unique
    public boolean isBabyMixin() {
        return this.isBaby();
    }

    @Override
    @Unique
    public boolean isAgeLocked() {
        return this.entityData.get(AGE_LOCKED) && BabyConfig.enableGoldenDandelion;
    }

    @Override
    @Unique
    public void setAgeLockedData() {
        this.setAgeLocked(!this.isAgeLocked());
        this.setAge(AgeableMob.BABY_START_AGE);
    }

    @Override
    @Unique
    public void setAgeLocked(final Mob mob, final Supplier<Boolean> isAgedLocked, final Player player, final ItemStack itemInHand, final Consumer<Mob> setAgeLockData) {
        setAgeLockData.accept(mob);
        if (!player.getAbilities().instabuild)
            itemInHand.shrink(1);
        mob.level().playSound(null, mob.blockPosition(), isAgedLocked.get() ? BabySteps.BabySounds.GOLDEN_DANDELION_USE.get() : BabySteps.BabySounds.GOLDEN_DANDELION_UNUSE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    @Unique
    public void setAgeLocked(final boolean locked) {
        this.entityData.set(AGE_LOCKED, locked);
    }

    @Override
    @Unique
    public int getAgeLockParticleTimer() {
        return this.ageLockParticleTimer;
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectDefineData(CallbackInfo ci) {
        entityData.define(AGE_LOCKED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("AgeLocked", this.isAgeLocked());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectLoadData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("AgeLocked")) {
            this.setAgeLocked(tag.getBoolean("AgeLocked"));
        }
    }

    @Inject(method = "setAge", at = @At("HEAD"), cancellable = true)
    private void setAgeInject(int age, CallbackInfo ci) {
        if (this.isAgeLocked() && age > AgeableMob.BABY_START_AGE && this.isBaby()) {
            ci.cancel();
        }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AgeableMob;setAge(I)V"))
    private void aiStepRedirect(AgeableMob instance, int newAge) {
        if (this.isAgeLocked() && this.isBaby()) {
            instance.setAge(AgeableMob.BABY_START_AGE);
        } else {
            instance.setAge(newAge);
        }
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void tickParticleTimer(CallbackInfo ci) {
        if (this.ageLockParticleTimer > 0) {
            this.ageLockParticleTimer--;

            if (this.level().isClientSide && this.ageLockParticleTimer % 5 == 0) {
                double x = this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth();
                double y = this.getY() + this.random.nextDouble() * (double)this.getBbHeight();
                double z = this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth();

                if (this.isLockingAction) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
                } else {
                    this.level().addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    @Unique
    public void triggerAgeLockEffect(boolean locking) {
        this.ageLockParticleTimer = 40;
        this.isLockingAction = locking;
    }
}
