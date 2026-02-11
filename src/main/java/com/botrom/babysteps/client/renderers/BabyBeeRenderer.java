package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyBeeModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

import java.util.Optional;

public class BabyBeeRenderer {

    public static final ModelLayerLocation BABY_BEE = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "bee_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/bee/bee_baby.png");
    private static final ResourceLocation ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/bee/bee_angry_baby.png");
    private static final ResourceLocation ANGRY_BABY_NECTAR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/bee/bee_angry_nectar_baby.png");
    private static final ResourceLocation BABY_NECTAR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/bee/bee_nectar_baby.png");

    protected final BabyBeeModel<Bee> babyModel;

    public BabyBeeRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyBeeModel<>(context.bakeLayer(BABY_BEE));
    }

    public Optional<BeeModel<Bee>> bakeModels(Bee entity) {
        if (entity.isBaby() && BabyConfig.enableBabyBee) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Bee entity) {
        if (entity.isBaby() && BabyConfig.enableBabyBee) {
            if (entity.isAngry()) {
                return entity.hasNectar() ? ANGRY_BABY_NECTAR_TEXTURE : ANGRY_BABY_TEXTURE;
            } else {
                return entity.hasNectar() ? BABY_NECTAR_TEXTURE : BABY_TEXTURE;
            }
        }
        return null;
    }

    public Optional<BeeModel<Bee>> getModel(Bee entity) {
        if (entity.isBaby() && BabyConfig.enableBabyBee) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
