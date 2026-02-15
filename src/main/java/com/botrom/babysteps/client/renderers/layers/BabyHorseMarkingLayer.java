package com.botrom.babysteps.client.renderers.layers;

import com.botrom.babysteps.client.models.BabyHorseModel;
import com.botrom.babysteps.client.renderers.BabyHorseRenderer;
import com.botrom.babysteps.utils.BabyConfig;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BabyHorseMarkingLayer extends RenderLayer<Horse, HorseModel<Horse>> {

    private static final Map<Markings, MarkingTextures> LOCATION_BY_MARKINGS = Util.make(Maps.newEnumMap(Markings.class), (map) -> {
        map.put(Markings.NONE, new MarkingTextures(null, null));
        map.put(Markings.WHITE, new MarkingTextures(HorseMarkingLayer.LOCATION_BY_MARKINGS.get(Markings.WHITE), BabyHorseRenderer.HORSE_MARKINGS_WHITE_BABY_TEXTURE));
        map.put(Markings.WHITE_FIELD, new MarkingTextures(HorseMarkingLayer.LOCATION_BY_MARKINGS.get(Markings.WHITE_FIELD), BabyHorseRenderer.HORSE_MARKINGS_WHITEFIELD_BABY_TEXTURE));
        map.put(Markings.WHITE_DOTS, new MarkingTextures(HorseMarkingLayer.LOCATION_BY_MARKINGS.get(Markings.WHITE_DOTS), BabyHorseRenderer.HORSE_MARKINGS_WHITEDOTS_BABY_TEXTURE));
        map.put(Markings.BLACK_DOTS, new MarkingTextures(HorseMarkingLayer.LOCATION_BY_MARKINGS.get(Markings.BLACK_DOTS), BabyHorseRenderer.HORSE_MARKINGS_BLACKDOTS_BABY_TEXTURE));
    });

    private final HorseModel<Horse> adultModel;
    private final BabyHorseModel<Horse> babyModel;

    public BabyHorseMarkingLayer(RenderLayerParent<Horse, HorseModel<Horse>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.adultModel = new HorseModel<>(modelSet.bakeLayer(ModelLayers.HORSE));
        this.babyModel = new BabyHorseModel<>(modelSet.bakeLayer(BabyHorseRenderer.BABY_HORSE));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, Horse entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Markings marking = entity.getMarkings();
        MarkingTextures textures = LOCATION_BY_MARKINGS.get(marking);

        if (textures != null && !entity.isInvisible()) {
            boolean isBaby = entity.isBaby() && BabyConfig.enableBabyHorse;
            ResourceLocation texture = isBaby ? textures.baby : textures.adult;

            if (texture != null) {
                HorseModel<Horse> modelToUse;

                if (isBaby) {
                    modelToUse = this.babyModel;
                } else {
                    modelToUse = this.adultModel;
                }
                this.getParentModel().copyPropertiesTo(modelToUse);
                modelToUse.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                modelToUse.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));
                modelToUse.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private record MarkingTextures(ResourceLocation adult, ResourceLocation baby) {}
}
