package com.example.binslot.mixin.client;

import com.example.binslot.BinSlot;
import com.example.binslot.event.callback.TrashSlotHoverCallback;
import com.example.binslot.mixin.client.accessor.HandledScreenAccessor;
import com.example.binslot.mixin.client.accessor.ScreenAccessor;
import com.example.binslot.extension.HotBarSlot;
import com.example.binslot.util.ScreenUtil;
import com.example.binslot.util.Constants;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.CrafterScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.CartographyTableScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin({
    GenericContainerScreen.class,
    InventoryScreen.class,
    Generic3x3ContainerScreen.class,
    CraftingScreen.class,
    CrafterScreen.class,
    ShulkerBoxScreen.class,
    HopperScreen.class,
    StonecutterScreen.class,
    GrindstoneScreen.class,
    AbstractFurnaceScreen.class,
    BeaconScreen.class,
    EnchantmentScreen.class,
    CartographyTableScreen.class,
    ForgingScreen.class,
    LoomScreen.class,
    BrewingStandScreen.class,
    HorseScreen.class,
    MerchantScreen.class
})
public class GenericContainerScreenMixin {
    @Unique
    private static final Identifier TRASH_SLOT_TEXTURE = BinSlot.id("textures/gui/sprites/container/bin_slot.png");

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void onDrawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        var screenHandler = ((HandledScreen<?>) (Object) this);

        if (screenHandler != null) {
            HandledScreenAccessor<?> accessor = ((HandledScreenAccessor<?>)screenHandler);
            
            Optional<Slot> hotBarSlot = accessor
                .getHandler().slots
                .stream()
                .filter(slot -> slot instanceof HotBarSlot)
                .findFirst();

            int topLeftCornerX = accessor.getX() + accessor.getBackgroundWidth() - Constants.TRASH_SLOT_X_OFFSET;

            if (hotBarSlot.isPresent()) {
                Slot slot = hotBarSlot.get();

                int topLeftCornerY = accessor.getY() + slot.y - Constants.TRASH_SLOT_Y_OFFSET;

                context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    TRASH_SLOT_TEXTURE,
                    topLeftCornerX,
                    topLeftCornerY,
                    0.0f, 0.0f,
                    Constants.TEXTURE_WIDTH, Constants.TEXTURE_HEIGHT,
                    Constants.TEXTURE_WIDTH, Constants.TEXTURE_HEIGHT
                );

                int clickableX = topLeftCornerX + Constants.CLICKABLE_OFFSET_X;
                int clickableY = topLeftCornerY + Constants.CLICKABLE_OFFSET_Y;

                boolean isHovering = ScreenUtil.isMouseOverArea(
                    mouseX, mouseY,
                    clickableX, clickableY,
                    Constants.CLICKABLE_WIDTH, Constants.CLICKABLE_HEIGHT
                );

                ScreenAccessor screenAccessor = (ScreenAccessor) accessor;

                if (isHovering) {
                    TrashSlotHoverCallback.EVENT.invoker().onTrashSlotHover(
                        screenAccessor.getTextRenderer(),
                        context,
                        topLeftCornerX, topLeftCornerY,
                        mouseX, mouseY,
                        Screen.hasShiftDown(),
                        callbackInfo
                    );
                }
            }
        }
    }
}
