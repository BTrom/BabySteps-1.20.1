package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyHorseRenderer;
import com.botrom.babysteps.client.renderers.layers.BabyHorseMarkingLayer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HorseRenderer.class)
public abstract class BabyHorseRendererMixin extends MobRendererMixin<Horse, HorseModel<Horse>> {
    @Unique
    private BabyHorseRenderer bs$babyHorseRenderer;

    public BabyHorseRendererMixin(EntityRendererProvider.Context context, HorseModel<Horse> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyHorseRenderer bs$getBabyHorseRenderer() {
        if (this.bs$babyHorseRenderer == null) {
            this.bs$babyHorseRenderer = new BabyHorseRenderer(this.context);
        }
        return this.bs$babyHorseRenderer;
    }

    // INJECT INTO CONSTRUCTOR to swap layers
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bs$initLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.layers.removeIf(layer -> layer instanceof HorseMarkingLayer);
        this.addLayer(new BabyHorseMarkingLayer((HorseRenderer)(Object)this, this.context.getModelSet()));
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/Horse;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Horse entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyHorseRenderer().getTexture(entity);
        if (this.bs$getBabyHorseRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyHorseRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull Horse entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        Optional<HorseModel<AbstractHorse>> babyModel = this.bs$getBabyHorseRenderer().getModel(entity);
        this.model = babyModel.map(abstractHorseHorseModel -> (HorseModel<Horse>) (Object) abstractHorseHorseModel).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
