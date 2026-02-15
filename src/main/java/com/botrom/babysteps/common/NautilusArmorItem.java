package com.botrom.babysteps.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class NautilusArmorItem extends Item {
    private final int protection;

    public NautilusArmorItem(Properties properties, int protection) {
        super(properties);
        this.protection = protection;
    }

    public int getProtection() {
        return this.protection;
    }
}