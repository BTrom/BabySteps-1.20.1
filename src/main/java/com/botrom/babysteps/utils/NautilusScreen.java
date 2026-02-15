package com.botrom.babysteps.utils;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.common.entities.AbstractNautilus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NautilusScreen extends AbstractContainerScreen<NautilusInventoryMenu> {
    private static final ResourceLocation SLOT_SPRITE = new ResourceLocation(BabySteps.MOD_ID, "textures/gui/nautilus_slots");
    private static final ResourceLocation NAUTILUS_INVENTORY_LOCATION = new ResourceLocation(BabySteps.MOD_ID, "textures/gui/nautilus.png");
    private final AbstractNautilus nautilus;
    private float xMouse;
    private float yMouse;

    public NautilusScreen(NautilusInventoryMenu menu, Inventory inventory, AbstractNautilus entity) {
        super(menu, inventory, entity.getDisplayName());
        this.nautilus = entity;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int x, int y, float partialTick) {
        this.renderBackground(graphics);
        this.xMouse = (float)x;
        this.yMouse = (float)y;
        super.render(graphics, x, y, partialTick);
        this.renderTooltip(graphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        int $$4 = (this.width - this.imageWidth) / 2;
        int $$5 = (this.height - this.imageHeight) / 2;
        graphics.blit(NAUTILUS_INVENTORY_LOCATION, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);
        if (this.nautilus instanceof AbstractNautilus) {
            AbstractNautilus $$6 = this.nautilus;
            if ($$6.isSaddled()) {
                graphics.blit(NAUTILUS_INVENTORY_LOCATION, $$4 + 79, $$5 + 17, 0, this.imageHeight, $$6.getInventoryColumns() * 18, 54);
            }
        }
        if (this.nautilus.isSaddleable()) {
            graphics.blit(NAUTILUS_INVENTORY_LOCATION, $$4 + 7, $$5 + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }
        graphics.blit(NAUTILUS_INVENTORY_LOCATION, $$4 + 7, $$5 + 35, 0, this.imageHeight + 54, 18, 18);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, $$4 + 51, $$5 + 60, 17, (float)($$4 + 51) - this.xMouse, (float)($$5 + 75 - 50) - this.yMouse, this.nautilus);
    }
}
