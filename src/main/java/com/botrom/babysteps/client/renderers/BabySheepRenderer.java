package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabySheepModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

import java.util.Optional;

public class BabySheepRenderer {

    public static final ModelLayerLocation BABY_SHEEP = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "sheep_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/sheep/sheep_baby.png");
    public static final ResourceLocation BABY_WOOL_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/sheep/sheep_wool_baby.png");

    protected final BabySheepModel<Sheep> babyModel;

    public BabySheepRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabySheepModel<>(context.bakeLayer(BABY_SHEEP));
    }

    public Optional<SheepModel<Sheep>> bakeModels(Sheep entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Sheep entity) {
        if (entity.isBaby()) {
            return BABY_TEXTURE;
        }
        return null;
    }

    public Optional<SheepModel<Sheep>> getModel(Sheep entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
