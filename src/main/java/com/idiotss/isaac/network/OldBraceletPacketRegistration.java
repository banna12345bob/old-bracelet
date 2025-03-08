package com.idiotss.isaac.network;

import com.idiotss.isaac.network.packet.c2s.play.PublishPlayerAnimationC2SPacket;
import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import commonnetwork.api.Network;

public class OldBraceletPacketRegistration {
    public static void register()
    {
        Network
                .registerPacket(TriggerBlockUpdateC2SPacket.type(), TriggerBlockUpdateC2SPacket.class,
                        TriggerBlockUpdateC2SPacket.PACKET_CODEC, TriggerBlockUpdateC2SPacket::handle)
                .registerPacket(PublishPlayerAnimationC2SPacket.type(), PublishPlayerAnimationC2SPacket.class,
                        PublishPlayerAnimationC2SPacket.PACKET_CODEC, PublishPlayerAnimationC2SPacket::handle);
    }
}
