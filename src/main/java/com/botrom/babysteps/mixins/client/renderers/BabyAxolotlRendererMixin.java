package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyAxolotlRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxolotlRenderer.class)
public abstract class BabyAxolotlRendererMixin extends MobRendererMixin<Axolotl, AxolotlModel<Axolotl>> {
    @Unique
    private BabyAxolotlRenderer bs$babyAxolotlRenderer;

    public BabyAxolotlRendererMixin(EntityRendererProvider.Context context, AxolotlModel<Axolotl> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyAxolotlRenderer bs$getBabyAxolotlRenderer() {
        if (this.bs$babyAxolotlRenderer == null) {
            this.bs$babyAxolotlRenderer = new BabyAxolotlRenderer(this.context);
        }
        return this.bs$babyAxolotlRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/axolotl/Axolotl;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Axolotl entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyAxolotlRenderer().getTexture(entity);
        if (this.bs$getBabyAxolotlRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyAxolotlRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(Axolotl entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyAxolotlRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
