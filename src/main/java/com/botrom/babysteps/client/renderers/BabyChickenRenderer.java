package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyChickenModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

import java.util.Optional;

public class BabyChickenRenderer {

    public static final ModelLayerLocation BABY_CHICKEN = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "baby_chicken"), "main");

    private static final ResourceLocation VANILLA_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/chicken/baby_chicken.png");

    protected final BabyChickenModel<Chicken> babyModel;

    public BabyChickenRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyChickenModel<>(context.bakeLayer(BABY_CHICKEN));
    }

    public Optional<ChickenModel<Chicken>> bakeModels(Chicken entity) {
        if (entity.isBaby() && BabyConfig.enableBabyChicken) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Chicken entity) {
        if (entity.isBaby() && BabyConfig.enableBabyChicken) {
            return VANILLA_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<ChickenModel<Chicken>> getModel(Chicken entity) {
        if (entity.isBaby() && BabyConfig.enableBabyChicken) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}