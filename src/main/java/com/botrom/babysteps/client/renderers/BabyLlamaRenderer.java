package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyLlamaModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;

import java.util.Optional;

public class BabyLlamaRenderer {

    public static final ModelLayerLocation BABY_LLAMA = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "llama_baby"), "main");
    public static final ModelLayerLocation BABY_LLAMA_DECOR = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "llama_baby"), "decor");

    private static final ResourceLocation LLAMA_BROWN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/llama/llama_brown_baby.png");
    private static final ResourceLocation LLAMA_CREAMY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/llama/llama_creamy_baby.png");
    private static final ResourceLocation LLAMA_GRAY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/llama/llama_gray_baby.png");
    private static final ResourceLocation LLAMA_WHITE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/llama/llama_white_baby.png");
    public static final ResourceLocation TRADER_LLAMA_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/llama/trader_llama_baby.png");

    protected final BabyLlamaModel<Llama> babyModel;

    public BabyLlamaRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyLlamaModel<>(context.bakeLayer(BABY_LLAMA));
    }

    public Optional<LlamaModel<Llama>> bakeModels(Llama entity) {
        if (entity.isBaby() && BabyConfig.enableBabyLlama) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Llama entity) {
        if (entity.isBaby() && BabyConfig.enableBabyLlama) {
            return switch (entity.getVariant().getId()) {
                case 1 -> LLAMA_WHITE_BABY_TEXTURE;
                case 2 -> LLAMA_BROWN_BABY_TEXTURE;
                case 3 -> LLAMA_GRAY_BABY_TEXTURE;
                default -> LLAMA_CREAMY_BABY_TEXTURE;
            };
        }
        return null;
    }

    public Optional<LlamaModel<Llama>> getModel(Llama entity) {
        if (entity.isBaby() && BabyConfig.enableBabyLlama) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
