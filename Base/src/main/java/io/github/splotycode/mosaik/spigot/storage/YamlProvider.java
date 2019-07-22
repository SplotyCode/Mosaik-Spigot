package io.github.splotycode.mosaik.spigot.storage;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public interface YamlProvider extends ConfigurationSection {

    void setBlockLocation(String path, Location location);
    Location getLocationPlayer(String path);

    void setPlayerLocation(String path, Location location);
    Location getLocationBlock(String path);

    void setUUID(final String path, final UUID uuid);
    UUID getUUID(final String path);

    OfflinePlayer getOfflinePlayer(final String path);
    void setOfflinePlayer(String path, OfflinePlayer player);

    void setEnum(final String path, final Enum<?> source);
    <T> T getEnum(final String path, Class<T> enumClass);

    double[] getDoubleArray(String path);
    String[] getStringArray(String path);
    long[] getLongArray(String path);
    Long[] getLangLongArray(String path);

    YamlSection getYamlSection(String path);
    YamlSection createYamlSection(String path);

}
