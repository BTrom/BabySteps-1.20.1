package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.SquidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Squid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.SquidRenderer.class)
public abstract class SquidRendererMixin extends MobRendererMixin<Squid, SquidModel<Squid>> {
    @Unique
    private SquidRenderer bs$babySquidRenderer;

    public SquidRendererMixin(EntityRendererProvider.Context context, SquidModel<Squid> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private SquidRenderer bs$getBabySquidRenderer() {
        if (this.bs$babySquidRenderer == null) {
            this.bs$babySquidRenderer = new SquidRenderer(this.context);
        }
        return this.bs$babySquidRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Squid;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Squid entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabySquidRenderer().getTexture(entity);
        if (this.bs$getBabySquidRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabySquidRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Squid entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabySquidRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
