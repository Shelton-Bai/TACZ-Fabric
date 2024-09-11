package com.tacz.guns.mixin.client;

import com.tacz.guns.api.mixin.CameraAngles;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public class CameraMixin implements CameraAngles {

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @Override
    public void tacz$setAnglesInternal(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
