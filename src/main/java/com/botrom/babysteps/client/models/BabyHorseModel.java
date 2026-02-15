package com.botrom.babysteps.client.models;

import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.Collections;

public class BabyHorseModel<T extends AbstractHorse> extends HorseModel<T> {
    private final ModelPart root;
    protected final ModelPart body;
    protected final ModelPart headParts;
    private final ModelPart rightHindBabyLeg;
    private final ModelPart leftHindBabyLeg;
    private final ModelPart rightFrontBabyLeg;
    private final ModelPart leftFrontBabyLeg;
    private final ModelPart tail;

    public BabyHorseModel(ModelPart root) {
        super(root);
        this.root = root;
        this.body = root.getChild("body");
        this.headParts = root.getChild("head_parts");
        this.rightHindBabyLeg = root.getChild("right_hind_baby_leg");
        this.leftHindBabyLeg = root.getChild("left_hind_baby_leg");
        this.rightFrontBabyLeg = root.getChild("right_front_baby_leg");
        this.leftFrontBabyLeg = root.getChild("left_front_baby_leg");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -3.5F, -7.0F, 8.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.5F, 0.0F));
        PartDefinition headParts = root.addOrReplaceChild("head_parts", CubeListBuilder.create().texOffs(30, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, -6.0F, 0.6109F, 0.0F, 0.0F));
        PartDefinition head = headParts.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.9484F, -6.705F, 6.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0516F, -0.2951F));
        root.addOrReplaceChild("left_hind_baby_leg", CubeListBuilder.create().texOffs(12, 46).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.4F, 16.0F, 5.4F));
        root.addOrReplaceChild("right_hind_baby_leg", CubeListBuilder.create().texOffs(0, 46).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4F, 16.0F, 5.4F));
        root.addOrReplaceChild("left_front_baby_leg", CubeListBuilder.create().texOffs(12, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.4F, 16.0F, -5.4F));
        root.addOrReplaceChild("right_front_baby_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4F, 16.0F, -5.4F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 34).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 7.0F, -0.7418F, 0.0F, 0.0F));
        body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -2.5F, -0.8F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.2484F, 1.9451F, 0.0F, 0.0F, 0.2618F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.2484F, 1.645F, 0.0F, 0.0F, -0.2618F));
        headParts.addOrReplaceChild("left_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("right_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("left_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("right_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("head_saddle", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("mouth_saddle_wrap", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float pAlpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, pAlpha);
    }

    @Override
    public Iterable<ModelPart> headParts() {
        return Collections.emptyList();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Collections.emptyList();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        float babyScale = 0.5F;

        float clampedYRot = Mth.clamp(netHeadYaw, -20.0F, 20.0F);
        float headRotXRad = headPitch * 0.017453292F;

        if (limbSwingAmount > 0.2F) {
            headRotXRad += Mth.cos(limbSwing * 0.8F) * 0.15F * limbSwingAmount;
        }

        float eating = entity.getEatAnim(0.0F);
        float standing = entity.getStandAnim(0.0F);
        float iStanding = 1.0F - standing;
        float feedingAnim = entity.getMouthAnim(0.0F);

        this.headParts.xRot = 0.5235988F + headRotXRad;
        this.headParts.yRot = clampedYRot * 0.017453292F;

        float waterMultiplier = entity.isInWater() ? 0.2F : 1.0F;
        float legAnim1 = Mth.cos(waterMultiplier * limbSwing * 0.6662F + 3.1415927F);
        float legXRotAnim = legAnim1 * 0.8F * limbSwingAmount;

        float baseHeadAngle = (1.0F - Math.max(standing, eating)) * (0.5235988F + headRotXRad + feedingAnim * Mth.sin(ageInTicks) * 0.05F);

        this.headParts.xRot = standing * (0.2617994F + headRotXRad) + eating * (2.1816616F + Mth.sin(ageInTicks) * 0.05F) + baseHeadAngle;
        this.headParts.yRot = standing * clampedYRot * 0.017453292F + (1.0F - Math.max(standing, eating)) * this.headParts.yRot;

        float eatingShift = 2.0F;
        float rearingShift = -4.0F;

        this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, rearingShift), eatingShift);
        this.headParts.z = Mth.lerp(standing, this.headParts.z, -2.0F);

        this.body.xRot = standing * -0.7853982F + iStanding * this.body.xRot;

        float standAngle = 0.2617994F * standing;
        float bobValue = Mth.cos(ageInTicks * 0.6F + 3.1415927F);

        float legLiftY = 8.0F * babyScale;
        this.leftFrontBabyLeg.y -= legLiftY * standing;

        this.rightFrontBabyLeg.y = this.leftFrontBabyLeg.y;
        this.rightFrontBabyLeg.z = this.leftFrontBabyLeg.z;

        this.leftHindBabyLeg.xRot = standAngle - legAnim1 * 0.5F * limbSwingAmount * iStanding;
        this.rightHindBabyLeg.xRot = standAngle + legAnim1 * 0.5F * limbSwingAmount * iStanding;

        this.leftFrontBabyLeg.xRot = (-1.0471976F + bobValue) * standing + legXRotAnim * iStanding;
        this.rightFrontBabyLeg.xRot = (-1.0471976F - bobValue) * standing - legXRotAnim * iStanding;

        this.tail.xRot = -1.0471976F + limbSwingAmount * 0.75F;
        this.tail.y += limbSwingAmount;
        this.tail.z += limbSwingAmount * 2.0F;

        if (entity.tailCounter != 0) {
            this.tail.yRot = Mth.cos(ageInTicks * 0.7F);
        } else {
            this.tail.yRot = 0.0F;
        }
    }
}
