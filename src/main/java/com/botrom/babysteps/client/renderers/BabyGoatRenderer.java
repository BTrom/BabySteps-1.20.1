package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyGoatModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.GoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.goat.Goat;

import java.util.Optional;

public class BabyGoatRenderer {

    public static final ModelLayerLocation BABY_GOAT = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "goat_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/goat/goat_baby.png");

    protected final BabyGoatModel<Goat> babyModel;

    public BabyGoatRenderer (EntityRendererProvider.Context context) {
        this.babyModel = new BabyGoatModel<>(context.bakeLayer(BABY_GOAT));
    }

    public Optional<GoatModel<Goat>> bakeModels(Goat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyGoat) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Goat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyGoat) {
            return BABY_TEXTURE;
        }
        return null;
    }

    public Optional<GoatModel<Goat>> getModel(Goat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyGoat) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
