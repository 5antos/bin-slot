package me.p5antos.binslot.network.payload;

import me.p5antos.binslot.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record TrashItemS2CPayload(ItemStack itemStack) implements CustomPayload {
    public static final CustomPayload.Id<TrashItemS2CPayload> ID = new CustomPayload.Id<>(Constants.TRASH_ITEM_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, TrashItemS2CPayload> CODEC = PacketCodec.tuple(
        ItemStack.OPTIONAL_PACKET_CODEC,
        TrashItemS2CPayload::itemStack,
        TrashItemS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
