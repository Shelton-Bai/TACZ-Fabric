package com.tacz.guns.compat.iris.pbr;

import com.tacz.guns.client.resource.texture.FilePackTexture;

import net.irisshaders.iris.texture.pbr.PBRType;
import net.irisshaders.iris.texture.pbr.loader.PBRTextureLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class FilePackTexturePBRLoader implements PBRTextureLoader<FilePackTexture> {
    @Override
    public void load(FilePackTexture filePackTexture, ResourceManager resourceManager, PBRTextureConsumer pbrTextureConsumer) {
        loadPBRTextures(filePackTexture.getRegisterId(), pbrTextureConsumer);
    }

    static void loadPBRTextures(Identifier id, PBRTextureConsumer pbrTextureConsumer) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        loadPBRTexture(id, PBRType.NORMAL, textureManager, pbrTextureConsumer::acceptNormalTexture);
        loadPBRTexture(id, PBRType.SPECULAR, textureManager, pbrTextureConsumer::acceptSpecularTexture);
    }

    private static void loadPBRTexture(Identifier id, PBRType type, TextureManager textureManager, java.util.function.Consumer<net.minecraft.client.texture.AbstractTexture> consumer) {
        Identifier pbrId = new Identifier(id.getNamespace(), id.getPath() + type.getSuffix());
        if (textureManager.textures.containsKey(pbrId)) {
            consumer.accept(textureManager.getTexture(pbrId));
        }
    }
}
