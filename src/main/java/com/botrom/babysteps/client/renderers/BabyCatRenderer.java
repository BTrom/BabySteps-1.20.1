package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyCatModel;
import com.botrom.babysteps.client.models.BabyOcelotModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;

import java.util.Optional;

public class BabyCatRenderer {

    public static final ModelLayerLocation BABY_CAT = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "cat_baby"), "main");
    public static final ModelLayerLocation BABY_OCELOT = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "ocelot_baby"), "main");

    private static final ResourceLocation CAT_ALL_BLACK_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_all_black_baby.png");
    private static final ResourceLocation CAT_BLACK_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_black_baby.png");
    private static final ResourceLocation CAT_BRITISH_SHORTHAIR_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_british_shorthair_baby.png");
    private static final ResourceLocation CAT_JELLIE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_jellie_baby.png");
    private static final ResourceLocation CAT_PERSIAN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_persian_baby.png");
    private static final ResourceLocation CAT_RAGDOLL_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_ragdoll_baby.png");
    private static final ResourceLocation CAT_RED_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_red_baby.png");
    private static final ResourceLocation CAT_SIAMESE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_siamese_baby.png");
    private static final ResourceLocation CAT_TABBY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_tabby_baby.png");
    private static final ResourceLocation CAT_WHITE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_white_baby.png");
    private static final ResourceLocation OCELOT_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/ocelot_baby.png");
    public static final ResourceLocation CAT_COLLAR_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/cat/cat_collar_baby.png");

    protected final BabyCatModel<Cat> babyCatModel;
    protected final BabyOcelotModel<Ocelot> babyOcelotModel;

    public BabyCatRenderer(EntityRendererProvider.Context context) {
        this.babyCatModel = new BabyCatModel<>(context.bakeLayer(BABY_CAT));
        this.babyOcelotModel = new BabyOcelotModel<>(context.bakeLayer(BABY_OCELOT));
    }

    public Optional<CatModel<Cat>> bakeModels(Cat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCat) {
            return Optional.of(this.babyCatModel);
        }
        return Optional.empty();
    }

    public Optional<OcelotModel<Ocelot>> bakeModels(Ocelot entity) {
        if (entity.isBaby() && BabyConfig.enableBabyOcelot) {
            return Optional.of(this.babyOcelotModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Cat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCat) {
            ResourceLocation vanillaTexture = entity.getVariant().texture();
            String path = vanillaTexture.getPath();

            if (path.endsWith("all_black.png")) return CAT_ALL_BLACK_BABY_TEXTURE;
            if (path.endsWith("black.png") && !path.endsWith("all_black.png")) return CAT_BLACK_BABY_TEXTURE;
            if (path.endsWith("british_shorthair.png")) return CAT_BRITISH_SHORTHAIR_BABY_TEXTURE;
            if (path.endsWith("jellie.png")) return CAT_JELLIE_BABY_TEXTURE;
            if (path.endsWith("persian.png")) return CAT_PERSIAN_BABY_TEXTURE;
            if (path.endsWith("ragdoll.png")) return CAT_RAGDOLL_BABY_TEXTURE;
            if (path.endsWith("red.png")) return CAT_RED_BABY_TEXTURE;
            if (path.endsWith("siamese.png")) return CAT_SIAMESE_BABY_TEXTURE;
            if (path.endsWith("tabby.png")) return CAT_TABBY_BABY_TEXTURE;
            if (path.endsWith("white.png")) return CAT_WHITE_BABY_TEXTURE;

            return CAT_BLACK_BABY_TEXTURE;
        }
        return null;
    }

    public ResourceLocation getTexture(Ocelot entity) {
        if (entity.isBaby() && BabyConfig.enableBabyOcelot) {
            return OCELOT_BABY_TEXTURE;
        }
        return null;
    }

    public Optional<CatModel<Cat>> getModel(Cat entity) {
        if (entity.isBaby() && BabyConfig.enableBabyCat) {
            return Optional.of(this.babyCatModel);
        }
        return Optional.empty();
    }

    public Optional<OcelotModel<Ocelot>> getModel(Ocelot entity) {
        if (entity.isBaby() && BabyConfig.enableBabyOcelot) {
            return Optional.of(this.babyOcelotModel);
        }
        return Optional.empty();
    }
}