package com.idiotss.isaac.events;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.animation.AnimatablePlayer;
import com.idiotss.isaac.camera.OldBraceletCamera;
import com.mojang.blaze3d.platform.InputUtil;
import io.socol.betterthirdperson.BetterThirdPerson;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static KeyBind targetKeybind;

    // Roll logic variables
    private static final float rollMultiplier = 2.5F;
    private static final float backsetpMultiplier = 0.5F;
    private static final float rollCooldown = 7.5F;
    private static final float backstepCooldown = 0.5F;
    private static boolean rollUsed = false;
    private static float lastTime;
    private static Vec3d newVelocity;

    // Target logic variables
    private static Entity closestEntity;
    private static boolean targetKeyUsed = false;
    private static OldBraceletCamera camera;

//     TODO: Figure our min & max distances
//    If player has no entities within the minDistance it should default reset the camera forwards (relative to the player)
    private static final float minDistance = 10F;
    private static final float maxDistance = 15F;

    public static void registerKeyInputs() {
        // Sprint & roll code
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                client.player.setJumping(false);
            }
            if(!client.options.sprintKey.isPressed()){
                rollUsed = false;
            }
            if(client.options.sprintKey.isPressed() && !rollUsed) {
                rollUsed = true;
                assert client.world != null;
                assert client.player != null;
                if (client.player.isOnGround()) {
                    if (!isClientMoving(client)) {
                        if (backstepCooldown < client.world.getTime() - lastTime) {
                            newVelocity = new Vec3d(-client.player.getRotationVector().x * backsetpMultiplier, client.player.getVelocity().y + 0.5f, -client.player.getRotationVector().z * backsetpMultiplier);
                            lastTime = client.world.getTime();
                            client.player.setVelocity(newVelocity);
                        }
                    } else {
                        if (rollCooldown < client.world.getTime() - lastTime) {
                            newVelocity = new Vec3d(client.player.getRotationVector().x * rollMultiplier, client.player.getVelocity().y, client.player.getRotationVector().z * rollMultiplier);
                            ((AnimatablePlayer) client.player).playAnimation("oldbracelet:player/run.animation", client.player.getVelocity(), 1.0F);

//                        TODO: Send Roll packet to server
//                        client.world.sendPacket();

                            lastTime = client.world.getTime();
                            client.player.setVelocity(newVelocity);
                        }
                    }
                }
            }
            if(client.options.sprintKey.isPressed() && rollUsed && (rollCooldown < client.world.getTime() - lastTime) && isClientMoving(client)) {
                client.player.setSprinting(true);
            } else if (client.player != null) {
                client.player.setSprinting(false);

            }
        });

        // Targeting code
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (closestEntity != null && !closestEntity.isAlive()) {
                closestEntity = null;
            }
            if (BetterThirdPerson.getCameraManager().hasCustomCamera()) {
                if(!targetKeybind.isPressed()){
                    targetKeyUsed = false;
                }
                if(targetKeybind.isPressed() && !targetKeyUsed) {
                    assert client.world != null;
                    targetKeyUsed = true;
                    for (Entity entity: client.world.getEntities()) {
                        if (entity != client.player && entity instanceof MobEntity) {
                            if (entity.getPos().distanceTo(client.player.getPos()) < minDistance && closestEntity == null) {
                                closestEntity = entity;
                            }
                            if (entity.getPos().distanceTo(client.player.getPos()) < minDistance && entity.getPos().distanceTo(client.player.getPos()) < closestEntity.getPos().distanceTo(client.player.getPos())) {
                                closestEntity = entity;
                            }
                        }
                    }
                }
                if (closestEntity != null && client.player != null) {
                    camera = new OldBraceletCamera(BetterThirdPerson.getCameraManager().getCustomCamera());
                    camera.lookAt(client.player.getPos(), closestEntity.getPos());
                    if (closestEntity.getPos().distanceTo(client.player.getPos()) > maxDistance){
                        closestEntity = null;
                    }
                }
            }
        });
    }

    public static void register() {
        targetKeybind = KeyBindingHelper.registerKeyBinding(new KeyBind(
                "key."+ OldBracelet.MOD_ID +".targetkey", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                KeyBind.MOVEMENT_CATEGORY // The translation key of the keybinding's category.
        ));
        registerKeyInputs();
    }

    // Returns true if the player is pressing movement keys
    public static boolean isClientMoving(MinecraftClient client){
        return client.options.forwardKey.isPressed() || client.options.backKey.isPressed() || client.options.rightKey.isPressed() || client.options.leftKey.isPressed();
    }
}
