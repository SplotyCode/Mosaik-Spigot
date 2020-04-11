package io.github.splotycode.mosaik.spigot.map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class KeepSpawnProvider implements SpawnProvider {

    protected SpawnProvider delegate;
    protected HashMap<UUID, Location> cache = new HashMap<>();

    public KeepSpawnProvider(SpawnProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return cache.computeIfAbsent(player.getUniqueId(), uuid -> delegate.getSpawnLocation(player));
    }
}
