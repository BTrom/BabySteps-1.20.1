package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyPigModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

import java.util.Optional;

public class BabyPigRenderer {

    public static final ModelLayerLocation BABY_PIG = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "baby_pig"), "main");

    private static final ResourceLocation VANILLA_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/pig/baby_pig.png");

    protected final BabyPigModel<Pig> babyModel;

    public BabyPigRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyPigModel<>(context.bakeLayer(BABY_PIG));
    }

    public Optional<PigModel<Pig>> bakeModels(Pig entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPig) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Pig entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPig) {
            return VANILLA_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<PigModel<Pig>> getModel(Pig entity) {
        if (entity.isBaby() && BabyConfig.enableBabyPig) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}