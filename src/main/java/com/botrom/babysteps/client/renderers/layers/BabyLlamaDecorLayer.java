package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.BabyCatModel;
import com.botrom.babysteps.client.models.BabyLlamaModel;
import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.botrom.babysteps.client.renderers.BabyLlamaRenderer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.DyeColor;

public class BabyLlamaDecorLayer extends RenderLayer<Llama, LlamaModel<Llama>> {



    private final BabyLlamaModel<Llama> babyModel;
    private final LlamaModel<Llama> adultModel;

    public BabyLlamaDecorLayer(RenderLayerParent<Llama, LlamaModel<Llama>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.babyModel = new BabyLlamaModel<>(modelSet.bakeLayer(BabyLlamaRenderer.BABY_LLAMA_DECOR));
        this.adultModel = new LlamaModel<>(modelSet.bakeLayer(ModelLayers.LLAMA_DECOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Llama entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isTraderLlama() && !entity.isInvisible()) {
            DyeColor color = entity.getSwag();

            if (entity.isBaby() && BabyConfig.enableBabyLlama) {
                this.getParentModel().copyPropertiesTo(this.babyModel);
                this.babyModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.babyModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.babyModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(BabyLlamaRenderer.TRADER_LLAMA_BABY_TEXTURE)), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                ResourceLocation colorTexture;
                if (color != null) {
                    colorTexture = LlamaDecorLayer.TEXTURE_LOCATION[color.getId()];
                } else {
                    if (!entity.isTraderLlama()) {
                        return;
                    }
                    colorTexture = LlamaDecorLayer.TRADER_LLAMA;
                }
                this.getParentModel().copyPropertiesTo(this.adultModel);
                this.adultModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.adultModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.adultModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(colorTexture)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
