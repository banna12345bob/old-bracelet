package com.idiotss.isaac;

import com.idiotss.isaac.events.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldBraceletClient implements ClientModInitializer {
	public static final String MOD_ID = "old-bracelet";

	public static final Logger CLIENT_LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		KeyInputHandler.register();
	}
}