package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabySheepRenderer;
import com.botrom.babysteps.client.renderers.layers.BabySheepWoolLayer;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SheepRenderer.class)
public abstract class SheepRendererMixin extends MobRendererMixin<Sheep, SheepModel<Sheep>> {
    @Unique private BabySheepRenderer bs$babySheepRenderer;

    public SheepRendererMixin(EntityRendererProvider.Context context, SheepModel<Sheep> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bs$onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        if (BabyConfig.enableBabySheep) {
            this.addLayer(new BabySheepWoolLayer(this, context.getModelSet()));
        }
    }

    @Unique
    private BabySheepRenderer bs$babySheepRenderer() {
        if (this.bs$babySheepRenderer == null) {
            this.bs$babySheepRenderer = new BabySheepRenderer(this.context);
        }
        return this.bs$babySheepRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Sheep;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Sheep entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.bs$babySheepRenderer().getTexture(entity);
        if (this.bs$babySheepRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$babySheepRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Sheep entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (entity.isBaby() && BabyConfig.enableBabySheep) {
            this.model = this.bs$babySheepRenderer().getModel(entity).orElse(this.defaultModel);

            List<RenderLayer<Sheep, SheepModel<Sheep>>> originalLayers = new ArrayList<>(this.layers);
            this.layers.clear();

            for (RenderLayer<Sheep, SheepModel<Sheep>> layer : originalLayers) {
                if (layer instanceof BabySheepWoolLayer) {
                    this.layers.add(layer);
                }
            }

            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

            this.layers.clear();
            this.layers.addAll(originalLayers);

        } else {
            this.model = this.defaultModel;
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
}