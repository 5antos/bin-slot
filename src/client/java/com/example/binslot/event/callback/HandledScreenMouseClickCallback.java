package com.example.binslot.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface HandledScreenMouseClickCallback {
    Event<HandledScreenMouseClickCallback> EVENT = EventFactory.createArrayBacked(HandledScreenMouseClickCallback.class,
        (listeners) -> (isRightClick, mouseX, mouseY, itemStack, callbackInfoReturnable) -> {
            for (HandledScreenMouseClickCallback listener : listeners) {
                listener.onMouseClick(isRightClick, mouseX, mouseY, itemStack, callbackInfoReturnable);
            }
        });

    void onMouseClick(boolean isRightClick, double mouseX, double mouseY, ItemStack itemStack, CallbackInfoReturnable<Boolean> callbackInfoReturnable);
}
