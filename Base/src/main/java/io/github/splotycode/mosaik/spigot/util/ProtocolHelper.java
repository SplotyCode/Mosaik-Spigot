package io.github.splotycode.mosaik.spigot.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProtocolHelper {

    public static PacketContainer create(PacketType type) {
        PacketContainer packet = new PacketContainer(type);
        packet.getModifier().writeDefaults();
        return packet;
    }

    public static void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Unable to send packet " + packet.getType(), e);
        }
    }

}
