package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyHorseRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChestedHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestedHorseRenderer.class)
public abstract class BabyChestedHorseRendererMixin extends MobRendererMixin<AbstractChestedHorse, ChestedHorseModel<AbstractChestedHorse>> {
    @Unique
    private BabyHorseRenderer bs$babyHorseRenderer;

    public BabyChestedHorseRendererMixin(EntityRendererProvider.Context context, ChestedHorseModel<AbstractChestedHorse> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyHorseRenderer bs$getBabyHorseRenderer() {
        if (this.bs$babyHorseRenderer == null) {
            this.bs$babyHorseRenderer = new BabyHorseRenderer(this.context);
        }
        return this.bs$babyHorseRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/AbstractChestedHorse;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(AbstractChestedHorse entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyHorseRenderer().getTexture(entity);
        if (this.bs$getBabyHorseRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyHorseRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull AbstractChestedHorse entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyHorseRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
