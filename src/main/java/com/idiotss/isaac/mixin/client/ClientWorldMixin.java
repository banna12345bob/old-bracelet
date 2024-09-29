package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Mutable
    @Shadow @Final private static Set<Item> MARKER_PARTICLE_ITEMS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addModItems(CallbackInfo ci) {
        MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT, OldBraceletBlocks.TRIGGER_BLOCK.asItem());
    }
}
