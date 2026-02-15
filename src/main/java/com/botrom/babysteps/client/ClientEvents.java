package com.botrom.babysteps.client;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.client.models.*;
import com.botrom.babysteps.client.renderers.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BabySteps.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(BabyAxolotlRenderer.BABY_AXOLOTL, BabyAxolotlModel::createBodyLayer);
        e.registerLayerDefinition(BabyBeeRenderer.BABY_BEE, BabyBeeModel::createBodyLayer);
        e.registerLayerDefinition(BabyCamelRenderer.BABY_CAMEL, BabyCamelModel::createBodyLayer);
        e.registerLayerDefinition(BabyCatRenderer.BABY_CAT, BabyCatModel::createBodyLayer);
        e.registerLayerDefinition(BabyCatRenderer.BABY_OCELOT, BabyOcelotModel::createBodyLayer);
        e.registerLayerDefinition(BabyChickenRenderer.BABY_CHICKEN, BabyChickenModel::createBodyLayer);
//        e.registerLayerDefinition(ChickenVariantRenderer.COLD_CHICKEN, ColdChickenModel::createBodyLayer);
        e.registerLayerDefinition(BabyCowRenderer.BABY_COW, BabyCowModel::createBodyLayer);
//        e.registerLayerDefinition(CowVariantRenderer.COLD_COW, ColdCowModel::createBodyLayer);
//        e.registerLayerDefinition(CowVariantRenderer.WARM_COW, WarmCowModel::createBodyLayer);
        e.registerLayerDefinition(BabyDolphinRenderer.BABY_DOLPHIN, BabyDolphinModel::createBodyLayer);
        e.registerLayerDefinition(BabyFoxRenderer.BABY_FOX, BabyFoxModel::createBodyLayer);
        e.registerLayerDefinition(BabyGoatRenderer.BABY_GOAT, BabyGoatModel::createBodyLayer);
        e.registerLayerDefinition(BabyHorseRenderer.BABY_DONKEY, BabyDonkeyModel::createBabyLayer);
        e.registerLayerDefinition(BabyHorseRenderer.BABY_HORSE, BabyHorseModel::createBabyLayer);
        e.registerLayerDefinition(BabyLlamaRenderer.BABY_LLAMA, BabyLlamaModel::createBodyLayer);
        e.registerLayerDefinition(BabyLlamaRenderer.BABY_LLAMA_DECOR, BabyLlamaModel::createBodyLayer);
        e.registerLayerDefinition(BabyPigRenderer.BABY_PIG, BabyPigModel::createBodyLayer);
//        e.registerLayerDefinition(PigVariantRenderer.COLD_PIG, ColdPigModel::createBodyLayer);
        e.registerLayerDefinition(BabyPolarBearRenderer.BABY_POLAR_BEAR, BabyPolarBearModel::createBodyLayer);
        e.registerLayerDefinition(RabbitRenderer.ADULT_RABBIT, RabbitModel::createBodyLayer);
        e.registerLayerDefinition(RabbitRenderer.BABY_RABBIT, BabyRabbitModel::createBodyLayer);
        e.registerLayerDefinition(BabySheepRenderer.BABY_SHEEP, BabySheepModel::createBodyLayer);
//        e.registerLayerDefinition(SheepWoolUndercoatLayer.SHEEP_WOOL_UNDERCOAT, SheepModel::createBodyLayer);
        e.registerLayerDefinition(SquidRenderer.BABY_SQUID, BabySquidModel::createBodyLayer);
        e.registerLayerDefinition(BabyTurtleRenderer.BABY_TURTLE, BabyTurtleModel::createBodyLayer);
        e.registerLayerDefinition(BabyWolfRenderer.BABY_WOLF, BabyWolfModel::createBodyLayer);
    }
}
