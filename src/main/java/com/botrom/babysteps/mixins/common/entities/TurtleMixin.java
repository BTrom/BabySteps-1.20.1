package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Turtle.class)
public abstract class TurtleMixin extends Animal {

    protected TurtleMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isBaby() ? BabySteps.BabySounds.TURTLE_HURT_BABY.get() : SoundEvents.TURTLE_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? BabySteps.BabySounds.TURTLE_DEATH_BABY.get() : SoundEvents.TURTLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.isBaby() ? BabySteps.BabySounds.TURTLE_SHAMBLE_BABY.get() : SoundEvents.TURTLE_SHAMBLE, 0.15F, 1.0F);
    }
}
