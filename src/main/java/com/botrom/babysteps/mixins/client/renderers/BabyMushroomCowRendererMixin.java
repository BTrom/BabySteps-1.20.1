package com.botrom.babysteps.mixins.client.renderers;

import com.botrom.babysteps.client.renderers.BabyCowRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MushroomCowRenderer.class)
public abstract class BabyMushroomCowRendererMixin extends MobRendererMixin<MushroomCow, CowModel<MushroomCow>> {

    @Unique
    private BabyCowRenderer bs$babyMooshroomRenderer;

    public BabyMushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel<MushroomCow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BabyCowRenderer bs$getBabyMooshroomRenderer() {
        if (this.bs$babyMooshroomRenderer == null) {
            this.bs$babyMooshroomRenderer = new BabyCowRenderer(this.context);
        }
        return this.bs$babyMooshroomRenderer;
    }

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/animal/MushroomCow;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bs$getTextureLocation(MushroomCow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation babyTex = this.bs$getBabyMooshroomRenderer().getTexture(entity);
        if (this.bs$getBabyMooshroomRenderer().getTexture(entity) != null) {
            cir.setReturnValue(this.bs$getBabyMooshroomRenderer().getTexture(entity));
        }
    }

    @Override
    public void render(MushroomCow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.bs$getBabyMooshroomRenderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
