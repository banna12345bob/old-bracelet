package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
public class TriggerBlockEntityRenderer implements BlockEntityRenderer<TriggerBlockEntity> {
    public TriggerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(TriggerBlockEntity triggerBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, int j) {
        if (MinecraftClient.getInstance().player.isCreativeLevelTwoOp() || MinecraftClient.getInstance().player.isSpectator()) {
            if (triggerBlockEntity.shouldShowBoundingBox() && MinecraftClient.getInstance().player.isHolding(OldBraceletBlocks.TRIGGER_BLOCK.asItem())) {
                double m = triggerBlockEntity.getTriggerBox().minX;
                double g = triggerBlockEntity.getTriggerBox().minY;
                double n = triggerBlockEntity.getTriggerBox().minZ;

                double o = triggerBlockEntity.getTriggerBox().maxX;
                double h = triggerBlockEntity.getTriggerBox().maxY;
                double p = triggerBlockEntity.getTriggerBox().maxZ;

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
                if (triggerBlockEntity.isPlayerInside(MinecraftClient.getInstance().player)) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, m, g, n, o, h, p, 0F, 0.9F, 0F, 1.0F, 0.0f, 0.0f, 0.0f);
                } else if (!triggerBlockEntity.isTriggerEnabled()) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, m, g, n, o, h, p, 0.9F, 0.0F, 0F, 1.0F, 0.9f, 0.0f, 0.0f);
                } else  {
                    WorldRenderer.drawBox(matrices, vertexConsumer, m, g, n, o, h, p, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
                }
//                this.renderInvisibleBlocks(triggerBlockEntity, vertexConsumers, matrices);
            }
        }
    }

    private void renderInvisibleBlocks(TriggerBlockEntity entity, VertexConsumerProvider vertexConsumers, MatrixStack matrices) {
        BlockView blockView = entity.getWorld();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        BlockPos blockPos = entity.getPos();
        BlockPos blockPos2 = blockPos.add(entity.getOffset());

        for (BlockPos blockPos3 : BlockPos.iterate(blockPos2, blockPos2.add(entity.getSize()).add(-1, -1, -1))) {
            BlockState blockState = blockView.getBlockState(blockPos3);
            boolean bl = blockState.isAir();
            if (bl) {
                float f = bl ? 0.05F : 0.0F;
                double d = (double)((float)(blockPos3.getX() - blockPos.getX()) + 0.45F - f);
                double e = (double)((float)(blockPos3.getY() - blockPos.getY()) + 0.45F - f);
                double g = (double)((float)(blockPos3.getZ() - blockPos.getZ()) + 0.45F - f);
                double h = (double)((float)(blockPos3.getX() - blockPos.getX()) + 0.55F + f);
                double i = (double)((float)(blockPos3.getY() - blockPos.getY()) + 0.55F + f);
                double j = (double)((float)(blockPos3.getZ() - blockPos.getZ()) + 0.55F + f);
                WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 0.5F, 0.5F, 1.0F, 1.0F, 0.5F, 0.5F, 1.0F);
            }
        }
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }
}
