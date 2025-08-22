package me.p5antos.binslot.util;

public class ScreenUtil {
    public static boolean isMouseOverArea(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static boolean isMouseOverTextureBounds(int mouseX, int mouseY, int textureX, int textureY, int textureWidth, int textureHeight) {
        return mouseX >= textureX && mouseX < textureX + textureWidth && 
               mouseY >= textureY && mouseY < textureY + textureHeight;
    }
}
