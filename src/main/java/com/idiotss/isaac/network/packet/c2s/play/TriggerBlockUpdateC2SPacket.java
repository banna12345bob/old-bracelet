package com.idiotss.isaac.network.packet.c2s.play;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import com.idiotss.isaac.network.OldBraceletPackets;
import com.idiotss.isaac.network.listener.OldBraceletServerPlayPacketListener;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class TriggerBlockUpdateC2SPacket implements Packet<ServerPlayPacketListener>, CustomPayload {
    public static final PacketCodec<PacketByteBuf, TriggerBlockUpdateC2SPacket> CODEC = Packet.create(
            TriggerBlockUpdateC2SPacket::write, TriggerBlockUpdateC2SPacket::new
    );
    private static final int IGNORE_ENTITIES_MASK = 1;
    private static final int SHOW_AIR_MASK = 2;
    private static final int SHOW_BOUNDING_BOX_MASK = 4;
    private final BlockPos pos;
    private final TriggerBlockEntity.Action action;
    private final String structureName;
    private final BlockPos offset;
    private final Vec3i size;
    private final BlockMirror mirror;
    private final BlockRotation rotation;
    private final boolean showBoundingBox;

    public TriggerBlockUpdateC2SPacket(
            BlockPos pos,
            TriggerBlockEntity.Action action,
            String structureName,
            BlockPos offset,
            Vec3i size,
            BlockMirror mirror,
            BlockRotation rotation,
            boolean showBoundingBox
    ) {
        this.pos = pos;
        this.action = action;
        this.structureName = structureName;
        this.offset = offset;
        this.size = size;
        this.mirror = mirror;
        this.rotation = rotation;
        this.showBoundingBox = showBoundingBox;
        OldBracelet.LOGGER.info("PACKET");
    }

    private TriggerBlockUpdateC2SPacket(PacketByteBuf buf) {
        this.pos = buf.readPos();
        this.action = buf.readEnumConstant(TriggerBlockEntity.Action.class);
        this.structureName = buf.readString();
        int i = 48;
        this.offset = new BlockPos(MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48));
        int j = 48;
        this.size = new Vec3i(MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48));
        this.mirror = buf.readEnumConstant(BlockMirror.class);
        this.rotation = buf.readEnumConstant(BlockRotation.class);
        int k = buf.readByte();
        this.showBoundingBox = (k & 4) != 0;
    }

    private void write(PacketByteBuf buf) {
        buf.writePos(this.pos);
        buf.writeEnumConstant(this.action);
        buf.writeString(this.structureName);
        buf.writeByte(this.offset.getX());
        buf.writeByte(this.offset.getY());
        buf.writeByte(this.offset.getZ());
        buf.writeByte(this.size.getX());
        buf.writeByte(this.size.getY());
        buf.writeByte(this.size.getZ());
        buf.writeEnumConstant(this.mirror);
        buf.writeEnumConstant(this.rotation);
        int i = 0;

        if (this.showBoundingBox) {
            i |= 4;
        }

        buf.writeByte(i);
    }

    @Override
    public PacketType<TriggerBlockUpdateC2SPacket> getType() {
        return OldBraceletPackets.SET_TRIGGER_BLOCK;
    }

    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        ((OldBraceletServerPlayPacketListener)serverPlayPacketListener).onTriggerBlockUpdate(this, serverPlayPacketListener);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public TriggerBlockEntity.Action getAction() {
        return this.action;
    }

    public String getStructureName() {
        return this.structureName;
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public Vec3i getSize() {
        return this.size;
    }

    public BlockMirror getMirror() {
        return this.mirror;
    }

    public BlockRotation getRotation() {
        return this.rotation;
    }

    public boolean shouldShowBoundingBox() {
        return this.showBoundingBox;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return new Id<>(OldBraceletPackets.SET_TRIGGER_BLOCK.id());
    }
}
