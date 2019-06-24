package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StringToPlayer extends ValueTransformer<String, Player> {
    @Override
    public Player transform(String name, DataFactory info) throws Exception {
        boolean uuid = name.contains("-") || name.length() > 16;
        Player player = uuid ? Bukkit.getPlayer(UUID.fromString(name)) : Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            return player;
        }
        OfflinePlayer offlinePlayer = uuid ? Bukkit.getOfflinePlayer(UUID.fromString(name)) : Bukkit.getOfflinePlayer(name);
        if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
            throw new TransformException("Der Spieler " + name + " ist nicht online");
        }
        throw new TransformException("Der Spieler " + name + " ist uns nicht bekannt");
    }
}
