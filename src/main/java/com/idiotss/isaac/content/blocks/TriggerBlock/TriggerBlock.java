package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


// Based on Structure Block
public class TriggerBlock extends BlockWithEntity implements OperatorBlock {
    public static final MapCodec<TriggerBlock> CODEC = createCodec(TriggerBlock::new);

    @Override
    public MapCodec<TriggerBlock> getCodec() {
        return CODEC;
    }

    public TriggerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TriggerBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TriggerBlockEntity) {
            return ((TriggerBlockEntity)blockEntity).openScreen(entity) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    

    private void doAction(ServerWorld world, TriggerBlockEntity blockEntity) {
//        switch (blockEntity.getMode()) {
//            case SAVE:
//                blockEntity.saveStructure(false);
//                break;
//            case LOAD:
//                blockEntity.placeStructure(world);
//                break;
//            case CORNER:
//                blockEntity.unloadStructure();
//            case DATA:
//        }
    }
}
