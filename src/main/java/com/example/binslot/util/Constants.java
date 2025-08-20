package com.example.binslot.util;

/**
 * Constants for trash slot positioning, sizing, and rendering
 */
public final class Constants {
    // Prevent instantiation
    private Constants() {}
    
    // Texture configuration for the 27x32 bin_slot.png
    public static final int TEXTURE_WIDTH = 27;
    public static final int TEXTURE_HEIGHT = 32;
    
    // Clickable area offsets within the texture (the actual slot area)
    public static final int CLICKABLE_OFFSET_X = 2;
    public static final int CLICKABLE_OFFSET_Y = 7;
    public static final int CLICKABLE_WIDTH = 18;
    public static final int CLICKABLE_HEIGHT = 18;
    
    // Trash slot positioning offsets relative to screen background
    public static final int TRASH_SLOT_X_OFFSET = 7;
    public static final int TRASH_SLOT_Y_OFFSET = 8;
    
    // Highlight sprite dimensions and offset
    public static final int HIGHLIGHT_SIZE = 24;
    public static final int HIGHLIGHT_OFFSET = 3;
    
    // Mouse button constants
    public static final int RIGHT_MOUSE_BUTTON = 1;
    
    // Slot and overlay constants
    public static final int SLOT_SIZE = 18;
    public static final int RED_OVERLAY_OPACITY = 0x40; // 25% opacity (64/255)
    public static final int RED_OVERLAY_COLOR = 0xFF0000; // Red color
    public static final int SLOT_OUTLINE_WIDTH = 1; // Width of slot outline/border in pixels
} 
