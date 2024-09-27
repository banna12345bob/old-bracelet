package com.idiotss.isaac.mixin;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import com.idiotss.isaac.network.listener.OldBraceletServerPlayPacketListener;
import com.idiotss.isaac.network.packet.c2s.play.TriggerBlockUpdateC2SPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements OldBraceletServerPlayPacketListener {
    @Shadow public ServerPlayerEntity player;

    @Override
    public void onTriggerBlockUpdate(TriggerBlockUpdateC2SPacket packet, ServerPlayPacketListener serverPlayPacketListener) {
        OldBracelet.LOGGER.info("onTriggerBlockUpdate");
        NetworkThreadUtils.forceMainThread(packet, serverPlayPacketListener, this.player.getServerWorld());
        if (this.player.isCreativeLevelTwoOp()) {
            BlockPos blockPos = packet.getPos();
            BlockState blockState = this.player.getWorld().getBlockState(blockPos);
            if (this.player.getWorld().getBlockEntity(blockPos) instanceof TriggerBlockEntity triggerBlockEntity) {
                triggerBlockEntity.setTriggerName(packet.getStructureName());
                triggerBlockEntity.setOffset(packet.getOffset());
                triggerBlockEntity.setSize(packet.getSize());
                triggerBlockEntity.setMirror(packet.getMirror());
                triggerBlockEntity.setRotation(packet.getRotation());
                triggerBlockEntity.setShowBoundingBox(packet.shouldShowBoundingBox());
                if (triggerBlockEntity.hasStructureName()) {
                    String string = triggerBlockEntity.getTriggerName();
//                    if (packet.getAction() == TriggerBlockEntity.Action.LOAD_AREA) {
//                        if (triggerBlockEntity.saveStructure()) {
//                            this.player.sendMessage(Text.translatable("structure_block.save_success", string), false);
//                        } else {
//                            this.player.sendMessage(Text.translatable("structure_block.save_failure", string), false);
//                        }
//                    } else if (packet.getAction() == TriggerBlockEntity.Action.LOAD_AREA) {
//                        if (!triggerBlockEntity.isStructureAvailable()) {
//                            this.player.sendMessage(Text.translatable("structure_block.load_not_found", string), false);
//                        } else if (triggerBlockEntity.placeStructureIfSameSize(this.player.getServerWorld())) {
//                            this.player.sendMessage(Text.translatable("structure_block.load_success", string), false);
//                        } else {
//                            this.player.sendMessage(Text.translatable("structure_block.load_prepare", string), false);
//                        }
//                    } else if (packet.getAction() == TriggerBlockEntity.Action.SCAN_AREA) {
//                        if (triggerBlockEntity.detectStructureSize()) {
//                            this.player.sendMessage(Text.translatable("structure_block.size_success", string), false);
//                        } else {
//                            this.player.sendMessage(Text.translatable("structure_block.size_failure"), false);
//                        }
//                    }
                } else {
                    this.player.sendMessage(Text.translatable("structure_block.invalid_structure_name", packet.getStructureName()), false);
                }

                triggerBlockEntity.markDirty();
                this.player.getWorld().updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        }
    }
}
