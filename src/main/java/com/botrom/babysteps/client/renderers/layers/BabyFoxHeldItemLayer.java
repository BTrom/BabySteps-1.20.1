package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BabyFoxHeldItemLayer extends RenderLayer<Fox, FoxModel<Fox>> {

    private final ItemInHandRenderer itemInHandRenderer;

    public BabyFoxHeldItemLayer(RenderLayerParent<Fox, FoxModel<Fox>> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, Fox fox, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        boolean isSleeping = fox.isSleeping();
        boolean isBaby = fox.isBaby();

        poseStack.pushPose();
        this.getParentModel().head.translateAndRotate(poseStack);

        if (isBaby) {
            poseStack.scale(0.75F, 0.75F, 0.75F);
            if (BabyConfig.enableBabyFox)
                poseStack.translate(-0.052F, 0.0F, -0.1F);
            else
                poseStack.translate(0.0F, 0.5F, 0.209375F);
        }

        if (isBaby) {
            if (isSleeping) {
                poseStack.translate(0.4F, 0.26F, 0.15F);
            } else {
                poseStack.translate(0.06F, 0.27F, -0.5F);
            }
        } else {
            if (isSleeping) {
                poseStack.translate(0.46F, 0.26F, 0.22F);
            } else {
                poseStack.translate(0.06F, 0.27F, -0.5F);
            }
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        if (isSleeping) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }

        ItemStack item = fox.getItemBySlot(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(fox, item, ItemDisplayContext.GROUND, false, poseStack, pBuffer, pPackedLight);
        poseStack.popPose();
    }
}