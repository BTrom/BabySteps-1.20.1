package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyFoxRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyFoxHeldItemLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoxRenderer.class)
public abstract class BabyFoxRendererMixin extends MobRendererMixin<Fox, FoxModel<Fox>> {
    @Unique
    private BabyFoxRenderer bs$babyFoxRenderer;

    public BabyFoxRendererMixin(EntityRendererProvider.Context context, FoxModel<Fox> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyFoxRenderer bs$getBabyFoxRenderer() {
        if (this.bs$babyFoxRenderer == null) {
            this.bs$babyFoxRenderer = new BabyFoxRenderer(this.context);
        }
        return this.bs$babyFoxRenderer;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.layers.removeIf(layer -> layer instanceof FoxHeldItemLayer);
        this.addLayer(new BabyFoxHeldItemLayer((FoxRenderer)(Object)this, context.getItemInHandRenderer()));
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Fox;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Fox entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyFoxRenderer().getTexture(entity);
        if (this.bs$getBabyFoxRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyFoxRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Fox entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyFoxRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
