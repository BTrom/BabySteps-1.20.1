package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyPolarBearRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PolarBearRenderer.class)
public abstract class BabyPolarBearRendererMixin extends MobRendererMixin<PolarBear, PolarBearModel<PolarBear>> {
    @Unique
    private BabyPolarBearRenderer bs$babyPolarBearRenderer;

    public BabyPolarBearRendererMixin(EntityRendererProvider.Context context, PolarBearModel<PolarBear> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyPolarBearRenderer bs$getBabyPolarBearRenderer() {
        if (this.bs$babyPolarBearRenderer == null) {
            this.bs$babyPolarBearRenderer = new BabyPolarBearRenderer(this.context);
        }
        return this.bs$babyPolarBearRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/PolarBear;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(PolarBear entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyPolarBearRenderer().getTexture(entity);
        if (this.bs$getBabyPolarBearRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyPolarBearRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull PolarBear entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyPolarBearRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
