package com.botrom.babysteps.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class BabySquidModel<T extends Entity> extends SquidModel<T> {
    private final ModelPart root;

    public BabySquidModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float pAlpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, pAlpha);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));
        CubeListBuilder tentacle = CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F);

        for(int i = 0; i < 8; ++i) {
            double angle = (double)i * Math.PI * 2.0 / 8.0;
            float x = (float)Math.cos(angle) * 3.0F;
            float y = 12.5F;
            float z = (float)Math.sin(angle) * 3.0F;
            angle = (double)i * Math.PI * -2.0 / 8.0 + 1.5707963267948966;
            float yRot = (float)angle;
            root.addOrReplaceChild(createTentacleName(i), tentacle, PartPose.offsetAndRotation(x, y, z, 0.0F, yRot, 0.0F));
        }

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
