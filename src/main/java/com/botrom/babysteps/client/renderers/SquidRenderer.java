package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabySquidModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.Squid;

import java.util.Optional;

public class SquidRenderer {

    public static final ModelLayerLocation BABY_SQUID = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "squid_baby"), "main");

    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/squid/squid_baby.png");
    private static final ResourceLocation GLOW_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/squid/glow_squid_baby.png");

    protected final BabySquidModel<Squid> babyModel;
    protected final BabySquidModel<GlowSquid> glowBabyModel;

    public SquidRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabySquidModel<>(context.bakeLayer(BABY_SQUID));
        this.glowBabyModel = new BabySquidModel<>(context.bakeLayer(BABY_SQUID));
    }

    public Optional<SquidModel<Squid>> bakeModels(Squid entity) {
        if (entity.isBaby() && BabyConfig.enableBabySquid) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public Optional<SquidModel<GlowSquid>> bakeModels(GlowSquid entity) {
        if (entity.isBaby() && BabyConfig.enableBabySquid) {
            return Optional.of(this.glowBabyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Squid entity) {
        if (entity.isBaby() && BabyConfig.enableBabySquid) {
            if (entity instanceof GlowSquid) {
                return GLOW_BABY_TEXTURE;
            } else {
                return BABY_TEXTURE;
            }
        }
        return null;
    }

    public Optional<SquidModel<Squid>> getModel(Squid entity) {
        if (entity.isBaby() && BabyConfig.enableBabySquid) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public Optional<SquidModel<GlowSquid>> getModel(GlowSquid entity) {
        if (entity.isBaby() && BabyConfig.enableBabySquid) {
            return Optional.of(this.glowBabyModel);
        }
        return Optional.empty();
    }
}
