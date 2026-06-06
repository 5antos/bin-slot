package net.pemiridosa.binslot.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface BinSlotHoverCallback {
    Event<BinSlotHoverCallback> EVENT = EventFactory.createArrayBacked(BinSlotHoverCallback.class,
        (listeners) -> (font, context, slotX, slotY, mouseX, mouseY, isShiftDown, isCreativeInventory, callbackInfo) -> {
            for (BinSlotHoverCallback listener : listeners) {
                listener.onBinSlotHover(font, context, slotX, slotY, mouseX, mouseY, isShiftDown, isCreativeInventory, callbackInfo);
            }
        });

    void onBinSlotHover(Font font, GuiGraphicsExtractor context, int slotX, int slotY, int mouseX, int mouseY, boolean isShiftDown, boolean isCreativeInventory, CallbackInfo callbackInfo);
}
