package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.utils.IAxolotlAnimationAccess;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends Animal implements IAxolotlAnimationAccess {

    @Shadow public abstract boolean isPlayingDead();

    @Unique public final AnimationState bs$swimAnimationState = new AnimationState();
    @Unique public final AnimationState bs$walkAnimationState = new AnimationState();
    @Unique public final AnimationState bs$walkUnderWaterAnimationState = new AnimationState();
    @Unique public final AnimationState bs$idleUnderWaterAnimationState = new AnimationState();
    @Unique public final AnimationState bs$idleUnderWaterOnGroundAnimationState = new AnimationState();
    @Unique public final AnimationState bs$idleOnGroundAnimationState = new AnimationState();
    @Unique public final AnimationState bs$playDeadAnimationState = new AnimationState();
    @Unique private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.5F, 0.25F);
    @Unique private static final float BABY_EYE_HEIGHT = 0.2F;

    protected AxolotlMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void injectConstructor(EntityType<? extends Axolotl> type, Level level, CallbackInfo ci) {
//        this.swimAnimationState = new AnimationState();
//        this.walkAnimationState = new AnimationState();
//        this.walkUnderWaterAnimationState = new AnimationState();
//        this.idleUnderWaterAnimationState = new AnimationState();
//        this.idleUnderWaterOnGroundAnimationState = new AnimationState();
//        this.idleOnGroundAnimationState = new AnimationState();
//        this.playDeadAnimationState = new AnimationState();
//        this.ALL_ANIMATIONS = ImmutableList.of(this.swimAnimationState, this.walkAnimationState, this.walkUnderWaterAnimationState, this.idleUnderWaterAnimationState, this.idleUnderWaterOnGroundAnimationState, this.idleOnGroundAnimationState, this.playDeadAnimationState);
//    }
//
    @Inject(method = "baseTick", at = @At("TAIL"))
    private void injectBabyTick(CallbackInfo ci) {
        if (this.level().isClientSide()) {
            if (this.isBaby()) {
                this.bs$tickBabyAnimations();
            }
//            else {
//                this.tickAdultAnimations();
//            }
        }
    }

    @Unique
    private void bs$tickBabyAnimations() {
        Axolotl self = (Axolotl) (Object) this;
        boolean isPlayingDead = this.isPlayingDead();
        boolean isInWater = this.isInWater();
        boolean onGround = this.onGround();
//        boolean isMoving = this.walkAnimation.isMoving() || this.getXRot() != this.xRotO || this.getYRot() != this.yRotO;
        boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;

        if (isPlayingDead) {
            this.bs$soloAnimation(this.bs$playDeadAnimationState);
        } else {
            if (isMoving) {
                if (isInWater && !onGround) {
                    this.bs$soloAnimation(this.bs$swimAnimationState);
                } else if (!isInWater && onGround) {
                    this.bs$soloAnimation(this.bs$walkAnimationState);
                } else {
                    this.bs$soloAnimation(this.bs$walkUnderWaterAnimationState);
                }
            } else if (isInWater && !onGround) {
                this.bs$soloAnimation(this.bs$idleUnderWaterAnimationState);
            } else if (isInWater && onGround) {
                this.bs$soloAnimation(this.bs$idleUnderWaterOnGroundAnimationState);
            } else {
                this.bs$soloAnimation(this.bs$idleOnGroundAnimationState);
            }
        }
    }

    @Unique
    private void bs$soloAnimation(final AnimationState toStart) {
        if (toStart != this.bs$swimAnimationState) this.bs$swimAnimationState.stop();
        if (toStart != this.bs$walkAnimationState) this.bs$walkAnimationState.stop();
        if (toStart != this.bs$walkUnderWaterAnimationState) this.bs$walkUnderWaterAnimationState.stop();
        if (toStart != this.bs$idleUnderWaterAnimationState) this.bs$idleUnderWaterAnimationState.stop();
        if (toStart != this.bs$idleUnderWaterOnGroundAnimationState) this.bs$idleUnderWaterOnGroundAnimationState.stop();
        if (toStart != this.bs$idleOnGroundAnimationState) this.bs$idleOnGroundAnimationState.stop();
        if (toStart != this.bs$playDeadAnimationState) this.bs$playDeadAnimationState.stop();

        toStart.startIfStopped(this.tickCount);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDimensions(pose);
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return this.isBaby() ? BABY_EYE_HEIGHT : super.getEyeHeight(pose);
    }

    @Override
    public AnimationState bs$getSwimAnimationState() {
        return bs$swimAnimationState;
    }

    @Override
    public AnimationState bs$getWalkAnimationState() {
        return bs$walkAnimationState;
    }

    @Override
    public AnimationState bs$getWalkUnderWaterAnimationState() {
        return bs$walkUnderWaterAnimationState;
    }

    @Override
    public AnimationState bs$getIdleUnderWaterAnimationState() {
        return bs$idleUnderWaterAnimationState;
    }

    @Override
    public AnimationState bs$getIdleUnderWaterOnGroundAnimationState() {
        return bs$idleUnderWaterOnGroundAnimationState;
    }

    @Override
    public AnimationState bs$getIdleOnGroundAnimationState() {
        return bs$idleOnGroundAnimationState;
    }

    @Override
    public AnimationState bs$getPlayDeadAnimationState() {
        return bs$playDeadAnimationState;
    }
}
