package me.p5antos.binslot.network;

import me.p5antos.binslot.network.payload.TrashItemS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ClientNetworkManager {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TrashItemS2CPayload.ID, ClientNetworkManager::handleTrashItem);
    }

    public static void handleTrashItem(TrashItemS2CPayload payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();

        if (player.isSpectator())
            return;

        ItemStack cursorStack = payload.itemStack().copy();

        ScreenHandler screenHandler = player.currentScreenHandler;

        screenHandler.setCursorStack(cursorStack);
    }
}
