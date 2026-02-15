package com.botrom.babysteps.client.models;

import com.botrom.babysteps.client.animations.BabyAxolotlAnimation;
import com.botrom.babysteps.utils.IAxolotlAnimationAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Collections;

public class BabyAxolotlModel<T extends Axolotl> extends AxolotlModel<T> {
    private final ModelPart root;
    private final ModelPart tail;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart topGills;
    private final ModelPart leftGills;
    private final ModelPart rightGills;
    private final HierarchicalModel<T> animationProxy;
    private static final float MAX_WALK_ANIMATION_SPEED = 15.0F;
    private static final float WALK_ANIMATION_SCALE_FACTOR = 30.0F;
    private final AnimationDefinition walkAnimation;
    private final AnimationDefinition walkUnderwaterAnimation;
    private final AnimationDefinition swimAnimation;
    private final AnimationDefinition idleOnGroundAnimation;
    private final AnimationDefinition idleOnGroundUnderWaterAnimation;
    private final AnimationDefinition idleUnderWaterAnimation;
    private final AnimationDefinition playDeadAnimation;

    public BabyAxolotlModel(ModelPart root) {
        super(root);
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightHindLeg = this.body.getChild("right_hind_leg");
        this.leftHindLeg = this.body.getChild("left_hind_leg");
        this.rightFrontLeg = this.body.getChild("right_front_leg");
        this.leftFrontLeg = this.body.getChild("left_front_leg");
        this.tail = this.body.getChild("tail");
        this.topGills = this.head.getChild("top_gills");
        this.leftGills = this.head.getChild("left_gills");
        this.rightGills = this.head.getChild("right_gills");
        this.swimAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_SWIM;
        this.walkAnimation = BabyAxolotlAnimation.AXOLOTL_WALK_FLOOR;
        this.walkUnderwaterAnimation = BabyAxolotlAnimation.WALK_FLOOR_UNDERWATER;
        this.idleUnderWaterAnimation = BabyAxolotlAnimation.IDLE_UNDERWATER;
        this.idleOnGroundUnderWaterAnimation = BabyAxolotlAnimation.IDLE_FLOOR_UNDERWATER;
        this.idleOnGroundAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_IDLE_FLOOR;
        this.playDeadAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_PLAY_DEAD;
        this.animationProxy = new HierarchicalModel<T>() {
            @Override @NotNull public ModelPart root() { return BabyAxolotlModel.this.root; }
            @Override public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
        };
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
//        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.75F, -2.75F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(0.0F, -1.75F, -2.75F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.25F, 1.75F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, -2.75F));
        head.addOrReplaceChild("top_gills", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -2.0F));
        head.addOrReplaceChild("left_gills", CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, -3.5F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -0.5F, -2.0F));
        head.addOrReplaceChild("right_gills", CubeListBuilder.create().texOffs(20, 3).addBox(-3.0F, -3.5F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -0.5F, -2.0F));
        PartDefinition right_leg = body.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 0.25F, 1.75F, 0.0F, 1.5708F, 1.5708F));
        right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(20, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.01F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));
        body.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(20, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.01F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.25F, 1.75F));
        body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(20, 16).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.01F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.25F, -1.25F));
        body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.01F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.25F, -1.25F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(10, 9).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 3.25F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float pAlpha) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.4F, 0.0F);
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, pAlpha);
        poseStack.popPose();
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
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        if (entity instanceof IAxolotlAnimationAccess ext) {
            this.animateWalk(this.walkAnimation, limbSwing, limbSwingAmount, 15.0f, 30.0f);
            this.animate(ext.bs$getSwimAnimationState(), this.swimAnimation, ageInTicks);
            this.animate(ext.bs$getWalkUnderWaterAnimationState(), this.walkUnderwaterAnimation, ageInTicks);
            this.animate(ext.bs$getIdleOnGroundAnimationState(), this.idleOnGroundAnimation, ageInTicks);
            this.animate(ext.bs$getIdleUnderWaterAnimationState(), this.idleUnderWaterAnimation, ageInTicks);
            this.animate(ext.bs$getIdleUnderWaterOnGroundAnimationState(), this.idleOnGroundUnderWaterAnimation, ageInTicks);
            this.animate(ext.bs$getPlayDeadAnimationState(), this.playDeadAnimation, ageInTicks);
        }

        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    protected void animate(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks) {
        this.animate(animationState, animationDefinition, ageInTicks, 1.0F);
    }

    protected void animate(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks, float speed) {
        animationState.updateTime(ageInTicks, speed);
        animationState.ifStarted((state) -> {
            KeyframeAnimations.animate(this.animationProxy, animationDefinition, state.getAccumulatedTime(), 1.0F, new Vector3f());
        });
    }

    protected void animateWalk(AnimationDefinition animationDefinition, float position, float speed, float maxSpeed, float scaleFactor) {
        // This math mimics the new 1.21 SpringAnimation/WalkAnimation logic
        // position = total distance walked (limbSwing)
        // speed = current speed (limbSwingAmount)

        long accumulatedTime = (long)(position * 50.0F * scaleFactor);
        float intensity = Math.min(speed, maxSpeed); // Cap the intensity

        // We always animate the walk, but scale it by speed (intensity)
        KeyframeAnimations.animate(this.animationProxy, animationDefinition, accumulatedTime, intensity, new Vector3f());
    }
}
