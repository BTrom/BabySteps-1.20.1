package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.models.RabbitModel;
import com.botrom.babysteps.client.renderers.RabbitRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.RabbitRenderer.class)
public abstract class RabbitRendererMixin extends MobRendererMixin<Rabbit, RabbitModel<Rabbit>> {
    @Unique
    private RabbitRenderer bs$rabbitRenderer;

    public RabbitRendererMixin(EntityRendererProvider.Context context, RabbitModel<Rabbit> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private RabbitRenderer bs$getBabyRabbitRenderer() {
        if (this.bs$rabbitRenderer == null) {
            this.bs$rabbitRenderer = new RabbitRenderer(this.context);
        }
        return this.bs$rabbitRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Rabbit;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Rabbit entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyRabbitRenderer().getTexture(entity);
        if (this.bs$getBabyRabbitRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyRabbitRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Rabbit entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyRabbitRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
