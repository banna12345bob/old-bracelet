package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.animation.AnimatablePlayer;
import com.idiotss.isaac.animation.AnimationRegistry;
import com.idiotss.isaac.camera.OldBraceletCamera;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import io.socol.betterthirdperson.BetterThirdPerson;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements AnimatablePlayer {
	@Shadow @Final public ClientWorld clientWorld;
	@Unique
	private final ModifierLayer base = new ModifierLayer(null);
	@Unique
	private static OldBraceletCamera camera;
	@Unique
	private static SpeedModifier speedModifier;

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void postInit(ClientWorld world, GameProfile profile, CallbackInfo ci) {
		var stack = ((IAnimatedPlayer) this).getAnimationStack();
//		base.addModifier(createAdjustmentModifier(), 0);
		stack.addAnimLayer(1000, base);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo ci){
		if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
			camera = new OldBraceletCamera(BetterThirdPerson.getCameraManager().getCustomCamera());
//			camera.lookAt(this.getPos(), new Vec3d(0, -60, 0));
		}
	}

	public void playAnimation(String animationName, Vec3d direction, float speed) {
		base.addModifier(speedModifier, 0);
		speedModifier.speed = speed;
		try {
			KeyframeAnimation animation = AnimationRegistry.animations.get(animationName);
			var copy = animation.mutableCopy();
			var fadeIn = copy.beginTick;
			base.replaceAnimationWithFade(
					AbstractFadeModifier.standardFadeIn(fadeIn, Ease.INOUTSINE),
					new KeyframeAnimationPlayer(copy.build(), 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}