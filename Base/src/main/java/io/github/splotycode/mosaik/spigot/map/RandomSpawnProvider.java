package io.github.splotycode.mosaik.spigot.map;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class RandomSpawnProvider implements SpawnProvider {

    protected Random random;
    protected List<Location> spawns;

    public RandomSpawnProvider() {
        this(new Random(), new ArrayList<>());
    }

    public RandomSpawnProvider(List<Location> spawns) {
        this.spawns = spawns;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        return spawns.get(random.nextInt(spawns.size()));
    }

}
