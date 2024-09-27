package com.idiotss.isaac.mixin;

import com.idiotss.isaac.OldBraceletScreenHandler;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements OldBraceletScreenHandler {
    @Override
    public void openTriggerBlockScreen(TriggerBlockEntity triggerBlock) {
    }
}
