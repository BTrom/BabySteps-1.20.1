package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.Collections;

public class BabyChickenModel<T extends Entity> extends ChickenModel<T> {
    private final ModelPart root;
    protected final ModelPart rightLeg;
    protected final ModelPart leftLeg;
    protected final ModelPart rightWing;
    protected final ModelPart leftWing;

    public BabyChickenModel(ModelPart root) {
        super(root);
        this.root = root;
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // We override the default AgeableListModel/ChickenModel rendering to use our custom root.
        // This ensures the whole hierarchical tree (baby or adult) is rendered correctly.
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /**
     * Disable the old "AgeableListModel" body parts system since we render root directly.
     */
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
        // BACKPORT NOTES:
        // 1.21 uses 'state.walkAnimationPos' -> 1.20 'limbSwing'
        // 1.21 uses 'state.walkAnimationSpeed' -> 1.20 'limbSwingAmount'
        // 1.21 uses 'state.flap' (complex calculation) -> 1.20 'ageInTicks'

        // In 1.20 ChickenRenderer, the 'ageInTicks' argument IS the flap rotation value
        // calculated by getBob(). So we can use it directly for the wings.

        // Leg Animation (Identical to 1.21 logic, just using different variable names)
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;

        // Wing Animation
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // NEW BABY MESH (1.21)
        // Notice: No separate "head". The head is part of the "body" group here.
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0F, -2.25F, -0.75F, 4.0F, 4.0F, 4.0F) // Head/Body main
                        .texOffs(10, 8).addBox(-1.0F, -0.25F, -1.75F, 2.0F, 1.0F, 1.0F), // Beak
                PartPose.offset(0.0F, 20.25F, -1.25F));

        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(2, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F)
                        .texOffs(0, 1).addBox(-0.5F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F),
                PartPose.offset(1.0F, 22.0F, 0.5F));

        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F)
                        .texOffs(0, 0).addBox(-0.5F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F),
                PartPose.offset(-1.0F, 22.0F, 0.5F));

        root.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(6, 8).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F),
                PartPose.offset(2.0F, 20.0F, 0.0F));

        root.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(4, 8).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F),
                PartPose.offset(-2.0F, 20.0F, 0.0F));

        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("red_thing", CubeListBuilder.create(), PartPose.ZERO);
        // Note: 16x16 texture size for the baby!
        return LayerDefinition.create(mesh, 16, 16);
    }
}