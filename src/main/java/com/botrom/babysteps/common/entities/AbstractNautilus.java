package com.botrom.babysteps.common.entities;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.common.NautilusArmorItem;
import com.botrom.babysteps.common.entities.ai.NautilusAi;
import com.botrom.babysteps.utils.ClientboundMountScreenOpenPacket;
import com.botrom.babysteps.utils.NautilusInventoryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AbstractNautilus extends TamableAnimal implements ContainerListener, HasCustomInventoryScreen, PlayerRideableJumping, Saddleable {
    public static final int INVENTORY_SLOT_OFFSET = 500;
    public static final int INVENTORY_ROWS = 3;
    public static final int SADDLE_SLOT = 0;
    public static final int SMALL_RESTRICTION_RADIUS = 16;
    public static final int LARGE_RESTRICTION_RADIUS = 32;
    public static final int RESTRICTION_RADIUS_BUFFER = 8;
    private static final int EFFECT_DURATION = 60;
    private static final int EFFECT_REFRESH_RATE = 40;
    private static final double NAUTILUS_WATER_RESISTANCE = 0.9;
    private static final float IN_WATER_SPEED_MODIFIER = 0.011F;
    private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325F;
    private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02F;
    private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BYTE);;
    private static final int DASH_COOLDOWN_TICKS = 40;
    private static final int DASH_MINIMUM_DURATION_TICKS = 5;
    private static final float DASH_MOMENTUM_IN_WATER = 1.2F;
    private static final float DASH_MOMENTUM_ON_LAND = 0.5F;
    private int dashCooldown = 0;
    protected float playerJumpPendingScale;
    protected SimpleContainer inventory;
    private static final double BUBBLE_SPREAD_FACTOR = 0.8;
    private static final double BUBBLE_DIRECTION_SCALE = 1.1;
    private static final double BUBBLE_Y_OFFSET = 0.25;
    private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0;
    private static final float BUBBLE_PROBABILITY_MIN = 0.15F;
    private static final float BUBBLE_PROBABILITY_MAX = 1.0F;

    protected AbstractNautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.011F, 0.0F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.createInventory();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.inventory != null) {
            ListTag listTag = new ListTag();
            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)i);
                    itemstack.save(compoundtag);
                    listTag.add(compoundtag);
                }
            }
            compound.put("Items", listTag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.inventory != null) {
            ListTag listtag = compound.getList("Items", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(compoundtag));
                }
            }
        }
        this.updateContainerEquipment();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return !this.isTame() && !this.isBaby() ? stack.is(BabySteps.BabyTags.NAUTILUS_TAMING_ITEMS) : stack.is(BabySteps.BabyTags.NAUTILUS_FOOD);
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        if (stack.is(BabySteps.BabyTags.NAUTILUS_BUCKET_FOOD)) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
        } else {
            super.usePlayerItem(player, hand, stack);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3F);
    }

    @Override
    public int getArmorValue() {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.CHEST);
        return super.getArmorValue() + (itemStack.getItem() instanceof NautilusArmorItem armor ? armor.getProtection() : 0);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader levelReader) {
        return 0.0F;
    }

    public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource source) {
        int i = levelAccessor.getSeaLevel();
        int j = i - 25;
        return blockPos.getY() >= j
                && blockPos.getY() <= i - 5
                && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER)
                && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return !this.isVehicle();
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return (LivingEntity)(this.isSaddled() && this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger());
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        float f = player.xxa;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (player.zza != 0.0F) {
            float f3 = Mth.cos(player.getXRot() * (float) (Math.PI / 180.0));
            float f4 = -Mth.sin(player.getXRot() * (float) (Math.PI / 180.0));
            if (player.zza < 0.0F) {
                f3 *= -0.5F;
                f4 *= -0.5F;
            }

            f2 = f4;
            f1 = f3;
        }

        return new Vec3(f, f2, f1);
    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        Vec2 vec2 = this.getRiddenRotation(player);
        float f = this.getYRot();
        float f1 = Mth.wrapDegrees(vec2.y - f);
        float f2 = 0.5F;
        f += f1 * 0.5F;
        this.setRot(f, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = f;
        if (this.isControlledByLocalInstance()) {
            if (this.playerJumpPendingScale > 0.0F && !this.jumping) {
                this.executeRidersJump(this.playerJumpPendingScale, player);
            }

            this.playerJumpPendingScale = 0.0F;
        }
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isAlive() && this.isInWater()) {
            float f = this.getSpeed();
            this.moveRelative(f, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return this.isInWater() ? 0.0325F * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) : 0.02F * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide()) {
            player.startRiding(this);
            if (!this.isVehicle()) {
                this.clearRestriction();
            }
        }
    }

    private int getNautilusRestrictionRadius() {
        return !this.isBaby() && !this.isSaddled() ? 32 : 16;
    }

    protected void checkRestriction() {
        if (!this.isLeashed() && !this.isVehicle() && this.isTame()) {
            int i = this.getNautilusRestrictionRadius();
            if (!this.hasRestriction() || !this.getRestrictCenter().closerThan(this.blockPosition(), i + 8) || i != this.getRestrictRadius()) {
                this.restrictTo(this.blockPosition(), i);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        this.checkRestriction();
        super.customServerAiStep();
    }

    private void applyEffects(Level level) {
        if (this.getFirstPassenger() instanceof Player player) {
            boolean flag = player.hasEffect(BabySteps.BabyEffects.BREATH_OF_THE_NAUTILUS.get());
            boolean flag1 = level.getGameTime() % 40L == 0L;
            if (!flag || flag1) {
                player.addEffect(new MobEffectInstance(BabySteps.BabyEffects.BREATH_OF_THE_NAUTILUS.get(), 60, 0, true, false, true));
            }
        }
    }

    private void spawnBubbles() {
        double d0 = this.getDeltaMovement().length();
        double d1 = Mth.clamp(d0 * 2.0, 0.15F, 1.0);
        if (this.random.nextFloat() < d1) {
            float f = this.getYRot();
            float f1 = Mth.clamp(this.getXRot(), -10.0F, 10.0F);
            Vec3 vec3 = this.calculateViewVector(f1, f);
            double d2 = this.random.nextDouble() * 0.8 * (1.0 + d0);
            double d3 = (this.random.nextFloat() - 0.5) * d2;
            double d4 = (this.random.nextFloat() - 0.5) * d2;
            double d5 = (this.random.nextFloat() - 0.5) * d2;
            this.level()
                    .addParticle(
                            ParticleTypes.BUBBLE,
                            this.getX() - vec3.x * 1.1,
                            this.getY() - vec3.y + 0.25,
                            this.getZ() - vec3.z * 1.1,
                            d3,
                            d4,
                            d5
                    );
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.applyEffects(this.level());
        }

        if (this.isDashing() && this.dashCooldown < 35) {
            this.setDashing(false);
        }

        if (this.dashCooldown > 0) {
            this.dashCooldown--;
            if (this.dashCooldown == 0) {
                this.playSound(Objects.requireNonNull(this.getDashReadySound()));
            }
        }

        if (this.isInWater()) {
            this.spawnBubbles();
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void onPlayerJump(int jumpAngle) {
        if (this.isSaddled() && this.dashCooldown <= 0) {
            this.playerJumpPendingScale = this.getPlayerJumpPendingScale(jumpAngle);
        }
    }

    private float getPlayerJumpPendingScale(int jumpAngle) {
        return jumpAngle >= 90 ? 1.0F : 0.4F + 0.4F * jumpAngle / 90.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DASH, false);
        this.entityData.define(DATA_ID_FLAGS, (byte)0);
    }

    public boolean isDashing() {
        return this.entityData.get(DASH);
    }

    public void setDashing(boolean dashing) {
        this.entityData.set(DASH, dashing);
    }

    protected void executeRidersJump(float jumpDistance, Player player) {
        this.addDeltaMovement(player.getLookAngle().scale((this.isInWater() ? 1.2F : 0.5F) * jumpDistance * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.getBlockSpeedFactor()));
        this.dashCooldown = 40;
        this.setDashing(true);
    }

    @Override
    public void handleStartJump(int jumpDistance) {
        this.playSound(Objects.requireNonNull(this.getDashSound()));
        this.gameEvent(GameEvent.SPLASH);
        this.setDashing(true);
    }

    @Override
    public int getJumpCooldown() {
        return this.dashCooldown;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (!this.firstTick && DASH.equals(dataAccessor)) {
            this.dashCooldown = this.dashCooldown == 0 ? 40 : this.dashCooldown;
        }

        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    protected void playStepSound(BlockPos p_452461_, BlockState p_455043_) {
    }

    protected @Nullable SoundEvent getDashSound() {
        return null;
    }

    protected @Nullable SoundEvent getDashReadySound() {
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (this.isBaby()) return super.mobInteract(player, hand);

        if (this.isTame() && player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.SUCCESS;
        } else {
            if (!itemStack.isEmpty()) {
                if (!this.level().isClientSide() && !this.isTame() && this.isFood(itemStack)) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.tryToTame(player);
                    return InteractionResult.SUCCESS;
                }

                if (this.isFood(itemStack)) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        FoodProperties foodproperties = itemStack.getItem().getFoodProperties();
                        this.heal(foodproperties != null ? 2 * foodproperties.getNutrition() : 1.0F);
                        this.usePlayerItem(player, hand, itemStack);
                        this.playEatingSound();
                        return InteractionResult.SUCCESS;
                    }
                    if (this.getAge() == 0 && this.canFallInLove()) {
                        this.usePlayerItem(player, hand, itemStack);
                        this.setInLove(player);
                        return InteractionResult.SUCCESS;
                    }
                }

                if (this.isTame() && !this.isBaby() && itemStack.getItem() instanceof SaddleItem && this.inventory.getItem(0).isEmpty()) {
                    this.inventory.setItem(0, itemStack.copy().split(1));
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }

                if (this.isTame() && !this.isBaby() && itemStack.getItem() instanceof NautilusArmorItem && this.inventory.getItem(1).isEmpty()) {
                    this.inventory.setItem(1, itemStack.copy().split(1));
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }

                InteractionResult interactionresult = itemStack.interactLivingEntity(player, this, hand);
                if (interactionresult.consumesAction()) {
                    return interactionresult;
                }
            }
            if (this.isTame() && !player.isSecondaryUseActive() && !this.isFood(itemStack)) {
                this.doPlayerRide(player);
                return InteractionResult.SUCCESS;
            } else {
                return super.mobInteract(player, hand);
            }
        }
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }
        this.playEatingSound();
    }

    protected void playEatingSound() {
        this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), BabySteps.BabySounds.NAUTILUS_EAT.get(), this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }

    @Override
    public boolean removeWhenFarAway(double distanceFromPlayer) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float damageAmount) {
        boolean flag = super.hurt(source, damageAmount);
        if (flag && source.getEntity() instanceof LivingEntity livingentity) {
            NautilusAi.setAngerTarget(this, livingentity);
        }
        return flag;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return mobEffectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(mobEffectInstance);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag dataTag) {
        RandomSource randomsource = levelAccessor.getRandom();
        NautilusAi.initMemories(this, randomsource);
        return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, dataTag);
    }

    public final int getInventorySize() {
        return 2 + (this.getInventoryColumns() * 3);
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());

        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; j++) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }
        this.inventory.addListener(this);
        this.updateContainerEquipment();
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide() && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            if (serverPlayer.containerMenu != serverPlayer.inventoryMenu) serverPlayer.closeContainer();

            serverPlayer.nextContainerCounter();
            int id = serverPlayer.containerCounter;
            int columns = this.getInventoryColumns();

            ClientboundMountScreenOpenPacket packet = new ClientboundMountScreenOpenPacket(id, columns, this.getId());
            BabySteps.BabyNetwork.sendToPlayer(serverPlayer, packet);

            serverPlayer.containerMenu = new NautilusInventoryMenu(id, serverPlayer.getInventory(), this.inventory, this, columns);
            serverPlayer.initMenu(serverPlayer.containerMenu);
        }
    }

    protected void setFlag(int pFlagId, boolean pValue) {
        byte b0 = (Byte)this.entityData.get(DATA_ID_FLAGS);
        if (pValue) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | pFlagId));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & ~pFlagId));
        }
    }

    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) {
            // Saddle Slot
            this.setFlag(4, !this.inventory.getItem(0).isEmpty());
            // Armor Slot
            this.setItemSlot(EquipmentSlot.CHEST, this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }

    @Override
    public void containerChanged(Container container) {
        boolean flag = this.isSaddled();
        this.updateContainerEquipment();
        if (this.tickCount > 20 && !flag && this.isSaddled()) {
            this.playSound(this.getSaddleSoundEvent(), 0.5F, 1.0F);
        }
    }

    @Override
    public @NotNull SoundEvent getSaddleSoundEvent() {
        return this.isInWater() ? BabySteps.BabySounds.NAUTILUS_SADDLE_UNDERWATER_EQUIP.get() : BabySteps.BabySounds.NAUTILUS_SADDLE_EQUIP.get();
    }

    public boolean hasInventoryChanged(Container container) {
        return this.inventory != container;
    }

    public int getInventoryColumns() {
        return 0;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    protected boolean getFlag(int pFlagId) {
        return (this.entityData.get(DATA_ID_FLAGS) & pFlagId) != 0;
    }

    protected boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }

    protected boolean isAggravated() {
        return this.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }
}