package com.botrom.babysteps.utils;

import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMountInventoryMenu extends AbstractContainerMenu {
    protected final Container mountContainer;
    protected final LivingEntity mount;
    protected final int SLOT_SADDLE = 0;
    protected final int SLOT_BODY_ARMOR = 1;
    protected final int SLOT_INVENTORY_START = 2;
    protected static final int INVENTORY_ROWS = 3;

    protected AbstractMountInventoryMenu(int menuId, Inventory playerInventory, Container mountInventory, LivingEntity mount) {
        super(null, menuId);
        this.mountContainer = mountInventory;
        this.mount = mount;
        mountInventory.startOpen(playerInventory.player);
    }

    protected abstract boolean hasInventoryChanged(Container container);

    @Override
    public boolean stillValid(Player player) {
        return !this.hasInventoryChanged(this.mountContainer) && this.mountContainer.stillValid(player) && this.mount.isAlive() && isWithinEntityInteractionRange(player, this.mount, 4.0);
    }

    // Added this function because the one above requires it, and reformatted to add the player as an argument since it's the class where it came from
    public boolean isWithinEntityInteractionRange(Player player, LivingEntity entity, double range) {
        double d0 = 3.0D + range;
        double d1 = entity.distanceToSqr(player.getEyePosition());
        return d1 < d0 * d0;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.mountContainer.stopOpen(player);
    }

    protected void addStandardInventorySlots(Inventory playerInventory, int x, int y) {
        // Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x + col * 18, y + row * 18));
            }
        }
        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, x + col * 18, y + 58));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player p_456705_, int p_457058_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_457058_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = 2 + this.mountContainer.getContainerSize();
            if (p_457058_ < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1) && !this.getSlot(0).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.mountContainer.getContainerSize() == 0 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (p_457058_ >= j && p_457058_ < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_457058_ >= i && p_457058_ < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public static int getInventorySize(int p_457364_) {
        return p_457364_ * 3;
    }
}