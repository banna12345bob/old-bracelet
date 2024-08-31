package com.idiotss.isaac;

import com.idiotss.isaac.events.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import com.idiotss.isaac.animation.AnimationRegistry;

public class OldBraceletClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			var resourceManager = MinecraftClient.getInstance().getResourceManager();
			AnimationRegistry.load(resourceManager);
		});

		KeyInputHandler.register();
	}
}