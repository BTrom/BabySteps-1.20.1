package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.Sheep;

import java.util.Collections;

public class BabySheepModel<T extends Sheep> extends SheepModel<T> {
    private final ModelPart root;

    public BabySheepModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -2.0F, -4.5F, 6.0F, 4.0F, 9.0F), PartPose.offset(0.0F, 17.0F, 0.5F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -4.5F, -3.5F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 15.5F, -2.5F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(-2.0F, 19.0F, 3.0F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(24, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(2.0F, 19.0F, 3.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(8, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(-2.0F, 19.0F, -2.0F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(24, 5).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(2.0F, 19.0F, -2.0F));
        return LayerDefinition.create(mesh, 64, 32);
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

    @Override
    public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        super.setupAnim(entity, pLimbSwing, pLimbSwingAmount, ageInTicks, pNetHeadYaw, pHeadPitch);
//        float partialTick = ageInTicks - entity.tickCount;
//        this.head.y += entity.getHeadEatPositionScale(partialTick) * 9.0F * entity.getScale();
//        this.head.xRot = entity.getHeadEatAngleScale(partialTick);
    }
}
