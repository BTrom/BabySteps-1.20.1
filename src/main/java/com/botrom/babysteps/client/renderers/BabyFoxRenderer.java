package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyFoxModel;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;

import java.util.Optional;

public class BabyFoxRenderer {

    public static final ModelLayerLocation BABY_FOX = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "fox_baby"), "main");

    private static final ResourceLocation FOX_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/fox/fox_baby.png");
    private static final ResourceLocation FOX_SLEEP_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/fox/fox_sleep_baby.png");
    private static final ResourceLocation FOX_SNOW_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/fox/fox_snow_baby.png");
    private static final ResourceLocation FOX_SNOW_SLEEP_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/fox/fox_snow_sleep_baby.png");

    protected final BabyFoxModel<Fox> babyModel;

    public BabyFoxRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyFoxModel<>(context.bakeLayer(BABY_FOX));
    }

    public Optional<FoxModel<Fox>> bakeModels(Fox entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Fox entity) {
        if (entity.isBaby()) {
            if (entity.getVariant().getId() == 1) {
                if (entity.isSleeping()) {
                    return FOX_SNOW_SLEEP_BABY_TEXTURE;
                } else {
                    return FOX_SNOW_BABY_TEXTURE;
                }
            } else {
                if (entity.isSleeping()) {
                    return FOX_SLEEP_BABY_TEXTURE;
                } else {
                    return FOX_BABY_TEXTURE;
                }
            }
        }
        return null;
    }

    public Optional<FoxModel<Fox>> getModel(Fox entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
