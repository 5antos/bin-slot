package me.p5antos.binslot.mixin;

import me.p5antos.binslot.extension.HotBarSlot;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {
    @Redirect(
        method = "addInventoryHotbarSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/Container;III)Lnet/minecraft/world/inventory/Slot;"
        )
    )
    private Slot redirectSlotConstruction(Container container, int index, int x, int y) {
        return new HotBarSlot(container, index, x, y);
    }
}
