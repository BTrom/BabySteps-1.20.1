package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

import java.util.Collections;

public class BabyPigModel<T extends Entity> extends PigModel<T> {
    private final ModelPart root;

    public BabyPigModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float pAlpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, pAlpha);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return Collections.emptyList();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Collections.emptyList();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -4.5F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.5F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-3.51F, -5.0F, -5.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(6, 27).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, -2.0F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 22.0F, -3.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(23, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 22.0F, -3.0F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 22.0F, 4.0F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(23, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 22.0F, 4.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }
}
