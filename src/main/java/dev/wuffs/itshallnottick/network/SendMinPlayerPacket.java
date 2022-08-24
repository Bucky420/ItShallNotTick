package dev.wuffs.itshallnottick.network;

import dev.wuffs.itshallnottick.ItShallNotTick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendMinPlayerPacket {
    private int minPlayers;

    public SendMinPlayerPacket(int minPlayers){
        this.minPlayers = minPlayers;
    }

    public SendMinPlayerPacket(FriendlyByteBuf buf) {
        this.minPlayers = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.minPlayers);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                return;
            }

            ItShallNotTick.minPlayer = this.minPlayers;
        });
        ctx.get().setPacketHandled(true);
    }
}
