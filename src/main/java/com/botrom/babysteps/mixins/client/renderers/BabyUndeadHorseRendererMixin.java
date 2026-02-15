package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyHorseRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UndeadHorseRenderer.class)
public abstract class BabyUndeadHorseRendererMixin extends MobRendererMixin<AbstractHorse, HorseModel<AbstractHorse>> {
    @Unique
    private BabyHorseRenderer bs$babyUndeadHorseRenderer;

    public BabyUndeadHorseRendererMixin(EntityRendererProvider.Context context, HorseModel<AbstractHorse> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyHorseRenderer bs$getBabyUndeadHorseRenderer() {
        if (this.bs$babyUndeadHorseRenderer == null) {
            this.bs$babyUndeadHorseRenderer = new BabyHorseRenderer(this.context);
        }
        return this.bs$babyUndeadHorseRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(AbstractHorse entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyUndeadHorseRenderer().getTexture(entity);
        if (this.bs$getBabyUndeadHorseRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyUndeadHorseRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull AbstractHorse entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyUndeadHorseRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
