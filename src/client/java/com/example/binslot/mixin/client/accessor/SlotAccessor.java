package com.example.binslot.mixin.client.accessor;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {
    @Accessor
    @Mutable
    void setX(int x);

    @Accessor()
    @Mutable
    void setY(int y);
}

