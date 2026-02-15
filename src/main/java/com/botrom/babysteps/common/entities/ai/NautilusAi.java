package com.botrom.babysteps.common.entities.ai;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.common.entities.AbstractNautilus;
import com.botrom.babysteps.common.entities.Nautilus;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

import java.util.Optional;
import java.util.function.Predicate;

public class NautilusAi {
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.3F;
    private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.4F;
    private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 1.6F;
    private static final UniformInt TIME_BETWEEN_NON_PLAYER_ATTACKS = UniformInt.of(2400, 3600);
    private static final float SPEED_WHEN_ATTACKING = 0.6F;
    private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
    private static final int ANGER_DURATION = 400;
    private static final int TIME_BETWEEN_ATTACKS = 80;
    private static final double MAX_CHARGE_DISTANCE = 12.0;
    private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0;

    protected static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((entity) -> {
                boolean mobGriefing = entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                boolean isArmorStand = entity.getType().equals(EntityType.ARMOR_STAND);
                boolean insideBorder = entity.level().getWorldBorder().isWithinBounds(entity.getBoundingBox());
                return (mobGriefing || !isArmorStand) && insideBorder;
            });

    protected static final ImmutableList<SensorType<? extends Sensor<? super Nautilus>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_ADULT,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY,
            BabySteps.BabySensorTypes.NAUTILUS_TEMPTATIONS.get()
    );

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.ATTACK_TARGET,
            BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get(),
            MemoryModuleType.HURT_BY,
            MemoryModuleType.ANGRY_AT,
            BabySteps.BabyMemoryModules.ATTACK_TARGET_COOLDOWN.get()
    );

    public static void initMemories(AbstractNautilus nautilus, RandomSource source) {
        nautilus.getBrain().setMemory(BabySteps.BabyMemoryModules.ATTACK_TARGET_COOLDOWN.get(), TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(source));
    }

    public static Brain.Provider<Nautilus> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<Nautilus> nautilusBrain) {
        initCoreActivity(nautilusBrain);
        initIdleActivity(nautilusBrain);
        initFightActivity(nautilusBrain);
        nautilusBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        nautilusBrain.setDefaultActivity(Activity.IDLE);
        nautilusBrain.useDefaultActivity();
        return nautilusBrain;
    }

    private static void initCoreActivity(Brain<Nautilus> nautilusBrain) {
        nautilusBrain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic(1.6F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get()),
                        new CountDownCooldownTicks(BabySteps.BabyMemoryModules.ATTACK_TARGET_COOLDOWN.get())
                )
        );
    }

    private static void initIdleActivity(Brain<Nautilus> nautilusBrain) {
        nautilusBrain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(1, new AnimalMakeLove(BabySteps.BabyEntities.NAUTILUS.get(), 0.4F)),
                        Pair.of(2, new FollowTemptation(speedModifier -> 1.3F, p_452711_ -> p_452711_.isBaby() ? 2.5 : 3.5)),
                        Pair.of(3, StartAttacking.create(NautilusAi::findNearestValidAttackTarget)),
                        Pair.of(4, new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.ORDERED,
                                        GateBehavior.RunningPolicy.TRY_ALL,
                                        ImmutableList.of(Pair.of(RandomStroll.swim(1.0F), 2), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 3))
                                )
                        )
                )
        );
    }

    private static void initFightActivity(Brain<Nautilus> nautilusBrain) {
        nautilusBrain.addActivityWithConditions(
                Activity.FIGHT,
                ImmutableList.of(Pair.of(0, new ChargeAttack(80, ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0, 11.0, BabySteps.BabySounds.NAUTILUS_DASH.get()))),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                        Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
                        Pair.of(BabySteps.BabyMemoryModules.CHARGE_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractNautilus nautilus) {
        if (!BehaviorUtils.isBreeding(nautilus) && nautilus.isInWater() && !nautilus.isBaby() && !nautilus.isTame()) {
            Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(nautilus, MemoryModuleType.ANGRY_AT)
                    .filter(targetEntity -> targetEntity.isInWater() && Sensor.isEntityAttackableIgnoringLineOfSight(nautilus, targetEntity));
            if (optional.isPresent()) {
                return optional;
            } else if (nautilus.getBrain().hasMemoryValue(BabySteps.BabyMemoryModules.ATTACK_TARGET_COOLDOWN.get())) {
                return Optional.empty();
            } else {
                nautilus.getBrain().setMemory(BabySteps.BabyMemoryModules.ATTACK_TARGET_COOLDOWN.get(), TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(nautilus.level().random));
                return nautilus.level().random.nextFloat() < 0.5F
                        ? Optional.empty()
                        : nautilus.getBrain()
                        .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                        .orElse(NearestVisibleLivingEntities.empty())
                        .findClosest(NautilusAi::isHostileTarget);
            }
        } else {
            return Optional.empty();
        }
    }

    public static void setAngerTarget(AbstractNautilus nautilus, LivingEntity entity) {
        if (Sensor.isEntityAttackableIgnoringLineOfSight(nautilus, entity)) {
            nautilus.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            nautilus.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, entity.getUUID(), 400L);
        }
    }

    private static boolean isHostileTarget(LivingEntity entity) {
        return entity.isInWater() && entity.getType().is(BabySteps.BabyTags.NAUTILUS_HOSTILES);
    }

    public static void updateActivity(Nautilus nautilus) {
        nautilus.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static Predicate<ItemStack> getTemptations() {
        return p_453636_ -> p_453636_.is(BabySteps.BabyTags.NAUTILUS_FOOD);
    }
}