package com.botrom.babysteps.utils;

import net.minecraft.world.entity.AnimationState;

public interface IRabbitAnimationAccess {
    AnimationState bs$getHopAnimationState();
    AnimationState bs$getIdleHeadTiltAnimationState();
    float bs$getJumpAnimationSpeed();
}