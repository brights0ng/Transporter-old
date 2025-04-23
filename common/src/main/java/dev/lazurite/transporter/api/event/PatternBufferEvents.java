package dev.lazurite.transporter.api.event;

import dev.lazurite.toolbox.api.event.Event;
import dev.lazurite.transporter.api.buffer.PatternBuffer;
import dev.lazurite.transporter.impl.pattern.NetworkHandler;

/**
 * An even that is triggered when the server receives new entries for the pattern buffer.
 * @see NetworkHandler
 * @since 1.0.0
 */
public final class PatternBufferEvents {

    public static Event<BufferUpdate> BUFFER_UPDATE = Event.create();

    PatternBufferEvents() {
    }

    @FunctionalInterface
    public interface BufferUpdate {
        void onBufferUpdate(PatternBuffer patternBuffer);
    }

}