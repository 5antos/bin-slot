package me.p5antos.binslot.network;

import me.p5antos.binslot.network.payload.MouseClickC2SPayload;
import me.p5antos.binslot.network.payload.TrashItemS2CPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NetworkManager {
    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(MouseClickC2SPayload.ID, MouseClickC2SPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TrashItemS2CPayload.ID, TrashItemS2CPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MouseClickC2SPayload.ID, NetworkManager::handleMouseClick);
    }

    private static void handleMouseClick(MouseClickC2SPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();

        if (player.isSpectator())
            return;

        ItemStack cursorStack = payload.itemStack().copy();

        AbstractContainerMenu menu = player.containerMenu;

        boolean isCreativeInventory = payload.isCreativeInventory();

        if (!ItemStack.isSameItemSameComponents(cursorStack, menu.getCarried()) && !isCreativeInventory)
            return;

        boolean isRightClick = payload.isRightClick();
        boolean isShiftClick = payload.isShiftClick();

        if (cursorStack.isEmpty() && isCreativeInventory && isShiftClick) {
            clearInventory(player);
            return;
        }

        ItemStack newCursorStack;

        if (isShiftClick) {
            deleteAllMatchingItems(player, cursorStack);

            newCursorStack = ItemStack.EMPTY;
        } else if (isRightClick) {
            cursorStack.split(1);

            newCursorStack = cursorStack;
        } else {
            newCursorStack = ItemStack.EMPTY;
        }

        menu.setCarried(newCursorStack);

        TrashItemS2CPayload newPayload = new TrashItemS2CPayload(newCursorStack);

        ServerPlayNetworking.send(player, newPayload);
    }

    private static void clearInventory(ServerPlayer player) {
        AbstractContainerMenu menu = player.containerMenu;

        for (int i = 0; i < menu.slots.size(); i++) {
            Slot slot = menu.slots.get(i);

            slot.set(ItemStack.EMPTY);
        }
    }

    private static void deleteAllMatchingItems(ServerPlayer player, ItemStack targetStack) {
        if (targetStack.isEmpty())
            return;

        AbstractContainerMenu menu = player.containerMenu;

        for (int i = 0; i < menu.slots.size(); i++) {
            Slot slot = menu.slots.get(i);

            ItemStack stack = slot.getItem();

            if (!stack.isEmpty() && stack.getItem() == targetStack.getItem())
                slot.set(ItemStack.EMPTY);
        }
    }
}
