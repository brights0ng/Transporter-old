package dev.lazurite.transporter.impl;

import dev.lazurite.transporter.api.buffer.PatternBuffer;
import dev.lazurite.transporter.impl.buffer.PatternBufferImpl;
import dev.lazurite.transporter.impl.pattern.NetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transporter {

    public static final String MODID = "transporter";
    public static final Logger LOGGER = LogManager.getLogger("Transporter");

    private static PatternBuffer BUFFER;

    public static void initialize() {
        NetworkHandler.register();
        LOGGER.info("Beam me up, Scotty!");
    }

    public static PatternBuffer getPatternBuffer() {
        if (BUFFER == null) {
            BUFFER = new PatternBufferImpl();
        }
        return BUFFER;
    }

}