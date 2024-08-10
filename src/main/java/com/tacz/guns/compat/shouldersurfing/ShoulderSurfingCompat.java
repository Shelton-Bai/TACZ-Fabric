package com.tacz.guns.compat.shouldersurfing;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public final class ShoulderSurfingCompat {
    private static final Version VERSION;
    private static final String MOD_ID = "shouldersurfing";
    private static boolean INSTALLED = false;
    private static boolean UNSUPPORTED = false;

    public static void init() {
        INSTALLED = FabricLoader.getInstance().isModLoaded(MOD_ID);
        if (INSTALLED) {
            FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(mod -> {
                if (mod.getMetadata().getVersion().compareTo(VERSION) < 0) {
                    UNSUPPORTED = true;
                }
            });
        }
    }

    public static boolean showCrosshair() {
        if (INSTALLED) {
            if (UNSUPPORTED) {
                return false;
            }
            return ShoulderSurfingCompatInner.showCrosshair();
        }
        return false;
    }

    static {
        try {
            VERSION = Version.parse("4.0.0");
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
    }
}
