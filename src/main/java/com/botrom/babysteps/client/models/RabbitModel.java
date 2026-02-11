package com.botrom.babysteps.client.models;

import com.botrom.babysteps.client.animations.RabbitAnimation;
import com.botrom.babysteps.utils.IRabbitAnimationAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Rabbit;

public class RabbitModel<T extends Rabbit> extends HierarchicalModel<T> {
    protected static final String FRONT_LEGS = "frontlegs";
    protected static final String BACK_LEGS = "backlegs";
    protected static final String LEFT_HAUNCH = "left_haunch";
    protected static final String RIGHT_HAUNCH = "right_haunch";
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    protected final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart frontLegs;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart backLegs;
    private final ModelPart rightHindLeg;
    private final ModelPart rightHaunch;
    private final ModelPart leftHindLeg;
    private final ModelPart leftHaunch;
    private final AnimationDefinition hopAnimation = RabbitAnimation.HOP;
    private final AnimationDefinition idleHeadTiltAnimation = RabbitAnimation.IDLE_HEAD_TILT;

    public RabbitModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail = body.getChild("tail");
        this.head = body.getChild("head");
        this.leftEar = head.getChild("left_ear");
        this.rightEar = head.getChild("right_ear");
        this.frontLegs = body.getChild("frontlegs");
        this.rightFrontLeg = frontLegs.getChild("right_front_leg");
        this.leftFrontLeg = frontLegs.getChild("left_front_leg");
        this.backLegs = root.getChild("backlegs");
        this.rightHindLeg = backLegs.getChild("right_hind_leg");
        this.rightHaunch = rightHindLeg.getChild("right_haunch");
        this.leftHindLeg = backLegs.getChild("left_hind_leg");
        this.leftHaunch = leftHindLeg.getChild("left_haunch");
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -9.0F, 8.0F, 6.0F, 10.0F), PartPose.offsetAndRotation(0.0F, 23.0F, 4.0F, -0.3927F, 0.0F, 0.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 16).addBox(-2.0F, -3.0084F, -1.0125F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, -4.9916F, 0.0125F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -3.0F, -4.0F, 5.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -5.2929F, -8.1213F, 0.3927F, 0.0F, 0.0F));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -4.2929F, -0.1213F, 2.0F, 5.0F, 1.0F), PartPose.offset(1.5F, -3.7071F, -0.8787F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -4.2929F, -0.1213F, 2.0F, 5.0F, 1.0F), PartPose.offset(-1.5F, -3.7071F, -0.8787F));
        PartDefinition frontLegs = body.addOrReplaceChild("frontlegs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5349F, -6.3108F));
        frontLegs.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(36, 18).addBox(-0.9F, -1.0F, -0.9F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 1.9239F, 0.3827F, 0.3927F, 0.0F, 0.0F));
        frontLegs.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(44, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 1.9239F, 0.4827F, 0.3927F, 0.0F, 0.0F));
        PartDefinition backLegs = root.addOrReplaceChild("backlegs", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 4.0F));
        PartDefinition rightBackLeg = backLegs.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.5F, 0.0F));
        rightBackLeg.addOrReplaceChild("right_haunch", CubeListBuilder.create().texOffs(20, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition leftBackLeg = backLegs.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(3.0F, 0.5F, 0.0F));
        leftBackLeg.addOrReplaceChild("left_haunch", CubeListBuilder.create().texOffs(36, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        float clampedYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        float clampedPitch = Mth.clamp(headPitch, -25.0F, 25.0F);

        this.head.yRot = clampedYaw * ((float)Math.PI / 180F);
        this.head.xRot = clampedPitch * ((float)Math.PI / 180F);

        if (entity instanceof IRabbitAnimationAccess access) {
            // CHANGED: Use the dynamic speed based on jumpDuration instead of fixed 0.8F.
            // If speed is 0 (entity not initialized), fallback to 1.0F.
            float speed = access.bs$getJumpAnimationSpeed();
            if (speed == 0.0F) speed = 1.0F;

            this.animate(access.bs$getHopAnimationState(), RabbitAnimation.HOP, ageInTicks, speed);
            this.animate(access.bs$getIdleHeadTiltAnimationState(), RabbitAnimation.IDLE_HEAD_TILT, ageInTicks, 1.0F);
        }
    }
}