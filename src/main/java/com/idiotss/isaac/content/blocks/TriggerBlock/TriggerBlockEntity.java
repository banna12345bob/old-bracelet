package com.idiotss.isaac.content.blocks.TriggerBlock;

import com.idiotss.isaac.OldBraceletScreenHandler;
import com.idiotss.isaac.content.blocks.OldBraceletBlockEntities;
import com.idiotss.isaac.content.blocks.OldBraceletBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.HolderLookup;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TriggerBlockEntity extends BlockEntity {
    @Nullable
    private String triggerName;
    private BlockPos offset = new BlockPos(0, 1, 0);
    private Vec3i size = Vec3i.ZERO;
    private BlockRotation rotation = BlockRotation.NONE;
    private boolean showBoundingBox = true;

    public TriggerBlockEntity(BlockPos pos, BlockState state) {
        super(OldBraceletBlockEntities.TRIGGER_BLOCK_ENTITY, pos, state);
//        this.mode = state.get(StructureBlock.MODE);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, HolderLookup.Provider lookupProvider) {
        super.writeNbt(nbt, lookupProvider);
        nbt.putString("name", this.getTriggerName());
        nbt.putInt("posX", this.offset.getX());
        nbt.putInt("posY", this.offset.getY());
        nbt.putInt("posZ", this.offset.getZ());
        nbt.putInt("sizeX", this.size.getX());
        nbt.putInt("sizeY", this.size.getY());
        nbt.putInt("sizeZ", this.size.getZ());
        nbt.putString("rotation", this.rotation.toString());
        nbt.putBoolean("showboundingbox", this.showBoundingBox);
    }

    @Override
    protected void readNbtImpl(NbtCompound nbt, HolderLookup.Provider lookupProvider) {
        super.readNbtImpl(nbt, lookupProvider);
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

    public boolean hasStructureName() {
        return this.triggerName != null;
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

    public boolean shouldShowBoundingBox() {
        return this.showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    public static enum Action {
        UPDATE_DATA,
        SAVE_AREA,
        LOAD_AREA,
        SCAN_AREA;
    }
}
