package net.pemiridosa.binslot.mixin.client;

import net.pemiridosa.binslot.event.callback.BinSlotHoverCallback;
import net.pemiridosa.binslot.mixin.client.accessor.HandledScreenAccessor;
import net.pemiridosa.binslot.mixin.client.accessor.ScreenAccessor;
import net.pemiridosa.binslot.extension.HotBarSlot;
import net.pemiridosa.binslot.util.ResourceUtil;
import net.pemiridosa.binslot.util.ScreenUtil;
import net.pemiridosa.binslot.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public class GenericContainerScreenMixin {
    @Inject(method = "extractContents", at = @At("TAIL"))
    private void onDrawBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks, CallbackInfo callbackInfo) {
        if ((Object)this instanceof CreativeModeInventoryScreen) return;

        var screenHandler = ((AbstractContainerScreen<?>) (Object) this);

        if (screenHandler != null) {
            HandledScreenAccessor<?> accessor = ((HandledScreenAccessor<?>)screenHandler);

            Optional<Slot> hotBarSlot = accessor
                .getHandler().slots
                .stream()
                .filter(slot -> slot instanceof HotBarSlot)
                .findFirst();

            int topLeftCornerX = accessor.getX() + accessor.getBackgroundWidth() - Constants.SLOT_X_OFFSET;

            if (hotBarSlot.isPresent()) {
                Slot slot = hotBarSlot.get();

                int topLeftCornerY = accessor.getY() + slot.y - Constants.SLOT_Y_OFFSET;

                boolean isSlotTextureBeingOverwritten = ResourceUtil.isTextureBeingOverwritten(
                    Minecraft.getInstance().getResourceManager(),
                    Constants.BIN_SLOT_TEXTURE
                );

                if (isSlotTextureBeingOverwritten) {
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.BIN_SLOT_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY,
                        0f, 0f,
                        27, 32,
                        27, 32
                    );
                } else {
                    // Top
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.TOP_TEXTURE_OFFSET_X,
                        topLeftCornerY,
                        (float) Constants.TOP_TEXTURE_X, (float) Constants.TOP_TEXTURE_Y,
                        Constants.TOP_TEXTURE_WIDTH, Constants.TOP_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Inner corner
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY,
                        (float) Constants.INNER_CORNER_TEXTURE_X, (float) Constants.INNER_CORNER_TEXTURE_Y,
                        Constants.INNER_CORNER_TEXTURE_WIDTH, Constants.INNER_CORNER_TEXTURE_HEIGHT,
                        Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_WIDTH, Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_HEIGHT
                    );

                    // Slot frame left padding
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY + Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_OFFSET_Y,
                        (float) Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_X, (float) Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_Y,
                        Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_WIDTH, Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Slot frame
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.SLOT_FRAME_TEXTURE_OFFSET_X,
                        topLeftCornerY + Constants.SLOT_FRAME_TEXTURE_OFFSET_Y,
                        (float) Constants.SLOT_FRAME_TEXTURE_X, (float) Constants.SLOT_FRAME_TEXTURE_Y,
                        Constants.SLOT_FRAME_TEXTURE_WIDTH, Constants.SLOT_FRAME_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Slot icon
                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.CREATIVE_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.SLOT_ICON_TEXTURE_OFFSET_X,
                        topLeftCornerY + Constants.SLOT_ICON_TEXTURE_OFFSET_Y,
                        (float) Constants.SLOT_ICON_TEXTURE_X, (float) Constants.SLOT_ICON_TEXTURE_Y,
                        Constants.SLOT_ICON_TEXTURE_WIDTH, Constants.SLOT_ICON_TEXTURE_HEIGHT,
                        Constants.CREATIVE_INVENTORY_TEXTURE_WIDTH, Constants.CREATIVE_INVENTORY_TEXTURE_HEIGHT
                    );
                }

                int clickableX = topLeftCornerX + Constants.CLICKABLE_OFFSET_X;
                int clickableY = topLeftCornerY + Constants.CLICKABLE_OFFSET_Y;

                boolean isHoveringOverBinSlot = ScreenUtil.isMouseOverArea(
                    mouseX, mouseY,
                    clickableX, clickableY,
                    Constants.CLICKABLE_WIDTH, Constants.CLICKABLE_HEIGHT
                );

                ScreenAccessor screenAccessor = (ScreenAccessor) accessor;

                if (isHoveringOverBinSlot) {
                    long windowHandle = Minecraft.getInstance().getWindow().handle();
                    boolean isShiftDown = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                        || GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

                    BinSlotHoverCallback.EVENT.invoker().onBinSlotHover(
                        screenAccessor.getTextRenderer(),
                        context,
                        topLeftCornerX, topLeftCornerY,
                        mouseX, mouseY,
                        isShiftDown,
                        false,
                        callbackInfo
                    );
                }
            }
        }
    }
}
