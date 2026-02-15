package com.botrom.babysteps.utils;

import net.minecraft.world.entity.AnimationState;

public interface IAxolotlAnimationAccess {
    AnimationState bs$getSwimAnimationState();
    AnimationState bs$getWalkAnimationState();
    AnimationState bs$getWalkUnderWaterAnimationState();
    AnimationState bs$getIdleUnderWaterAnimationState();
    AnimationState bs$getIdleUnderWaterOnGroundAnimationState();
    AnimationState bs$getIdleOnGroundAnimationState();
    AnimationState bs$getPlayDeadAnimationState();
}
