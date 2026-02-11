package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyRabbitModel;
import com.botrom.babysteps.client.models.RabbitModel;
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
            return entity.isBaby() ? RABBIT_TOAST_BABY : RABBIT_TOAST;
        } else {
            return switch (entity.getVariant().id()) {
                case 1 -> entity.isBaby() ? RABBIT_WHITE_BABY : RABBIT_WHITE;
                case 2 -> entity.isBaby() ? RABBIT_BLACK_BABY : RABBIT_BLACK;
                case 3 -> entity.isBaby() ? RABBIT_WHITE_SPLOTCHED_BABY : RABBIT_WHITE_SPLOTCHED;
                case 4 -> entity.isBaby() ? RABBIT_GOLD_BABY : RABBIT_GOLD;
                case 5 -> entity.isBaby() ? RABBIT_SALT_BABY : RABBIT_SALT;
                case 99 -> entity.isBaby() ? RABBIT_CAERBANNOG_BABY : RABBIT_CAERBANNOG;
                default -> entity.isBaby() ? RABBIT_BROWN_BABY : RABBIT_BROWN;
            };
        }
    }

    public Optional<RabbitModel<Rabbit>> getModel(Rabbit entity) {
        if (entity.isBaby()) {
            return Optional.of(this.babyModel);
        } else {
            return Optional.of(this.adultModel);
        }
    }
}
