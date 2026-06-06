package net.pemiridosa.binslot.network;

import net.pemiridosa.binslot.network.payload.TrashItemS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ClientNetworkManager {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TrashItemS2CPayload.ID, ClientNetworkManager::handleTrashItem);
    }

    public static void handleTrashItem(TrashItemS2CPayload payload, ClientPlayNetworking.Context context) {
        LocalPlayer player = context.player();

        if (player.isSpectator())
            return;

        ItemStack cursorStack = payload.itemStack().copy();

        AbstractContainerMenu menu = player.containerMenu;

        menu.setCarried(cursorStack);
    }
}
