package me.p5antos.binslot.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface BinSlotHoverCallback {
    Event<BinSlotHoverCallback> EVENT = EventFactory.createArrayBacked(BinSlotHoverCallback.class,
        (listeners) -> (textRenderer, context, slotX, slotY, mouseX, mouseY, isShiftDown, callbackInfo) -> {
            for (BinSlotHoverCallback listener : listeners) {
                listener.onBinSlotHover(textRenderer, context, slotX, slotY, mouseX, mouseY, isShiftDown, callbackInfo);
            }
        });

    void onBinSlotHover(TextRenderer textRenderer, DrawContext context, int slotX, int slotY, int mouseX, int mouseY, boolean isShiftDown, CallbackInfo callbackInfo);
}
