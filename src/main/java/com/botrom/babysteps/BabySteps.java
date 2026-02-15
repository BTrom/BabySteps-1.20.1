package com.botrom.babysteps;

import com.botrom.babysteps.client.ClientEvents;
import com.botrom.babysteps.client.renderers.NautilusRenderer;
import com.botrom.babysteps.common.NautilusArmorItem;
import com.botrom.babysteps.common.entities.AbstractNautilus;
import com.botrom.babysteps.common.entities.Nautilus;
import com.botrom.babysteps.utils.BabyConfig;
import com.botrom.babysteps.utils.ClientboundMountScreenOpenPacket;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
@Mod(BabySteps.MOD_ID)
public class BabySteps {
    public static final String MOD_ID = "babysteps";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BabySteps(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::clientSetup);
        modEventBus.register(new ClientEvents());

        BabyEntities.ENTITIES.register(modEventBus);
        BabyEffects.EFFECTS.register(modEventBus);
        BabyBlocks.BLOCKS.register(modEventBus);
        BabyItems.ITEMS.register(modEventBus);
        BabySounds.SOUND_EVENTS.register(modEventBus);
        BabyMemoryModules.MEMORY_MODULE_TYPES.register(modEventBus);
        BabySensorTypes.SENSOR_TYPES.register(modEventBus);

        modEventBus.addListener(this::creativeTabsInjection);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BabyConfig.CLIENT_SPEC, MOD_ID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BabyConfig.COMMON_SPEC, MOD_ID + "-common.toml");
    }

    private void clientSetup(final FMLCommonSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BabyBlocks.GOLDEN_DANDELION.get(), RenderType.cutoutMipped());
        event.enqueueWork(BabyNetwork::register);
        event.enqueueWork(() -> EntityRenderers.register(BabyEntities.NAUTILUS.get(), NautilusRenderer::new));
    }

    private void creativeTabsInjection(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            var entries = event.getEntries();
            entries.putAfter(Items.BEETROOT.getDefaultInstance(), BabyItems.GOLDEN_DANDELION.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            var entries = event.getEntries();
            entries.putAfter(Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), BabyItems.COPPER_NAUTILUS_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(BabyItems.COPPER_NAUTILUS_ARMOR.get().getDefaultInstance(), BabyItems.IRON_NAUTILUS_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(BabyItems.IRON_NAUTILUS_ARMOR.get().getDefaultInstance(), BabyItems.GOLDEN_NAUTILUS_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(BabyItems.GOLDEN_NAUTILUS_ARMOR.get().getDefaultInstance(), BabyItems.DIAMOND_NAUTILUS_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(BabyItems.DIAMOND_NAUTILUS_ARMOR.get().getDefaultInstance(), BabyItems.NETHERITE_NAUTILUS_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            var entries = event.getEntries();
            entries.putAfter(Items.GLOW_SQUID_SPAWN_EGG.getDefaultInstance(), BabyItems.NAUTILUS_SPAWN_EGG.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    public static class BabyItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
        public static final RegistryObject<Item> GOLDEN_DANDELION = ITEMS.register("golden_dandelion", () -> new BlockItem(BabyBlocks.GOLDEN_DANDELION.get(), new Item.Properties()));

        public static final RegistryObject<Item> COPPER_NAUTILUS_ARMOR = ITEMS.register("copper_nautilus_armor", () -> new NautilusArmorItem(new Item.Properties().stacksTo(1), 4));
        public static final RegistryObject<Item> IRON_NAUTILUS_ARMOR = ITEMS.register("iron_nautilus_armor", () -> new NautilusArmorItem(new Item.Properties().stacksTo(1), 5));
        public static final RegistryObject<Item> GOLDEN_NAUTILUS_ARMOR = ITEMS.register("golden_nautilus_armor", () -> new NautilusArmorItem(new Item.Properties().stacksTo(1), 7));
        public static final RegistryObject<Item> DIAMOND_NAUTILUS_ARMOR = ITEMS.register("diamond_nautilus_armor", () -> new NautilusArmorItem(new Item.Properties().stacksTo(1), 11));
        public static final RegistryObject<Item> NETHERITE_NAUTILUS_ARMOR = ITEMS.register("netherite_nautilus_armor", () -> new NautilusArmorItem(new Item.Properties().fireResistant().stacksTo(1), 19));

        public static final RegistryObject<Item> NAUTILUS_SPAWN_EGG = ITEMS.register("nautilus_spawn_egg", () -> new ForgeSpawnEggItem(BabyEntities.NAUTILUS, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));
    }

    public static class BabyBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
        public static final RegistryObject<Block> GOLDEN_DANDELION = BLOCKS.register("golden_dandelion", () -> new FlowerBlock(MobEffects.SATURATION, 1, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
    }

    public static class BabySounds {
        public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
        // Cat
        public static final RegistryObject<SoundEvent> CAT_PURR_BABY = createSoundEvent("entity/baby_cat_purr");
        public static final RegistryObject<SoundEvent> CAT_PURREOW_BABY = createSoundEvent("entity/baby_cat_purreow");
        public static final RegistryObject<SoundEvent> CAT_AMBIENT_BABY = createSoundEvent("entity/baby_cat_ambient");
        public static final RegistryObject<SoundEvent> CAT_STRAY_AMBIENT_BABY = createSoundEvent("entity/baby_cat_stray_ambient");
        public static final RegistryObject<SoundEvent> CAT_HISS_BABY = createSoundEvent("entity/baby_cat_hiss");
        public static final RegistryObject<SoundEvent> CAT_HURT_BABY = createSoundEvent("entity/baby_cat_hurt");
        public static final RegistryObject<SoundEvent> CAT_DEATH_BABY = createSoundEvent("entity/baby_cat_death");
        public static final RegistryObject<SoundEvent> CAT_EAT_BABY = createSoundEvent("entity/baby_cat_eat");
        public static final RegistryObject<SoundEvent> CAT_BEG_FOR_FOOD_BABY = createSoundEvent("entity/baby_cat_beg_for_food");
        // Chicken
        public static final RegistryObject<SoundEvent> CHICKEN_AMBIENT_BABY = createSoundEvent("entity/chicken_baby_ambient");
        public static final RegistryObject<SoundEvent> CHICKEN_HURT_BABY = createSoundEvent("entity/chicken_baby_hurt");
        public static final RegistryObject<SoundEvent> CHICKEN_DEATH_BABY = createSoundEvent("entity/chicken_baby_death");
        // Horse
        public static final RegistryObject<SoundEvent> HORSE_BREATHE_BABY = createSoundEvent("entity/horse_breathe_baby");
        public static final RegistryObject<SoundEvent> HORSE_AMBIENT_BABY = createSoundEvent("entity/horse_ambient_baby");
        public static final RegistryObject<SoundEvent> HORSE_DEATH_BABY = createSoundEvent("entity/horse_death_baby");
        public static final RegistryObject<SoundEvent> HORSE_EAT_BABY = createSoundEvent("entity/horse_eat_baby");
        public static final RegistryObject<SoundEvent> HORSE_HURT_BABY = createSoundEvent("entity/horse_hurt_baby");
        public static final RegistryObject<SoundEvent> HORSE_ANGRY_BABY = createSoundEvent("entity/horse_angry_baby");
        public static final RegistryObject<SoundEvent> HORSE_LAND_BABY = createSoundEvent("entity/horse_land_baby");
        public static final RegistryObject<SoundEvent> HORSE_STEP_BABY = createSoundEvent("entity/horse_step_baby");
        // Nautilus
        public static final RegistryObject<SoundEvent> NAUTILUS_AMBIENT = createSoundEvent("entity/nautilus_ambient");
        public static final RegistryObject<SoundEvent> NAUTILUS_AMBIENT_ON_LAND = createSoundEvent("entity/nautilus_ambient_land");
        public static final RegistryObject<SoundEvent> NAUTILUS_DASH = createSoundEvent("entity/nautilus_dash");
        public static final RegistryObject<SoundEvent> NAUTILUS_DASH_ON_LAND = createSoundEvent("entity/nautilus_dash_land");
        public static final RegistryObject<SoundEvent> NAUTILUS_DASH_READY = createSoundEvent("entity/nautilus_dash_ready");
        public static final RegistryObject<SoundEvent> NAUTILUS_DASH_READY_ON_LAND = createSoundEvent("entity/nautilus_dash_ready_land");
        public static final RegistryObject<SoundEvent> NAUTILUS_DEATH = createSoundEvent("entity/nautilus_death");
        public static final RegistryObject<SoundEvent> NAUTILUS_DEATH_ON_LAND = createSoundEvent("entity/nautilus_death_land");
        public static final RegistryObject<SoundEvent> NAUTILUS_EAT = createSoundEvent("entity/nautilus_eat");
        public static final RegistryObject<SoundEvent> NAUTILUS_HURT = createSoundEvent("entity/nautilus_hurt");
        public static final RegistryObject<SoundEvent> NAUTILUS_HURT_ON_LAND = createSoundEvent("entity/nautilus_hurt_land");
        public static final RegistryObject<SoundEvent> NAUTILUS_SWIM = createSoundEvent("entity/nautilus_swim");
        public static final RegistryObject<SoundEvent> NAUTILUS_RIDING = createSoundEvent("entity/nautilus_riding");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_AMBIENT = createSoundEvent("entity/baby_nautilus_ambient");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_AMBIENT_ON_LAND = createSoundEvent("entity/baby_nautilus_ambient_land");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_DEATH = createSoundEvent("entity/baby_nautilus_death");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_DEATH_ON_LAND = createSoundEvent("entity/baby_nautilus_death_land");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_EAT = createSoundEvent("entity/baby_nautilus_eat");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_HURT = createSoundEvent("entity/baby_nautilus_hurt");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_HURT_ON_LAND = createSoundEvent("entity/baby_nautilus_hurt_land");
        public static final RegistryObject<SoundEvent> BABY_NAUTILUS_SWIM = createSoundEvent("entity/baby_nautilus_swim");
        public static final RegistryObject<SoundEvent> NAUTILUS_SADDLE_UNDERWATER_EQUIP = createSoundEvent("item/nautilus_saddle_underwater_equip");
        public static final RegistryObject<SoundEvent> NAUTILUS_SADDLE_EQUIP = createSoundEvent("item/nautilus_saddle_equip");
        // Pig
        public static final RegistryObject<SoundEvent> PIG_AMBIENT_BABY = createSoundEvent("entity/baby_pig_ambient");
        public static final RegistryObject<SoundEvent> PIG_HURT_BABY = createSoundEvent("entity/baby_pig_hurt");
        public static final RegistryObject<SoundEvent> PIG_DEATH_BABY = createSoundEvent("entity/baby_pig_death");
        public static final RegistryObject<SoundEvent> PIG_STEP_BABY = createSoundEvent("entity/baby_pig_step");
        // Turtle
        public static final RegistryObject<SoundEvent> TURTLE_HURT_BABY = createSoundEvent("entity/turtle_hurt_baby");
        public static final RegistryObject<SoundEvent> TURTLE_DEATH_BABY = createSoundEvent("entity/turtle_death_baby");
        public static final RegistryObject<SoundEvent> TURTLE_SHAMBLE_BABY = createSoundEvent("entity/turtle_shamble_baby");
        // Wolf
        public static final RegistryObject<SoundEvent> WOLF_STEP_BABY = createSoundEvent("entity/wolf_step_baby");
        public static final RegistryObject<SoundEvent> WOLF_HURT_BABY = createSoundEvent("entity/wolf_hurt_baby");
        public static final RegistryObject<SoundEvent> WOLF_DEATH_BABY = createSoundEvent("entity/wolf_death_baby");
        public static final RegistryObject<SoundEvent> WOLF_GROWL_BABY = createSoundEvent("entity/wolf_growl_baby");
        public static final RegistryObject<SoundEvent> WOLF_AMBIENT_BABY = createSoundEvent("entity/wolf_ambient_baby");
        public static final RegistryObject<SoundEvent> WOLF_WHINE_BABY = createSoundEvent("entity/wolf_whine_baby");
        public static final RegistryObject<SoundEvent> WOLF_PANT_BABY = createSoundEvent("entity/wolf_pant_baby");
        // Golden Dandelion
        public static final RegistryObject<SoundEvent> GOLDEN_DANDELION_USE = createSoundEvent("item/golden_dandelion_use");
        public static final RegistryObject<SoundEvent> GOLDEN_DANDELION_UNUSE = createSoundEvent("item/golden_dandelion_unuse");

        private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
            return SOUND_EVENTS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, soundName)));
        }
    }

    public static class BabyTags {
        public static final TagKey<Item> NAUTILUS_TAMING_ITEMS = bindItemTag("nautilus_taming_items");
        public static final TagKey<Item> NAUTILUS_FOOD = bindItemTag("nautilus_food");
        public static final TagKey<Item> NAUTILUS_BUCKET_FOOD = bindItemTag("nautilus_bucket_food");

        public static final TagKey<EntityType<?>> CANNOT_BE_AGE_LOCKED = bindEntityTag("cannot_be_age_locked");
        public static final TagKey<EntityType<?>> AGEABLE_WATER_CREATURE = bindEntityTag("ageable_water_creature");
        public static final TagKey<EntityType<?>> NAUTILUS_HOSTILES = bindEntityTag("nautilus_hostiles");

        public static final TagKey<Biome> SPAWNS_NAUTILUS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_nautilus"));

        private static TagKey<Item> bindItemTag(String name) {
            return ItemTags.create(new ResourceLocation(MOD_ID, name));
        }

        private static TagKey<EntityType<?>> bindEntityTag(@NotNull String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MOD_ID, name));
        }


    }

    public static class BabyEffects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);

        public static final RegistryObject<MobEffect> BREATH_OF_THE_NAUTILUS = EFFECTS.register("breath_of_the_nautilus", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 65518));
    }

    @Mod.EventBusSubscriber(modid = BabySteps.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BabyEntities {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

        public static final RegistryObject<EntityType<Nautilus>> NAUTILUS = ENTITIES.register("nautilus",
                () -> EntityType.Builder.of(Nautilus::new, MobCategory.WATER_CREATURE)
                        .sized(0.875F, 0.95F)
                        .clientTrackingRange(10)
                        .build("nautilus"));

        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(NAUTILUS.get(), Nautilus.createAttributes().build());
        }
        @SubscribeEvent
        public static void registerEntitySpawnPlacements(SpawnPlacementRegisterEvent event) {
            event.register(NAUTILUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractNautilus::checkNautilusSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        }
    }

    public static class BabyMemoryModules {
        public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MOD_ID);

        public static final RegistryObject<MemoryModuleType<Integer>> ATTACK_TARGET_COOLDOWN = MEMORY_MODULE_TYPES.register("attack_target_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
        public static final RegistryObject<MemoryModuleType<Integer>> CHARGE_COOLDOWN_TICKS = MEMORY_MODULE_TYPES.register("charge_cooldown_ticks", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
        public static final RegistryObject<MemoryModuleType<Boolean>> DANGER_DETECTED_RECENTLY = MEMORY_MODULE_TYPES.register("danger_detected_recently", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    }

    public static class BabySensorTypes {
        public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, MOD_ID);

        public static final RegistryObject<SensorType<NautilusTemptationsSensor>> NAUTILUS_TEMPTATIONS = SENSOR_TYPES.register("nautilus_temptations", () -> new SensorType<>(NautilusTemptationsSensor::new));

        public static class NautilusTemptationsSensor extends TemptingSensor {
            public NautilusTemptationsSensor() {
                super(Ingredient.of(BabyTags.NAUTILUS_FOOD));
            }
        }
    }

    public static class BabyBiomeModifiers {
        public static final ResourceKey<BiomeModifier> SPAWN_NAUTILUS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(MOD_ID, "spawn_nautilus"));

//        public static void bootstrap(BootstapContext<BiomeModifier> context) {
//            var biomes = context.lookup(Registries.BIOME);
//
//            context.register(SPAWN_NAUTILUS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
//                    biomes.getOrThrow(BabyTags.SPAWNS_NAUTILUS),
//                    List.of(new MobSpawnSettings.SpawnerData(BabyEntities.NAUTILUS.get(), 8, 1, 3))
//            ));
//        }
    }

    public static class BabyNetwork {
        private static final String PROTOCOL_VERSION = "1";
        public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(BabySteps.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

        public static void register() {
            CHANNEL.registerMessage(0,
                    ClientboundMountScreenOpenPacket.class,
                    ClientboundMountScreenOpenPacket::encode,
                    ClientboundMountScreenOpenPacket::new,
                    ClientboundMountScreenOpenPacket::handle
            );
        }

        public static <MSG> void sendToPlayer(ServerPlayer player, MSG msg) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }
}
