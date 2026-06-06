package net.pemiridosa.binslot.network.payload;

import net.pemiridosa.binslot.util.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record TrashItemS2CPayload(ItemStack itemStack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TrashItemS2CPayload> ID = new CustomPacketPayload.Type<>(Constants.TRASH_ITEM_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, TrashItemS2CPayload> CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
        TrashItemS2CPayload::itemStack,
        TrashItemS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
