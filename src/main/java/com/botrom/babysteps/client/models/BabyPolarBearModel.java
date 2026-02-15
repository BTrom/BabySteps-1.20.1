package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.PolarBear;

import java.util.Collections;

public class BabyPolarBearModel<T extends PolarBear> extends PolarBearModel<T> {
    private final ModelPart root;

    public BabyPolarBearModel(final ModelPart root) {
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-4.0F, -3.5F, -6.0F, 8.0F, 7.0F, 12.0F), PartPose.offset(0.0F, 17.5F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.625F, -4.25F, 6.0F, 5.0F, 4.0F).texOffs(20, 3).addBox(-2.0F, 0.375F, -6.25F, 4.0F, 2.0F, 2.0F).texOffs(20, 0).addBox(-4.0F, -3.625F, -2.75F, 2.0F, 2.0F, 1.0F).texOffs(26, 0).addBox(2.0F, -3.625F, -2.75F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 18.625F, -5.75F));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(-2.5F, 21.5F, 4.5F));
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(12, 34).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(2.5F, 21.5F, 4.5F));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 28).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(-2.5F, 21.5F, -4.5F));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(12, 28).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(2.5F, 21.5F, -4.5F));
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
    public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        super.setupAnim(entity, pLimbSwing, pLimbSwingAmount, ageInTicks, pNetHeadYaw, pHeadPitch);
        this.body.xRot = 0.0F;

        // The parent moves the legs/head to Adult Y/Z coords. We force them back to Baby coords.
        // (These values match your createBodyLayer definitions)
        this.head.setPos(0.0F, 18.625F, -5.75F);
        this.body.setPos(0.0F, 17.5F, 0.0F);
        this.rightHindLeg.setPos(-2.5F, 21.5F, 4.5F);
        this.leftHindLeg.setPos(2.5F, 21.5F, 4.5F);
        this.rightFrontLeg.setPos(-2.5F, 21.5F, -4.5F);
        this.leftFrontLeg.setPos(2.5F, 21.5F, -4.5F);

        // 4. Now apply your custom Baby Rearing Animation
        // (This logic is safe now because we reset the baseline positions above)
        float partialTick = ageInTicks - entity.tickCount;
        float standScale = entity.getStandingAnimationScale(partialTick) * entity.getStandingAnimationScale(partialTick);
        float bodyAgeScale = entity.getScale();

        this.body.xRot -= standScale * 3.1415927F * 0.35F;
        this.body.y += standScale * bodyAgeScale * 2.0F;

        this.rightFrontLeg.y -= standScale * bodyAgeScale * 20.0F;
        this.rightFrontLeg.z += standScale * bodyAgeScale * 4.0F;

        // Note: The parent 'super' call ALREADY added rearing rotation to the legs.
        // If you find the legs rotate 'too much' during standing, comment out the next line.
        this.rightFrontLeg.xRot -= standScale * 3.1415927F * 0.45F;

        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        this.leftFrontLeg.xRot -= standScale * 3.1415927F * 0.45F; // Same note as above

        this.head.y -= standScale * 24.0F;
        this.head.z += standScale * 13.0F;
        this.head.xRot += standScale * 3.1415927F * 0.15F;
    }
}
