package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyWolfModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

import java.util.Optional;

public class BabyWolfRenderer {

    public static final ModelLayerLocation BABY_WOLF = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "wolf_baby"), "main");

    public static final ResourceLocation WOLF_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_baby.png");
    public static final ResourceLocation WOLF_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_angry_baby.png");
    public static final ResourceLocation WOLF_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_tame_baby.png");
    public static final ResourceLocation WOLF_ASHEN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_ashen_baby.png");
    public static final ResourceLocation WOLF_ASHEN_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_ashen_angry_baby.png");
    public static final ResourceLocation WOLF_ASHEN_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_ashen_tame_baby.png");
    public static final ResourceLocation WOLF_BLACK_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_black_baby.png");
    public static final ResourceLocation WOLF_BLACK_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_black_angry_baby.png");
    public static final ResourceLocation WOLF_BLACK_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_black_tame_baby.png");
    public static final ResourceLocation WOLF_CHESTNUT_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_chestnut_baby.png");
    public static final ResourceLocation WOLF_CHESTNUT_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_chestnut_angry_baby.png");
    public static final ResourceLocation WOLF_CHESTNUT_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_chestnut_tame_baby.png");
    public static final ResourceLocation WOLF_RUSTY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_rusty_baby.png");
    public static final ResourceLocation WOLF_RUSTY_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_rusty_angry_baby.png");
    public static final ResourceLocation WOLF_RUSTY_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_rusty_tame_baby.png");
    public static final ResourceLocation WOLF_SNOWY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_snowy_baby.png");
    public static final ResourceLocation WOLF_SNOWY_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_snowy_angry_baby.png");
    public static final ResourceLocation WOLF_SNOWY_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_snowy_tame_baby.png");
    public static final ResourceLocation WOLF_SPOTTED_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_spotted_baby.png");
    public static final ResourceLocation WOLF_SPOTTED_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_spotted_angry_baby.png");
    public static final ResourceLocation WOLF_SPOTTED_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_spotted_tame_baby.png");
    public static final ResourceLocation WOLF_STRIPED_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_striped_baby.png");
    public static final ResourceLocation WOLF_STRIPED_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_striped_angry_baby.png");
    public static final ResourceLocation WOLF_STRIPED_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_striped_tame_baby.png");
    public static final ResourceLocation WOLF_WOODS_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_woods_baby.png");
    public static final ResourceLocation WOLF_WOODS_ANGRY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_woods_angry_baby.png");
    public static final ResourceLocation WOLF_WOODS_TAME_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_woods_tame_baby.png");
    public static final ResourceLocation WOLF_COLLAR_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/wolf/wolf_collar_baby.png");

    protected final BabyWolfModel<Wolf> babyModel;

    public BabyWolfRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyWolfModel<>(context.bakeLayer(BABY_WOLF));
    }

    public Optional<WolfModel<Wolf>> bakeModels(Wolf entity) {
        if (entity.isBaby() && BabyConfig.enableBabyWolf) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Wolf entity) {
        if (entity.isBaby() && BabyConfig.enableBabyWolf) {
            if (entity.isAngry())
                return WOLF_ANGRY_BABY_TEXTURE;
            else if (entity.isTame())
                return WOLF_TAME_BABY_TEXTURE;
            else
                return WOLF_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<WolfModel<Wolf>> getModel(Wolf entity) {
        if (entity.isBaby() && BabyConfig.enableBabyWolf) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
