package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.client.gui.screen.ingame.TriggerBlockScreen;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import com.idiotss.isaac.mixin.PlayerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends PlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Override
    public void openTriggerBlockScreen(TriggerBlockEntity triggerBlock) {
        client.setScreen(new TriggerBlockScreen(triggerBlock));
    }
}
