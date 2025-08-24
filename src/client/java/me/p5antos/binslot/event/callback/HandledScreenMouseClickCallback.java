package me.p5antos.binslot.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;

public interface HandledScreenMouseClickCallback {
    Event<HandledScreenMouseClickCallback> EVENT = EventFactory.createArrayBacked(HandledScreenMouseClickCallback.class,
        (listeners) -> (isRightClick, mouseX, mouseY, itemStack, isCreativeInventory) -> {
            for (HandledScreenMouseClickCallback listener : listeners) {
                listener.onMouseClick(isRightClick, mouseX, mouseY, itemStack, isCreativeInventory);
            }
        });

    void onMouseClick(boolean isRightClick, double mouseX, double mouseY, ItemStack itemStack, boolean isCreativeInventory);
}
