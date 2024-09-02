package com.idiotss.isaac.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyBinding.class)
public interface KeybindingAccessor {

    @Accessor("timesPressed")
    int timesPressed();

    @Accessor("timesPressed")
    public void setTimesPressed(int timesPressed);
}
