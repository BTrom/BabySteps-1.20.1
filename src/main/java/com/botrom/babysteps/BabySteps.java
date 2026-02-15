package com.botrom.babysteps;

import com.botrom.babysteps.client.ClientEvents;
import com.botrom.babysteps.utils.BabyConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@SuppressWarnings("deprecation")
@Mod(BabySteps.MOD_ID)
public class BabySteps {
    public static final String MOD_ID = "babysteps";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BabySteps(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::clientSetup);
        modEventBus.register(new ClientEvents());

        BabyBlocks.BLOCKS.register(modEventBus);
        BabyItems.ITEMS.register(modEventBus);
        BabySounds.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(this::creativeTabsInjection);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BabyConfig.CLIENT_SPEC, BabySteps.MOD_ID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BabyConfig.COMMON_SPEC, BabySteps.MOD_ID + "-common.toml");
    }

    private void clientSetup(final FMLCommonSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BabyBlocks.GOLDEN_DANDELION.get(), RenderType.cutoutMipped());
    }

    private void creativeTabsInjection(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            var entries = event.getEntries();
            entries.putAfter(Items.BEETROOT.getDefaultInstance(), BabyItems.GOLDEN_DANDELION.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    public static class BabyItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BabySteps.MOD_ID);
        public static final RegistryObject<Item> GOLDEN_DANDELION = ITEMS.register("golden_dandelion", () -> new BlockItem(BabyBlocks.GOLDEN_DANDELION.get(), new Item.Properties()));
    }

    public static class BabyBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BabySteps.MOD_ID);
        public static final RegistryObject<Block> GOLDEN_DANDELION = BLOCKS.register("golden_dandelion", () -> new FlowerBlock(MobEffects.SATURATION, 1, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
    }

    public static class BabySounds {
        public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BabySteps.MOD_ID);
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
            return SOUND_EVENTS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BabySteps.MOD_ID, soundName)));
        }
    }

    public static class BabyTags {
        public static final TagKey<EntityType<?>> CANNOT_BE_AGE_LOCKED = bindEntityTag("cannot_be_age_locked");
        public static final TagKey<EntityType<?>> AGEABLE_WATER_CREATURE = bindEntityTag("ageable_water_creature");

        private static TagKey<EntityType<?>> bindEntityTag(@NotNull String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(BabySteps.MOD_ID, name));
        }
    }
}
