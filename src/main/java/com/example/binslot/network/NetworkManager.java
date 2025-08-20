package com.example.binslot.network;

import com.example.binslot.network.payload.MouseClickC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
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

        // Get the item ID for comparison
        Identifier targetItemId = Registries.ITEM.getId(targetStack.getItem());

        PlayerInventory playerInventory = player.getInventory();
        
        // Check all inventory slots (main inventory, hotbar, and offhand) in one loop
        for (int i = 0; i < playerInventory.size(); i++) {
            ItemStack stack = playerInventory.getStack(i);

            if (!stack.isEmpty() && Registries.ITEM.getId(stack.getItem()).equals(targetItemId))
                playerInventory.setStack(i, ItemStack.EMPTY);
        }
    }
}
