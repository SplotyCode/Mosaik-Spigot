package io.github.splotycode.mosaik.spigot.map;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class MaxRangeSpawnProvider implements SpawnProvider {

    private static final Random RANDOM = new Random();

    private List<Location> spawns;
    private final ArrayList<Location> usedSpawns = new ArrayList<>();

    private Location getSpawnLocation() {
        if (usedSpawns.isEmpty()) {
            return spawns.remove(RANDOM.nextInt(spawns.size()));
        }

        Location best = null;
        double bestDistance = Double.MAX_VALUE;
        for (Location spawn : spawns) {
            double distance = 0; /* usedSpawns can not be empty so we can use 0 safely */
            for (Location usedSpawn : usedSpawns) {
                distance += spawn.distanceSquared(usedSpawn);
            }
            if (distance < bestDistance) {
                best = spawn;
                bestDistance = distance;
            }
        }
        spawns.remove(best);
        return best;
    }

    @Override
    public Location getSpawnLocation(Player player) {
        if (spawns.isEmpty()) {
            return null;
        }
        Location result = getSpawnLocation();
        usedSpawns.add(result);
        return result;
    }

}
