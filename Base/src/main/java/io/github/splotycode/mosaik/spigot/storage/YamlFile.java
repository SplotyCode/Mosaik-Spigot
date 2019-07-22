package io.github.splotycode.mosaik.spigot.storage;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class YamlFile extends YamlConfiguration implements YamlProvider {

    private final File file;

    public YamlFile(){
        file = null;
    }

    public YamlFile(String path){
        this(new File(path));
    }

    public YamlFile(File file){
        this.file = file;
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockLocation(String path, Location location){
        set(path,
                location.getWorld().getName() + ":" +
                location.getBlockX() + ":" +
                location.getBlockY() + ":" +
                location.getBlockZ());
    }

    @Override
    public void setPlayerLocation(String path, Location location){
        set(path,
                location.getWorld().getName() + ":" +
                location.getX() + ":" +
                location.getY() + ":" +
                location.getZ() + ":" +
                location.getYaw() + ":" +
                location.getPitch() + ":");
    }

    @Override
    public Location getLocationPlayer(String path){
        if (!contains(path)) return null;
        String[] args = getString(path).split(":");
        String world = args[0];
        double x = Double.valueOf(args[1]);
        double y = Double.valueOf(args[2]);
        double z = Double.valueOf(args[3]);
        float yaw = Float.valueOf(args[4]);
        float pitch = Float.valueOf(args[5]);
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public float getFloat(String str){
        return (float) getDouble(str);
    }

    public float getFloat(String str, float def){
        return (float) getDouble(str, def);
    }

    @Override
    public Location getLocationBlock(String path){
        if (!contains(path)) return null;
        String[] args = getString(path).split(":");
        String world = args[0];
        double x = Double.valueOf(args[1]);
        double y = Double.valueOf(args[2]);
        double z = Double.valueOf(args[3]);
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    @Override
    public void setUUID(final String path, final UUID uuid){
        set(path, uuid.getMostSignificantBits() + ":" + uuid.getLeastSignificantBits());
    }

    @Override
    public UUID getUUID(final String path){
        if (!contains(path)) return null;
        String[] args = getString(path).split(":");
        long most = Long.valueOf(args[0]);
        long least = Long.valueOf(args[1]);
        return new UUID(most, least);
    }

    @Override
    public void setOfflinePlayer(String path, OfflinePlayer player){
        setUUID(path, player.getUniqueId());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(final String path){
        return Bukkit.getOfflinePlayer(getUUID(path));
    }

    @Override
    public void setEnum(final String path, final Enum<?> source){
        set(path, source.ordinal());
    }

    @Override
    public <T> T getEnum(final String path, Class<T> enumClass){
        return enumClass.getEnumConstants()[getInt(path)];
    }

    @Override
    public double[] getDoubleArray(String path){
        Object[] list = getDoubleList(path).toArray();
        return ArrayUtils.toPrimitive(Arrays.copyOf(list, list.length, Double[].class));
    }

    @Override
    public String[] getStringArray(String path){
        List<String> list = getStringList(path);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public long[] getLongArray(String path){
        Object[] list = getLongList(path).toArray();
        return ArrayUtils.toPrimitive(Arrays.copyOf(list, list.length, Long[].class));
    }

    @Override
    public Long[] getLangLongArray(String path){
        List<Long> list = getLongList(path);
        return list.toArray(new Long[list.size()]);
    }

    @Override
    public YamlSection getYamlSection(String path){
        return new YamlSection(super.getConfigurationSection(path));
    }

    @Override
    public YamlSection createYamlSection(String path){
        return new YamlSection(super.createSection(path));
    }

    public void save(){
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
