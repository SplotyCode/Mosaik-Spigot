package io.github.splotycode.mosaik.spigot.storage;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class YamlSection implements YamlProvider {

    private ConfigurationSection yamlSection;

    public YamlSection(ConfigurationSection yamlSection) {
        this.yamlSection = yamlSection;
    }

    public ConfigurationSection getYamlSection() {
        return yamlSection;
    }

    public void setEnum(final String path, final Enum<?> source){
        set(path, source.ordinal());
    }

    public <T> T getEnum(final String path, Class<T> enumClass){
        return enumClass.getEnumConstants()[getInt(path)];
    }

    public OfflinePlayer getOfflinePlayer(final String path){
        return Bukkit.getOfflinePlayer(getUUID(path));
    }

    public float getFloat(String str){
        return (float) getDouble(str);
    }

    public float getFloat(String str, float def){
        return (float) getDouble(str, def);
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
    public void setOfflinePlayer(String path, OfflinePlayer player) {
        setUUID(path, player.getUniqueId());
    }

    @Override
    public YamlSection getYamlSection(String path){
        return new YamlSection(yamlSection.getConfigurationSection(path));
    }

    @Override
    public YamlSection createYamlSection(String path) {
        return new YamlSection(yamlSection.createSection(path));
    }

    @Override
    public Set<String> getKeys(boolean b) {
        return yamlSection.getKeys(b);
    }

    @Override
    public Map<String, Object> getValues(boolean b) {
        return yamlSection.getValues(b);
    }

    @Override
    public boolean contains(String s) {
        return yamlSection.contains(s);
    }

    @Override
    public boolean isSet(String s) {
        return yamlSection.isSet(s);
    }

    @Override
    public String getCurrentPath() {
        return yamlSection.getCurrentPath();
    }

    @Override
    public String getName() {
        return yamlSection.getName();
    }

    @Override
    public Configuration getRoot() {
        return yamlSection.getRoot();
    }

    @Override
    public ConfigurationSection getParent() {
        return yamlSection.getParent();
    }

    @Override
    public Object get(String s) {
        return yamlSection.get(s);
    }

    @Override
    public Object get(String s, Object o) {
        return yamlSection.get(s, o);
    }

    @Override
    public void set(String s, Object o) {
        yamlSection.set(s, o);
    }

    @Override
    public ConfigurationSection createSection(String s) {
        return yamlSection.createSection(s);
    }

    @Override
    public ConfigurationSection createSection(String s, Map<?, ?> map) {
        return yamlSection.createSection(s, map);
    }

    @Override
    public String getString(String s) {
        return yamlSection.getString(s);
    }

    @Override
    public String getString(String s, String s1) {
        return yamlSection.getString(s, s1);
    }

    @Override
    public boolean isString(String s) {
        return yamlSection.isString(s);
    }

    @Override
    public int getInt(String s) {
        return yamlSection.getInt(s);
    }

    @Override
    public int getInt(String s, int i) {
        return yamlSection.getInt(s, i);
    }

    @Override
    public boolean isInt(String s) {
        return yamlSection.isInt(s);
    }

    @Override
    public boolean getBoolean(String s) {
        return yamlSection.getBoolean(s);
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        return yamlSection.getBoolean(s, b);
    }

    @Override
    public boolean isBoolean(String s) {
        return yamlSection.isBoolean(s);
    }

    @Override
    public double getDouble(String s) {
        return yamlSection.getDouble(s);
    }

    @Override
    public double getDouble(String s, double v) {
        return yamlSection.getDouble(s, v);
    }

    @Override
    public boolean isDouble(String s) {
        return yamlSection.isDouble(s);
    }

    @Override
    public long getLong(String s) {
        return yamlSection.getLong(s);
    }

    @Override
    public long getLong(String s, long l) {
        return yamlSection.getLong(s, l);
    }

    @Override
    public boolean isLong(String s) {
        return yamlSection.isLong(s);
    }

    @Override
    public List<?> getList(String s) {
        return yamlSection.getList(s);
    }

    @Override
    public List<?> getList(String s, List<?> list) {
        return yamlSection.getList(s, list);
    }

    @Override
    public boolean isList(String s) {
        return yamlSection.isList(s);
    }

    @Override
    public List<String> getStringList(String s) {
        return yamlSection.getStringList(s);
    }

    @Override
    public List<Integer> getIntegerList(String s) {
        return yamlSection.getIntegerList(s);
    }

    @Override
    public List<Boolean> getBooleanList(String s) {
        return yamlSection.getBooleanList(s);
    }

    @Override
    public List<Double> getDoubleList(String s) {
        return yamlSection.getDoubleList(s);
    }

    @Override
    public List<Float> getFloatList(String s) {
        return yamlSection.getFloatList(s);
    }

    @Override
    public List<Long> getLongList(String s) {
        return yamlSection.getLongList(s);
    }

    @Override
    public List<Byte> getByteList(String s) {
        return yamlSection.getByteList(s);
    }

    @Override
    public List<Character> getCharacterList(String s) {
        return yamlSection.getCharacterList(s);
    }

    @Override
    public List<Short> getShortList(String s) {
        return yamlSection.getShortList(s);
    }

    @Override
    public List<Map<?, ?>> getMapList(String s) {
        return yamlSection.getMapList(s);
    }

    @Override
    public Vector getVector(String s) {
        return yamlSection.getVector(s);
    }

    @Override
    public Vector getVector(String s, Vector vector) {
        return yamlSection.getVector(s, vector);
    }

    @Override
    public boolean isVector(String s) {
        return yamlSection.isVector(s);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s, OfflinePlayer offlinePlayer) {
        return yamlSection.getOfflinePlayer(s, offlinePlayer);
    }

    @Override
    public boolean isOfflinePlayer(String s) {
        return yamlSection.isOfflinePlayer(s);
    }

    @Override
    public ItemStack getItemStack(String s) {
        return yamlSection.getItemStack(s);
    }

    @Override
    public ItemStack getItemStack(String s, ItemStack itemStack) {
        return yamlSection.getItemStack(s, itemStack);
    }

    @Override
    public boolean isItemStack(String s) {
        return yamlSection.isItemStack(s);
    }

    @Override
    public Color getColor(String s) {
        return yamlSection.getColor(s);
    }

    @Override
    public Color getColor(String s, Color color) {
        return yamlSection.getColor(s, color);
    }

    @Override
    public boolean isColor(String s) {
        return yamlSection.isColor(s);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String s) {
        return yamlSection.getConfigurationSection(s);
    }

    @Override
    public boolean isConfigurationSection(String s) {
        return yamlSection.isConfigurationSection(s);
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        return yamlSection.getDefaultSection();
    }

    @Override
    public void addDefault(String s, Object o) {
        yamlSection.addDefault(s, o);
    }

}
