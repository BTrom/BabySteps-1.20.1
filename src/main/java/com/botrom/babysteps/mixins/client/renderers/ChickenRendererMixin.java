package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyChickenRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRendererMixin<Chicken, ChickenModel<Chicken>> {
    @Unique private BabyChickenRenderer bs$renderer;

    public ChickenRendererMixin(EntityRendererProvider.Context context, ChickenModel<Chicken> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyChickenRenderer bs$renderer() {
        if (this.bs$renderer == null) {
            this.bs$renderer = new BabyChickenRenderer(this.context);
        }
        return this.bs$renderer;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Chicken;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bs$getTextureLocation(Chicken entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.bs$renderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$renderer().getTexture(entity));
        }
    }

    @Override
    public void render(Chicken entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$renderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}