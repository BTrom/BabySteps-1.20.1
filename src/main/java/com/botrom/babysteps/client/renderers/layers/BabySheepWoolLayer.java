package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.BabySheepModel;
import com.botrom.babysteps.client.renderers.BabySheepRenderer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class BabySheepWoolLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {

    private final BabySheepModel<Sheep> model;

    public BabySheepWoolLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BabySheepModel<>(modelSet.bakeLayer(BabySheepRenderer.BABY_SHEEP));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Sheep entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isBaby() || entity.isInvisible() || !BabyConfig.enableBabySheep) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float r, g, b;
        if (entity.hasCustomName() && "jeb_".equals(entity.getName().getString())) {
            int offset = entity.tickCount / 25 + entity.getId();
            int size = DyeColor.values().length;
            int fromIndex = offset % size;
            int toIndex = (offset + 1) % size;
            float delta = ((float)(entity.tickCount % 25) + partialTick) / 25.0F;
            float[] fromColor = Sheep.getColorArray(DyeColor.byId(fromIndex));
            float[] toColor = Sheep.getColorArray(DyeColor.byId(toIndex));
            r = fromColor[0] * (1.0F - delta) + toColor[0] * delta;
            g = fromColor[1] * (1.0F - delta) + toColor[1] * delta;
            b = fromColor[2] * (1.0F - delta) + toColor[2] * delta;
        } else {
            float[] color = Sheep.getColorArray(entity.getColor());
            r = color[0];
            g = color[1];
            b = color[2];
        }

        renderColoredCutoutModel(this.model, BabySheepRenderer.BABY_WOOL_TEXTURE, poseStack, buffer, packedLight, entity, r, g, b);
    }
}