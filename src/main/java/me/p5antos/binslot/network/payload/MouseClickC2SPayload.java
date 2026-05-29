package me.p5antos.binslot.network.payload;

import me.p5antos.binslot.util.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record MouseClickC2SPayload(ItemStack itemStack, boolean isRightClick, boolean isShiftClick, boolean isCreativeInventory) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MouseClickC2SPayload> ID = new CustomPacketPayload.Type<>(Constants.MOUSE_CLICK_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MouseClickC2SPayload> CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
        MouseClickC2SPayload::itemStack,
        ByteBufCodecs.BOOL,
        MouseClickC2SPayload::isRightClick,
        ByteBufCodecs.BOOL,
        MouseClickC2SPayload::isShiftClick,
        ByteBufCodecs.BOOL,
        MouseClickC2SPayload::isCreativeInventory,
        MouseClickC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
