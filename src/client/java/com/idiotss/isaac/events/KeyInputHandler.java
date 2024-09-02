package com.idiotss.isaac.events;

import com.idiotss.isaac.animation.AnimatablePlayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class KeyInputHandler {
    private static final float rollMultiplier = 2.5F;
    private static final float backsetpMultiplier = 0.5F;
    private static final float rollCooldown = 7.5F;
    private static final float backstepCooldown = 0.5F;
    private static boolean rollUsed = false;
    private static float lastTime;
    private static Vec3d newVelocity;

    public static void registerKeyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if(!client.options.sprintKey.isPressed()){
                rollUsed = false;
            }
            if(client.options.sprintKey.isPressed() && !rollUsed) {
                rollUsed = true;
                assert client.world != null;
                assert client.player != null;
                if (!isClientMoving(client)) {
                    if (backstepCooldown < client.world.getTime() - lastTime) {
                        newVelocity = new Vec3d(-client.player.getRotationVector().x * backsetpMultiplier, client.player.getVelocity().y, -client.player.getRotationVector().z * backsetpMultiplier);
                        lastTime = client.world.getTime();
                        client.player.setVelocity(newVelocity);
                    }
                } else {
                    if (rollCooldown < client.world.getTime() - lastTime) {
                        newVelocity = new Vec3d(client.player.getRotationVector().x * rollMultiplier, client.player.getVelocity().y, client.player.getRotationVector().z * rollMultiplier);
                        ((AnimatablePlayer) client.player).playAnimation("old-bracelet:player/run.animation", client.player.getVelocity(), 1.0F);
                        lastTime = client.world.getTime();
                        client.player.setVelocity(newVelocity);
                    }
                }
            }
        });

    }

    public static void register() {
        registerKeyInputs();
    }

    // Returns true if the player is pressing movement keys
    public static boolean isClientMoving(MinecraftClient client){
        return client.options.forwardKey.isPressed() || client.options.backKey.isPressed() || client.options.rightKey.isPressed() || client.options.leftKey.isPressed();
    }
}
