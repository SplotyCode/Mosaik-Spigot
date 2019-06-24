package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StringToOfflinePlayer extends ValueTransformer<String, OfflinePlayer> {

    @Override
    public OfflinePlayer transform(String name, DataFactory info) throws Exception {
        boolean uuid = name.contains("-") || name.length() > 16;
        OfflinePlayer offlinePlayer = uuid ? Bukkit.getOfflinePlayer(UUID.fromString(name)) : Bukkit.getOfflinePlayer(name);
        if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
            return offlinePlayer;
        }
        throw new TransformException("Der Spieler " + name + " ist uns nicht bekannt");
    }
}
