package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyGoatRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GoatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GoatRenderer.class)
public abstract class BabyGoatRendererMixin extends MobRendererMixin<Goat, GoatModel<Goat>> {
    @Unique
    private BabyGoatRenderer bs$babyGoatRenderer;

    public BabyGoatRendererMixin(EntityRendererProvider.Context context, GoatModel<Goat> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyGoatRenderer bs$getBabyGoatRenderer() {
        if (this.bs$babyGoatRenderer == null) {
            this.bs$babyGoatRenderer = new BabyGoatRenderer(this.context);
        }
        return this.bs$babyGoatRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/goat/Goat;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Goat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyGoatRenderer().getTexture(entity);
        if (this.bs$getBabyGoatRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyGoatRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Goat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyGoatRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
