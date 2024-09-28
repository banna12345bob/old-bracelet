package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.OldBracelet;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;


// Based on Structure Block
public class TriggerBlock extends BlockWithEntity implements OperatorBlock {
    public static final MapCodec<TriggerBlock> CODEC = createCodec(TriggerBlock::new);

    public static final BooleanProperty POWERED = Properties.POWERED;

    @Override
    public MapCodec<TriggerBlock> getCodec() {
        return CODEC;
    }

    public TriggerBlock(AbstractBlock.Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, Boolean.valueOf(false)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    protected boolean isRedstonePowerSource(BlockState state) {
        return true;
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

    private int getRedstoneOutput(BlockState state) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return this.getRedstoneOutput(state);
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? this.getRedstoneOutput(state) : 0;
    }

    // Ticker ain't Ticking
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
        OldBracelet.LOGGER.info("TICKER");
        if (world.getBlockEntity(pos) instanceof TriggerBlockEntity triggerBlockEntity) {
            for (PlayerEntity player: world.getPlayers()) {
                if (triggerBlockEntity.isPlayerInside(player)) {
                    setRedstoneOutput(state, 15);
                    world.setBlockState(pos, state, Block.NOTIFY_ALL);
                    neighborUpdate(state, world, pos, this, pos, true);
                    world.emitGameEvent(player, state.get(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
                } else {
                    setRedstoneOutput(state, 0);
                    world.setBlockState(pos, state, Block.NOTIFY_ALL);
                    neighborUpdate(state, world, pos, this, pos, true);
                    world.emitGameEvent(player, state.get(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
                }
            }
        }
    }

    protected BlockState setRedstoneOutput(BlockState state, int rsOut) {
        return state.with(POWERED, Boolean.valueOf(rsOut > 0));
    }
}
