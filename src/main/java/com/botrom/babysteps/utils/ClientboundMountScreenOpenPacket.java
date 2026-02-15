package com.botrom.babysteps.utils;

import com.botrom.babysteps.common.entities.AbstractNautilus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMountScreenOpenPacket {
    private final int containerId;
    private final int inventoryColumns;
    private final int entityId;

    public ClientboundMountScreenOpenPacket(int containerId, int inventoryColumns, int entityId) {
        this.containerId = containerId;
        this.inventoryColumns = inventoryColumns;
        this.entityId = entityId;
    }

    public ClientboundMountScreenOpenPacket(FriendlyByteBuf buffer) {
        this.containerId = buffer.readInt();
        this.inventoryColumns = buffer.readVarInt();
        this.entityId = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.containerId);
        buffer.writeVarInt(this.inventoryColumns);
        buffer.writeInt(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> { // Double lambda???
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);

            if (entity instanceof AbstractNautilus nautilus) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                NautilusInventoryMenu menu = new NautilusInventoryMenu(containerId, player.getInventory(), nautilus.getInventory(), nautilus, inventoryColumns);
                player.containerMenu = menu;
                Minecraft.getInstance().setScreen(new NautilusScreen(menu, player.getInventory(), nautilus));
            }
        }));
        context.get().setPacketHandled(true);
    }
}
