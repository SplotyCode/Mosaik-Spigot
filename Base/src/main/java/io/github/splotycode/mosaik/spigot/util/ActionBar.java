package io.github.splotycode.mosaik.spigot.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import static io.github.splotycode.mosaik.spigot.util.ProtocolHelper.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionBar {

    public void send(Player player, String message) {
        PacketContainer chat = create(PacketType.Play.Server.CHAT);
        chat.getChatComponents().write(0, WrappedChatComponent.fromText(message));
        chat.getBytes().write(0, (byte) 2);
        sendPacket(player, chat);
    }



}
