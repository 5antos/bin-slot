package net.pemiridosa.binslot.mixin.client;

import net.pemiridosa.binslot.event.callback.HandledScreenMouseClickCallback;
import net.pemiridosa.binslot.extension.HotBarSlot;
import net.pemiridosa.binslot.mixin.client.accessor.HandledScreenAccessor;
import net.pemiridosa.binslot.util.ScreenUtil;
import net.pemiridosa.binslot.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> {
    @Final @Shadow protected T handler;

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void onOnMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo callbackInfo) {
        // Check if this is an action that might result in item dropping
        if (slotId == -999 && actionType == SlotActionType.PICKUP) {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Use the same coordinate system as the rendering (scaled coordinates)
            double mouseX = client.mouse.getX() * client.getWindow().getScaledWidth() / client.getWindow().getWidth();
            double mouseY = client.mouse.getY() * client.getWindow().getScaledHeight() / client.getWindow().getHeight();

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
    private void onMouseClick(Click click, boolean doubleClick, CallbackInfoReturnable<Boolean> callbackInfoReturnable)
    {
        boolean isRightClick = click.button() == Constants.RIGHT_MOUSE_BUTTON;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            HandledScreenMouseClickCallback.EVENT.invoker().onMouseClick(
                isRightClick,
                click.x(),
                click.y(),
                this.handler.getCursorStack(),
                false
            );
        }
    }
}
