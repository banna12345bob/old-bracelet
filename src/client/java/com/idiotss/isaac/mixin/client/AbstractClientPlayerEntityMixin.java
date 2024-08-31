package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.OldBraceletClient;
import com.idiotss.isaac.animation.AnimatablePlayer;
import com.idiotss.isaac.animation.AnimationRegistry;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements AnimatablePlayer {
	@Unique
	private final ModifierLayer base = new ModifierLayer(null);

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void postInit(ClientWorld world, GameProfile profile, CallbackInfo ci) {
		var stack = ((IAnimatedPlayer) this).getAnimationStack();
//		base.addModifier(createAdjustmentModifier(), 0);
//		base.addModifier(speedModifier, 0);
//		speedModifier.speed = 1.2f;
		stack.addAnimLayer(1000, base);
	}

	private Vec3d lastRollDirection;

	public void playRollAnimation(String animationName, Vec3d direction) {
		try {
			KeyframeAnimation animation = AnimationRegistry.animations.get(animationName);
			var copy = animation.mutableCopy();
//			lastRollDirection = direction;

			var fadeIn = copy.beginTick;
			base.replaceAnimationWithFade(
					AbstractFadeModifier.standardFadeIn(fadeIn, Ease.INOUTSINE),
					new KeyframeAnimationPlayer(copy.build(), 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}