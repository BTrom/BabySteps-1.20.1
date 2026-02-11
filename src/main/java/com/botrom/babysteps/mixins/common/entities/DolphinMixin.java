package com.botrom.babysteps.mixins.common.entities;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Dolphin.class)
public abstract class DolphinMixin extends WaterAnimal {

    protected DolphinMixin(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return this.isBaby() ? size.height * 0.5F : super.getStandingEyeHeight(pose, size);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }
}
