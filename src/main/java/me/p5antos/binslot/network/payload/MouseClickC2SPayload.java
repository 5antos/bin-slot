package me.p5antos.binslot.network.payload;

import me.p5antos.binslot.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MouseClickC2SPayload(ItemStack itemStack, boolean isRightClick, boolean isShiftClick, boolean isCreativeInventory) implements CustomPayload {
    public static final CustomPayload.Id<MouseClickC2SPayload> ID = new CustomPayload.Id<>(Constants.MOUSE_CLICK_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, MouseClickC2SPayload> CODEC = PacketCodec.tuple(
        ItemStack.OPTIONAL_PACKET_CODEC,
        MouseClickC2SPayload::itemStack,
        PacketCodecs.BOOLEAN,
        MouseClickC2SPayload::isRightClick,
        PacketCodecs.BOOLEAN,
        MouseClickC2SPayload::isShiftClick,
        PacketCodecs.BOOLEAN,
        MouseClickC2SPayload::isCreativeInventory,
        MouseClickC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
