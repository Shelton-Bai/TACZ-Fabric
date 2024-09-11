package com.tacz.guns.compat.iris;

import com.tacz.guns.init.CompatRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.function.Function;
import java.util.function.Supplier;

public final class IrisCompat {
    private static Function<VertexConsumerProvider.Immediate, Boolean> END_BATCH_FUNCTION;
    private static Supplier<Boolean> IS_RENDER_SHADOW_SUPPER;

    public static void initCompat() {
        FabricLoader.getInstance().getModContainer(CompatRegistry.IRIS).ifPresent(mod -> {
            END_BATCH_FUNCTION = IrisCompatInner::endBatch;
            IS_RENDER_SHADOW_SUPPER = IrisCompatInner::isRenderShadow;
            IrisCompatInner.registerPBRLoader();
        });
    }

    public static boolean isRenderShadow() {
        if (FabricLoader.getInstance().isModLoaded(CompatRegistry.IRIS)) {
            return IS_RENDER_SHADOW_SUPPER.get();
        }
        return false;
    }

    public static boolean isUsingRenderPack() {
        if (FabricLoader.getInstance().isModLoaded(CompatRegistry.IRIS)) {
            return IrisApi.getInstance().isShaderPackInUse();
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean endBatch(VertexConsumerProvider.Immediate bufferSource) {
        if (FabricLoader.getInstance().isModLoaded(CompatRegistry.IRIS)) {
            return END_BATCH_FUNCTION.apply(bufferSource);
        }
        return false;
    }
}
