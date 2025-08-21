package com.example.binslot.network.payload;

import com.example.binslot.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MouseClickC2SPayload(ItemStack itemStack, boolean isRightClick, boolean isShiftClick) implements CustomPayload {
    public static final CustomPayload.Id<MouseClickC2SPayload> ID = new CustomPayload.Id<>(Constants.TRASH_ITEM_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, MouseClickC2SPayload> CODEC = PacketCodec.tuple(
        ItemStack.OPTIONAL_PACKET_CODEC,
        MouseClickC2SPayload::itemStack,
        PacketCodecs.BOOLEAN,
        MouseClickC2SPayload::isRightClick,
        PacketCodecs.BOOLEAN,
        MouseClickC2SPayload::isShiftClick,
        MouseClickC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
