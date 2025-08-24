package me.p5antos.binslot.mixin.client;

import me.p5antos.binslot.event.callback.BinSlotHoverCallback;
import me.p5antos.binslot.mixin.client.accessor.HandledScreenAccessor;
import me.p5antos.binslot.mixin.client.accessor.ScreenAccessor;
import me.p5antos.binslot.extension.HotBarSlot;
import me.p5antos.binslot.util.ResourceUtil;
import me.p5antos.binslot.util.ScreenUtil;
import me.p5antos.binslot.util.Constants;
import net.minecraft.client.MinecraftClient;
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
import org.spongepowered.asm.mixin.Mixin;
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

            int topLeftCornerX = accessor.getX() + accessor.getBackgroundWidth() - Constants.SLOT_X_OFFSET;

            if (hotBarSlot.isPresent()) {
                Slot slot = hotBarSlot.get();

                int topLeftCornerY = accessor.getY() + slot.y - Constants.SLOT_Y_OFFSET;

                boolean isSlotTextureBeingOverwritten = ResourceUtil.isTextureBeingOverwritten(
                    MinecraftClient.getInstance().getResourceManager(),
                    Constants.BIN_SLOT_TEXTURE
                );

                if (isSlotTextureBeingOverwritten) {
                    // Draw the slot from the resource pack texture

                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.BIN_SLOT_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY,
                        0, 0,
                        27, 32,
                        27, 32
                    );
                } else {
                    // Draw the slot using built-in inventory textures (even if overridden by resource packs) to match the inventory style

                    // Top
                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.TOP_TEXTURE_OFFSET_X,
                        topLeftCornerY,
                        Constants.TOP_TEXTURE_X, Constants.TOP_TEXTURE_Y,
                        Constants.TOP_TEXTURE_WIDTH, Constants.TOP_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Inner corner
                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY,
                        Constants.INNER_CORNER_TEXTURE_X, Constants.INNER_CORNER_TEXTURE_Y,
                        Constants.INNER_CORNER_TEXTURE_WIDTH, Constants.INNER_CORNER_TEXTURE_HEIGHT,
                        Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_WIDTH, Constants.CREATIVE_INVENTORY_TOP_SELECTED_TAB_TEXTURE_HEIGHT
                    );

                    // Slot frame left padding
                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX,
                        topLeftCornerY + Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_OFFSET_Y,
                        Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_X, Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_Y,
                        Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_WIDTH, Constants.SLOT_FRAME_LEFT_PADDING_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Slot frame
                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.SURVIVAL_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.SLOT_FRAME_TEXTURE_OFFSET_X,
                        topLeftCornerY + Constants.SLOT_FRAME_TEXTURE_OFFSET_Y,
                        Constants.SLOT_FRAME_TEXTURE_X, Constants.SLOT_FRAME_TEXTURE_Y,
                        Constants.SLOT_FRAME_TEXTURE_WIDTH, Constants.SLOT_FRAME_TEXTURE_HEIGHT,
                        Constants.SURVIVAL_INVENTORY_TEXTURE_WIDTH, Constants.SURVIVAL_INVENTORY_TEXTURE_HEIGHT
                    );

                    // Slot icon
                    context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        Constants.CREATIVE_INVENTORY_TEXTURE,
                        topLeftCornerX + Constants.SLOT_ICON_TEXTURE_OFFSET_X,
                        topLeftCornerY + Constants.SLOT_ICON_TEXTURE_OFFSET_Y,
                        Constants.SLOT_ICON_TEXTURE_X, Constants.SLOT_ICON_TEXTURE_Y,
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
                    BinSlotHoverCallback.EVENT.invoker().onBinSlotHover(
                        screenAccessor.getTextRenderer(),
                        context,
                        topLeftCornerX, topLeftCornerY,
                        mouseX, mouseY,
                        Screen.hasShiftDown(),
                        false,
                        callbackInfo
                    );
                }
            }
        }
    }
}
