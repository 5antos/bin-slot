package me.p5antos.binslot.event;

import me.p5antos.binslot.extension.BinSlot;
import me.p5antos.binslot.event.callback.HandledScreenMouseClickCallback;
import me.p5antos.binslot.event.callback.BinSlotHoverCallback;
import me.p5antos.binslot.mixin.client.accessor.HandledScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.SlotAccessor;
import me.p5antos.binslot.network.payload.MouseClickC2SPayload;
import me.p5antos.binslot.util.ScreenUtil;
import me.p5antos.binslot.util.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {
    private static final BinSlot binSlot = new BinSlot();

    public static void init() {
        HandledScreenMouseClickCallback.EVENT.register(ClientEventHandler::onMouseClick);
        BinSlotHoverCallback.EVENT.register(ClientEventHandler::onBinSlotHover);
    }

    public static void onMouseClick(boolean isRightClick, double mouseX, double mouseY, ItemStack itemStack, boolean isCreativeInventory) {
        if (!isCreativeInventory && itemStack.isEmpty())
            return;

        boolean isHoveringOverBinSlot = ScreenUtil.isMouseOverArea(
            (int)mouseX, (int)mouseY,
            binSlot.x, binSlot.y,
            Constants.CLICKABLE_WIDTH, Constants.CLICKABLE_HEIGHT
        );

        if (isCreativeInventory || isHoveringOverBinSlot) {
            long windowHandle = Minecraft.getInstance().getWindow().handle();
            boolean isShiftClick = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

            MouseClickC2SPayload payload = new MouseClickC2SPayload(itemStack, isRightClick, isShiftClick, isCreativeInventory);

            ClientPlayNetworking.send(payload);
        }
    }

    public static void onBinSlotHover(Font font, GuiGraphicsExtractor context, int slotX, int slotY, int mouseX, int mouseY, boolean isShiftDown, boolean isCreativeInventory, CallbackInfo callbackInfo) {
        if (!isCreativeInventory) {
            SlotAccessor slotAccessor = (SlotAccessor) binSlot;

            int clickableX = slotX + Constants.CLICKABLE_OFFSET_X;
            int clickableY = slotY + Constants.CLICKABLE_OFFSET_Y;

            slotAccessor.setX(clickableX);
            slotAccessor.setY(clickableY);

            int highlightX = clickableX - Constants.SLOT_HIGHLIGHT_TEXTURE_OFFSET;
            int highlightY = clickableY - Constants.SLOT_HIGHLIGHT_TEXTURE_OFFSET;

            context.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                Constants.BIN_SLOT_HIGHLIGHT_BACK_TEXTURE,
                Constants.SLOT_HIGHLIGHT_TEXTURE_WIDTH, Constants.SLOT_HIGHLIGHT_TEXTURE_HEIGHT,
                0, 0,
                highlightX,
                highlightY,
                Constants.SLOT_HIGHLIGHT_TEXTURE_WIDTH, Constants.SLOT_HIGHLIGHT_TEXTURE_HEIGHT
            );

            context.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                Constants.BIN_SLOT_HIGHLIGHT_FRONT_TEXTURE,
                Constants.SLOT_HIGHLIGHT_TEXTURE_WIDTH, Constants.SLOT_HIGHLIGHT_TEXTURE_HEIGHT,
                0, 0,
                highlightX,
                highlightY,
                Constants.SLOT_HIGHLIGHT_TEXTURE_WIDTH, Constants.SLOT_HIGHLIGHT_TEXTURE_HEIGHT
            );

            List<Component> tooltip = new ArrayList<>();

            tooltip.add(Component.translatable("inventory.binSlot"));

            context.setComponentTooltipForNextFrame(font, tooltip, mouseX, mouseY);
        }

        if (isShiftDown)
            showRedOverlaysOnMatchingItems(context);
    }

    private static void showRedOverlaysOnMatchingItems(GuiGraphicsExtractor context) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null || client.screen == null)
            return;

        AbstractContainerMenu menu = client.player.containerMenu;

        ItemStack cursorStack = menu.getCarried();

        if (cursorStack.isEmpty())
            return;

        if (!(client.screen instanceof AbstractContainerScreen<?> handledScreen))
            return;

        HandledScreenAccessor<?> accessor = (HandledScreenAccessor<?>) handledScreen;
        int containerX = accessor.getX();
        int containerY = accessor.getY();

        for (int i = 0; i < menu.slots.size(); i++) {
            Slot slot = menu.slots.get(i);

            ItemStack slotStack = slot.getItem();

            if (!slotStack.isEmpty() && ItemStack.isSameItem(slotStack, cursorStack)) {
                int slotXPos = containerX + slot.x;
                int slotYPos = containerY + slot.y;

                context.fill(
                    slotXPos, slotYPos,
                    slotXPos + Constants.VANILLA_SLOT_WIDTH - 2*Constants.SLOT_ICON_TEXTURE_OUTLINE_WIDTH,
                    slotYPos + Constants.VANILLA_SLOT_HEIGHT - 2*Constants.SLOT_ICON_TEXTURE_OUTLINE_WIDTH,
                    (Constants.MATCHING_SLOTS_OVERLAY_OPACITY << 24) | Constants.MATCHING_SLOTS_OVERLAY_COLOR
                );
            }
        }
    }
}
