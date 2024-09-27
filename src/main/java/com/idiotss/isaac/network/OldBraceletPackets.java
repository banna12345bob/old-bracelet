package com.idiotss.isaac.network;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.util.Identifier;

public class OldBraceletPackets {
    public static final PacketType<TriggerBlockUpdateC2SPacket> SET_TRIGGER_BLOCK = createC2S("set_trigger_block");

    private static <T extends Packet<ClientPlayPacketListener>> PacketType<T> createS2C(String id) {
        return new PacketType<>(NetworkSide.S2C, Identifier.of(OldBracelet.MOD_ID, id));
    }

    private static <T extends Packet<ServerPlayPacketListener>> PacketType<T> createC2S(String id) {
        return new PacketType<>(NetworkSide.C2S, Identifier.of(OldBracelet.MOD_ID, id));
    }


    public static void register() {
        // This sucks balls
        PayloadTypeRegistry.playC2S().register(new CustomPayload.Id<>(SET_TRIGGER_BLOCK.id()), TriggerBlockUpdateC2SPacket.CODEC);
    }
}
