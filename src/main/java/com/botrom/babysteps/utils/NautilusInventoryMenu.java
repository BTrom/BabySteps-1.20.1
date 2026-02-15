package com.botrom.babysteps.utils;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.common.NautilusArmorItem;
import com.botrom.babysteps.common.entities.AbstractNautilus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SaddleItem;
import org.jetbrains.annotations.NotNull;

public class NautilusInventoryMenu extends AbstractMountInventoryMenu {
    private static final ResourceLocation SADDLE_SLOT_SPRITE = new ResourceLocation(BabySteps.MOD_ID, "gui/sprites/container/slot/saddle");
    private static final ResourceLocation ARMOR_SLOT_SPRITE = new ResourceLocation(BabySteps.MOD_ID, "gui/sprites/container/slot/nautilus_armor_slot");

    public NautilusInventoryMenu(int containerId, Inventory playerInventory, Container mountInventory, final AbstractNautilus nautilus, int columns) {
        super(containerId, playerInventory, mountInventory, nautilus);

        // SLOT 0: Saddle
        this.addSlot(new Slot(mountInventory, 0, 8, 18) {
            @Override
            public boolean isActive() {
                return !nautilus.isBaby() && nautilus.isAlive() && nautilus.isSaddleable();
            }
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof SaddleItem && nautilus.isSaddleable();
            }
//            @Override
//            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
//                return Pair.of(InventoryMenu.BLOCK_ATLAS, SADDLE_SLOT_SPRITE);
//            }
        });

        // SLOT 1: Armor
        this.addSlot(new Slot(mountInventory, 1, 8, 36) {
            @Override
            public boolean isActive() {
                return !nautilus.isBaby() && nautilus.isAlive();
            }
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof NautilusArmorItem;
            }
//            @Override
//            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
//                return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_SPRITE);
//            }
        });

        if (columns > 0) {
            for (int k = 0; k < 3; ++k) { // 3 Rows
                for (int l = 0; l < columns; ++l) { // Columns (usually 9)
                    this.addSlot(new Slot(mountInventory, 2 + l + k * columns, 80 + l * 18, 18 + k * 18));
                }
            }
        }

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    protected boolean hasInventoryChanged(Container container) {
        return ((AbstractNautilus)this.mount).hasInventoryChanged(container);
    }
}