package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Dolphin.class)
public abstract class DolphinMixin extends WaterAnimal {

    protected DolphinMixin(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return (this.isBaby() && BabyConfig.enableBabyDolphin) ? size.height * 0.5F : super.getStandingEyeHeight(pose, size);
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return BabyConfig.enableBabyDolphin || super.canBeLeashed(player);
    }
}
