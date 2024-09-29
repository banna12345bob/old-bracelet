package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import com.idiotss.isaac.util.TickableBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


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
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return context.isHolding(OldBraceletBlocks.TRIGGER_BLOCK.asItem()) ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
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

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
        if (world.getBlockEntity(pos) instanceof TriggerBlockEntity triggerBlockEntity) {
            for (PlayerEntity player: world.getPlayers()) {
                if (triggerBlockEntity.isPlayerInside(player)) {

                    if (triggerBlockEntity.shouldRunCommands()) {
                        CommandBlockExecutor commandBlockExecutor = triggerBlockEntity.getCommandExecutor();
                        if (!Objects.equals(commandBlockExecutor.getCommand(), "")) {
                            boolean bl = !ChatUtil.isEmpty(commandBlockExecutor.getCommand());
                            this.execute(state, world, pos, commandBlockExecutor, bl);
                        }
                    }
                    state = setRedstoneOutput(state, true);
                } else {
                    state = setRedstoneOutput(state, false);
                }
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                neighborUpdate(state, world, pos, this, pos, true);
                world.emitGameEvent(player, state.get(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    private void execute(BlockState state, World world, BlockPos pos, CommandBlockExecutor executor, boolean hasCommand) {
        if (hasCommand) {
            executor.execute(world);
        } else {
            executor.setSuccessCount(0);
        }

        executeCommandChain(world, pos);
    }

    private static void executeCommandChain(World world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();

        BlockState blockState = world.getBlockState(mutable);
        Block block = blockState.getBlock();
        if (world.getBlockEntity(mutable) instanceof TriggerBlockEntity triggerBlockEntity) {
            CommandBlockExecutor commandBlockExecutor = triggerBlockEntity.getCommandExecutor();
            commandBlockExecutor.execute(world);

            world.updateComparators(mutable, block);
        }

    }

    protected BlockState setRedstoneOutput(BlockState state, boolean value) {
        return state.with(POWERED, value);
    }
}
