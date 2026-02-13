package com.botrom.babysteps.utils;

import com.botrom.babysteps.BabySteps;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = BabySteps.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BabyConfig {

    public static boolean enableBabyBee;
    public static boolean enableBabyCat;
    public static boolean enableBabyChicken;
    public static boolean enableBabyCow;
    public static boolean enableBabyFox;
    public static boolean enableBabyGoat;
    public static boolean enableBabyPig;
    public static boolean enableAdultRabbitModel;
    public static boolean enableBabyRabbitModel;
    public static boolean enableBabySheep;
    public static boolean enableBabyTurtle;

    public static boolean enableAdultRabbitChanges;
    public static boolean enableBabyRabbitChanges;
    public static boolean enableBabyDolphin;
    public static boolean enableBabySquid;
    public static boolean enableGoldenDandelion;

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue enableBabyBee;
        public final ForgeConfigSpec.BooleanValue enableBabyCat;
        public final ForgeConfigSpec.BooleanValue enableBabyChicken;
        public final ForgeConfigSpec.BooleanValue enableBabyCow;
        public final ForgeConfigSpec.BooleanValue enableBabyFox;
        public final ForgeConfigSpec.BooleanValue enableBabyGoat;
        public final ForgeConfigSpec.BooleanValue enableBabyPig;
        public final ForgeConfigSpec.BooleanValue enableAdultRabbitModel;
        public final ForgeConfigSpec.BooleanValue enableBabyRabbitModel;
        public final ForgeConfigSpec.BooleanValue enableBabySheep;
        public final ForgeConfigSpec.BooleanValue enableBabyTurtle;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("rendering");

            enableBabyBee = builder
                    .comment("Whether to use the new baby model for bees.")
                    .define("enableBabyBee", true);

            enableBabyCat = builder
                    .comment("Whether to use the new baby model for cats.")
                    .define("enableBabyCat", true);

            enableBabyChicken = builder
                    .comment("Whether to use the new baby model for chickens.")
                    .define("enableBabyChicken", true);

            enableBabyCow = builder
                    .comment("Whether to use the new baby model for cows.")
                    .define("enableBabyCow", true);

            enableBabyFox = builder
                    .comment("Whether to use the new baby model for foxes.")
                    .define("enableBabyFox", true);

            enableBabyGoat = builder
                    .comment("Whether to use the new baby model for goats.")
                    .define("enableBabyGoat", true);

            enableBabyPig = builder
                    .comment("Whether to use the new baby model for pigs.")
                    .define("enableBabyPig", true);

            enableAdultRabbitModel = builder
                    .comment("Whether to use the new model for adult rabbits.\nWarning: It will look weird if the respective server side config is disabled!")
                    .define("enableAdultRabbitModel", true);

            enableBabyRabbitModel = builder
                    .comment("Whether to use the new baby model for rabbits.\nWarning: It will look weird if the respective server side config is disabled!")
                    .define("enableBabyRabbitModel", true);

            enableBabySheep = builder
                    .comment("Whether to use the new baby model for sheep.")
                    .define("enableBabySheep", true);

            enableBabyTurtle = builder
                    .comment("Whether to use the new baby model for turtles.")
                    .define("enableBabyTurtle", true);

            builder.pop();
        }
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue enableBabyDolphin;
        public final ForgeConfigSpec.BooleanValue enableAdultRabbitChanges;
        public final ForgeConfigSpec.BooleanValue enableBabyRabbitChanges;
        public final ForgeConfigSpec.BooleanValue enableBabySquid;
        public final ForgeConfigSpec.BooleanValue enableGoldenDandelion;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("mechanics");

            enableBabyDolphin = builder
                    .comment("Whether to enable the aging of dolphins. This is required for baby dolphins to exist.")
                    .define("enableBabyDolphin", true);

            enableAdultRabbitChanges = builder
                    .comment("Whether to enable the mixin changes on adult rabbits, which adds the new animation and changes the hopping height.")
                    .define("enableAdultRabbitChanges", true);

            enableBabyRabbitChanges = builder
                    .comment("Whether to enable the mixin changes on baby rabbits, which adds the new animation and changes the hopping height.")
                    .define("enableBabyRabbitChanges", true);

            enableBabySquid = builder
                    .comment("Whether to enable the aging of squids. This is required for baby squids to exist.")
                    .define("enableBabySquid", true);

            enableGoldenDandelion = builder
                    .comment("Whether to enable age locking in the game through golden dandelions. The item stays in the game.\nWarning: Disabling this will make all babies resume aging!")
                    .define("enableGoldenDandelion", true);

            builder.pop();
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event.getConfig());
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        updateConfig(event.getConfig());
    }

    private static void updateConfig(final ModConfig config) {
        if (config.getSpec() == CLIENT_SPEC) {
            enableBabyBee = CLIENT.enableBabyBee.get();
            enableBabyCat = CLIENT.enableBabyCat.get();
            enableBabyChicken = CLIENT.enableBabyChicken.get();
            enableBabyCow = CLIENT.enableBabyCow.get();
            enableBabyFox = CLIENT.enableBabyFox.get();
            enableBabyGoat = CLIENT.enableBabyGoat.get();
            enableBabyPig = CLIENT.enableBabyPig.get();
            enableAdultRabbitModel = CLIENT.enableAdultRabbitModel.get();
            enableBabyRabbitModel = CLIENT.enableBabyRabbitModel.get();
            enableBabySheep = CLIENT.enableBabySheep.get();
            enableBabyTurtle = CLIENT.enableBabyTurtle.get();
        }

        if (config.getSpec() == COMMON_SPEC) {
            enableBabyDolphin = COMMON.enableBabyDolphin.get();
            enableAdultRabbitChanges = COMMON.enableAdultRabbitChanges.get();
            enableBabyRabbitChanges = COMMON.enableBabyRabbitChanges.get();
            enableBabySquid = COMMON.enableBabySquid.get();
            enableGoldenDandelion = COMMON.enableGoldenDandelion.get();
        }
    }
}