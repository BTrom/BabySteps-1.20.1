package com.botrom.babysteps.client.renderers;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.NautilusModel;
import com.botrom.babysteps.client.renderers.layers.NautilusArmorLayer;
import com.botrom.babysteps.client.renderers.layers.NautilusSaddleLayer;
import com.botrom.babysteps.common.entities.Nautilus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NautilusRenderer extends MobRenderer<Nautilus, NautilusModel> {
	public static final ModelLayerLocation LAYER_MAIN = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "nautilus"), "main");
	public static final ModelLayerLocation LAYER_BABY = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "nautilus"), "baby");
	public static final ModelLayerLocation LAYER_SADDLE = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "nautilus"), "saddle");
	public static final ModelLayerLocation LAYER_ARMOR = new ModelLayerLocation(new ResourceLocation(BabySteps.MOD_ID, "nautilus"), "armor");

	private static final ResourceLocation ADULT_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus.png");
	private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_baby.png");
	public static final ResourceLocation NAUTILUS_SADDLE_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_saddle.png");
	public static final ResourceLocation COPPER_ARMOR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_armor_copper.png");
	public static final ResourceLocation IRON_ARMOR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_armor_iron.png");
	public static final ResourceLocation GOLD_ARMOR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_armor_gold.png");
	public static final ResourceLocation DIAMOND_ARMOR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_armor_diamond.png");
	public static final ResourceLocation NETHERITE_ARMOR_TEXTURE = new ResourceLocation(BabySteps.MOD_ID, "textures/entity/nautilus/nautilus_armor_netherite.png");

	private final NautilusModel adultModel;
	private final NautilusModel babyModel;

	public NautilusRenderer(EntityRendererProvider.Context context) {
		super(context, new NautilusModel(context.bakeLayer(LAYER_MAIN)), 0.7F);

		this.adultModel = this.getModel();
		this.babyModel = new NautilusModel(context.bakeLayer(LAYER_BABY));

		this.addLayer(new NautilusSaddleLayer(this, context));
		this.addLayer(new NautilusArmorLayer(this, context));
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull Nautilus entity) {
		return entity.isBaby() ? BABY_TEXTURE : ADULT_TEXTURE;
	}

	@Override
	public void render(Nautilus entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
		if (entity.isBaby()) {
			this.model = this.babyModel;
			this.shadowRadius = 0.35F;
		} else {
			this.model = this.adultModel;
			this.shadowRadius = 0.7F;
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}
}