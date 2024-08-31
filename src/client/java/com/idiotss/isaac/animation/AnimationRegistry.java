package com.idiotss.isaac.animation;

import com.idiotss.isaac.OldBracelet;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationSerializing;
import net.minecraft.resource.ResourceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationRegistry {
    public static Map<String, KeyframeAnimation> animations = new HashMap<>();

    public static void load(ResourceManager resourceManager) {
        var dataFolder = "animations";
        for (var entry : resourceManager.findResources(dataFolder, fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                List<KeyframeAnimation> readAnimations = AnimationSerializing.deserializeAnimation(resource.getInputStream());
                KeyframeAnimation animation = readAnimations.get(0);

                var id = identifier
                        .toString()
                        .replace(dataFolder + "/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                AnimationRegistry.animations.put(id, animation);
            } catch (Exception e) {
                OldBracelet.LOGGER.error("Failed to load animation " + identifier.toString());
                e.printStackTrace();
            }
        }
    }
}