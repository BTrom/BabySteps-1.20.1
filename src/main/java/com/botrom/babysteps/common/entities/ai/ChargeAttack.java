package com.botrom.babysteps.common.entities.ai;

import com.botrom.babysteps.BabySteps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ChargeAttack extends Behavior<Animal> {
    private final int timeBetweenAttacks;
    private final TargetingConditions chargeTargeting;
    private final float speed;
    private final float knockbackForce;
    private final double maxTargetDetectionDistance;
    private final double maxChargeDistance;
    private final SoundEvent chargeSound;
    private Vec3 chargeVelocityVector;
    private Vec3 startPosition;

    public ChargeAttack(
            int timeBetweenAttacks, TargetingConditions chargeTargeting, float speed, float knockbackForce, double maxChargeDistance, double maxTargetDetectionDistance, SoundEvent chargeSound
    ) {
        super(ImmutableMap.of(BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
        this.timeBetweenAttacks = timeBetweenAttacks;
        this.chargeTargeting = chargeTargeting;
        this.speed = speed;
        this.knockbackForce = knockbackForce;
        this.maxChargeDistance = maxChargeDistance;
        this.maxTargetDetectionDistance = maxTargetDetectionDistance;
        this.chargeSound = chargeSound;
        this.chargeVelocityVector = Vec3.ZERO;
        this.startPosition = Vec3.ZERO;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, Animal p_458647_) {
        return p_458647_.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }

    protected boolean canStillUse(ServerLevel level, Animal animal, long chargeDuration) {
        Brain<?> brain = animal.getBrain();
        Optional<LivingEntity> optional = brain.getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optional.isEmpty()) {
            return false;
        } else {
            LivingEntity livingentity = optional.get();
            if (animal instanceof TamableAnimal tamableanimal && tamableanimal.isTame()) {
                return false;
            } else if (animal.position().subtract(this.startPosition).lengthSqr() >= this.maxChargeDistance * this.maxChargeDistance) {
                return false;
            } else if (livingentity.position().subtract(animal.position()).lengthSqr() >= this.maxTargetDetectionDistance * this.maxTargetDetectionDistance) {
                return false;
            } else {
                return !animal.hasLineOfSight(livingentity) ? false : !brain.hasMemoryValue(BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get());
            }
        }
    }

    protected void start(ServerLevel level, Animal animal, long chargeDuration) {
        Brain<?> brain = animal.getBrain();
        this.startPosition = animal.position();
        LivingEntity livingentity = brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
        Vec3 vec3 = livingentity.position().subtract(animal.position()).normalize();
        this.chargeVelocityVector = vec3.scale(this.speed);
        if (this.canStillUse(level, animal, chargeDuration)) {
            animal.playSound(this.chargeSound);
        }
    }

    protected void tick(ServerLevel level, Animal animal, long duration) {
        Brain<?> brain = animal.getBrain();
        LivingEntity livingentity = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        animal.lookAt(livingentity, 360.0F, 360.0F);
        animal.setDeltaMovement(this.chargeVelocityVector);
        List<LivingEntity> list = new ArrayList<>(1);
        level.getEntities(
                EntityTypeTest.forClass(LivingEntity.class), animal.getBoundingBox(), p_455049_ -> this.chargeTargeting.test(animal, p_455049_), list, 1
        );
        if (!list.isEmpty()) {
            LivingEntity livingentity1 = list.get(0);
            if (animal.hasPassenger(livingentity1)) {
                return;
            }

            this.dealDamageToTarget(level, animal, livingentity1);
            this.dealKnockBack(animal, livingentity1);
            this.stop(level, animal, duration);
        }
    }

    private void dealDamageToTarget(ServerLevel level, Animal animal, LivingEntity entity) {
        DamageSource damagesource = level.damageSources().mobAttack(animal);
        float f = (float)animal.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (entity.hurt(damagesource, f)) {
            EnchantmentHelper.doPostHurtEffects(entity, Objects.requireNonNull(damagesource.getDirectEntity()));
        }
    }

    private void dealKnockBack(Animal animal, LivingEntity entity) {
        int i = animal.hasEffect(MobEffects.MOVEMENT_SPEED) ? animal.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
        int j = animal.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? animal.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
        float f = 0.25F * (i - j);
        float f1 = Mth.clamp(this.speed * (float)animal.getAttributeValue(Attributes.MOVEMENT_SPEED), 0.2F, 2.0F) + f;
        causeExtraKnockback(animal, entity, f1 * this.knockbackForce, animal.getDeltaMovement());
    }

    public void causeExtraKnockback(Animal animal, Entity entity, float power, Vec3 vec3) {
        if (power > 0.0F && entity instanceof LivingEntity livingentity) {
            livingentity.knockback(
                    power, Mth.sin(animal.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(animal.getYRot() * (float) (Math.PI / 180.0))
            );
            animal.setDeltaMovement(animal.getDeltaMovement().multiply(0.6, 1.0, 0.6));
        }
    }

    protected void stop(ServerLevel level, Animal animal, long chargeDuration) {
        animal.getBrain().setMemory(BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get(), this.timeBetweenAttacks);
        animal.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }
}