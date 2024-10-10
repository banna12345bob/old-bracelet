package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.OldBraceletScreenHandler;
import com.idiotss.isaac.content.blocks.OldBraceletBlockEntities;
import com.idiotss.isaac.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.HolderLookup;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.CommandBlockExecutor;
import org.jetbrains.annotations.Nullable;


public class TriggerBlockEntity extends BlockEntity implements TickableBlockEntity {
    @Nullable
    private String triggerName;
    private BlockPos offset = new BlockPos(0, 1, 0);
    private Vec3i size = Vec3i.ZERO;
    private BlockRotation rotation = BlockRotation.NONE;
    private boolean showBoundingBox = true;
    private boolean enabled = true;
    private boolean shouldDisableOnTrigger = false;
    private boolean shouldRunCommands = true;

    private final CommandBlockExecutor commandExecutor = new CommandBlockExecutor() {
        @Override
        public void setCommand(String command) {
            super.setCommand(command);
            TriggerBlockEntity.this.markDirty();
        }

        @Override
        public ServerWorld getWorld() {
            return (ServerWorld)TriggerBlockEntity.this.world;
        }

        @Override
        public void markDirty() {
            BlockState blockState = TriggerBlockEntity.this.world.getBlockState(TriggerBlockEntity.this.pos);
            this.getWorld().updateListeners(TriggerBlockEntity.this.pos, blockState, blockState, Block.NOTIFY_ALL);
        }

        @Override
        public Vec3d getPos() {
            return Vec3d.ofCenter(TriggerBlockEntity.this.pos);
        }

        @Override
        public ServerCommandSource getSource() {
            return new ServerCommandSource(
                    this,
                    Vec3d.ofCenter(TriggerBlockEntity.this.pos),
                    new Vec2f(0.0F, 0.0F),
                    this.getWorld(),
                    2,
                    this.getName().getString(),
                    this.getName(),
                    this.getWorld().getServer(),
                    null
            );
        }

        @Override
        public boolean isValid() {
            return !TriggerBlockEntity.this.isRemoved();
        }
    };

    public TriggerBlockEntity(BlockPos pos, BlockState state) {
        super(OldBraceletBlockEntities.TRIGGER_BLOCK_ENTITY, pos, state);
    }

    public boolean isPlayerInside(PlayerEntity player) {
        if (player.isSpectator() || !this.enabled) {
            return false;
        }
        if (shouldDisableOnTrigger && getTriggerBoxWithOffset().contains(player.getPos())) {
            this.enabled = false;
        }
        return getTriggerBoxWithOffset().contains(player.getPos());
    }

    @Override
    protected void writeNbt(NbtCompound nbt, HolderLookup.Provider lookupProvider) {
        super.writeNbt(nbt, lookupProvider);
        this.commandExecutor.writeNbt(nbt, lookupProvider);
        nbt.putString("name", this.getTriggerName());
        nbt.putInt("posX", this.offset.getX());
        nbt.putInt("posY", this.offset.getY());
        nbt.putInt("posZ", this.offset.getZ());
        nbt.putInt("sizeX", this.size.getX());
        nbt.putInt("sizeY", this.size.getY());
        nbt.putInt("sizeZ", this.size.getZ());
        nbt.putString("rotation", this.rotation.toString());
        nbt.putBoolean("showboundingbox", this.showBoundingBox);
        nbt.putBoolean("enabled", this.enabled);
        nbt.putBoolean("shouldDisableOnTrigger", this.shouldDisableOnTrigger);
    }

    @Override
    protected void readNbtImpl(NbtCompound nbt, HolderLookup.Provider lookupProvider) {
        super.readNbtImpl(nbt, lookupProvider);
        this.commandExecutor.readNbt(nbt, lookupProvider);
        this.setTriggerName(nbt.getString("name"));
        int i = MathHelper.clamp(nbt.getInt("posX"), -48, 48);
        int j = MathHelper.clamp(nbt.getInt("posY"), -48, 48);
        int k = MathHelper.clamp(nbt.getInt("posZ"), -48, 48);
        this.offset = new BlockPos(i, j, k);
        int l = MathHelper.clamp(nbt.getInt("sizeX"), 0, 48);
        int m = MathHelper.clamp(nbt.getInt("sizeY"), 0, 48);
        int n = MathHelper.clamp(nbt.getInt("sizeZ"), 0, 48);
        this.size = new Vec3i(l, m, n);

        try {
            this.rotation = BlockRotation.valueOf(nbt.getString("rotation"));
        } catch (IllegalArgumentException var12) {
            this.rotation = BlockRotation.NONE;
        }

        this.showBoundingBox = nbt.getBoolean("showboundingbox");
        this.enabled = nbt.getBoolean("enabled");
        this.shouldDisableOnTrigger = nbt.getBoolean("shouldDisableOnTrigger");
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.of(this);
    }

    @Override
    public NbtCompound toSyncedNbt(HolderLookup.Provider lookupProvider) {
        return this.toComponentlessNbt(lookupProvider);
    }

    public boolean openScreen(PlayerEntity player) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        } else {
            if (player.getCommandSenderWorld().isClient) {
                ((OldBraceletScreenHandler)player).openTriggerBlockScreen(this);
            }
        }

        return true;
    }

    public String getTriggerName() {
        return this.triggerName == null ? "" : this.triggerName.toString();
    }

    public CommandBlockExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

    public boolean shouldRunCommands() {
        return this.shouldRunCommands;
    }

    public void setTriggerName(@Nullable String triggerName) {
        this.triggerName = triggerName;
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public void setOffset(BlockPos pos) {
        this.offset = pos;
    }

    public Vec3i getSize() {
        return this.size;
    }

    public void setSize(Vec3i size) {
        this.size = size;
    }

    public BlockRotation getRotation() {
        return this.rotation;
    }

    public void setRotation(BlockRotation rotation) {
        this.rotation = rotation;
    }

    public boolean isTriggerEnabled() {
        return this.enabled;
    }

    public void setTriggerEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean shouldDisableOnTrigger() {
        return this.shouldDisableOnTrigger;
    }

    public void setShouldDisableOnTrigger(boolean shouldDisableOnTrigger) {
        this.shouldDisableOnTrigger = shouldDisableOnTrigger;
    }

    public boolean shouldShowBoundingBox() {
        return this.showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    @Override
    public void tick() {
        if (this.enabled)
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 1);
    }

    public Box getTriggerBox() {
        double d = offset.getX();
        double e = offset.getZ();
        double g = offset.getY();
        double h = g + (double)size.getY();
        double k = size.getX();
        double l = size.getZ();

        double m;
        double n;
        double o;
        double p;
        switch (this.getRotation()) {
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
        return new Box(m, g, n, o, h, p);
    }

    public Box getTriggerBoxWithOffset() {
        return getTriggerBox().offset(pos);
    }

    public static enum Action {
        UPDATE_DATA,
        SAVE_AREA,
        LOAD_AREA,
        SCAN_AREA;
    }
}
