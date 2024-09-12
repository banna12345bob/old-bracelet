package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.animation.AnimatablePlayer;
import com.idiotss.isaac.animation.AnimationRegistry;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements AnimatablePlayer {
	@Shadow protected abstract PlayerListEntry getPlayerListEntry();

	@Unique
	private final ModifierLayer base = new ModifierLayer(null);
	@Unique
	private static SpeedModifier speedModifier = new SpeedModifier();

	@Unique
	private static KeyframeAnimation.AnimationBuilder copy;

	@Unique
	private static int currentAnimationTick = 0;

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void postInit(ClientWorld world, GameProfile profile, CallbackInfo ci) {
		var stack = ((IAnimatedPlayer) this).getAnimationStack();
		stack.addAnimLayer(1000, base);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo ci){
//		Forces the custom camera if player in adventure mode
		if (Objects.requireNonNull(this.getPlayerListEntry()).getGameMode() == GameMode.ADVENTURE) {
			MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
		}

		if (base.isActive()) {
			OldBracelet.setInvincibility(this, copy.getPart("invincibility").x.findAtTick(currentAnimationTick) == 0);
			currentAnimationTick++;
		} else {
			currentAnimationTick = 0;
		}
	}

	public void playAnimation(String animationName, Vec3d direction, float speed) {
		base.addModifier(speedModifier, 0);
		speedModifier.speed = speed;
		try {
			KeyframeAnimation animation = AnimationRegistry.animations.get(animationName);
			copy = animation.mutableCopy();
			var fadeIn = copy.beginTick;
			base.replaceAnimationWithFade(
					AbstractFadeModifier.standardFadeIn(fadeIn, Ease.INOUTSINE),
					new KeyframeAnimationPlayer(copy.build(), 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

