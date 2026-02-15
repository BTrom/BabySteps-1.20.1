package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyPolarBearModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;

import java.util.Optional;

public class BabyPolarBearRenderer {

    public static final ModelLayerLocation BABY_POLAR_BEAR = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "polar_bear_baby"), "main");

    private static final ResourceLocation POLAR_BEAR_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/bear/polarbear_baby.png");

    protected final BabyPolarBearModel<PolarBear> babyModel;

    public BabyPolarBearRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyPolarBearModel<>(context.bakeLayer(BABY_POLAR_BEAR));
    }

    public Optional<PolarBearModel<PolarBear>> bakeModels(PolarBear entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPolarBear) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(PolarBear entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPolarBear) {
            return POLAR_BEAR_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<PolarBearModel<PolarBear>> getModel(PolarBear entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPolarBear) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
