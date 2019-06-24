package io.github.splotycode.mosaik.spigot.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import static io.github.splotycode.mosaik.spigot.util.ProtocolHelper.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TitleApi {

    public static void clear(Player player) {
        PacketContainer packet = create(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.CLEAR);
        sendPacket(player, packet);
    }

    public static void reset(Player player) {
        PacketContainer packet = create(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.RESET);
        sendPacket(player, packet);
    }

    public static void send(Player player, String title) {
        PacketContainer packet = create(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        sendPacket(player, packet);
    }

    public static void sendSup(Player player, String title) {
        PacketContainer packet = create(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        sendPacket(player, packet);
    }

    public void send(Player player, String title, String subTitle) {
        send(player, title);
        sendSup(player, subTitle);
    }

    public static void send(Player player, int fadeIn, int stayTime, int fadeOut) {
        PacketContainer packet = create(PacketType.Play.Server.TITLE);
        packet.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
        packet.getIntegers().write(0, fadeIn);
        packet.getIntegers().write(1, stayTime);
        packet.getIntegers().write(2, fadeOut);
        sendPacket(player, packet);
    }

}
