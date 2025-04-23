package dev.lazurite.transporter.impl.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.lazurite.transporter.impl.pattern.QuadConsumer;

public interface Sodium {

    @ExpectPlatform
    static boolean isInstalled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    static QuadConsumer getSodiumCompatibleConsumer() {
        throw new AssertionError();
    }

}
