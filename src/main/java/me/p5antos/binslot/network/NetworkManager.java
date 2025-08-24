package me.p5antos.binslot.network;

import me.p5antos.binslot.network.payload.MouseClickC2SPayload;
import me.p5antos.binslot.network.payload.TrashItemS2CPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class NetworkManager {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(MouseClickC2SPayload.ID, MouseClickC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TrashItemS2CPayload.ID, TrashItemS2CPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MouseClickC2SPayload.ID, NetworkManager::handleMouseClick);
    }

    private static void handleMouseClick(MouseClickC2SPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        if (player.isSpectator())
            return;

        ItemStack cursorStack = payload.itemStack().copy();

        ScreenHandler screenHandler = player.currentScreenHandler;

        boolean isCreativeInventory = payload.isCreativeInventory();

        if (!ItemStack.areEqual(cursorStack, screenHandler.getCursorStack()) && !isCreativeInventory)
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

        screenHandler.setCursorStack(newCursorStack);

        TrashItemS2CPayload newPayload = new TrashItemS2CPayload(newCursorStack);

        ServerPlayNetworking.send(player, newPayload);
    }

    private static void clearInventory(ServerPlayerEntity player) {
        ScreenHandler screenHandler = player.currentScreenHandler;

        for (int i = 0; i < screenHandler.slots.size(); i++) {
            Slot slot = screenHandler.slots.get(i);

            slot.setStack(ItemStack.EMPTY);
        }
    }

    private static void deleteAllMatchingItems(ServerPlayerEntity player, ItemStack targetStack) {
        if (targetStack.isEmpty())
            return;

        Identifier targetItemId = Registries.ITEM.getId(targetStack.getItem());

        ScreenHandler screenHandler = player.currentScreenHandler;

        for (int i = 0; i < screenHandler.slots.size(); i++) {
            Slot slot = screenHandler.slots.get(i);

            ItemStack stack = slot.getStack();

            if (!stack.isEmpty() && Registries.ITEM.getId(stack.getItem()).equals(targetItemId))
                slot.setStack(ItemStack.EMPTY);
        }
    }
}
