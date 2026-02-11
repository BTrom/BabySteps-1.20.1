package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.TurtleModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.Turtle;

import java.util.Collections;

public class BabyTurtleModel<T extends Turtle> extends TurtleModel<T> {
    private final ModelPart root;

    public BabyTurtleModel(ModelPart root) {
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
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F), PartPose.offset(0.0F, 22.9F, 1.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 22.9F, -1.0F));
        root.addOrReplaceChild("egg_belly", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(-1, 0).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.1F, 1.0F), PartPose.offset(-2.0F, 23.9F, 2.5F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(-1, 1).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.1F, 1.0F), PartPose.offset(2.0F, 23.9F, 2.5F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.1F, 1.0F), PartPose.offset(-2.0F, 23.9F, -0.5F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(8, 7).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.1F, 1.0F), PartPose.offset(2.0F, 23.9F, -0.5F));
        return LayerDefinition.create(mesh, 16, 16);
    }
}