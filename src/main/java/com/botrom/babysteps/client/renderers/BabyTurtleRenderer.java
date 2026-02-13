package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyTurtleModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.TurtleModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Turtle;

import java.util.Optional;

public class BabyTurtleRenderer {

    public static final ModelLayerLocation BABY_TURTLE = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "turtle_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/turtle/turtle_baby.png");

    protected final BabyTurtleModel<Turtle> babyModel;

    public BabyTurtleRenderer (EntityRendererProvider.Context context) {
        this.babyModel = new BabyTurtleModel<>(context.bakeLayer(BABY_TURTLE));
    }

    public Optional<TurtleModel<Turtle>> bakeModels(Turtle entity) {
        if (entity.isBaby() && BabyConfig.enableBabyTurtle) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Turtle entity) {
        if (entity.isBaby() && BabyConfig.enableBabyTurtle) {
            return BABY_TEXTURE;
        }
        return null;
    }

    public Optional<TurtleModel<Turtle>> getModel(Turtle entity) {
        if (entity.isBaby() && BabyConfig.enableBabyTurtle) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
