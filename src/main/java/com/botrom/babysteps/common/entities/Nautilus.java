package com.botrom.babysteps.common.entities;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.common.entities.ai.NautilusAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Nautilus extends AbstractNautilus {
	private static final int NAUTILUS_TOTAL_AIR_SUPPLY = 300;
	private static final Ingredient TEMPT_ITEMS = Ingredient.of(BabySteps.BabyTags.NAUTILUS_TAMING_ITEMS);

	public Nautilus(EntityType<? extends Nautilus> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected Brain.Provider<Nautilus> brainProvider() {
		return NautilusAi.brainProvider();
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		return NautilusAi.makeBrain(this.brainProvider().makeBrain(dynamic));
	}

	@Override
	public Brain<Nautilus> getBrain() {
		return (Brain<Nautilus>)super.getBrain();
	}

	public @Nullable Nautilus getBreedOffspring(ServerLevel level, AgeableMob mob) {
		Nautilus nautilus = BabySteps.BabyEntities.NAUTILUS.get().create(level);
		if (nautilus != null && this.isTame()) {
			nautilus.setOwnerUUID(this.getOwnerUUID());
			nautilus.setTame(true);
		}

		return nautilus;
	}

	@Override
	protected void customServerAiStep() {
		ProfilerFiller profilerfiller = this.level().getProfiler();
		
		profilerfiller.push("nautilusBrain");
		if (!this.level().isClientSide)
			this.getBrain().tick((ServerLevel) this.level(), this);
		profilerfiller.pop();
		
		profilerfiller.push("nautilusActivityUpdate");
		NautilusAi.updateActivity(this);
		profilerfiller.pop();
		
		super.customServerAiStep();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		if (this.isBaby()) {
			return this.isUnderWater() ? BabySteps.BabySounds.BABY_NAUTILUS_AMBIENT.get() : BabySteps.BabySounds.BABY_NAUTILUS_AMBIENT_ON_LAND.get();
		} else {
			return this.isUnderWater() ? BabySteps.BabySounds.NAUTILUS_AMBIENT.get() : BabySteps.BabySounds.NAUTILUS_AMBIENT_ON_LAND.get();
		}
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		if (this.isBaby()) {
			return this.isUnderWater() ? BabySteps.BabySounds.BABY_NAUTILUS_HURT.get() : BabySteps.BabySounds.BABY_NAUTILUS_HURT_ON_LAND.get();
		} else {
			return this.isUnderWater() ? BabySteps.BabySounds.NAUTILUS_HURT.get() : BabySteps.BabySounds.NAUTILUS_HURT_ON_LAND.get();
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		if (this.isBaby()) {
			return this.isUnderWater() ? BabySteps.BabySounds.BABY_NAUTILUS_DEATH.get() : BabySteps.BabySounds.BABY_NAUTILUS_DEATH_ON_LAND.get();
		} else {
			return this.isUnderWater() ? BabySteps.BabySounds.NAUTILUS_DEATH.get() : BabySteps.BabySounds.NAUTILUS_DEATH_ON_LAND.get();
		}
	}

	@Override
	protected SoundEvent getDashSound() {
		return this.isUnderWater() ? BabySteps.BabySounds.NAUTILUS_DASH.get() : BabySteps.BabySounds.NAUTILUS_DASH_ON_LAND.get();
	}

	@Override
	protected SoundEvent getDashReadySound() {
		return this.isUnderWater() ? BabySteps.BabySounds.NAUTILUS_DASH_READY.get() : BabySteps.BabySounds.NAUTILUS_DASH_READY_ON_LAND.get();
	}

	@Override
	protected void playEatingSound() {
		SoundEvent soundEvent = this.isBaby() ? BabySteps.BabySounds.BABY_NAUTILUS_EAT.get() : BabySteps.BabySounds.NAUTILUS_EAT.get();
		this.playSound(soundEvent, 1.0F, 1.0F);
	}

	@Override
	protected @NotNull SoundEvent getSwimSound() {
		return this.isBaby() ? BabySteps.BabySounds.BABY_NAUTILUS_SWIM.get() : BabySteps.BabySounds.NAUTILUS_SWIM.get();
	}

	@Override
	public int getMaxAirSupply() {
		return 300;
	}

	protected void handleAirSupply(ServerLevel level, int currentAir) {
		if (this.isAlive() && !this.isInWater()) {
			this.setAirSupply(currentAir - 1);
			if (this.getAirSupply() <= -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().dryOut(), 2.0F);
			}
		} else {
			this.setAirSupply(300);
		}
	}

	@Override
	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		if (!this.isNoAi() && this.level() instanceof ServerLevel serverlevel) {
			this.handleAirSupply(serverlevel, i);
		}
	}

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return !this.isAggravated();
	}

	@Override
	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby() && this.isTame();
	}

	@Override
	public void equipSaddle(@Nullable SoundSource soundSource) {
		this.inventory.setItem(0, new ItemStack(Items.SADDLE));
		if (soundSource != null) {
			this.level().playSound(null, this, this.getSaddleSoundEvent(), soundSource, 0.5f, 1.0f);
		} else {
			this.playSound(this.getSaddleSoundEvent(), 0.5f, 1.0f);
		}
	}

	@Override
	public boolean isSaddled() {
		return this.getFlag(4);
	}
}