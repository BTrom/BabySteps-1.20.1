package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCatRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.OcelotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Ocelot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OcelotRenderer.class)
public abstract class BabyOcelotRendererMixin extends MobRendererMixin<Ocelot, OcelotModel<Ocelot>> {
    @Unique
    private BabyCatRenderer bs$babyOcelotRenderer;

    public BabyOcelotRendererMixin(EntityRendererProvider.Context context, OcelotModel<Ocelot> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyCatRenderer bs$getBabyOcelotRenderer() {
        if (this.bs$babyOcelotRenderer == null) {
            this.bs$babyOcelotRenderer = new BabyCatRenderer(this.context);
        }
        return this.bs$babyOcelotRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Ocelot;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(Ocelot entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyOcelotRenderer().getTexture(entity);
        if (this.bs$getBabyOcelotRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyOcelotRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(@NotNull Ocelot entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyOcelotRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
