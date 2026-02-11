package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyDolphinModel;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Dolphin;

import java.util.Optional;

public class BabyDolphinRenderer {

    public static final ModelLayerLocation BABY_DOLPHIN = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "dolphin_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/dolphin/dolphin_baby.png");

    protected final BabyDolphinModel<Dolphin> babyModel;

    public BabyDolphinRenderer (EntityRendererProvider.Context context) {
        this.babyModel = new BabyDolphinModel<>(context.bakeLayer(BABY_DOLPHIN));
    }

    public Optional<DolphinModel<Dolphin>> bakeModels(Dolphin entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Dolphin entity) {
        if (entity.isBaby()) {
            return BABY_TEXTURE;
        }
        return null;
    }

    public Optional<DolphinModel<Dolphin>> getModel(Dolphin entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
