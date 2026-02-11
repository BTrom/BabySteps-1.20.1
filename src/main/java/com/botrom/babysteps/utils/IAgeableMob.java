package com.botrom.babysteps.utils;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IAgeableMob {

    boolean isBabyMixin();
    boolean isAgeLocked();
    int getAgeLockParticleTimer();

    void setAgeLocked(boolean locked);
    void setAgeLocked(final Mob mob, final Supplier<Boolean> isAgedLocked, final Player player, final ItemStack itemInHand, final Consumer<Mob> setAgeLockData);
    void setAgeLockedData();
    void triggerAgeLockEffect(boolean locking);

}