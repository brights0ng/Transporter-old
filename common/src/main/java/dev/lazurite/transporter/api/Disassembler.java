package dev.lazurite.transporter.api;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lazurite.transporter.api.buffer.PatternBuffer;
import dev.lazurite.transporter.api.pattern.Pattern;
import dev.lazurite.transporter.impl.pattern.NetworkHandler;
import dev.lazurite.transporter.impl.pattern.BufferEntry;
import dev.lazurite.transporter.impl.pattern.QuadConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The calls in this class are important in that they're necessary in order for anything within this library to work.
 * From here, you can obtain a {@link Pattern} based on a block, block entity, entity, or item.
 *
 * In each of these methods, you have the option to transform the pattern any way you want before it
 * is read as a {@link Pattern}. This is done via passing a {@link PoseStack}.
 *
 * @see PatternBuffer
 * @see Pattern
 * @since 1.0.0
 */
public interface Disassembler {

    static Pattern getBlock(BlockState blockState) {
        return getBlock(blockState, null);
    }

    static Pattern getBlock(BlockState blockState, @Nullable PoseStack transformation) {
        if (transformation == null) {
            transformation = new PoseStack();
        }

        var client = Minecraft.getInstance();
        var consumer = QuadConsumer.create();
        var model = client.getBlockRenderer().getBlockModel(blockState);

        Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(transformation.last(), consumer, blockState, model, 0, 0, 0, 0, 0);

        var entry = new BufferEntry(consumer, Pattern.Type.BLOCK, Block.getId(blockState));
        NetworkHandler.sendPattern(entry);
        return entry;
    }

    static Pattern getBlockEntity(BlockEntity blockEntity) {
        return getBlockEntity(blockEntity, null);
    }

    static Pattern getBlockEntity(BlockEntity blockEntity, @Nullable PoseStack transformation) {
        if (transformation == null) {
            transformation = new PoseStack();
        }

        var consumer = QuadConsumer.create();
        var renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);

        if (renderer != null) {
            renderer.render(blockEntity, 0, transformation, consumer.asProvider(), 0, 0);
        }

        var entry = new BufferEntry(consumer, Pattern.Type.BLOCK, Block.getId(blockEntity.getBlockState()));
        NetworkHandler.sendPattern(entry);
        return entry;
    }

    static Pattern getEntity(Entity entity) {
        return getEntity(entity, null);
    }

    static Pattern getEntity(Entity entity, @Nullable PoseStack transformation) {
        if (transformation == null) {
            transformation = new PoseStack();
        }

        var consumer = QuadConsumer.create();
        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity)
                .render(entity, 0, 0, transformation, consumer.asProvider(), 0);

        var entry = new BufferEntry(consumer, Pattern.Type.ENTITY, BuiltInRegistries.ENTITY_TYPE.getId(entity.getType()));
        NetworkHandler.sendPattern(entry);
        return entry;
    }

    static Pattern getItem(Item item) {
        return getItem(item, null);
    }

    static Pattern getItem(Item item, @Nullable PoseStack transformation) {
        if (transformation == null) {
            transformation = new PoseStack();
        }

        var consumer = QuadConsumer.create();
        Minecraft.getInstance().getItemRenderer()
                .renderStatic(new ItemStack(item), ItemDisplayContext.GROUND, 0, 0, transformation, consumer.asProvider(), null, 0);

        var entry = new BufferEntry(consumer, Pattern.Type.ITEM, BuiltInRegistries.ITEM.getId(item));
        NetworkHandler.sendPattern(entry);
        return entry;
    }
}