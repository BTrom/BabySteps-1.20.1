package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.BabySteps;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NautilusArmorLayer extends RenderLayer<Nautilus, NautilusModel> {
    private final NautilusModel model;

    public NautilusArmorLayer(RenderLayerParent<Nautilus, NautilusModel> parent, EntityRendererProvider.Context context) {
        super(parent);
        this.model = new NautilusModel(context.bakeLayer(NautilusRenderer.LAYER_ARMOR));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull Nautilus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (!itemStack.isEmpty()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexConsumer = null;
            if (itemStack.getItem() == BabySteps.BabyItems.COPPER_NAUTILUS_ARMOR.get())
                vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.COPPER_ARMOR_TEXTURE));
            else if (itemStack.getItem() == BabySteps.BabyItems.IRON_NAUTILUS_ARMOR.get())
                vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.IRON_ARMOR_TEXTURE));
            else if (itemStack.getItem() == BabySteps.BabyItems.GOLDEN_NAUTILUS_ARMOR.get())
                vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.GOLD_ARMOR_TEXTURE));
            else if (itemStack.getItem() == BabySteps.BabyItems.DIAMOND_NAUTILUS_ARMOR.get())
                vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.DIAMOND_ARMOR_TEXTURE));
            else if (itemStack.getItem() == BabySteps.BabyItems.NETHERITE_NAUTILUS_ARMOR.get())
                vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(NautilusRenderer.NETHERITE_ARMOR_TEXTURE));

            if (vertexConsumer != null)
                this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}