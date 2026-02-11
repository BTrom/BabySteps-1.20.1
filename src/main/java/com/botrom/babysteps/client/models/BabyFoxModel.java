package com.botrom.babysteps.client.models;

import com.botrom.babysteps.client.animations.BabyFoxAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Collections;

public class BabyFoxModel<T extends Fox> extends FoxModel<T> {
    public final ModelPart head;
    protected final ModelPart root;
    protected final ModelPart body;
    protected final ModelPart rightHindLeg;
    protected final ModelPart leftHindLeg;
    protected final ModelPart rightFrontLeg;
    protected final ModelPart leftFrontLeg;
    protected final ModelPart tail;
    private float legMotionPos;
    private final AnimationDefinition babyWalkAnimation;

    public BabyFoxModel(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = this.body.getChild("tail");
        this.babyWalkAnimation = BabyFoxAnimation.FOX_BABY_WALK;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.125F, -5.125F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(18, 20).addBox(-1.0F, 0.875F, -7.125F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(22, 8).addBox(-3.0F, -4.125F, -4.125F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(22, 11).addBox(1.0F, -4.125F, -4.125F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.125F, 0.125F));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 2.0F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(22, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.0F, 4.0F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 22.0F, 4.0F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(22, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.0F, 0.0F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 22.0F, 0.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -1.48F, -1.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 3.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        float partialTick = ageInTicks - entity.tickCount;

        this.setWalkingPose(entity, partialTick);

        if (entity.isCrouching()) {
            this.setCrouchingPose(entity, ageInTicks, partialTick);
        } else if (entity.isSleeping()) {
            this.setSleepingPose();
        } else if (entity.isSitting()) {
            setSittingPose();
            this.body.xRot = -0.959931F;
            this.body.z -= 4.5F * entity.getScale();
            this.body.y += 3.0F * entity.getScale();
            this.tail.y -= 0.6F;
            this.tail.z -= 2.0F * entity.getScale();
            this.tail.xRot = 0.95993114F;
            this.head.y -= 0.75F;
            this.head.z += 0.0F;
            this.rightFrontLeg.xRot = -0.2617994F;
            this.leftFrontLeg.xRot = -0.2617994F;
            this.rightFrontLeg.z -= 1.0F;
            this.leftFrontLeg.z -= 1.0F;
            this.rightFrontLeg.x += 0.01F;
            this.leftFrontLeg.x -= 0.01F;
            this.rightHindLeg.z -= 3.75F;
            this.leftHindLeg.z -= 3.75F;
            this.rightHindLeg.x += 0.01F;
            this.leftHindLeg.x -= 0.01F;
        }

        if (entity.isPouncing()) {
            this.setPouncingPose();
        }

        if (!entity.isSleeping() && !entity.isFaceplanted() && !entity.isCrouching()) {
            this.head.xRot = headPitch * ((float)Math.PI / 180F);
            this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        }

        if (entity.isSleeping()) {
            this.head.xRot = 0.0F;
            this.head.yRot = -2.0943952F;
            this.head.zRot = Mth.cos(ageInTicks * 0.027F) / 22.0F;
        }

        if (entity.isFaceplanted()) {
            this.legMotionPos += 0.67F;
            this.rightHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F) * 0.1F;
            this.leftHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F + 3.1415927F) * 0.1F;
            this.rightFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F + 3.1415927F) * 0.1F;
            this.leftFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F) * 0.1F;
        } else {
            this.legMotionPos = 0.0F;
        }
    }

    protected void setSleepingPose() {
        this.rightHindLeg.visible = false;
        this.leftHindLeg.visible = false;
        this.rightFrontLeg.visible = false;
        this.leftFrontLeg.visible = false;
        this.body.zRot = -1.5707964F;
        this.body.xRot = -0.17453292F;
        ++this.body.y;
        this.body.z += 2.5F;
        this.body.x += 2.5F;
        this.tail.xRot = -2.1816616F;
        this.tail.x -= 0.7F;
        this.tail.z += 0.6F;
        this.tail.y += 0.9F;
        this.head.x += 2.0F;
        this.head.y += 2.8F;
        this.head.yRot = -2.0943952F;
        this.head.zRot = 0.0F;
    }

    protected void setWalkingPose(T entity, float partialTick) {
        this.head.zRot = entity.getHeadRollAngle(partialTick);
        this.rightHindLeg.visible = true;
        this.leftHindLeg.visible = true;
        this.rightFrontLeg.visible = true;
        this.leftFrontLeg.visible = true;

        float walkPos = entity.walkAnimation.position();
        float walkSpeed = entity.walkAnimation.speed();

        // 1.21 uses a 2.5F multiplier for speed in applyWalk.
        // We multiply the time accumulator by 2.5F to match that foot speed.
        KeyframeAnimations.animate(
                new HierarchicalModel<T>() {
                    @Override public @NotNull ModelPart root() { return BabyFoxModel.this.root; }
                    @Override public void setupAnim(@NotNull T e, float f, float f1, float f2, float f3, float f4) {}
                },
                babyWalkAnimation,
                (long)(walkPos * 50.0F * 2.5F), // Multiplied speed factor
                walkSpeed,
                new Vector3f()
        );
    }

    protected void setCrouchingPose(T entity, float ageInTicks, float partialTick) {
        this.body.xRot += 0.10471976F;
        this.head.y += entity.getCrouchAmount(partialTick) * entity.getScale();
        float wiggleAmount = Mth.cos(ageInTicks) * 0.05F;
        this.body.yRot = wiggleAmount;
        this.rightHindLeg.zRot = wiggleAmount;
        this.leftHindLeg.zRot = wiggleAmount;
        this.rightFrontLeg.zRot = wiggleAmount / 2.0F;
        this.leftFrontLeg.zRot = wiggleAmount / 2.0F;
        this.body.y += entity.getCrouchAmount(partialTick) / 6.0F;
    }

    protected void setSittingPose() {
        this.head.xRot = 0.0F;
        this.head.yRot = 0.0F;
    }

    protected void setPouncingPose() {

    }
}
