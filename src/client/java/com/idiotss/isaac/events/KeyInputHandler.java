package com.idiotss.isaac.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static com.idiotss.isaac.OldBraceletClient.CLIENT_LOGGER;

public class KeyInputHandler {
    public static KeyBinding rollKeybind;

    public static void registerKeyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (KeyInputHandler.rollKeybind.wasPressed()) {
                client.player.setVelocity(new Vec3d(0, 5, 0));
                CLIENT_LOGGER.info(String.valueOf(client.player.limbAnimator.isLimbMoving()));
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
