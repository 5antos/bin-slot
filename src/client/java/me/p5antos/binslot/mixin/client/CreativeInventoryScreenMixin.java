package me.p5antos.binslot.mixin.client;

import me.p5antos.binslot.event.callback.BinSlotHoverCallback;
import me.p5antos.binslot.event.callback.HandledScreenMouseClickCallback;
import me.p5antos.binslot.mixin.client.accessor.CreativeInventoryScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.HandledScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.ScreenAccessor;
import me.p5antos.binslot.util.Constants;
import me.p5antos.binslot.util.ScreenUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.objectweb.asm.Opcodes;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Inject(
        method = "slotClicked",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;destroyItemSlot:Lnet/minecraft/world/inventory/Slot;",
            opcode = Opcodes.GETFIELD
        ),
        cancellable = true
    )
    private void onMouseClick(Slot slot, int slotId, int button, ContainerInput actionType, CallbackInfo callbackInfo) {
        CreativeInventoryScreenAccessor screen = (CreativeInventoryScreenAccessor) this;

        if (slot != null && slot == screen.getDeleteItemSlot()) {
            if (callbackInfo.isCancellable() && !callbackInfo.isCancelled())
                callbackInfo.cancel();

            boolean isRightClick = button == Constants.RIGHT_MOUSE_BUTTON;

            HandledScreenAccessor<?> genericAccessor = (HandledScreenAccessor<?>) screen;
            {
                AbstractContainerMenu handler = genericAccessor.getHandler();

                if (handler instanceof CreativeModeInventoryScreen.ItemPickerMenu)
                    HandledScreenMouseClickCallback.EVENT.invoker().onMouseClick(
                        isRightClick,
                        slot.x,
                        slot.y,
                        handler.getCarried(),
                        true
                    );
            }
        }
    }

    @Inject(
        method = "extractRenderState",
        at = @At("TAIL")
    )
    private void onDrawBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks, CallbackInfo callbackInfo) {
        var screenHandler = ((AbstractContainerScreen<?>) (Object) this);

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

                if (isHoveringOverDeleteItemSlot) {
                    long windowHandle = net.minecraft.client.Minecraft.getInstance().getWindow().handle();
                    boolean isShiftDown = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                        || GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

                    BinSlotHoverCallback.EVENT.invoker().onBinSlotHover(
                        screenAccessor.getTextRenderer(),
                        context,
                        deleteItemSlot.x, deleteItemSlot.y,
                        mouseX, mouseY,
                        isShiftDown,
                        true,
                        callbackInfo
                    );
                }
            }
        }
    }
}
