package com.idiotss.isaac.mixin.client;

import net.minecraft.client.option.KeyBind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBind.class)
public interface KeybindingAccessor {

    @Accessor("timesPressed")
    int timesPressed();

    @Accessor("timesPressed")
    public void setTimesPressed(int timesPressed);
}
