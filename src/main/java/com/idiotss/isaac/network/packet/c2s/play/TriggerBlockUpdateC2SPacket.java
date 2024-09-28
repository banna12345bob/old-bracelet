package com.idiotss.isaac.network.packet.c2s.play;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketEncoder;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.network.codec.PacketCodec;

import commonnetwork.networking.data.PacketContext;

public class TriggerBlockUpdateC2SPacket {
    public static final Identifier CHANNEL = Identifier.of(OldBracelet.MOD_ID, "set_trigger_block");
    public static final PacketCodec<PacketByteBuf, TriggerBlockUpdateC2SPacket> PACKET_CODEC =
            PacketCodec.create(new PacketEncoder<PacketByteBuf, TriggerBlockUpdateC2SPacket>() {
                                   @Override
                                   public void encode(PacketByteBuf buf, TriggerBlockUpdateC2SPacket triggerBlockUpdateC2SPacket) {
                                       triggerBlockUpdateC2SPacket.write(buf);
                                   }
                               }, TriggerBlockUpdateC2SPacket::new);

    private static final int IGNORE_ENTITIES_MASK = 1;
    private static final int SHOW_AIR_MASK = 2;
    private static final int SHOW_BOUNDING_BOX_MASK = 4;
    private static BlockPos pos;
    private static TriggerBlockEntity.Action action;
    private static String triggerName;
    private static BlockPos offset;
    private static Vec3i size;
    private static BlockRotation rotation;
    private static boolean showBoundingBox;

    public TriggerBlockUpdateC2SPacket(
            BlockPos pos,
            TriggerBlockEntity.Action action,
            String triggerName,
            BlockPos offset,
            Vec3i size,
            BlockRotation rotation,
            boolean showBoundingBox
    ) {
        this.pos = pos;
        this.action = action;
        this.triggerName = triggerName;
        this.offset = offset;
        this.size = size;
        this.rotation = rotation;
        this.showBoundingBox = showBoundingBox;
    }

    public TriggerBlockUpdateC2SPacket(PacketByteBuf buf) {
        this.pos = buf.readPos();
        this.action = buf.readEnumConstant(TriggerBlockEntity.Action.class);
        this.triggerName = buf.readString();
        int i = 48;
        this.offset = new BlockPos(MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48));
        int j = 48;
        this.size = new Vec3i(MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48));
        this.rotation = buf.readEnumConstant(BlockRotation.class);
        int k = buf.readByte();
        this.showBoundingBox = (k & 4) != 0;
    }

    private void write(PacketByteBuf buf) {
        buf.writePos(this.pos);
        buf.writeEnumConstant(this.action);
        buf.writeString(this.triggerName);
        buf.writeByte(this.offset.getX());
        buf.writeByte(this.offset.getY());
        buf.writeByte(this.offset.getZ());
        buf.writeByte(this.size.getX());
        buf.writeByte(this.size.getY());
        buf.writeByte(this.size.getZ());
        buf.writeEnumConstant(this.rotation);
        int i = 0;

        if (this.showBoundingBox) {
            i |= 4;
        }

        buf.writeByte(i);
    }

    public static CustomPayload.Id<CustomPayload> type()
    {
        return new CustomPayload.Id<>(CHANNEL);
    }

//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        OldBracelet.LOGGER.info("HERE");
//        OldBracelet.LOGGER.info(serverPlayPacketListener.getPhase().name());
//        ((OldBraceletServerPlayPacketListener)serverPlayPacketListener).onTriggerBlockUpdate(this, serverPlayPacketListener);
//    }

    public static void handle(PacketContext<TriggerBlockUpdateC2SPacket> ctx) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        NetworkThreadUtils.forceMainThread(ctx, ctx.message(), ctx.sender().getServerWorld());
        if (player.isCreativeLevelTwoOp()) {
            BlockPos blockPos = ctx.message().getPos();
            BlockState blockState = player.getWorld().getBlockState(blockPos);
            BlockEntity blockEntity = player.getWorld().getBlockEntity(blockPos);
            if (blockEntity instanceof TriggerBlockEntity) {
                TriggerBlockEntity triggerBlockEntity = (TriggerBlockEntity)blockEntity;
                triggerBlockEntity.setTriggerName(getTriggerName());
                triggerBlockEntity.setOffset(getOffset());
                triggerBlockEntity.setSize(getSize());
                triggerBlockEntity.setRotation(getRotation());
                triggerBlockEntity.setShowBoundingBox(shouldShowBoundingBox());

                // Doesn't update NBT data
                triggerBlockEntity.notifyAll();
                ctx.sender().getWorld().updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public static TriggerBlockEntity.Action getAction() {
        return action;
    }

    public static String getTriggerName() {
        return triggerName;
    }

    public static BlockPos getOffset() {
        return offset;
    }

    public static Vec3i getSize() {
        return size;
    }

    public static BlockRotation getRotation() {
        return rotation;
    }

    public static boolean shouldShowBoundingBox() {
        return showBoundingBox;
    }
}
