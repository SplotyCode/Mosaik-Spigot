package io.github.splotycode.mosaik.spigot.map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface SpawnProvider {

    Location getSpawnLocation(Player player);

}
