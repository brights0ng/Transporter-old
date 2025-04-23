package dev.lazurite.transporter.impl.pattern;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.lazurite.transporter.api.Disassembler;
import dev.lazurite.transporter.api.pattern.Pattern;
import dev.lazurite.transporter.impl.compat.Sodium;
import dev.lazurite.transporter.impl.pattern.model.Quad;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Another implementation of {@link Pattern} other than {@link BufferEntry}, this class
 * is used by {@link Disassembler} to capture vertex information and translate it into
 * a list of {@link Quad}s.
 * @see Disassembler
 * @see Quad
 */
public class QuadConsumer extends BufferBuilder implements Pattern {

    protected final List<Quad> quads = new LinkedList<>();
    protected final List<Vec3> points = new LinkedList<>();

    public static QuadConsumer create() {
        if (Sodium.isInstalled()) {
            return Sodium.getSodiumCompatibleConsumer();
        }
        return new QuadConsumer();
    }

    public QuadConsumer() {
        super(0x200000);
        this.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
    }

    @Override
    public @NotNull VertexConsumer vertex(double x, double y, double z) {
        points.add(new Vec3(x, y, z));
        return this;
    }

    /**
     * For every four points, create a new {@link Quad} and add it to quads.
     */
    @Override
    public void endVertex() {
        if (points.size() >= 4) {
            quads.add(new Quad(points));
            points.clear();
        }
    }

    @Override
    public List<Quad> getQuads() {
        return this.quads;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuadConsumer quadConsumer) {
            return quadConsumer.getQuads().equals(getQuads());
        }
        return false;
    }

    public Provider asProvider() {
        return new Provider(this);
    }

    /**
     * In some instances, a {@link MultiBufferSource} is required
     * instead of a {@link VertexConsumer}. In this situation, {@link QuadConsumer#asProvider()}
     * can be called and one of these objects will be returned containing the original
     * {@link QuadConsumer}.
     */
    private static class Provider implements MultiBufferSource {
        private final QuadConsumer pattern;

        public Provider(QuadConsumer pattern) {
            this.pattern = pattern;
        }

        @Override
        public @NotNull VertexConsumer getBuffer(RenderType type) {
            return pattern;
        }
    }

}