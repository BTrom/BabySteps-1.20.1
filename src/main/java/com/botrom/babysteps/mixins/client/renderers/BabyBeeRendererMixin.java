package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyBeeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeRenderer.class)
public abstract class BabyBeeRendererMixin extends MobRendererMixin<Bee, BeeModel<Bee>> {
    @Unique
    private BabyBeeRenderer bs$babyBeeRenderer;

    public BabyBeeRendererMixin(EntityRendererProvider.Context context, BeeModel<Bee> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyBeeRenderer bs$getBabyBeeRenderer() {
        if (this.bs$babyBeeRenderer == null) {
            this.bs$babyBeeRenderer = new BabyBeeRenderer(this.context);
        }
        return this.bs$babyBeeRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Bee;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Bee entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyBeeRenderer().getTexture(entity);
        if (this.bs$getBabyBeeRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyBeeRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Bee entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyBeeRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
