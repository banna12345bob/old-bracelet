package com.idiotss.isaac.network.listener;

import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import net.minecraft.network.listener.ServerPlayPacketListener;

public interface OldBraceletServerPlayPacketListener {

    void onTriggerBlockUpdate(TriggerBlockUpdateC2SPacket packet, ServerPlayPacketListener serverPlayPacketListener);
}
