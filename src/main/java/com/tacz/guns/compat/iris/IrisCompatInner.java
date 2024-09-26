package com.tacz.guns.compat.iris;

import com.tacz.guns.compat.iris.pbr.PBRRegister;
import net.irisshaders.batchedentityrendering.impl.FullyBufferedMultiBufferSource;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import net.minecraft.client.render.VertexConsumerProvider;

public class IrisCompatInner {
    public static boolean isRenderShadow() {
        return ShadowRenderingState.areShadowsCurrentlyBeingRendered();
    }

    public static boolean endBatch(VertexConsumerProvider.Immediate bufferSource) {
        if (bufferSource instanceof FullyBufferedMultiBufferSource fullyBufferedMultiBufferSource) {
            fullyBufferedMultiBufferSource.draw();
            return true;
        }
        return false;
    }

    public static void registerPBRLoader() {
        PBRRegister.registerPBRLoader();
    }
}
