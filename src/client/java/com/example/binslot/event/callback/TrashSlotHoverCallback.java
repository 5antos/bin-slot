package com.example.binslot.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface TrashSlotHoverCallback {
    Event<TrashSlotHoverCallback> EVENT = EventFactory.createArrayBacked(TrashSlotHoverCallback.class,
        (listeners) -> (textRenderer, context, slotX, slotY, mouseX, mouseY, isShiftDown, callbackInfo) -> {
            for (TrashSlotHoverCallback listener : listeners) {
                listener.onTrashSlotHover(textRenderer, context, slotX, slotY, mouseX, mouseY, isShiftDown, callbackInfo);
            }
        });

    void onTrashSlotHover(TextRenderer textRenderer, DrawContext context, int slotX, int slotY, int mouseX, int mouseY, boolean isShiftDown, CallbackInfo callbackInfo);
}
