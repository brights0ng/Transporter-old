package dev.lazurite.transporter.impl.pattern;

import dev.lazurite.toolbox.api.network.ClientNetworking;
import dev.lazurite.toolbox.api.network.PacketRegistry;
import dev.lazurite.transporter.api.event.PatternBufferEvents;
import dev.lazurite.transporter.api.pattern.Pattern;
import dev.lazurite.transporter.impl.Transporter;
import dev.lazurite.transporter.impl.buffer.PatternBufferImpl;
import dev.lazurite.transporter.impl.pattern.model.Quad;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;

public interface NetworkHandler {

    ResourceLocation IDENTIFIER = new ResourceLocation(Transporter.MODID, "pattern");

    static void register() {
        PacketRegistry.registerServerbound(IDENTIFIER, NetworkHandler::onReceivePattern);
    }

    static void sendPattern(BufferEntry pattern) {
        ClientNetworking.send(IDENTIFIER, buf -> {
            buf.writeEnum(pattern.getType());
            buf.writeInt(pattern.getRegistryId());
            buf.writeInt(pattern.getQuads().size());

            for (final var quad : pattern.getQuads()) {
                quad.serialize(buf);
            }
        });
    }

    private static void onReceivePattern(PacketRegistry.ServerboundContext ctx) {
        var buf = ctx.byteBuf();
        var type = buf.readEnum(Pattern.Type.class);
        var registryId = buf.readInt();
        var quadCount = buf.readInt();
        var quads = new LinkedList<Quad>();

        for (var j = 0; j < quadCount; j++) {
            quads.add(Quad.deserialize(buf));
        }

        var entry = new BufferEntry(quads, type, registryId);
        var buffer = (PatternBufferImpl) Transporter.getPatternBuffer();
        buffer.put(entry);

        PatternBufferEvents.BUFFER_UPDATE.invoke(buffer);
    }

}
