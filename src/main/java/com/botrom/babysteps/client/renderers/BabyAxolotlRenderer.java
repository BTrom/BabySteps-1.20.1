package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.BabyAxolotlModel;
import com.botrom.babysteps.client.models.BabyBeeModel;
import com.botrom.babysteps.utils.BabyConfig;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

import java.util.Optional;

public class BabyAxolotlRenderer {

    public static final ModelLayerLocation BABY_AXOLOTL = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "axolotl_baby"), "main");

    private static final ResourceLocation AXOLOTL_BLUE_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/axolotl/axolotl_blue_baby.png");
    private static final ResourceLocation AXOLOTL_CYAN_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/axolotl/axolotl_cyan_baby.png");
    private static final ResourceLocation AXOLOTL_GOLD_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/axolotl/axolotl_gold_baby.png");
    private static final ResourceLocation AXOLOTL_LUCY_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/axolotl/axolotl_lucy_baby.png");
    private static final ResourceLocation AXOLOTL_WILD_BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/axolotl/axolotl_wild_baby.png");

    protected final BabyAxolotlModel<Axolotl> babyModel;

    public BabyAxolotlRenderer(EntityRendererProvider.Context context) {
        this.babyModel = new BabyAxolotlModel<>(context.bakeLayer(BABY_AXOLOTL));
    }

    public Optional<AxolotlModel<Axolotl>> bakeModels(Axolotl entity) {
        if (entity.isBaby() && BabyConfig.enableBabyAxolotl) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(Axolotl entity) {
        if (entity.isBaby() && BabyConfig.enableBabyAxolotl) {
            return switch (entity.getVariant().getId()) {
                case 1 -> AXOLOTL_WILD_BABY_TEXTURE;
                case 2 -> AXOLOTL_GOLD_BABY_TEXTURE;
                case 3 -> AXOLOTL_CYAN_BABY_TEXTURE;
                case 4 -> AXOLOTL_BLUE_BABY_TEXTURE;
                default -> AXOLOTL_LUCY_BABY_TEXTURE;
            };
        }
        return null;
    }

    public Optional<AxolotlModel<Axolotl>> getModel(Axolotl entity) {
        if (entity.isBaby() && BabyConfig.enableBabyAxolotl) {
            return Optional.of(this.babyModel);
        }
        return Optional.empty();
    }
}
