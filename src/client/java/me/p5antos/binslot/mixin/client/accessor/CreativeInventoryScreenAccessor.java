package me.p5antos.binslot.mixin.client.accessor;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeInventoryScreen.class)
public interface CreativeInventoryScreenAccessor {
    @Accessor("destroyItemSlot")
    @Nullable
    Slot getDeleteItemSlot();
}
