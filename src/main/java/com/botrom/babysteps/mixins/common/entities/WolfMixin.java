package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob {

    protected WolfMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

//    private Holder<WolfSoundVariant> getSoundVariant() {
//        return (Holder)this.entityData.get(DATA_SOUND_VARIANT_ID);
//    }

//    private WolfSoundVariant.WolfSoundSet getSoundSet() {
//        return this.isBaby() ? ((WolfSoundVariant)this.getSoundVariant().value()).babySounds() : ((WolfSoundVariant)this.getSoundVariant().value()).adultSounds();
//    }

//    private void setSoundVariant(final Holder<WolfSoundVariant> soundVariant) {
//        this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
//    }

//    This one also depends on a custom data serializer for the sounds

//    public <T> @Nullable T get(final DataComponentType<? extends T> type) {
//        if (type == DataComponents.WOLF_VARIANT) {
//            return castComponentValue(type, this.getVariant());
//        } else if (type == DataComponents.WOLF_SOUND_VARIANT) {
//            return castComponentValue(type, this.getSoundVariant());
//        } else {
//            return type == DataComponents.WOLF_COLLAR ? castComponentValue(type, this.getCollarColor()) : super.get(type);
//        }
//    }


    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound(this.isBaby() ? BabySteps.BabySounds.WOLF_STEP_BABY.get() : SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return this.isBaby() ? BabySteps.BabySounds.WOLF_GROWL_BABY.get() : SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) != 0) {
            return this.isBaby() ? BabySteps.BabySounds.WOLF_AMBIENT_BABY.get() : SoundEvents.WOLF_AMBIENT;
        } else {
            if (this.isTame() && this.getHealth() < 10.0F)
                return this.isBaby() ? BabySteps.BabySounds.WOLF_WHINE_BABY.get() : SoundEvents.WOLF_WHINE;
            else
                return this.isBaby() ? BabySteps.BabySounds.WOLF_PANT_BABY.get() : SoundEvents.WOLF_PANT;
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return this.isBaby() ? BabySteps.BabySounds.WOLF_HURT_BABY.get() : SoundEvents.WOLF_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? BabySteps.BabySounds.WOLF_DEATH_BABY.get() : SoundEvents.WOLF_DEATH;
    }


}
