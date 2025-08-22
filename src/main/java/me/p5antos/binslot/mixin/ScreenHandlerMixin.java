package me.p5antos.binslot.mixin;

import me.p5antos.binslot.extension.HotBarSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Redirect(
        method = "addPlayerHotbarSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/inventory/Inventory;III)Lnet/minecraft/screen/slot/Slot;"
        )
    )
    private Slot redirectSlotConstruction(Inventory inventory, int index, int x, int y) {
        return new HotBarSlot(inventory, index, x, y);
    }
}
