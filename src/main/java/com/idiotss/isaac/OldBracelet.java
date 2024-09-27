package com.idiotss.isaac;

import com.idiotss.isaac.content.blocks.OldBraceletBlockEntities;
import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.idiotss.isaac.network.OldBraceletPackets;
import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import net.fabricmc.api.ModInitializer;

import net.minecraft.feature_flags.FeatureFlagBitSet;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Identifier;
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
		OldBraceletPackets.register();
	}
}