package com.botrom.babysteps.mixins.common.entities;

import com.botrom.babysteps.BabySteps;
import com.botrom.babysteps.utils.IAgeableMob;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Shadow public abstract InteractionResult interact(Player player, InteractionHand hand);
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);
    @Shadow public abstract void setItemSlot(EquipmentSlot slot, ItemStack stack);

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void bs$injectGoldenDandelionInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this instanceof IAgeableMob lockedMob) {
//            AgeableMob ageable = (AgeableMob) (Object) this;
            ItemStack itemInHand = player.getItemInHand(hand);

            if (bs$canUseGoldenDandelion(itemInHand, lockedMob.isBabyMixin(), lockedMob.getAgeLockParticleTimer(), (Mob)(Object)this)) {

                lockedMob.setAgeLocked((Mob)(Object)this, lockedMob::isAgeLocked, player, itemInHand, (mob) -> {
                    lockedMob.setAgeLockedData();
                    lockedMob.triggerAgeLockEffect(lockedMob.isAgeLocked());
                });

                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    private static boolean bs$canUseGoldenDandelion(final ItemStack itemInHand, final boolean isBaby, final int cooldown, final Mob mob) {
        return (itemInHand.getItem() == BabySteps.BabyItems.GOLDEN_DANDELION.get() || itemInHand.getItem() == BabySteps.BabyBlocks.GOLDEN_DANDELION.get().asItem()) && isBaby && cooldown == 0 && !mob.getType().is(BabySteps.BabyTags.CANNOT_BE_AGE_LOCKED);
    }
}