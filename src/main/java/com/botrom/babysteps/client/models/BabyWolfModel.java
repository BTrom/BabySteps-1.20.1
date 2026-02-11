package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;

import java.util.Collections;

public class BabyWolfModel<T extends Wolf> extends WolfModel<T> {
    private final ModelPart root;
    protected final ModelPart head;
    protected final ModelPart body;
    protected final ModelPart rightHindLeg;
    protected final ModelPart leftHindLeg;
    protected final ModelPart rightFrontLeg;
    protected final ModelPart leftFrontLeg;
    protected final ModelPart tail;

    public BabyWolfModel(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = root.getChild("tail");
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
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -3.25F, -3.0F, 6.0F, 5.0F, 5.0F).texOffs(17, 12).addBox(-1.5F, -0.25F, -5.0F, 3.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 18.25F, -4.0F));
        head.addOrReplaceChild("real_head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 8.0F), PartPose.offset(0.0F, 19.0F, 0.0F));
        head.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offset(-2.0F, -4.25F, -0.5F));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(20, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offset(2.0F, -4.25F, -0.5F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-1.5F, 21.0F, 3.0F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(1.5F, 21.0F, 3.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-1.5F, 21.0F, -3.0F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(1.5F, 21.0F, -3.0F));
        PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 16).addBox(-1.0F, -0.5F, -1.25F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 18.5F, 3.75F, 0.9599F, 0.0F, 0.0F));
        tail.addOrReplaceChild("real_tail", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        float partialTick = ageInTicks - entity.tickCount;
        if (entity.isAngry()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos((float) (limbSwing * 0.6662F)) * 1.4F * limbSwingAmount;
        }
        if (entity.isInSittingPose()) {
            this.setSittingPose(entity);
        } else {
            this.rightHindLeg.xRot = Mth.cos((float) (limbSwing * 0.6662F)) * 1.4F * limbSwingAmount;
            this.leftHindLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + 3.1415927F)) * 1.4F * limbSwingAmount;
            this.rightFrontLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + 3.1415927F)) * 1.4F * limbSwingAmount;
            this.leftFrontLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F)) * 1.4F * limbSwingAmount;
        }

        this.shakeOffWater(entity, partialTick);
        this.head.xRot = entity.xRot * 0.017453292F;
        this.head.yRot = entity.yRot * 0.017453292F;
        this.tail.xRot = entity.getTailAngle();
    }

    protected void shakeOffWater(T entity, float partialTick) {
        this.body.zRot = entity.getBodyRollAngle(-0.16F, partialTick);
        this.head.zRot = entity.getHeadRollAngle(partialTick) + entity.getBodyRollAngle(0.0F, partialTick);
        this.tail.zRot = entity.getBodyRollAngle(-0.2F, partialTick);
    }

    protected void setSittingPose(T entity) {
        float ageScale = entity.getScale();
        this.body.y += 4.0F * ageScale;
        this.body.z -= 2.0F * ageScale;
        this.body.xRot = 0.7853982F;
        this.tail.y += 9.0F * ageScale;
        this.tail.z -= 2.0F * ageScale;
        this.rightHindLeg.y += 6.7F * ageScale;
        this.rightHindLeg.z -= 5.0F * ageScale;
        this.rightHindLeg.xRot = 4.712389F;
        this.leftHindLeg.y += 6.7F * ageScale;
        this.leftHindLeg.z -= 5.0F * ageScale;
        this.leftHindLeg.xRot = 4.712389F;
        this.rightFrontLeg.xRot = 5.811947F;
        this.rightFrontLeg.x += 0.01F * ageScale;
        this.rightFrontLeg.y += ageScale;
        this.leftFrontLeg.xRot = 5.811947F;
        this.leftFrontLeg.x -= 0.01F * ageScale;
        this.leftFrontLeg.y += ageScale;
        ++this.body.xRot;
    }

}
