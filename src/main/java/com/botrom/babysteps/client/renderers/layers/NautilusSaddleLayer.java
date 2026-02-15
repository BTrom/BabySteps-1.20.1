package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.NautilusModel;
import com.botrom.babysteps.client.renderers.NautilusRenderer;
import com.botrom.babysteps.common.entities.Nautilus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class NautilusSaddleLayer extends RenderLayer<Nautilus, NautilusModel> {
    private final NautilusModel model;

    public NautilusSaddleLayer(RenderLayerParent<Nautilus, NautilusModel> parent, EntityRendererProvider.Context context) {
        super(parent);
        this.model = new NautilusModel(context.bakeLayer(NautilusRenderer.LAYER_SADDLE));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull Nautilus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isSaddled()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.NAUTILUS_SADDLE_TEXTURE));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}