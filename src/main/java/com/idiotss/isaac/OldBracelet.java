package com.idiotss.isaac;

import com.idiotss.isaac.content.blocks.OldBraceletBlockEntities;
import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.idiotss.isaac.network.OldBraceletPacketRegistration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldBracelet implements ModInitializer {
	public static final String MOD_ID = "old-bracelet";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		OldBraceletBlocks.register();
		OldBraceletBlockEntities.register();
		OldBraceletAttributes.register();
		OldBraceletPacketRegistration.register();
	}
}