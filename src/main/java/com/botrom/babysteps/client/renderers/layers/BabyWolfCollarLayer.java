package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.BabyCatModel;
import com.botrom.babysteps.client.models.BabyWolfModel;
import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.botrom.babysteps.client.renderers.BabyWolfRenderer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;

public class BabyWolfCollarLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {

    private final BabyWolfModel<Wolf> babyCollarModel;
    private final WolfModel<Wolf> adultCollarModel;

    public BabyWolfCollarLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.babyCollarModel = new BabyWolfModel<>(modelSet.bakeLayer(BabyWolfRenderer.BABY_WOLF));
        this.adultCollarModel = new WolfModel<>(modelSet.bakeLayer(ModelLayers.WOLF));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Wolf entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isTame() && !entity.isInvisible()) {
            float[] colors = entity.getCollarColor().getTextureDiffuseColors();

            if (entity.isBaby() && BabyConfig.enableBabyWolf) {
                this.getParentModel().copyPropertiesTo(this.babyCollarModel);
                this.babyCollarModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.babyCollarModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                renderColoredCutoutModel(this.babyCollarModel, BabyWolfRenderer.WOLF_COLLAR_BABY_TEXTURE, poseStack, buffer, packedLight, entity, colors[0], colors[1], colors[2]);
            } else {
                this.getParentModel().copyPropertiesTo(this.adultCollarModel);
                this.adultCollarModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.adultCollarModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                renderColoredCutoutModel(this.adultCollarModel, WolfCollarLayer.WOLF_COLLAR_LOCATION, poseStack, buffer, packedLight, entity, colors[0], colors[1], colors[2]);
            }
        }
    }
}