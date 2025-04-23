package dev.lazurite.transporter.impl.compat.fabric;

import dev.lazurite.transporter.impl.compat.pattern.QuadConsumerProvider;
import dev.lazurite.transporter.impl.pattern.QuadConsumer;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumImpl {

    public static boolean isInstalled() {
        return FabricLoader.getInstance().isModLoaded("sodium");
    }

    public static QuadConsumer getSodiumCompatibleConsumer() {
        return QuadConsumerProvider.createSodiumQuadConsumer();
    }

}
