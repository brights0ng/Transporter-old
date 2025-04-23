package dev.lazurite.transporter.forge;

import dev.lazurite.transporter.impl.Transporter;
import net.minecraftforge.fml.common.Mod;

@Mod(Transporter.MODID)
public class TransporterForge {

    public TransporterForge() {
        Transporter.initialize();
    }

}
