package com.tacz.guns.compat.iris;

import com.tacz.guns.compat.iris.pbr.PBRRegister;
import com.tacz.guns.init.CompatRegistry;

import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.batchedentityrendering.impl.FullyBufferedMultiBufferSource;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import net.minecraft.client.render.VertexConsumerProvider;

public final class IrisCompat {
    private static boolean isIrisLoaded = false;

    public static void initCompat() {
        isIrisLoaded = FabricLoader.getInstance().isModLoaded(CompatRegistry.IRIS);
        
        if (isIrisLoaded) {
            PBRRegister.registerPBRLoader();
        }
    }

    public static boolean isRenderShadow() {
        if (!isIrisLoaded) return false;
        return ShadowRenderingState.areShadowsCurrentlyBeingRendered();
    }

    public static boolean isUsingRenderPack() {
        if (!isIrisLoaded) return false;
        return IrisApi.getInstance().isShaderPackInUse();
    }

    public static boolean endBatch(VertexConsumerProvider.Immediate bufferSource) {
        if (!isIrisLoaded) return false;
        if (bufferSource instanceof FullyBufferedMultiBufferSource fullyBufferedMultiBufferSource) {
            fullyBufferedMultiBufferSource.draw();
            return true;
        }
        return false;
    }
}
