package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.OldBracelet;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Environment(EnvType.CLIENT)
public class TriggerBlockEntityRenderer implements BlockEntityRenderer<TriggerBlockEntity> {
    public TriggerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(TriggerBlockEntity triggerBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, int j) {
        if (MinecraftClient.getInstance().player.isCreativeLevelTwoOp() || MinecraftClient.getInstance().player.isSpectator()) {
            BlockPos blockPos = triggerBlockEntity.getOffset();
            Vec3i vec3i = triggerBlockEntity.getSize();
            if (vec3i.getX() >= 1 && vec3i.getY() >= 1 && vec3i.getZ() >= 1) {
                double d = blockPos.getX();
                double e = blockPos.getZ();
                double g = blockPos.getY();
                double h = g + (double)vec3i.getY();
                double k = vec3i.getX();
                double l = vec3i.getZ();

                double m;
                double n;
                double o;
                double p;
                switch (triggerBlockEntity.getRotation()) {
                    case CLOCKWISE_90:
                        m = l < 0.0 ? d : d + 1.0;
                        n = k < 0.0 ? e + 1.0 : e;
                        o = m - l;
                        p = n + k;
                        break;
                    case CLOCKWISE_180:
                        m = k < 0.0 ? d : d + 1.0;
                        n = l < 0.0 ? e : e + 1.0;
                        o = m - k;
                        p = n - l;
                        break;
                    case COUNTERCLOCKWISE_90:
                        m = l < 0.0 ? d + 1.0 : d;
                        n = k < 0.0 ? e : e + 1.0;
                        o = m + l;
                        p = n - k;
                        break;
                    default:
                        m = k < 0.0 ? d + 1.0 : d;
                        n = l < 0.0 ? e + 1.0 : e;
                        o = m + k;
                        p = n + l;
                }

                float q = 1.0F;
                float r = 0.9F;
                float s = 0.5F;
                if (triggerBlockEntity.shouldShowBoundingBox()) {
                    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
                    if (triggerBlockEntity.isPlayerInside(MinecraftClient.getInstance().player)){
                        WorldRenderer.drawBox(matrices, vertexConsumer, m, g, n, o, h, p, 0F, 0.9F, 0F, 1.0F, 0.0f, 0.0f, 0.0f);
                    } else {
                        WorldRenderer.drawBox(matrices, vertexConsumer, m, g, n, o, h, p, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
                    }
                }
            }
        }
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }
}
