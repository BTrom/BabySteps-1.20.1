package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.utils.IRabbitAnimationAccess;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal implements IRabbitAnimationAccess {

    @Shadow private int jumpTicks;
    @Shadow private int jumpDuration;

    @Unique public final AnimationState bs$hopAnimationState = new AnimationState();
    @Unique public final AnimationState bs$idleHeadTiltAnimationState = new AnimationState();
    @Unique private int bs$idleAnimationTimeout;

    protected RabbitMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public AnimationState bs$getHopAnimationState() {
        return this.bs$hopAnimationState;
    }

    @Override
    public AnimationState bs$getIdleHeadTiltAnimationState() {
        return this.bs$idleHeadTiltAnimationState;
    }

    @Override
    @Unique
    public float bs$getJumpAnimationSpeed() {
        return this.jumpDuration == 0 ? 0.0F : 15.0F / (float)this.jumpDuration;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bs$initAnimationValues(CallbackInfo ci) {
        this.bs$idleAnimationTimeout = this.random.nextInt(40) + 180;
        this.refreshDimensions();
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void bs$handleAnimationStates(CallbackInfo ci) {
        if (this.level().isClientSide()) {
            this.bs$setupAnimationStates();
        }
    }

    @Unique
    private void bs$setupAnimationStates() {
        boolean isNotLeashed = !this.isLeashed();

        if (this.bs$idleAnimationTimeout <= 0 && isNotLeashed && !this.isNoAi()) {
            this.bs$idleAnimationTimeout = this.random.nextInt(40) + 180;
            this.bs$idleHeadTiltAnimationState.start(this.tickCount);
        } else if (this.jumpTicks > 0) {
            this.bs$hopAnimationState.startIfStopped(this.tickCount);
            this.bs$idleHeadTiltAnimationState.stop();
        } else {
            --this.bs$idleAnimationTimeout;
            this.bs$hopAnimationState.stop();
        }
    }

    @Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
    private void bs$boostJumpPower(CallbackInfoReturnable<Float> cir) {
        float originalPower = cir.getReturnValue();
        cir.setReturnValue(originalPower * 1.4F);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(1.2f);
    }

    @Inject(method = "startJumping", at = @At("TAIL"))
    private void bs$forceJumpDuration(CallbackInfo ci) {
        this.jumpDuration = 15;
        this.jumpTicks = 0;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void bs$handleEntityEventHead(byte id, CallbackInfo ci) {
        if (id == 1) {
            this.spawnSprintParticle();
            this.jumpDuration = 15;
            this.jumpTicks = 0;
            ci.cancel();
        }
    }
}
