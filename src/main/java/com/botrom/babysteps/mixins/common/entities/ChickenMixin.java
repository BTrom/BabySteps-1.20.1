package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends MobMixin {

    protected ChickenMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author Botrom
     * @reason Backport 1.21 baby chicken ambient sounds
     */
    @Overwrite
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? BabySteps.BabySounds.CHICKEN_AMBIENT_BABY.get() : SoundEvents.CHICKEN_AMBIENT;
    }

    /**
     * @author Botrom
     * @reason Backport 1.21 baby chicken hurt sounds
     */
    @Overwrite
    protected SoundEvent getHurtSound(final @NotNull DamageSource source) {
        return this.isBaby() ? BabySteps.BabySounds.CHICKEN_HURT_BABY.get() : SoundEvents.CHICKEN_HURT;
    }

    /**
     * @author Botrom
     * @reason Backport 1.21 baby chicken death sounds
     */
    @Overwrite
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? BabySteps.BabySounds.CHICKEN_DEATH_BABY.get() : SoundEvents.CHICKEN_DEATH;
    }
}