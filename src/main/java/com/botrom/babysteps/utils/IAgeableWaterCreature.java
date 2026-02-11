package com.botrom.babysteps.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IAgeableWaterCreature {

    // Aging
    int getAge();
    void setAge(int age);
    boolean isBaby();
    void setBaby(boolean baby);
    void ageUp(int seconds, boolean forced);
    void ageUp(int seconds);
    boolean canAgeUp();
    void ageBoundaryReached();

    // Age Locking
    boolean isAgeLocked();
    void setAgeLocked(boolean locked);
    void setAgeLocked(Mob mob, Supplier<Boolean> isAgedLocked, Player player, ItemStack itemInHand, Consumer<Mob> setAgeLockData);
    void setAgeLockedData();
    void triggerAgeLockEffect(boolean locking);
    int getAgeLockParticleTimer();

    // Breeding
    AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner);
    boolean canBreed();
    boolean canFallInLove();
    void setInLove(Player pPlayer);
    void setInLoveTime(int pInLove);
    int getInLoveTime();
    ServerPlayer getLoveCause();
    boolean isInLove();
    void resetLove();
    boolean canMate(Mob otherMob);
    void spawnChildFromBreeding(ServerLevel level, Animal mate);
    void finalizeSpawnChildFromBreeding(ServerLevel level, Animal partner, AgeableMob offspring);

}