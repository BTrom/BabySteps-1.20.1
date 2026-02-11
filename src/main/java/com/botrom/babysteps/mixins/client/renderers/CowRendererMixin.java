package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCowRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRendererMixin<Cow, CowModel<Cow>> {
    @Unique private BabyCowRenderer bs$renderer;

    public CowRendererMixin(EntityRendererProvider.Context context, CowModel<Cow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyCowRenderer bs$renderer() {
        if (this.bs$renderer == null) {
            this.bs$renderer = new BabyCowRenderer(this.context);
        }
        return this.bs$renderer;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bs$getTextureLocation(Cow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.bs$renderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$renderer().getTexture(entity));
        }
    }

    @Override
    public void render(Cow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$renderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}