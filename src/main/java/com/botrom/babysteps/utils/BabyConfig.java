package com.botrom.babysteps.utils;

import com.botrom.babysteps.BabySteps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = BabySteps.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BabyConfig {

    public static boolean enableBabyBee;
    public static boolean enableBabyCat;
    public static boolean enableBabyChicken;
    public static boolean enableBabyCow;
    public static boolean enableBabyDolphin;
    public static boolean enableBabyFox;
    public static boolean enableBabyGoat;
    public static boolean enableBabyPig;
    public static boolean enableAdultRabbit;
    public static boolean enableBabyRabbit;
    public static boolean enableBabySheep;
    public static boolean enableBabySquid;
    public static boolean enableBabyTurtle;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_BEE = BUILDER
            .comment("Whether to use the new baby model for bees.")
            .define("enableBabyBee", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_CAT = BUILDER
            .comment("Whether to use the new baby model for cats.")
            .define("enableBabyCat", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_CHICKEN = BUILDER
            .comment("Whether to use the new baby model for chickens.")
            .define("enableBabyChicken", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_COW = BUILDER
            .comment("Whether to use the new baby model for cows.")
            .define("enableBabyCow", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_DOLPHIN = BUILDER
            .comment("Whether to use the new baby model for dolphins.")
            .define("enableBabyDolphin", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_FOX = BUILDER
            .comment("Whether to use the new baby model for foxes.")
            .define("enableBabyFox", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_GOAT = BUILDER
            .comment("Whether to use the new baby model for goats.")
            .define("enableBabyGoat", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_PIGS = BUILDER
            .comment("Whether to use the new baby model for pigs.")
            .define("enableBabyPig", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_ADULT_RABBIT = BUILDER
            .comment("Whether to use the new model for adult rabbits.")
            .define("enableAdultRabbit", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_BABY_RABBIT = BUILDER
            .comment("Whether to use the new baby model for rabbits.")
            .define("enableBabyRabbit", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_SHEEP = BUILDER
            .comment("Whether to use the new baby model for sheeps.")
            .define("enableBabySheep", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_SQUID = BUILDER
            .comment("Whether to use the new baby model for squids.")
            .define("enableBabySquid", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_TURTLE = BUILDER
            .comment("Whether to use the new baby model for turtles.")
            .define("enableBabyTurtle", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableBabyBee = ENABLE_BEE.get();
        enableBabyCat = ENABLE_CAT.get();
        enableBabyChicken = ENABLE_CHICKEN.get();
        enableBabyCow = ENABLE_COW.get();
        enableBabyDolphin = ENABLE_DOLPHIN.get();
        enableBabyFox = ENABLE_FOX.get();
        enableBabyGoat = ENABLE_GOAT.get();
        enableBabyPig = ENABLE_PIGS.get();
        enableAdultRabbit = ENABLE_ADULT_RABBIT.get();
        enableBabyRabbit = ENABLE_BABY_RABBIT.get();
        enableBabySheep = ENABLE_SHEEP.get();
        enableBabySquid = ENABLE_SQUID.get();
        enableBabyTurtle = ENABLE_TURTLE.get();
    }
}