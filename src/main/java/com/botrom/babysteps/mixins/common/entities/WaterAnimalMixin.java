package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.utils.BabyConfig;
import com.botrom.babysteps.utils.IAgeableMob;
import com.botrom.babysteps.utils.IAgeableWaterCreature;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(WaterAnimal.class)
public abstract class WaterAnimalMixin extends PathfinderMob implements IAgeableWaterCreature, IAgeableMob {

    @Unique private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(WaterAnimal.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(WaterAnimal.class, EntityDataSerializers.BOOLEAN);

    @Unique private static final int FORCED_AGE_PARTICLE_TICKS = 40;

    @Unique private int age = 0;
    @Unique private int forcedAge = 0;
    @Unique private int forcedAgeTimer;
    @Unique private int ageLockParticleTimer = 0;
    @Unique private boolean isLockingAction = true;
    @Unique private int inLove = 0;
    @Unique private @Nullable UUID loveCause;

    protected WaterAnimalMixin(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData groupData, @Nullable CompoundTag dataTag) {
        if (this.bs$isBabyConfigEnabled() && this.getType().is(BabySteps.BabyTags.AGEABLE_WATER_CREATURE)) {
            if (groupData == null) {
                groupData = new AgeableMob.AgeableMobGroupData(true);
            }
            if (groupData instanceof AgeableMob.AgeableMobGroupData ageableData) {
                if (ageableData.isShouldSpawnBaby() && ageableData.getGroupSize() > 0 && level.getRandom().nextFloat() <= ageableData.getBabySpawnChance()) {
                    this.setAge(AgeableMob.BABY_START_AGE);
                }
                ageableData.increaseGroupSizeByOne();
            }
        }
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData, dataTag);
    }

    @Override
    @Unique
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    @Unique
    public boolean canBreed() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.bs$isBabyConfigEnabled() || !this.getType().is(BabySteps.BabyTags.AGEABLE_WATER_CREATURE)) return;

        if (this.getAge() != 0) {
            this.inLove = 0;
        }

        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        }

        if (this.level().isClientSide()) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                }
                --this.forcedAgeTimer;
            }

            if (this.ageLockParticleTimer > 0) {
                if (this.ageLockParticleTimer % 4 == 0) {
                    SimpleParticleType particle = this.isLockingAction ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SMOKE;
                    this.level().addParticle(particle, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                }
                --this.ageLockParticleTimer;
            }

        } else if (this.isAlive() && !this.isAgeLocked()) {
            int age = this.getAge();
            if (age < 0) {
                ++age;
                this.setAge(age);
            } else if (age > 0) {
                --age;
                this.setAge(age);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.inLove = 0;
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_BABY_ID, false);
        entityData.define(AGE_LOCKED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Age", this.getAge());
        tag.putInt("ForcedAge", this.forcedAge);
        tag.putBoolean("AgeLocked", this.isAgeLocked());
        tag.putInt("InLove", this.inLove);
        if (this.loveCause != null) {
            tag.putUUID("LoveCause", this.loveCause);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAge(tag.contains("Age") ? tag.getInt("Age") : 0);
        this.forcedAge = tag.contains("ForcedAge") ? tag.getInt("ForcedAge") : 0;
        this.setAgeLocked(tag.contains("AgeLocked") && tag.getBoolean("AgeLocked"));
        this.inLove = tag.contains("InLove") ? tag.getInt("InLove") : 0;
        this.loveCause = tag.hasUUID("LoveCause") ? tag.getUUID("LoveCause") : null;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if (DATA_BABY_ID.equals(accessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    @Unique
    public int getAge() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DATA_BABY_ID) ? -1 : 1;
        } else {
            return this.age;
        }
    }

    @Override
    @Unique
    public void setAge(int newAge) {
        int oldAge = this.getAge();
        this.age = newAge;
        if (oldAge < 0 && newAge >= 0 || oldAge >= 0 && newAge < 0) {
            this.entityData.set(DATA_BABY_ID, newAge < 0);
            this.ageBoundaryReached();
        }
    }

    @Override
    @Unique
    public boolean isBabyMixin() {
        return this.isBaby();
    }

    @Override
    @Unique
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    @Unique
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
    }

    @Override
    @Unique
    public void ageBoundaryReached() {
        if (!this.isBaby() && this.isPassenger()) {
            if (this.getVehicle() instanceof Boat boat && !boat.hasEnoughSpaceFor(this)) {
                this.stopRiding();
            }
        }
    }

    @Override
    @Unique
    public boolean canAgeUp() {
        return this.isBaby() && !this.isAgeLocked();
    }

    @Override
    @Unique
    public void ageUp(int seconds, boolean forced) {
        int age = this.getAge();
        int oldAge = age;
        age += seconds * 20;
        if (age > 0) {
            age = 0;
        }

        int delta = age - oldAge;
        this.setAge(age);
        if (forced) {
            this.forcedAge += delta;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = FORCED_AGE_PARTICLE_TICKS;
            }
        }

        if (this.getAge() == 0) {
            this.setAge(this.forcedAge);
        }
    }

    @Override
    @Unique
    public void ageUp(int seconds) {
        this.ageUp(seconds, false);
    }

    @Unique
    private static int getSpeedUpSecondsWhenFeeding(int ticksUntilAdult) {
        return (int)((float)(ticksUntilAdult / 20) * 0.1F);
    }

    @Override
    @Unique
    public boolean isAgeLocked() {
        return this.entityData.get(AGE_LOCKED);
    }

    @Override
    @Unique
    public void setAgeLocked(boolean locked) {
        this.entityData.set(AGE_LOCKED, locked);
    }

    @Override
    @Unique
    public void setAgeLocked(Mob mob, Supplier<Boolean> isAgedLocked, Player player, ItemStack itemInHand, Consumer<Mob> setAgeLockData) {
        setAgeLockData.accept(mob);
        if (!player.getAbilities().instabuild)
            itemInHand.shrink(1);
        mob.level().playSound(null, mob.blockPosition(), isAgedLocked.get() ? BabySteps.BabySounds.GOLDEN_DANDELION_USE.get() : BabySteps.BabySounds.GOLDEN_DANDELION_UNUSE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    @Unique
    public void setAgeLockedData() {
        this.setAgeLocked(!this.isAgeLocked());
        this.setAge(AgeableMob.BABY_START_AGE);
    }

    @Override
    @Unique
    public void triggerAgeLockEffect(boolean locking) {
        this.ageLockParticleTimer = FORCED_AGE_PARTICLE_TICKS;
        this.isLockingAction = locking;
    }

    @Override
    @Unique
    public int getAgeLockParticleTimer() {
        return this.ageLockParticleTimer;
    }

    @Override
    @Unique
    public boolean canFallInLove() {
        return this.inLove <= 0;
    }

    @Override
    @Unique
    public void setInLove(@Nullable Player pPlayer) {
        this.inLove = 600;
        if (pPlayer != null) {
            this.loveCause = pPlayer.getUUID();
        }
        this.level().broadcastEntityEvent(this, (byte)18);
    }

    @Override
    @Unique
    public void setInLoveTime(int pInLove) {
        this.inLove = pInLove;
    }

    @Override
    @Unique
    public int getInLoveTime() {
        return this.inLove;
    }

    @Override
    @Unique
    @Nullable
    public ServerPlayer getLoveCause() {
        if (this.loveCause == null) {
            return null;
        } else {
            Player player = this.level().getPlayerByUUID(this.loveCause);
            return player instanceof ServerPlayer ? (ServerPlayer)player : null;
        }
    }

    @Override
    @Unique
    public boolean isInLove() {
        return this.inLove > 0;
    }

    @Override
    @Unique
    public void resetLove() {
        this.inLove = 0;
    }

    @Override
    @Unique
    public boolean canMate(Mob otherMob) {
        if (otherMob.equals(this)) {
            return false;
        } else if (otherMob.getClass() != this.getClass()) {
            return false;
        } else {
            WaterAnimalMixin otherAnimal = (WaterAnimalMixin) otherMob;
            return this.isInLove() && otherAnimal.isInLove();
        }
    }

    @Override
    @Unique
    public void spawnChildFromBreeding(ServerLevel level, Animal mate) {
        AgeableMob ageableMob = this.getBreedOffspring(level, mate);
        BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, mate, ageableMob);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        ageableMob = event.getChild();
        if (cancelled) {
            this.setAge(Animal.PARENT_AGE_AFTER_BREEDING);
            mate.setAge(Animal.PARENT_AGE_AFTER_BREEDING);
            this.resetLove();
            mate.resetLove();
        } else {
            if (ageableMob != null) {
                ageableMob.setBaby(true);
                ageableMob.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                this.finalizeSpawnChildFromBreeding(level, mate, ageableMob);
                level.addFreshEntityWithPassengers(ageableMob);
            }
        }
    }

    @Override
    @Unique
    public void finalizeSpawnChildFromBreeding(ServerLevel level, Animal partner, @Nullable AgeableMob offspring) {
        Optional.ofNullable(this.getLoveCause()).or(
                () -> Optional.ofNullable(partner.getLoveCause())
        ).ifPresent((cause) -> {
            cause.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(cause, (Animal)(Mob) this, partner, offspring);
        });
        this.setAge(Animal.PARENT_AGE_AFTER_BREEDING);
        partner.setAge(Animal.PARENT_AGE_AFTER_BREEDING);
        this.resetLove();
        partner.resetLove();
        level.broadcastEntityEvent(this, (byte)18);
        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            level.addFreshEntity(new ExperienceOrb(level, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Unique
    private boolean bs$isBabyConfigEnabled() {
        if (this.getType() == EntityType.DOLPHIN) {
            return BabyConfig.enableBabyDolphin;
        }
        if (this.getType() == EntityType.SQUID) {
            return BabyConfig.enableBabySquid;
        }
        // Fallback: If it's in the tag but not a specific vanilla mob (e.g. modded), allow it.
        return false;
    }
}
