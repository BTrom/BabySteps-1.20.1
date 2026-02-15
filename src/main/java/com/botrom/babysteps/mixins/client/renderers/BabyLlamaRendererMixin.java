package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyLlamaRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyLlamaDecorLayer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LlamaRenderer.class)
public abstract class BabyLlamaRendererMixin extends MobRendererMixin<Llama, LlamaModel<Llama>> {
    @Unique
    private BabyLlamaRenderer bs$babyLlamaRenderer;

    public BabyLlamaRendererMixin(EntityRendererProvider.Context context, LlamaModel<Llama> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyLlamaRenderer bs$getBabyLlamaRenderer() {
        if (this.bs$babyLlamaRenderer == null) {
            this.bs$babyLlamaRenderer = new BabyLlamaRenderer(this.context);
        }
        return this.bs$babyLlamaRenderer;
    }

    // INJECT INTO CONSTRUCTOR to swap layers
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context pContext, ModelLayerLocation pLayer, CallbackInfo ci) {
        this.layers.removeIf(layer -> layer instanceof LlamaDecorLayer);
        this.addLayer(new BabyLlamaDecorLayer((LlamaRenderer)(Object)this, this.context.getModelSet()));
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/Llama;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Llama entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyLlamaRenderer().getTexture(entity);
        if (this.bs$getBabyLlamaRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyLlamaRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull Llama entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyLlamaRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
