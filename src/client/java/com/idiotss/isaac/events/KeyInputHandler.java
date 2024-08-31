package com.idiotss.isaac.events;

import com.idiotss.isaac.animation.AnimatablePlayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static KeyBinding rollKeybind;
    public static int multiplier = 10;
    public static Vec3d currentVelocity;
    public static Vec3d newVelocity;

    public static void registerKeyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (KeyInputHandler.rollKeybind.wasPressed()) {
                currentVelocity = client.player.getVelocity();
                if (currentVelocity.x != 0 && currentVelocity.y != 0) {
                    newVelocity = new Vec3d(currentVelocity.x * multiplier, 0, currentVelocity.z * multiplier);
                    client.player.setVelocity(newVelocity);
                    ((AnimatablePlayer)client.player).playRollAnimation("old-bracelet:roll", client.player.getVelocity());
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
