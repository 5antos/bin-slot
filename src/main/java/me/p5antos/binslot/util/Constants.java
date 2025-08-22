package me.p5antos.binslot.util;

import me.p5antos.binslot.BinSlotModInitializer;
import net.minecraft.util.Identifier;

public final class Constants {
    // Identifiers
    public static final Identifier
        // Textures
        BIN_SLOT_TEXTURE = BinSlotModInitializer.id("textures/gui/slot.png"),

        CREATIVE_INVENTORY_TEXTURE = Identifier.ofVanilla("textures/gui/container/creative_inventory/tab_inventory.png"),
        CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/creative_inventory/tab_top_selected_1.png"),
        SURVIVAL_INVENTORY_TEXTURE = Identifier.ofVanilla("textures/gui/container/inventory.png"),

        BIN_SLOT_HIGHLIGHT_BACK_TEXTURE = Identifier.ofVanilla("container/slot_highlight_back"),
        BIN_SLOT_HIGHLIGHT_FRONT_TEXTURE = Identifier.ofVanilla("container/slot_highlight_front"),



        // Payloads
        TRASH_ITEM_PAYLOAD_ID = BinSlotModInitializer.id("mouse_click")
    ;



    public static final int
        // Textures
        TEXTURE_WIDTH = 25,
        TEXTURE_HEIGHT = 32,

        SURVIVAL_INVENTORY_TEXTURE_WIDTH = 256,
        SURVIVAL_INVENTORY_TEXTURE_HEIGHT = 256,
        CREATIVE_INVENTORY_TEXTURE_WIDTH = 256,
        CREATIVE_INVENTORY_TEXTURE_HEIGHT = 256,
        CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_WIDTH = 26,
        CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_HEIGHT = 32,

        TOP_TEXTURE_X = 151,
        TOP_TEXTURE_Y = 0,
        TOP_TEXTURE_WIDTH = 25,
        TOP_TEXTURE_HEIGHT = 7,
        TOP_TEXTURE_OFFSET_X = 2,

        INNER_CORNER_TEXTURE_X = 19,
        INNER_CORNER_TEXTURE_Y = 28,
        INNER_CORNER_TEXTURE_WIDTH = 7,
        INNER_CORNER_TEXTURE_HEIGHT = 3,

        SLOT_FRAME_LEFT_PADDING_TEXTURE_X = 5,
        SLOT_FRAME_LEFT_PADDING_TEXTURE_Y = 137,
        SLOT_FRAME_LEFT_PADDING_TEXTURE_WIDTH = 2,
        SLOT_FRAME_LEFT_PADDING_TEXTURE_HEIGHT = 29,
        SLOT_FRAME_LEFT_PADDING_TEXTURE_OFFSET_Y = 3,

        SLOT_FRAME_TEXTURE_OFFSET_X = 2,
        SLOT_FRAME_TEXTURE_OFFSET_Y = 3,
        SLOT_FRAME_TEXTURE_X = 151,
        SLOT_FRAME_TEXTURE_Y = 137,
        SLOT_FRAME_TEXTURE_WIDTH = 25,
        SLOT_FRAME_TEXTURE_HEIGHT = 29,

        SLOT_ICON_TEXTURE_OFFSET_X = 3,
        SLOT_ICON_TEXTURE_OFFSET_Y = 8,
        SLOT_ICON_TEXTURE_X = 173,
        SLOT_ICON_TEXTURE_Y = 112,
        SLOT_ICON_TEXTURE_WIDTH = 16,
        SLOT_ICON_TEXTURE_HEIGHT = 16,

        SLOT_ICON_TEXTURE_OUTLINE_WIDTH = 1,

        SLOT_HIGHLIGHT_TEXTURE_WIDTH = 24,
        SLOT_HIGHLIGHT_TEXTURE_HEIGHT = 24,
        SLOT_HIGHLIGHT_TEXTURE_OFFSET = 3,

        VANILLA_SLOT_WIDTH = 18,
        VANILLA_SLOT_HEIGHT = 18,



        // Clickable area offsets within the texture (the actual slot area)
        CLICKABLE_OFFSET_X = 2,
        CLICKABLE_OFFSET_Y = 7,
        CLICKABLE_WIDTH = 18,
        CLICKABLE_HEIGHT = 18,



        // Slot positioning offsets relative to screen background
        SLOT_X_OFFSET = 7,
        SLOT_Y_OFFSET = 8,



        // Mouse button constants
        RIGHT_MOUSE_BUTTON = 1,



        // Color constants
        MATCHING_SLOTS_OVERLAY_OPACITY = 0x40, // 25% opacity (64/255)
        MATCHING_SLOTS_OVERLAY_COLOR = 0xFF0000 // Red
    ;
}
