package com.tacz.guns.compat.iris.pbr;

import com.tacz.guns.client.resource.texture.ZipPackTexture;

import net.irisshaders.iris.texture.pbr.loader.PBRTextureLoader;
import net.minecraft.resource.ResourceManager;

public class ZipPackTexturePBRLoader implements PBRTextureLoader<ZipPackTexture> {
    @Override
    public void load(ZipPackTexture zipPackTexture, ResourceManager resourceManager, PBRTextureConsumer pbrTextureConsumer) {
        FilePackTexturePBRLoader.loadPBRTextures(zipPackTexture.getRegisterId(), pbrTextureConsumer);
    }
}