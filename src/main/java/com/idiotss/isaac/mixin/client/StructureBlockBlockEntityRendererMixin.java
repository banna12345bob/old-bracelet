package com.idiotss.isaac.mixin.client;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockBlockEntityRenderer.class)
public class StructureBlockBlockEntityRendererMixin {

    @Inject(method = "renderInvisibleBlocks", at = @At("HEAD"), cancellable = true)
    private void renderInvisibleBlocksWithModBlocks(StructureBlockBlockEntity entity, VertexConsumerProvider vertexConsumers, MatrixStack matrices, CallbackInfo ci) {
        BlockView blockView = entity.getWorld();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        BlockPos blockPos = entity.getPos();
        BlockPos blockPos2 = StructureTestUtil.getStructureOrigin(entity);

        for (BlockPos blockPos3 : BlockPos.iterate(blockPos2, blockPos2.add(entity.getSize()).add(-1, -1, -1))) {
            BlockState blockState = blockView.getBlockState(blockPos3);
            boolean bl = blockState.isAir();
            boolean bl2 = blockState.isOf(Blocks.STRUCTURE_VOID);
            boolean bl3 = blockState.isOf(Blocks.BARRIER);
            boolean bl4 = blockState.isOf(Blocks.LIGHT);

            boolean bl1 = blockState.isOf(OldBraceletBlocks.TRIGGER_BLOCK);

            boolean bl5 = bl2 || bl3 || bl4 || bl1;
            if (bl || bl5) {
                float f = bl ? 0.05F : 0.0F;
                double d = (double)((float)(blockPos3.getX() - blockPos.getX()) + 0.45F - f);
                double e = (double)((float)(blockPos3.getY() - blockPos.getY()) + 0.45F - f);
                double g = (double)((float)(blockPos3.getZ() - blockPos.getZ()) + 0.45F - f);
                double h = (double)((float)(blockPos3.getX() - blockPos.getX()) + 0.55F + f);
                double i = (double)((float)(blockPos3.getY() - blockPos.getY()) + 0.55F + f);
                double j = (double)((float)(blockPos3.getZ() - blockPos.getZ()) + 0.55F + f);
                if (bl) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 0.5F, 0.5F, 1.0F, 1.0F, 0.5F, 0.5F, 1.0F);
                } else if (bl2) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 1.0F, 0.75F, 0.75F, 1.0F, 1.0F, 0.75F, 0.75F);
                } else if (bl3) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F);
                } else if (bl4) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F);
                } else if (bl1) {
                    WorldRenderer.drawBox(matrices, vertexConsumer, d, e, g, h, i, j, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F);
                }
            }
        }
        ci.cancel();
    }
}
