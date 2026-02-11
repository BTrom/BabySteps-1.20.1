package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Bee;

import java.util.Collections;

public class BabyBeeModel<T extends Bee> extends BeeModel<T> {
    private final ModelPart root;
    private final ModelPart bone;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    // Define the Base Y position here so we can reset to it every frame
    private static final float BODY_BASE_Y = 19.6667F;

    public BabyBeeModel(final ModelPart root) {
        super(root);
        this.root = root;
        this.bone = root.getChild("bone");
        ModelPart body = this.bone.getChild("body");
        this.stinger = body.getChild("stinger");
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild("front_legs");
        this.midLeg = this.bone.getChild("middle_legs");
        this.backLeg = this.bone.getChild("back_legs");
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
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
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(6, 12).addBox(1.0F, -1.6667F, -2.1633F, 1.0F, 2.0F, 2.0F).texOffs(0, 12).addBox(-2.0F, -1.6667F, -2.1933F, 1.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 19.6667F, -1.8567F));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F), PartPose.offset(0.0F, 1.3333F, 2.3567F));
        body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(13, 2).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.5F, 2.5F));
        body.addOrReplaceChild("left_antenna", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("right_antenna", CubeListBuilder.create(), PartPose.ZERO);
        bone.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(3, 9).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F), PartPose.offsetAndRotation(-1.0F, -0.6667F, 0.8567F, 0.2182F, 0.3491F, 0.0F));
        bone.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(-3, 9).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F).mirror(false), PartPose.offsetAndRotation(1.0F, -0.6667F, 0.8567F, 0.2182F, -0.3491F, 0.0F));
        bone.addOrReplaceChild("front_legs", CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 1.8567F));
        bone.addOrReplaceChild("middle_legs", CubeListBuilder.create().texOffs(13, 1).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 2.8567F));
        bone.addOrReplaceChild("back_legs", CubeListBuilder.create().texOffs(13, 2).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 3.8567F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.stinger.visible = !entity.hasStung();
        float partialTick = ageInTicks - entity.tickCount;
        float rollAmount = entity.getRollAmount(partialTick);

        if (!entity.onGround()) {
            float wingSpeed = ageInTicks * 120.32113F * 0.017453292F;
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(wingSpeed) * 3.1415927F * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = 0.7853982F;
            this.midLeg.xRot = 0.7853982F;
            this.backLeg.xRot = 0.7853982F;
        }

        if (!entity.isAngry() && !entity.onGround()) {
            float bobAmount = Mth.cos(ageInTicks * 0.18F);
            this.bobUpAndDown(bobAmount, ageInTicks);
        }

        if (rollAmount > 0.0F) {
            this.bone.xRot = rotLerpRad(rollAmount, this.bone.xRot, 3.0915928F);
        }
    }

    protected void bobUpAndDown(final float speed, final float ageInTicks) {
        this.bone.xRot = 0.1F + speed * 3.1415927F * 0.025F;
        this.bone.y = BODY_BASE_Y - Mth.cos(ageInTicks * 0.18F) * 0.9F;
        this.frontLeg.xRot = -speed * 3.1415927F * 0.1F + 0.3926991F;
        this.backLeg.xRot = -speed * 3.1415927F * 0.05F + 0.7853982F;
    }

    private static float rotLerpRad(final float a, final float from, final float to) {
        float diff = to - from;
        while (diff < -3.1415927F) {
            diff += 6.2831855F;
        }
        while (diff >= 3.1415927F) {
            diff -= 6.2831855F;
        }
        return from + a * diff;
    }
}
