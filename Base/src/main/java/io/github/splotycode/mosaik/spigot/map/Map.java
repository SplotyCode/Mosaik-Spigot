package io.github.splotycode.mosaik.spigot.map;

import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
public class Map {

    private static void unzip(File zip, File destination) throws IOException {
        try (ZipFile zipFile = new ZipFile(zip)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destination, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        FileUtil.writeToFile(entryDestination, in);
                    }
                }
            }
        }
    }

    private final String worldName;
    private World world;
    private SpawnProvider spawnProvider;

    public Map(SpawnProvider spawnProvider, String worldName) {
        this.spawnProvider = spawnProvider;
        this.worldName = worldName;
    }

    public void teleport(Collection<Player> players) {
        for (Player player : players) {
            player.teleport(spawnProvider.getSpawnLocation(player));
        }
    }

    public void load() throws IOException {
        if (world != null) {
            return;
        }

        world = Bukkit.getWorld(worldName);
        if (world != null) {
            return;
        }

        File worldDirectory = new File(Bukkit.getWorldContainer(), worldName);
        if (!worldDirectory.isDirectory()) {
            File worldZip = new File(Bukkit.getWorldContainer(), worldName + ".zip");
            unzip(worldZip, worldDirectory);
        }
        world = Bukkit.createWorld(new WorldCreator(worldName));
    }

    public void unload() {
        unload(false);
    }

    public void unload(boolean save) {
        if (world != null) {
            if (!Bukkit.unloadWorld(world, save)) {
                throw new IllegalStateException("Failed to unload world");
            }
        }
    }

}
