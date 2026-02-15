package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.BabyCatModel;
import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.Cat;

public class BabyCatCollarLayer extends RenderLayer<Cat, CatModel<Cat>> {

    private final BabyCatModel<Cat> babyCollarModel;
    private final CatModel<Cat> adultCollarModel;

    public BabyCatCollarLayer(RenderLayerParent<Cat, CatModel<Cat>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.babyCollarModel = new BabyCatModel<>(modelSet.bakeLayer(BabyCatRenderer.BABY_CAT));
        this.adultCollarModel = new CatModel<>(modelSet.bakeLayer(ModelLayers.CAT_COLLAR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Cat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isTame() && !entity.isInvisible()) {
            float[] colors = entity.getCollarColor().getTextureDiffuseColors();

            if (entity.isBaby() && BabyConfig.enableBabyCat) {
                this.getParentModel().copyPropertiesTo(this.babyCollarModel);
                this.babyCollarModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.babyCollarModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                renderColoredCutoutModel(this.babyCollarModel, BabyCatRenderer.CAT_COLLAR_BABY_TEXTURE, poseStack, buffer, packedLight, entity, colors[0], colors[1], colors[2]);
            } else {
                this.getParentModel().copyPropertiesTo(this.adultCollarModel);
                this.adultCollarModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                this.adultCollarModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                renderColoredCutoutModel(this.adultCollarModel, CatCollarLayer.CAT_COLLAR_LOCATION, poseStack, buffer, packedLight, entity, colors[0], colors[1], colors[2]);
            }
        }
    }
}