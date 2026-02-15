package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyCamelModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.camel.Camel;

import java.util.Optional;

public class BabyCamelRenderer {

    public static final ModelLayerLocation BABY_CAMEL = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "camel_baby"), "main");

    private static final ResourceLocation CAMEL_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/camel/camel_baby.png");

    protected final BabyCamelModel<Camel> babyModel;

    public BabyCamelRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyCamelModel<>(context.bakeLayer(BABY_CAMEL));
    }

    public Optional<CamelModel<Camel>> bakeModels(Camel entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCamel) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Camel entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCamel) {
            return CAMEL_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<CamelModel<Camel>> getModel(Camel entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCamel) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
