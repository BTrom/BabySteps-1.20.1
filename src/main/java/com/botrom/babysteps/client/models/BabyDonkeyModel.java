package com.botrom.babysteps.client.models;

import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

import java.util.Collections;

public class BabyDonkeyModel<T extends AbstractChestedHorse> extends ChestedHorseModel<T> {
    private final ModelPart root;
    private final ModelPart headParts;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart rightHindBabyLeg;
    private final ModelPart leftHindBabyLeg;
    private final ModelPart rightFrontBabyLeg;
    private final ModelPart leftFrontBabyLeg;

    public BabyDonkeyModel(ModelPart root) {
        super(root);
        this.root = root;
        this.body = root.getChild("body");
        this.headParts = root.getChild("head_parts");
        this.tail = this.body.getChild("tail");
        this.rightHindBabyLeg = root.getChild("right_hind_baby_leg");
        this.leftHindBabyLeg = root.getChild("left_hind_baby_leg");
        this.rightFrontBabyLeg = root.getChild("right_front_baby_leg");
        this.leftFrontBabyLeg = root.getChild("left_front_baby_leg");
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -3.0F, -7.0F, 8.0F, 6.0F, 14.0F), PartPose.offset(0.0F, 14.0F, 0.0F));
        PartDefinition headParts = root.addOrReplaceChild("head_parts", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.0F, -6.9F, 0.17453292F, 0.0F, 0.0F));
        root.addOrReplaceChild("right_hind_baby_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 7.0F, 3.0F), PartPose.offset(-2.4F, 18.5F, 5.4F));
        root.addOrReplaceChild("left_hind_baby_leg", CubeListBuilder.create().texOffs(12, 33).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 7.0F, 3.0F), PartPose.offset(2.4F, 18.5F, 5.4F));
        root.addOrReplaceChild("right_front_baby_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 7.0F, 3.0F), PartPose.offset(-2.4F, 18.5F, -5.4F));
        root.addOrReplaceChild("left_front_baby_leg", CubeListBuilder.create().texOffs(12, 33).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 7.0F, 3.0F), PartPose.offset(2.4F, 18.5F, -5.4F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-1.0F, 5.75F, 6.5F));
        body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(30, 9).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 8.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3926991F, 0.0F, 0.0F));
        headParts.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.6F, -0.6F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -4.0F, -7.4F, 0.34906584F, 0.0F, 0.0F));
        headParts.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 0).addBox(-1.45F, -6.5F, 0.0F, 2.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(2.0F, -8.5F, -2.0F, 0.47996554F, 0.0F, 0.47996554F));
        headParts.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(22, 0).mirror().addBox(-0.55F, -6.5F, 0.0F, 2.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(-2.0F, -8.5F, -2.0F, 0.47996554F, 0.0F, -0.47996554F));
        headParts.addOrReplaceChild("left_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("right_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("left_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("right_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("head_saddle", CubeListBuilder.create(), PartPose.ZERO);
        headParts.addOrReplaceChild("mouth_saddle_wrap", CubeListBuilder.create(), PartPose.ZERO);
        tail.addOrReplaceChild("tail_cube", CubeListBuilder.create().texOffs(24, 33).addBox(-1.5F, -1.0F, -7.5F, 3.0F, 3.0F, 8.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, -6.0F, 1.0F, 0.34906584F, 0.0F, 0.0F));
        body.addOrReplaceChild("right_chest", CubeListBuilder.create(), PartPose.offset(-1.0F, 10.0F, 0.0F));
        body.addOrReplaceChild("left_chest", CubeListBuilder.create(), PartPose.offset(-1.0F, 10.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
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
        float fixedHeadPitch = -30.0F;
        float headRotXRad = fixedHeadPitch * 0.017453292F;

        if (limbSwingAmount > 0.2F) {
            headRotXRad += Mth.cos(limbSwing * 0.8F) * 0.15F * limbSwingAmount;
        }

        float eating = entity.getEatAnim(0.0F);
        float standing = entity.getStandAnim(0.0F);
        float iStanding = 1.0F - standing;
        float feedingAnim = entity.getMouthAnim(0.0F);

        // Head and Body rotation logic
        this.headParts.xRot = 0.5235988F + headRotXRad;
        this.headParts.yRot = clampedYRot * 0.017453292F;

        float waterMultiplier = entity.isInWater() ? 0.2F : 1.0F;
        float legAnim1 = Mth.cos(waterMultiplier * limbSwing * 0.6662F + 3.1415927F);
        float legXRotAnim = legAnim1 * 0.8F * limbSwingAmount;

        float baseHeadAngle = (1.0F - Math.max(standing, eating)) * (0.5235988F + headRotXRad + feedingAnim * Mth.sin(ageInTicks) * 0.05F);
        this.headParts.xRot = standing * (0.2617994F + headRotXRad) + eating * (1.5707964F + Mth.sin(ageInTicks) * 0.05F) + baseHeadAngle; // Eating rotation
        this.headParts.yRot = standing * clampedYRot * 0.017453292F + (1.0F - Math.max(standing, eating)) * this.headParts.yRot;

        this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, -4.0F), 3.0F);
        this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0F);

        this.body.xRot = standing * -0.7853982F + iStanding * this.body.xRot;

        float standAngle = 0.2617994F * standing;
        float bobValue = Mth.cos(ageInTicks * 0.6F + 3.1415927F);

        // Leg Logic (Applying babyScale)
        float legLiftY = 12.0F * babyScale;
        float legLiftZ = -1.0F * babyScale;

        // Front legs
        this.leftFrontBabyLeg.y -= legLiftY * standing;
        this.leftFrontBabyLeg.z += legLiftZ * standing;
        this.rightFrontBabyLeg.y = this.leftFrontBabyLeg.y;
        this.rightFrontBabyLeg.z = this.leftFrontBabyLeg.z;

        this.leftFrontBabyLeg.xRot = (-1.0471976F + bobValue) * standing + legXRotAnim * iStanding;
        this.rightFrontBabyLeg.xRot = (-1.0471976F - bobValue) * standing - legXRotAnim * iStanding;

        // Hind legs
        this.leftHindBabyLeg.xRot = standAngle - legAnim1 * 0.5F * limbSwingAmount * iStanding;
        this.rightHindBabyLeg.xRot = standAngle + legAnim1 * 0.5F * limbSwingAmount * iStanding;

        // Tail
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
