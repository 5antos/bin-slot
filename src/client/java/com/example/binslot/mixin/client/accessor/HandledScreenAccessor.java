package com.example.binslot.mixin.client.accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor<T extends ScreenHandler> {
    @Accessor
    int getX();

    @Accessor
    int getY();

    @Accessor
    int getBackgroundWidth();

    @Accessor
    T getHandler();
}

