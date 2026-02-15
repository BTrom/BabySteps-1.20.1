package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyDonkeyModel;
import com.botrom.babysteps.client.models.BabyHorseModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.*;

import java.util.Optional;

public class BabyHorseRenderer {

    public static final ModelLayerLocation BABY_HORSE = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "horse_baby"), "main");
    public static final ModelLayerLocation BABY_DONKEY = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "donkey_baby"), "main");

    private static final ResourceLocation DONKEY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/donkey_baby.png");
    private static final ResourceLocation HORSE_BLACK_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_black_baby.png");
    private static final ResourceLocation HORSE_BROWN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_brown_baby.png");
    private static final ResourceLocation HORSE_CHESTNUT_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_chestnut_baby.png");
    private static final ResourceLocation HORSE_CREAMY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_creamy_baby.png");
    private static final ResourceLocation HORSE_DARKBROWN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_darkbrown_baby.png");
    private static final ResourceLocation HORSE_GRAY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_gray_baby.png");
    private static final ResourceLocation HORSE_WHITE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_white_baby.png");
    private static final ResourceLocation HORSE_SKELETON_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_skeleton_baby.png");
    private static final ResourceLocation HORSE_ZOMBIE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_zombie_baby.png");
    private static final ResourceLocation MULE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/mule_baby.png");
    public static final ResourceLocation HORSE_MARKINGS_BLACKDOTS_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_markings_blackdots_baby.png");
    public static final ResourceLocation HORSE_MARKINGS_WHITE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_markings_white_baby.png");
    public static final ResourceLocation HORSE_MARKINGS_WHITEDOTS_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_markings_whitedots_baby.png");
    public static final ResourceLocation HORSE_MARKINGS_WHITEFIELD_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/horse/horse_markings_whitefield_baby.png");

    protected final BabyHorseModel<AbstractHorse> horseBabyModel;
    protected final BabyDonkeyModel<AbstractChestedHorse> donkeyBabyModel;

    public BabyHorseRenderer(EntityRendererProvider.Context context) {
        this.horseBabyModel = new BabyHorseModel<>(context.bakeLayer(BABY_HORSE));
        this.donkeyBabyModel = new BabyDonkeyModel<>(context.bakeLayer(BABY_DONKEY));
    }

    public ResourceLocation getTexture(AbstractHorse entity) {
        if (entity.isBaby()) {
            if (entity instanceof Horse horse && BabyConfig.enableBabyHorse) {
                return switch (horse.getVariant().getId()) {
                    case 1 -> HORSE_CREAMY_BABY_TEXTURE;
                    case 2 -> HORSE_CHESTNUT_BABY_TEXTURE;
                    case 3 -> HORSE_BROWN_BABY_TEXTURE;
                    case 4 -> HORSE_BLACK_BABY_TEXTURE;
                    case 5 -> HORSE_GRAY_BABY_TEXTURE;
                    case 6 -> HORSE_DARKBROWN_BABY_TEXTURE;
                    default -> HORSE_WHITE_BABY_TEXTURE;
                };
            } else if (entity instanceof SkeletonHorse && BabyConfig.enableBabySkeletonHorse) {
                return HORSE_SKELETON_BABY_TEXTURE;
            } else if (entity instanceof ZombieHorse && BabyConfig.enableBabyZombieHorse) {
                return HORSE_ZOMBIE_BABY_TEXTURE;
            } else if (entity instanceof Donkey && BabyConfig.enableBabyDonkey) {
                return DONKEY_BABY_TEXTURE;
            } else if (entity instanceof Mule && BabyConfig.enableBabyMule) {
                return MULE_BABY_TEXTURE;
            }
        }
        return null;
    }

    public Optional<HorseModel<AbstractHorse>> getModel(AbstractHorse entity) {
        if (entity.isBaby() && ((entity instanceof Horse && BabyConfig.enableBabyHorse) || (entity instanceof SkeletonHorse && BabyConfig.enableBabySkeletonHorse) || (entity instanceof ZombieHorse && BabyConfig.enableBabyZombieHorse))) {
            return Optional.of(this.horseBabyModel);
        }
        return Optional.empty();
    }

    public Optional<ChestedHorseModel<AbstractChestedHorse>> getModel(AbstractChestedHorse entity) {
        boolean isDonkey = entity instanceof Donkey && BabyConfig.enableBabyDonkey;
        boolean isMule = entity instanceof Mule && BabyConfig.enableBabyMule;
        if (entity.isBaby() && (isDonkey || isMule)) {
            return Optional.of(this.donkeyBabyModel);
        }
        return Optional.empty();
    }
}
