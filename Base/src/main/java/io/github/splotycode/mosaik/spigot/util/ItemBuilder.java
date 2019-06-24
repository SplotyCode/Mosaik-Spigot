package io.github.splotycode.mosaik.spigot.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder extends ItemStack {

    public ItemBuilder() {
    }

    public ItemBuilder(String name) {
        setName(name);
    }

    public ItemBuilder(Material type) {
        super(type);
    }

    public ItemBuilder(Material type, String name) {
        super(type);
        setName(name);
    }

    public ItemBuilder(Material type, int amount) {
        super(type, amount);
    }

    public ItemBuilder(Material type, int amount, String name) {
        super(type, amount);
        setName(name);
    }

    public ItemBuilder(Material type, int amount, short damage) {
        super(type, amount, damage);
    }

    public ItemBuilder(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    public ItemBuilder setName(String name) {
        return metaOperation(itemMeta -> itemMeta.setDisplayName(name));
    }

    public ItemBuilder metaOperation(Consumer<ItemMeta> callback) {
        ItemMeta meta = getItemMeta();
        callback.accept(meta);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder createLohre(String... lohre) {
        return createLohre(Arrays.asList(lohre));
    }

    public ItemBuilder createLohre(List<String> lohre) {
        return metaOperation(itemMeta -> itemMeta.setLore(lohre));
    }

    public ItemBuilder lohre(String lohre) {
        return createLohre(Collections.singletonList(lohre));
    }

    public ItemBuilder head(Player player) {
        return createHead(player.getName());
    }

    public ItemBuilder createHead(String player) {
        setType(Material.SKULL_ITEM);
        setDurability((short) 3);
        return metaOperation(itemMeta -> ((SkullMeta) itemMeta).setOwner(player));
    }

    /* See http://minecraftplayerheadsdatabase.weebly.com/mobsanimals.html */
    public ItemBuilder animalHead(String name) {
        return createHead("MHF_" + name);
    }

    public ItemBuilder wool(int color) {
        setType(Material.WOOL);
        setDurability((short) color);
        return this;
    }

    public ItemBuilder wool(ColorCode colorCode) {
        return wool(colorCode.getId());
    }

    public ItemBuilder booleanWool(boolean bool) {
        return wool(bool ? ColorCode.LIME : ColorCode.RED);
    }

    public ItemBuilder stainedGlassBoolean(boolean bool) {
        return stainedGlass(bool ? ColorCode.LIME : ColorCode.RED);
    }

    public ItemBuilder stainedGlass(ColorCode color) {
        return stainedGlass(color.getId());
    }

    public ItemBuilder stainedGlass(int color) {
        setType(Material.STAINED_GLASS);
        setDurability((short) color);
        return this;
    }

    public ItemStack leatherArmor(Color color) {
        return metaOperation(itemMeta -> ((LeatherArmorMeta) itemMeta).setColor(color));
    }

    public ItemBuilder hideFlags() {
        return metaOperation(itemMeta -> {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        });
    }

    public ItemBuilder unbrankeable() {
        return metaOperation(itemMeta -> itemMeta.spigot().setUnbreakable(true));
    }

}
