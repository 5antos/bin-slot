package me.p5antos.binslot.mixin.client;

import me.p5antos.binslot.event.callback.BinSlotHoverCallback;
import me.p5antos.binslot.event.callback.HandledScreenMouseClickCallback;
import me.p5antos.binslot.mixin.client.accessor.CreativeInventoryScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.HandledScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.ScreenAccessor;
import me.p5antos.binslot.util.Constants;
import me.p5antos.binslot.util.ScreenUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Inject(
        method = "onMouseClick",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;deleteItemSlot:Lnet/minecraft/screen/slot/Slot;",
            opcode = Opcodes.GETFIELD
        ),
        cancellable = true
    )
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo callbackInfo) {
        CreativeInventoryScreenAccessor screen = (CreativeInventoryScreenAccessor) this;

        if (slot != null && slot == screen.getDeleteItemSlot()) {
            if (callbackInfo.isCancellable() && !callbackInfo.isCancelled())
                callbackInfo.cancel();

            boolean isRightClick = button == Constants.RIGHT_MOUSE_BUTTON;

            HandledScreenAccessor<?> genericAccessor = (HandledScreenAccessor<?>) screen;
            {
                ScreenHandler handler = genericAccessor.getHandler();

                if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler)
                    HandledScreenMouseClickCallback.EVENT.invoker().onMouseClick(
                        isRightClick,
                        slot.x,
                        slot.y,
                        handler.getCursorStack(),
                        true
                    );
            }
        }
    }

    @Inject(
        method = "drawBackground",
        at = @At("TAIL")
    )
    private void onDrawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        var screenHandler = ((HandledScreen<?>) (Object) this);

        if (screenHandler != null) {
            HandledScreenAccessor<?> accessor = ((HandledScreenAccessor<?>) screenHandler);

            ScreenAccessor screenAccessor = (ScreenAccessor) accessor;
            CreativeInventoryScreenAccessor creativeInventoryScreenAccessor = (CreativeInventoryScreenAccessor) accessor;

            Slot deleteItemSlot = creativeInventoryScreenAccessor.getDeleteItemSlot();

            if (deleteItemSlot != null) {
                int slotTopLeftCornerX = accessor.getX() + deleteItemSlot.x - Constants.SLOT_ICON_TEXTURE_OUTLINE_WIDTH;
                int slotTopLeftCornerY = accessor.getY() + deleteItemSlot.y - Constants.SLOT_ICON_TEXTURE_OUTLINE_WIDTH;

                boolean isHoveringOverDeleteItemSlot = ScreenUtil.isMouseOverArea(
                    mouseX, mouseY,
                    slotTopLeftCornerX, slotTopLeftCornerY,
                    Constants.CLICKABLE_WIDTH, Constants.CLICKABLE_HEIGHT
                );

                if (isHoveringOverDeleteItemSlot)
                    BinSlotHoverCallback.EVENT.invoker().onBinSlotHover(
                        screenAccessor.getTextRenderer(),
                        context,
                        deleteItemSlot.x, deleteItemSlot.y,
                        mouseX, mouseY,
                        Screen.hasShiftDown(),
                        true,
                        callbackInfo
                    );
            }
        }
    }
}
