package io.github.splotycode.mosaik.spigot.map;

import io.github.splotycode.mosaik.util.collection.RoundRobin;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class RoundRobinSpawnProvider implements SpawnProvider {

    private Iterator<Location> spawns;

    public RoundRobinSpawnProvider(List<Location> spawns) {
        this.spawns = new RoundRobin<>(spawns).iterator();
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return spawns.hasNext() ? spawns.next() : null;
    }

}
