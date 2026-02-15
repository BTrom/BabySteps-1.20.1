package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyWolfRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyWolfCollarLayer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfRenderer.class)
public abstract class BabyWolfRendererMixin extends MobRendererMixin<Wolf, WolfModel<Wolf>> {
    @Unique
    private BabyWolfRenderer bs$babyWolfRenderer;

    public BabyWolfRendererMixin(EntityRendererProvider.Context context, WolfModel<Wolf> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyWolfRenderer bs$getBabyWolfRenderer() {
        if (this.bs$babyWolfRenderer == null) {
            this.bs$babyWolfRenderer = new BabyWolfRenderer(this.context);
        }
        return this.bs$babyWolfRenderer;
    }

    // INJECT INTO CONSTRUCTOR to swap layers
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.layers.removeIf(layer -> layer instanceof WolfCollarLayer);
        this.addLayer(new BabyWolfCollarLayer((WolfRenderer)(Object)this, this.context.getModelSet()));
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Wolf entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyWolfRenderer().getTexture(entity);
        if (this.bs$getBabyWolfRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyWolfRenderer().getTexture(entity));
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/animal/Wolf;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(Wolf entity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        this.model = this.bs$getBabyWolfRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
    }
}
