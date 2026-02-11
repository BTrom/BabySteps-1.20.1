package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal implements VariantHolder<CatVariant> {

    @Shadow @Nullable private TemptGoal temptGoal;
    @Shadow public abstract boolean isLying();
    @Shadow public abstract boolean isRelaxStateOne();
    @Shadow protected abstract void updateLieDownAmount();
    @Shadow protected abstract void updateRelaxStateOneAmount();

    protected CatMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.isTame()) {
            if (this.isInLove()) {
                return this.isBaby() ? BabySteps.BabySounds.CAT_PURR_BABY.get() : SoundEvents.CAT_PURR;
            } else if (this.random.nextInt(4) == 0) {
                return this.isBaby() ? BabySteps.BabySounds.CAT_PURREOW_BABY.get() : SoundEvents.CAT_PURREOW;
            } else {
                return this.isBaby() ? BabySteps.BabySounds.CAT_AMBIENT_BABY.get() : SoundEvents.CAT_AMBIENT;
            }
        } else {
            return this.isBaby() ? BabySteps.BabySounds.CAT_STRAY_AMBIENT_BABY.get() : SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    /**
     * @author Botrom
     * @reason Adding new baby sounds
     */
    @Overwrite
    public void hiss() {
        this.playSound(this.isBaby() ? BabySteps.BabySounds.CAT_HISS_BABY.get() : SoundEvents.CAT_HISS);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return this.isBaby() ? BabySteps.BabySounds.CAT_HURT_BABY.get() : SoundEvents.CAT_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? BabySteps.BabySounds.CAT_DEATH_BABY.get() : SoundEvents.CAT_DEATH;
    }

    @Override
    protected void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack) {
        if (this.isFood(pStack)) {
            this.playSound(this.isBaby() ? BabySteps.BabySounds.CAT_EAT_BABY.get() : SoundEvents.CAT_EAT, 1.0F, 1.0F);
        }

        super.usePlayerItem(pPlayer, pHand, pStack);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTame() && this.tickCount % 100 == 0) {
            this.playSound(this.isBaby() ? BabySteps.BabySounds.CAT_BEG_FOR_FOOD_BABY.get() : SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }
        this.handleLieDown();
    }

    /**
     * @author Botrom
     * @reason Adding new baby sounds
     */
    @Overwrite
    private void handleLieDown() {
        if ((this.isLying() || this.isRelaxStateOne()) && this.tickCount % 5 == 0) {
            this.playSound(this.isBaby() ? BabySteps.BabySounds.CAT_PURR_BABY.get() : SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.updateLieDownAmount();
        this.updateRelaxStateOneAmount();
    }
}