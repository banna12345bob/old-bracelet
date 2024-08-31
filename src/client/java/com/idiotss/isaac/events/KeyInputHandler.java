package com.idiotss.isaac.events;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.animation.AnimatablePlayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static KeyBinding rollKeybind;
    private static int multiplier = 10;
    private static int maxSpeed = 10;
    private static float cooldown = 2.5F;
    private static float lastTime;
    private static Vec3d currentVelocity;
    private static Vec3d newVelocity;

    public static void registerKeyInputs() {
//        lastTime = TimeHelper.SECOND_IN_MILLIS;
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (KeyInputHandler.rollKeybind.wasPressed()) {
//                TODO: Implement cooldown
                OldBracelet.LOGGER.info(String.valueOf(TimeHelper.SECOND_IN_MILLIS - lastTime));
                if (cooldown < TimeHelper.SECOND_IN_MILLIS - lastTime) {
                    currentVelocity = client.player.getVelocity();
                    if (currentVelocity.x != 0 && currentVelocity.y != 0) {
                        newVelocity = new Vec3d(currentVelocity.x * multiplier, 0, currentVelocity.z * multiplier);
                        if (Math.abs(((int)newVelocity.x^2) + ((int)newVelocity.z^2)) < maxSpeed) {
                            client.player.setVelocity(newVelocity);
                            lastTime = TimeHelper.SECOND_IN_MILLIS;
                        }
                        ((AnimatablePlayer)client.player).playAnimation("old-bracelet:roll", client.player.getVelocity());
                    }
                }
            }
        });

    }

    public static void register() {
        rollKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.oldbracelet.rollkey", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "key.category.oldbracelet.test" // The translation key of the keybinding's category.
        ));

        registerKeyInputs();
    }
}
