package me.p5antos.binslot.network;

import me.p5antos.binslot.network.payload.MouseClickC2SPayload;
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

        ServerPlayNetworking.registerGlobalReceiver(MouseClickC2SPayload.ID, NetworkManager::handleMouseClick);
    }

    private static void handleMouseClick(MouseClickC2SPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        if (player.isSpectator())
            return;

        ItemStack cursorStack = payload.itemStack().copy();

        ScreenHandler screenHandler = player.currentScreenHandler;

        if (!ItemStack.areEqual(cursorStack, screenHandler.getCursorStack()))
            return;
        else if (cursorStack.isEmpty())
            return;

        boolean isRightClick = payload.isRightClick();
        boolean isShiftClick = payload.isShiftClick();

        if (isShiftClick) {
            deleteAllMatchingItems(player, cursorStack);

            screenHandler.setCursorStack(ItemStack.EMPTY);
        } else if (isRightClick) {
            cursorStack.split(1);

            screenHandler.setCursorStack(cursorStack);
        } else {
            ItemStack newCursorStack = ItemStack.EMPTY;
            
            screenHandler.setCursorStack(newCursorStack);
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
