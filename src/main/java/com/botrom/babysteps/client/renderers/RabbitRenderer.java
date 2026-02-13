package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyRabbitModel;
import com.botrom.babysteps.client.models.RabbitModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;

import java.util.Optional;

public class RabbitRenderer {

    public static final ModelLayerLocation ADULT_RABBIT = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "rabbit"), "main");
    public static final ModelLayerLocation BABY_RABBIT = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "rabbit_baby"), "main");

    private static final ResourceLocation RABBIT_BLACK = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_black.png");
    private static final ResourceLocation RABBIT_BLACK_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_black_baby.png");
    private static final ResourceLocation RABBIT_BROWN = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_brown.png");
    private static final ResourceLocation RABBIT_BROWN_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_brown_baby.png");
    private static final ResourceLocation RABBIT_CAERBANNOG = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_caerbannog.png");
    private static final ResourceLocation RABBIT_CAERBANNOG_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_caerbannog_baby.png");
    private static final ResourceLocation RABBIT_GOLD = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_gold.png");
    private static final ResourceLocation RABBIT_GOLD_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_gold_baby.png");
    private static final ResourceLocation RABBIT_SALT = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_salt.png");
    private static final ResourceLocation RABBIT_SALT_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_salt_baby.png");
    private static final ResourceLocation RABBIT_TOAST = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_toast.png");
    private static final ResourceLocation RABBIT_TOAST_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_toast_baby.png");
    private static final ResourceLocation RABBIT_WHITE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_white.png");
    private static final ResourceLocation RABBIT_WHITE_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_white_baby.png");
    private static final ResourceLocation RABBIT_WHITE_SPLOTCHED = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_white_splotched.png");
    private static final ResourceLocation RABBIT_WHITE_SPLOTCHED_BABY = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/rabbit/rabbit_white_splotched_baby.png");

    protected final BabyRabbitModel<Rabbit> babyModel;
    protected final RabbitModel<Rabbit> adultModel;

    public RabbitRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyRabbitModel<>(context.bakeLayer(BABY_RABBIT));
        this.adultModel = new RabbitModel<>(context.bakeLayer(ADULT_RABBIT));
    }

//    public Optional<RabbitModel<Rabbit>> bakeModels(Rabbit entity) {
//        if (entity.isBaby()) {
//            return Optional.of(this.babyModel);
//        }
//        return Optional.empty();
//    }

    public ResourceLocation getTexture(Rabbit entity) {
        String name = ChatFormatting.stripFormatting(entity.getName().getString());
        if ("Toast".equals(name)) {
            if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                return RABBIT_TOAST_BABY;
            } else if (BabyConfig.enableAdultRabbitModel){
                return RABBIT_TOAST;
            } else
                return null;
        } else {
            switch (entity.getVariant().id()) {
                case 1:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_WHITE_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_WHITE;
                    } else
                        return null;
                case 2:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_BLACK_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_BLACK;
                    } else
                        return null;
                case 3:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_WHITE_SPLOTCHED_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_WHITE_SPLOTCHED;
                    } else
                        return null;
                case 4:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_GOLD_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_GOLD;
                    } else
                        return null;
                case 5:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_SALT_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_SALT;
                    } else
                        return null;
                case 99:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_CAERBANNOG_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_CAERBANNOG;
                    } else
                        return null;
                default:
                    if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
                        return RABBIT_BROWN_BABY;
                    } else if (BabyConfig.enableAdultRabbitModel){
                        return RABBIT_BROWN;
                    } else
                        return null;
            }
        }
    }

    public Optional<RabbitModel<Rabbit>> getModel(Rabbit entity) {
        if (entity.isBaby() && BabyConfig.enableBabyRabbitModel) {
            return Optional.of(this.babyModel);
        } else if (BabyConfig.enableAdultRabbitModel) {
            return Optional.of(this.adultModel);
        } else
            return Optional.empty();
    }
}
