package com.idiotss.isaac.network.packet.c2s.play;

import com.idiotss.isaac.OldBracelet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketEncoder;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.codec.PacketCodec;

import commonnetwork.networking.data.PacketContext;

public class PublishPlayerAnimationC2SPacket {
    public static final Identifier CHANNEL = Identifier.of(OldBracelet.MOD_ID, "publish_player_animation");
    public static final PacketCodec<PacketByteBuf, PublishPlayerAnimationC2SPacket> PACKET_CODEC =
            PacketCodec.create(new PacketEncoder<PacketByteBuf, PublishPlayerAnimationC2SPacket>() {
                @Override
                public void encode(PacketByteBuf buf, PublishPlayerAnimationC2SPacket rollPublishC2SPacket) {
                    rollPublishC2SPacket.write(buf);
                }
            }, PublishPlayerAnimationC2SPacket::new);

    private static String animationName;
    private static Vec3d direction;
    private static float speed;

    public PublishPlayerAnimationC2SPacket(
            String animationName,
            Vec3d direction,
            float speed
    ) {
        this.animationName = animationName;
        this.direction = direction;
        this.speed = speed;
    }

    public PublishPlayerAnimationC2SPacket(PacketByteBuf buf) {
        this.animationName = buf.readString();
        this.direction = buf.readVec3d();
        this.speed = buf.readFloat();
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.animationName);
        buf.writeVec3d(this.direction);
        buf.writeFloat(this.speed);
    }

    public static CustomPayload.Id<CustomPayload> type()
    {
        return new CustomPayload.Id<>(CHANNEL);
    }

    public static void handle(PacketContext<PublishPlayerAnimationC2SPacket> ctx) {
        ServerPlayerEntity player = ctx.sender();
        OldBracelet.LOGGER.info("TODO: Handle animation on: " + player.getProfileName());
    }

    public static String getAnimationName() { return animationName; }

    public static Vec3d getDirection() { return direction; }

    public static float getSpeed() { return speed; }
}
