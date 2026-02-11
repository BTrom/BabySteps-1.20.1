package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyDolphinRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Dolphin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DolphinRenderer.class)
public abstract class BabyDolphinRendererMixin extends MobRendererMixin<Dolphin, DolphinModel<Dolphin>> {
    @Unique
    private BabyDolphinRenderer bs$babyDolphinRenderer;

    public BabyDolphinRendererMixin(EntityRendererProvider.Context context, DolphinModel<Dolphin> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyDolphinRenderer bs$getBabyDolphinRenderer() {
        if (this.bs$babyDolphinRenderer == null) {
            this.bs$babyDolphinRenderer = new BabyDolphinRenderer(this.context);
        }
        return this.bs$babyDolphinRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Dolphin;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Dolphin entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyDolphinRenderer().getTexture(entity);
        if (this.bs$getBabyDolphinRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyDolphinRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Dolphin entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyDolphinRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
