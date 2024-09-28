package com.idiotss.isaac.content.blocks;

import com.idiotss.isaac.OldBracelet;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntity;
import com.idiotss.isaac.content.blocks.TriggerBlock.TriggerBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OldBraceletBlockEntities {

    public static final BlockEntityType<TriggerBlockEntity> TRIGGER_BLOCK_ENTITY = registerBlockEntity(
            "trigger_block",
            BlockEntityType.Builder.create(TriggerBlockEntity::new, OldBraceletBlocks.TRIGGER_BLOCK)
    );

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType.Builder<T> builder) {
        Identifier id = Identifier.of(OldBracelet.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build());
    }

    public static void register() {
        OldBracelet.LOGGER.info("Registering blocks entities for " + OldBracelet.MOD_ID);
        BlockEntityRendererFactories.register(TRIGGER_BLOCK_ENTITY, TriggerBlockEntityRenderer::new);
    }
}
