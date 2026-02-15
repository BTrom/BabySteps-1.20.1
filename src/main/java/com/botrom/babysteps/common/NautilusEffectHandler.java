package com.botrom.babysteps.common;

import com.botrom.babysteps.BabySteps;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.WeakHashMap;

@Mod.EventBusSubscriber
public class NautilusEffectHandler {

    private static final WeakHashMap<Player, Integer> AIR_CACHE = new WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player.isEyeInFluid(FluidTags.WATER) && player.hasEffect(BabySteps.BabyEffects.BREATH_OF_THE_NAUTILUS.get())) {

            if (event.phase == TickEvent.Phase.START) {
                AIR_CACHE.put(player, player.getAirSupply());

            } else if (event.phase == TickEvent.Phase.END) {
                if (AIR_CACHE.containsKey(player)) {
                    int previousAir = AIR_CACHE.get(player);

                    if (player.getAirSupply() < previousAir) {
                        player.setAirSupply(previousAir);
                    }
                }
            }
        }
    }
}