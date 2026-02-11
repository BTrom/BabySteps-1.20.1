package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyTurtleRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.TurtleModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TurtleRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Turtle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleRenderer.class)
public abstract class BabyTurtleRendererMixin extends MobRendererMixin<Turtle, TurtleModel<Turtle>> {
    @Unique
    private BabyTurtleRenderer bs$babyTurtleRenderer;

    public BabyTurtleRendererMixin(EntityRendererProvider.Context context, TurtleModel<Turtle> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyTurtleRenderer bs$getBabyTurtleRenderer() {
        if (this.bs$babyTurtleRenderer == null) {
            this.bs$babyTurtleRenderer = new BabyTurtleRenderer(this.context);
        }
        return this.bs$babyTurtleRenderer;
    }

    @Inject(
            method = "getTextureLocation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Turtle entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyTurtleRenderer().getTexture(entity);
        if (this.bs$getBabyTurtleRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyTurtleRenderer().getTexture(entity));
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/animal/Turtle;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
    )
    public void render(Turtle entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.model = this.bs$getBabyTurtleRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
    }
}
