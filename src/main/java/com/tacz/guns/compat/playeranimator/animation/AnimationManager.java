package com.tacz.guns.compat.playeranimator.animation;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.compat.playeranimator.AnimationName;
import com.tacz.guns.compat.playeranimator.PlayerAnimatorCompat;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.fabricmc.api.EnvType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class AnimationManager {
    public static boolean hasPlayerAnimator3rd(ClientGunIndex gunIndex) {
        Identifier location = gunIndex.getPlayerAnimator3rd();
        if (location == null) {
            return false;
        }
        return PlayerAnimatorAssetManager.INSTANCE.containsKey(location);
    }

    public static boolean isFlying(AbstractClientPlayerEntity player) {
        return !player.isOnGround() && player.getAbilities().flying;
    }

    public static void playRotationAnimation(AbstractClientPlayerEntity player, ClientGunIndex gunIndex) {
        String animationName = AnimationName.EMPTY;
        Identifier dataId = PlayerAnimatorCompat.ROTATION_ANIMATION;
        Identifier animator3rd = gunIndex.getPlayerAnimator3rd();
        if (animator3rd == null) {
            return;
        }
        if (!PlayerAnimatorAssetManager.INSTANCE.containsKey(animator3rd)) {
            return;
        }
        PlayerAnimatorAssetManager.INSTANCE.getAnimations(animator3rd, animationName).ifPresent(keyframeAnimation -> {
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer == null) {
                return;
            }
            AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
            modifierLayer.replaceAnimationWithFade(fadeModifier, new KeyframeAnimationPlayer(keyframeAnimation));
        });
    }

    public static void playLowerAnimation(AbstractClientPlayerEntity player, ClientGunIndex gunIndex, float limbSwingAmount) {
        // 如果玩家骑乘，不播放任何下半身动画
        if (player.getVehicle() != null) {
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.RIDE_LOWER);
            return;
        }
        // 如果玩家在天上，下半身动画就是站立动画
        if (isFlying(player)) {
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.HOLD_LOWER);
            return;
        }
        if (player.isSprinting()) {
            if (player.getPose() == EntityPose.CROUCHING) {
                playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.CROUCH_WALK_LOWER);
            } else {
                playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.RUN_LOWER);
            }
            return;
        }
        if (limbSwingAmount > 0.05) {
            if (player.getPose() == EntityPose.CROUCHING) {
                playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.CROUCH_WALK_LOWER);
            } else {
                playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.WALK_LOWER);
            }
            return;
        }
        if (player.getPose() == EntityPose.CROUCHING) {
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.CROUCH_LOWER);
        } else {
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOWER_ANIMATION, AnimationName.HOLD_LOWER);
        }
    }

    public static void playLoopUpperAnimation(AbstractClientPlayerEntity player, ClientGunIndex gunIndex, float limbSwingAmount) {
        IGunOperator operator = IGunOperator.fromLivingEntity(player);
        float aimingProgress = operator.getSynAimingProgress();
        if (aimingProgress <= 0) {
            if (!isFlying(player) && player.isSprinting()) {
                if (player.getPose() == EntityPose.CROUCHING) {
                    playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION, AnimationName.WALK_UPPER);
                } else {
                    playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION, AnimationName.RUN_UPPER);
                }
                return;
            }
            if (!isFlying(player) && limbSwingAmount > 0.05) {
                playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION, AnimationName.WALK_UPPER);
                return;
            }
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION, AnimationName.HOLD_UPPER);
        } else {
            playLoopAnimation(player, gunIndex, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION, AnimationName.AIM_UPPER);
        }
    }

    @SuppressWarnings("unchecked")
    public static void playLoopAnimation(AbstractClientPlayerEntity player, ClientGunIndex gunIndex, Identifier dataId, String animationName) {
        Identifier animator3rd = gunIndex.getPlayerAnimator3rd();
        if (animator3rd == null) {
            return;
        }
        if (!PlayerAnimatorAssetManager.INSTANCE.containsKey(animator3rd)) {
            return;
        }
        PlayerAnimatorAssetManager.INSTANCE.getAnimations(animator3rd, animationName).ifPresent(keyframeAnimation -> {
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer == null) {
                return;
            }
            if (modifierLayer.getAnimation() instanceof KeyframeAnimationPlayer animationPlayer && animationPlayer.isActive()) {
                Object extraDataName = animationPlayer.getData().extraData.get("name");
                if (extraDataName instanceof String name && !animationName.equals(name)) {
                    AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
                    modifierLayer.replaceAnimationWithFade(fadeModifier, new KeyframeAnimationPlayer(keyframeAnimation));
                }
                return;
            }
            AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
            modifierLayer.replaceAnimationWithFade(fadeModifier, new KeyframeAnimationPlayer(keyframeAnimation));
        });
    }

    @SuppressWarnings("unchecked")
    public static void playOnceAnimation(AbstractClientPlayerEntity player, ClientGunIndex gunIndex, Identifier dataId, String animationName) {
        Identifier animator3rd = gunIndex.getPlayerAnimator3rd();
        if (animator3rd == null) {
            return;
        }
        if (!PlayerAnimatorAssetManager.INSTANCE.containsKey(animator3rd)) {
            return;
        }
        PlayerAnimatorAssetManager.INSTANCE.getAnimations(animator3rd, animationName).ifPresent(keyframeAnimation -> {
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer == null) {
                return;
            }
            IAnimation animation = modifierLayer.getAnimation();
            if (animation == null || !animation.isActive()) {
                AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
                modifierLayer.replaceAnimationWithFade(fadeModifier, new KeyframeAnimationPlayer(keyframeAnimation));
            }
        });
    }

    public static void stopAllAnimation(AbstractClientPlayerEntity player) {
        stopAnimation(player, PlayerAnimatorCompat.LOWER_ANIMATION);
        stopAnimation(player, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION);
        stopAnimation(player, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION);
        stopAnimation(player, PlayerAnimatorCompat.ROTATION_ANIMATION);
    }

    @SuppressWarnings("unchecked")
    private static void stopAnimation(AbstractClientPlayerEntity player, Identifier dataId) {
        var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
        var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
        if (modifierLayer != null && modifierLayer.isActive()) {
            AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
            modifierLayer.replaceAnimationWithFade(fadeModifier, null);
        }
    }

    public ActionResult onFire(LivingEntity shooter, ItemStack gunItemStack, EnvType side) {
        if (side == EnvType.SERVER) {
            return ActionResult.PASS;
        }
        if (!(shooter instanceof AbstractClientPlayerEntity player)) {
            return ActionResult.PASS;
        }
        IGun iGun = IGun.getIGunOrNull(gunItemStack);
        if (iGun == null) {
            return ActionResult.PASS;
        }
        TimelessAPI.getClientGunIndex(iGun.getGunId(gunItemStack)).ifPresent(index -> {
            IGunOperator operator = IGunOperator.fromLivingEntity(player);
            float aimingProgress = operator.getSynAimingProgress();
            if (aimingProgress <= 0) {
                playOnceAnimation(player, index, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION, AnimationName.NORMAL_FIRE_UPPER);
            } else {
                playOnceAnimation(player, index, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION, AnimationName.AIM_FIRE_UPPER);
            }
        });
        return ActionResult.PASS;
    }

    public ActionResult onReload(LivingEntity shooter, ItemStack gunItemStack, EnvType side) {
        if (side == EnvType.SERVER) {
            return ActionResult.PASS;
        }
        if (!(shooter instanceof AbstractClientPlayerEntity player)) {
            return ActionResult.PASS;
        }
        IGun iGun = IGun.getIGunOrNull(gunItemStack);
        if (iGun == null) {
            return ActionResult.PASS;
        }
        TimelessAPI.getClientGunIndex(iGun.getGunId(gunItemStack)).ifPresent(index -> playOnceAnimation(player, index, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION, AnimationName.RELOAD_UPPER));
        return ActionResult.PASS;
    }

    public ActionResult onMelee(LivingEntity shooter, ItemStack gunItemStack, EnvType side) {
        if (side == EnvType.SERVER) {
            return ActionResult.PASS;
        }
        if (!(shooter instanceof AbstractClientPlayerEntity player)) {
            return ActionResult.PASS;
        }
        IGun iGun = IGun.getIGunOrNull(gunItemStack);
        if (iGun == null) {
            return ActionResult.PASS;
        }
        int randomIndex = shooter.getRandom().nextInt(3);
        String animationName = switch (randomIndex) {
            case 0 -> AnimationName.MELEE_UPPER;
            case 1 -> AnimationName.MELEE_2_UPPER;
            default -> AnimationName.MELEE_3_UPPER;
        };
        TimelessAPI.getClientGunIndex(iGun.getGunId(gunItemStack)).ifPresent(index -> playOnceAnimation(player, index, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION, animationName));
        return ActionResult.PASS;
    }

    public ActionResult onDraw(LivingEntity entity, ItemStack previousGunItem, ItemStack currentGunItem, EnvType side) {
        if (side == EnvType.SERVER) {
            return ActionResult.PASS;
        }
        if (!(entity instanceof AbstractClientPlayerEntity player)) {
            return ActionResult.PASS;
        }
        // 在切枪时，重置上半身动画
        if (currentGunItem.getItem() instanceof IGun && previousGunItem.getItem() instanceof IGun) {
            stopAnimation(player, PlayerAnimatorCompat.LOOP_UPPER_ANIMATION);
            stopAnimation(player, PlayerAnimatorCompat.ONCE_UPPER_ANIMATION);
        }
        return ActionResult.PASS;
    }
}
