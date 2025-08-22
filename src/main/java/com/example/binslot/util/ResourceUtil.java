package com.example.binslot.util;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ResourceUtil {
    public static boolean isTextureBeingOverwritten(ResourceManager resourceManager, Identifier textureIdentifier) {
        return resourceManager.getAllResources(textureIdentifier).size() > 1;
    }
}
