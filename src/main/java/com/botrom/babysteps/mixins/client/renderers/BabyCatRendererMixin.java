package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyCatCollarLayer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.jetbrains.annotations.NotNull;
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
    private BabyCatRenderer bs$getBabyCatRenderer() {
        if (this.bs$babyCatRenderer == null) {
            this.bs$babyCatRenderer = new BabyCatRenderer(this.context);
        }
        return this.bs$babyCatRenderer;
    }

    // INJECT INTO CONSTRUCTOR to swap layers
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.layers.removeIf(layer -> layer instanceof CatCollarLayer);
        this.addLayer(new BabyCatCollarLayer((CatRenderer)(Object)this, this.context.getModelSet()));

    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cat;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Cat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyCatRenderer().getTexture(entity);
        if (this.bs$getBabyCatRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyCatRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull Cat entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyCatRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
