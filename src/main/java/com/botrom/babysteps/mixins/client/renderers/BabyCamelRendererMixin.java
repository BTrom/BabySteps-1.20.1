package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCamelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.camel.Camel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CamelRenderer.class)
public abstract class BabyCamelRendererMixin extends MobRendererMixin<Camel, CamelModel<Camel>> {
    @Unique
    private BabyCamelRenderer bs$babyCamelRenderer;

    public BabyCamelRendererMixin(EntityRendererProvider.Context context, CamelModel<Camel> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyCamelRenderer bs$getBabyCamelRenderer() {
        if (this.bs$babyCamelRenderer == null) {
            this.bs$babyCamelRenderer = new BabyCamelRenderer(this.context);
        }
        return this.bs$babyCamelRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/camel/Camel;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Camel entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyCamelRenderer().getTexture(entity);
        if (this.bs$getBabyCamelRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyCamelRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull Camel entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyCamelRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
