package com.botrom.babysteps.client.models;

import com.botrom.babysteps.client.animations.BabyCamelAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.camel.Camel;

import java.util.Collections;

public class BabyCamelModel<T extends Camel> extends CamelModel<T> {
    private static final float MAX_WALK_ANIMATION_SPEED = 2.0F;
    private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
    private final ModelPart root;
    protected final ModelPart head;
    private final AnimationDefinition walkAnimation;
    private final AnimationDefinition sitAnimation;
    private final AnimationDefinition sitPoseAnimation;
    private final AnimationDefinition standupAnimation;
    private final AnimationDefinition idleAnimation;
    private final AnimationDefinition dashAnimation;

    public BabyCamelModel(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("body").getChild("head");
        this.walkAnimation = BabyCamelAnimation.CAMEL_BABY_WALK;
        this.sitAnimation = BabyCamelAnimation.CAMEL_BABY_SIT;
        this.sitPoseAnimation = BabyCamelAnimation.CAMEL_BABY_SIT_POSE;
        this.standupAnimation = BabyCamelAnimation.CAMEL_BABY_STANDUP;
        this.idleAnimation = BabyCamelAnimation.CAMEL_BABY_IDLE;
        this.dashAnimation = BabyCamelAnimation.CAMEL_BABY_DASH;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.5F, -4.0F, -8.0F, 9.0F, 8.0F, 16.0F), PartPose.offset(0.0F, 7.0F, 0.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(50, 38).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 9.0F, 0.0F), PartPose.offset(0.0F, -1.5F, 8.05F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 0).addBox(-2.5F, -3.0F, -7.5F, 5.0F, 5.0F, 7.0F).texOffs(0, 0).addBox(-2.5F, -12.0F, -7.5F, 5.0F, 9.0F, 5.0F).texOffs(0, 14).addBox(-2.5F, -12.0F, -10.5F, 5.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 1.0F, -7.5F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(-2.5F, -11.0F, -4.0F));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(47, 0).addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(2.5F, -11.0F, -4.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(36, 14).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offset(-3.0F, 11.5F, -5.5F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(48, 14).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offset(3.0F, 11.5F, -5.5F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(12, 38).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offset(3.0F, 11.5F, 5.5F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 38).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offset(-3.0F, 11.5F, 5.5F));
        body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("reins", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("bridle", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float pAlpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, pAlpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        this.applyHeadRotation(entity, netHeadYaw, headPitch);
        this.animateWalk(this.walkAnimation, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        this.animate(entity.sitAnimationState, this.sitAnimation, ageInTicks, 1.0f);
        this.animate(entity.sitPoseAnimationState, this.sitPoseAnimation, ageInTicks, 1.0f);
        this.animate(entity.sitUpAnimationState, this.standupAnimation, ageInTicks, 1.0f);
        this.animate(entity.idleAnimationState, this.idleAnimation, ageInTicks, 1.0f);
        this.animate(entity.dashAnimationState, this.dashAnimation, ageInTicks, 1.0f);
    }

    private void applyHeadRotation(Camel entity, float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);
        if (entity.getJumpCooldown() > 0.0F) {
            float headRotation = 45.0F * entity.getJumpCooldown() / 55.0F;
            headPitch = Mth.clamp(headPitch + headRotation, -25.0F, 70.0F);
        }
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
    }
}
