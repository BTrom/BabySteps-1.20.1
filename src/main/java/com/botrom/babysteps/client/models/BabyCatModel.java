package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cat;

import java.util.Collections;

public class BabyCatModel<T extends Cat> extends CatModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    public BabyCatModel(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, -2.875F, 5.0F, 4.0F, 4.0F).texOffs(18, 0).addBox(-2.0F, -4.0F, -0.875F, 1.0F, 1.0F, 2.0F).texOffs(24, 0).addBox(1.0F, -4.0F, -0.875F, 1.0F, 1.0F, 2.0F).texOffs(18, 3).addBox(-1.5F, -1.0F, -3.875F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 20.0F, -3.125F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -1.5F, -3.5F, 4.0F, 3.0F, 7.0F), PartPose.offset(0.0F, 20.5F, 0.5F));
        partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -0.107F, 0.0849F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 19.107F, 3.9151F, -0.567232F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(18, 22).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(1.0F, 22.0F, 2.5F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(12, 22).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(-1.0F, 22.0F, 2.5F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(18, 18).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(1.0F, 22.0F, -1.5F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(12, 18).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(-1.0F, 22.0F, -1.5F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(1.25F, 1.25F, 1.25F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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

        float partialTick = ageInTicks - entity.tickCount;

        if (entity.isCrouching()) {
            this.body.y += 1.0F;
            this.head.y += 2.0F;
            this.tail1.y += 1.0F;
            this.tail2.y += -4.0F;
        }

        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = netHeadYaw * 0.017453292F;

        if (!entity.isInSittingPose()) {
            float animationSpeed = entity.walkAnimation.speed(partialTick);
            float animationPos = entity.walkAnimation.position(partialTick) * 2.0F;
            if (entity.isSprinting()) {
                this.leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
                this.rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F + 0.3F) * animationSpeed;
                this.leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + 3.1415927F + 0.3F) * animationSpeed;
                this.rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + 3.1415927F) * animationSpeed;
                this.tail2.xRot = 1.7278761F + 0.31415927F * Mth.cos(animationPos) * animationSpeed;
            } else {
                this.leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
                this.rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F + 3.1415927F) * animationSpeed;
                this.leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + 3.1415927F) * animationSpeed;
                this.rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
                if (!entity.isCrouching()) {
                    this.tail2.xRot = 1.7278761F + 0.7853982F * Mth.cos(animationPos) * animationSpeed;
                } else {
                    this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(animationPos) * animationSpeed;
                }
            }
        } else if (entity.isInSittingPose()) {
            this.body.xRot += -0.43633232F;
            ++this.body.y;
            this.head.z += 0.75F;
            this.tail1.xRot += 0.5454154F;
            this.tail1.y += 4.0F;
            this.tail1.z -= 0.9F;
            this.leftHindLeg.z -= 0.9F;
            this.rightHindLeg.z -= 0.9F;
        }

        if (entity.getLieDownAmount(partialTick) > 0.0F) {
            ++this.body.x;
            this.head.xRot = Mth.rotLerp(entity.getLieDownAmount(partialTick), this.head.xRot, 0.17453292F);
            this.head.zRot = Mth.rotLerp(entity.getLieDownAmount(partialTick), this.head.zRot, -1.3089969F);
            ++this.head.x;
            this.head.y += 0.75F;
            this.head.z -= 0.5F;
            this.rightFrontLeg.xRot = -0.7853982F;
            this.rightFrontLeg.x += 3.5F;
            this.rightFrontLeg.y -= 0.5F;
            this.rightFrontLeg.z += 0.0F;
            this.leftFrontLeg.xRot = -1.5707964F;
            ++this.leftFrontLeg.x;
            --this.leftFrontLeg.y;
            this.leftFrontLeg.z -= 2.0F;
            this.rightHindLeg.xRot = 0.6981317F;
            this.rightHindLeg.yRot = 0.34906584F;
            this.rightHindLeg.zRot = -0.34906584F;
            this.rightHindLeg.x += 2.5F;
            this.rightHindLeg.y -= 0.25F;
            this.rightHindLeg.z += 0.5F;
            ++this.leftHindLeg.x;
            --this.leftHindLeg.z;
            this.tail1.xRot += Mth.rotLerp(entity.getLieDownAmountTail(partialTick), this.tail1.xRot, -0.5235988F);
            this.tail1.yRot += Mth.rotLerp(entity.getLieDownAmountTail(partialTick), this.tail1.yRot, 0.0F);
            this.tail1.zRot += Mth.rotLerp(entity.getLieDownAmountTail(partialTick), this.tail1.zRot, -0.17453292F);
            ++this.tail1.x;
            this.tail1.y += 0.5F;
            this.tail1.z -= 0.25F;
        }

        if (entity.getRelaxStateOneAmount(partialTick) > 0.0F) {
            this.head.xRot = Mth.rotLerp(entity.getRelaxStateOneAmount(partialTick), this.head.xRot, -0.58177644F);
        }
    }
}
