package com.botrom.babysteps.mixins.common.entities;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Squid.class)
public class SquidMixin extends WaterAnimal {

    @Unique private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.5F, 0.63F);
    @Unique private static final float BABY_EYE_HEIGHT = 0.37F;

    protected SquidMixin(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return this.isBaby() ? size.height * 0.5F : super.getStandingEyeHeight(pose, size);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        if (this.isBaby()) {
            return BABY_DIMENSIONS;
        }
        return super.getDimensions(pose);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }
}
