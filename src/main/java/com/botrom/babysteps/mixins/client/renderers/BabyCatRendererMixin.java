package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyCatCollarLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatRenderer.class)
public abstract class BabyCatRendererMixin extends MobRendererMixin<Cat, CatModel<Cat>> {
    @Unique
    private BabyCatRenderer bs$babyCatRenderer;

    public BabyCatRendererMixin(EntityRendererProvider.Context context, CatModel<Cat> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyCatRenderer getBabyCatRenderer() {
        if (this.bs$babyCatRenderer == null) {
            this.bs$babyCatRenderer = new BabyCatRenderer(this.context);
        }
        return this.bs$babyCatRenderer;
    }

    // INJECT INTO CONSTRUCTOR to swap layers
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context p_173943_, CallbackInfo ci) {
        // 1. Remove the vanilla collar layer (it uses the Adult model and floats)
        this.layers.removeIf(layer -> layer instanceof CatCollarLayer);
        // 2. Add our custom baby-compatible layer
        // We cast 'this' because we are inside the Renderer class
        this.addLayer(new BabyCatCollarLayer((CatRenderer)(Object)this, context.getModelSet()));
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cat;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Cat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.getBabyCatRenderer().getTexture(entity);
        if (this.getBabyCatRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.getBabyCatRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Cat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.getBabyCatRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
