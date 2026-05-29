package me.p5antos.binslot.mixin.client;

import me.p5antos.binslot.event.callback.HandledScreenMouseClickCallback;
import me.p5antos.binslot.extension.HotBarSlot;
import me.p5antos.binslot.mixin.client.accessor.HandledScreenAccessor;
import me.p5antos.binslot.util.ScreenUtil;
import me.p5antos.binslot.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin<T extends AbstractContainerMenu> {
    @Final @Shadow protected T menu;

    @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
    private void onOnMouseClick(Slot slot, int slotId, int button, ContainerInput actionType, CallbackInfo callbackInfo) {
        if (slotId == -999 && actionType == ContainerInput.PICKUP) {
            Minecraft client = Minecraft.getInstance();

            double mouseX = client.mouseHandler.xpos() * client.getWindow().getGuiScaledWidth() / client.getWindow().getScreenWidth();
            double mouseY = client.mouseHandler.ypos() * client.getWindow().getGuiScaledHeight() / client.getWindow().getScreenHeight();

            HandledScreenAccessor<?> accessor = ((HandledScreenAccessor<?>)this);

            Optional<Slot> hotBarSlot = accessor
                .getHandler().slots
                .stream()
                .filter(s -> s instanceof HotBarSlot)
                .findFirst();

            if (hotBarSlot.isPresent()) {
                int topLeftCornerX = accessor.getX() + accessor.getBackgroundWidth() - Constants.SLOT_X_OFFSET;

                Slot hotBarSlotInstance = hotBarSlot.get();

                int topLeftCornerY = accessor.getY() + hotBarSlotInstance.y - Constants.SLOT_Y_OFFSET;

                boolean isWithinTextureBounds = ScreenUtil.isMouseOverTextureBounds(
                    (int)mouseX, (int)mouseY,
                    topLeftCornerX, topLeftCornerY,
                    Constants.TEXTURE_WIDTH, Constants.TEXTURE_HEIGHT
                );

                if (isWithinTextureBounds)
                    callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClick(MouseButtonEvent event, boolean b, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        boolean isRightClick = event.button() == Constants.RIGHT_MOUSE_BUTTON;

        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            HandledScreenMouseClickCallback.EVENT.invoker().onMouseClick(
                isRightClick,
                event.x(),
                event.y(),
                this.menu.getCarried(),
                false
            );
        }
    }
}
