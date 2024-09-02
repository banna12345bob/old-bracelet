package com.idiotss.isaac.events;

import com.idiotss.isaac.animation.AnimatablePlayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
//    private static KeyBinding rollKeybind;
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
                        ((AnimatablePlayer) client.player).playAnimation("old-bracelet:roll", client.player.getVelocity(), 1.0F);
                        lastTime = client.world.getTime();
                        client.player.setVelocity(newVelocity);
                    }
                }
            }
        });

    }

    public static void register() {
//        rollKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                "key.oldbracelet.rollkey", // The translation key of the keybinding's name
//                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
//                GLFW.GLFW_KEY_R, // The keycode of the key
//                KeyBinding.MOVEMENT_CATEGORY // The translation key of the keybinding's category.
//        ));

        registerKeyInputs();
    }

    // Returns true if the player is pressing movement keys
    public static boolean isClientMoving(MinecraftClient client){
        return client.options.forwardKey.isPressed() || client.options.backKey.isPressed() || client.options.rightKey.isPressed() || client.options.leftKey.isPressed();
    }
}
