package com.example.binslot.event;

import com.example.binslot.TrashSlot;
import com.example.binslot.event.callback.HandledScreenMouseClickCallback;
import com.example.binslot.event.callback.TrashSlotHoverCallback;
import com.example.binslot.mixin.client.accessor.SlotAccessor;
import com.example.binslot.network.payload.MouseClickC2SPayload;
import com.example.binslot.util.ScreenUtil;
import com.example.binslot.util.Constants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import com.example.binslot.mixin.client.accessor.HandledScreenAccessor;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {
    private static final TrashSlot trashSlot = new TrashSlot();

    public static void init() {
        HandledScreenMouseClickCallback.EVENT.register(ClientEventHandler::onMouseClick);
        TrashSlotHoverCallback.EVENT.register(ClientEventHandler::onTrashSlotHover);
    }

    public static void onMouseClick(boolean isRightClick, double mouseX, double mouseY, ItemStack itemStack, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (itemStack.isEmpty())
            return;

        boolean isHovering = ScreenUtil.isMouseOverArea(
            (int)mouseX, (int)mouseY,
            trashSlot.x, trashSlot.y,
            Constants.CLICKABLE_WIDTH, Constants.CLICKABLE_HEIGHT
        );

        if (isHovering) {
            boolean isShiftClick = Screen.hasShiftDown();
            MouseClickC2SPayload payload = new MouseClickC2SPayload(itemStack, isRightClick, isShiftClick);
            ClientPlayNetworking.send(payload);
        }
    }

    public static void onTrashSlotHover(TextRenderer textRenderer, DrawContext context, int slotX, int slotY, int mouseX, int mouseY, boolean isShiftDown, CallbackInfo callbackInfo) {
        SlotAccessor slotAccessor = (SlotAccessor) trashSlot;

        // Calculate the clickable area position (the actual slot area within the texture)
        int clickableX = slotX + Constants.CLICKABLE_OFFSET_X;
        int clickableY = slotY + Constants.CLICKABLE_OFFSET_Y;
        
        slotAccessor.setX(clickableX);
        slotAccessor.setY(clickableY);

        Identifier TRASH_SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.ofVanilla("container/slot_highlight_back");
        Identifier TRASH_SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.ofVanilla("container/slot_highlight_front");
        
        int highlightX = clickableX - Constants.HIGHLIGHT_OFFSET;
        int highlightY = clickableY - Constants.HIGHLIGHT_OFFSET;

        // Position the highlight sprites relative to the clickable area
        context.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            TRASH_SLOT_HIGHLIGHT_BACK_SPRITE,
            Constants.HIGHLIGHT_SIZE, Constants.HIGHLIGHT_SIZE,
            0, 0,
            highlightX,
            highlightY,
            Constants.HIGHLIGHT_SIZE, Constants.HIGHLIGHT_SIZE
        );

        context.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            TRASH_SLOT_HIGHLIGHT_FRONT_SPRITE,
            Constants.HIGHLIGHT_SIZE, Constants.HIGHLIGHT_SIZE,
            0, 0,
            highlightX,
            highlightY,
            Constants.HIGHLIGHT_SIZE, Constants.HIGHLIGHT_SIZE
        );

        if (isShiftDown)
            showRedOverlaysOnMatchingItems(context);

        List<Text> tooltip = new ArrayList<>();

        tooltip.add(Text.translatable("inventory.binSlot"));

        context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
    }

    private static void showRedOverlaysOnMatchingItems(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.currentScreen == null)
            return;

        ScreenHandler screenHandler = client.player.currentScreenHandler;

        ItemStack cursorStack = screenHandler.getCursorStack();

        if (cursorStack.isEmpty())
            return;
        
        PlayerInventory playerInventory = client.player.getInventory();
        
        if (!(client.currentScreen instanceof HandledScreen<?> handledScreen))
            return;

        HandledScreenAccessor<?> accessor = (HandledScreenAccessor<?>) handledScreen;
        
        int containerX = accessor.getX();
        int containerY = accessor.getY();
        
        for (int i = 0; i < screenHandler.slots.size(); i++) {
            Slot slot = screenHandler.slots.get(i);
            
            if (!slot.inventory.equals(playerInventory))
                continue;
            
            ItemStack slotStack = slot.getStack();
            
            if (!slotStack.isEmpty() && ItemStack.areItemsEqual(slotStack, cursorStack)) {
                int slotXPos = containerX + slot.x;
                int slotYPos = containerY + slot.y;
                
                context.fill(
                    slotXPos, slotYPos, 
                    slotXPos + Constants.SLOT_SIZE - 2*Constants.SLOT_OUTLINE_WIDTH, 
                    slotYPos + Constants.SLOT_SIZE - 2*Constants.SLOT_OUTLINE_WIDTH, 
                    (Constants.RED_OVERLAY_OPACITY << 24) | Constants.RED_OVERLAY_COLOR
                );
            }
        }
    }
}

