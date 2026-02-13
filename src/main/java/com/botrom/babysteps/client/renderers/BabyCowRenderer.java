package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyCowModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

import java.util.Optional;

public class BabyCowRenderer {
    public static final ModelLayerLocation BABY_COW = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "baby_cow"), "main");

    private static final ResourceLocation VANILLA_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cow/baby_cow.png");

    protected final BabyCowModel<Cow> babyModel;

    public BabyCowRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyCowModel<>(context.bakeLayer(BABY_COW));
    }

    public Optional<CowModel<Cow>> bakeModels(Cow entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCow) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Cow entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCow) {
            return VANILLA_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<CowModel<Cow>> getModel(Cow entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCow) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}