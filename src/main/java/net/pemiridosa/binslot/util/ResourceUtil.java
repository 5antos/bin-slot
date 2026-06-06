package net.pemiridosa.binslot.util;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceUtil {
    public static boolean isTextureBeingOverwritten(ResourceManager resourceManager, Identifier textureIdentifier) {
        return resourceManager.getResourceStack(textureIdentifier).size() > 1;
    }
}
