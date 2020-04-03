package io.github.splotycode.mosaik.spigot.util;

import io.github.splotycode.mosaik.util.i18n.I18N;
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


    private I18N translator;

    public ItemBuilder(I18N translator) {
        this.translator = translator;
    }

    public ItemBuilder(I18N translator, String name, Object... objects) {
        this(translator);
        setTranslationName(name, objects);
    }


    public ItemBuilder(I18N translator, String name, Material material, Object... objects) {
        this(translator);
        setType(material);
        setTranslationName(name, objects);
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

    public ItemBuilder setTranslationName(String name, Object... objects) {
        return setName(translator.get(name, objects));
    }

    public ItemBuilder metaOperation(Consumer<ItemMeta> callback) {
        ItemMeta meta = getItemMeta();
        callback.accept(meta);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder translateLohre(String... lohre) {
        return translateLohre(translator, lohre);
    }

    public ItemBuilder translateLohre(String lohre, Object... objects) {
        return lohre(translator.get(lohre, objects));
    }

    public ItemBuilder translateLohre(List<String> lohre) {
        return translateLohre(translator, lohre);
    }

    public ItemBuilder translateLohre(I18N translator, String... lohre) {
        for (int i = 0; i < lohre.length; i++) {
            lohre[i] = translator.get(lohre[i]);
        }
        return lohre(lohre);
    }

    public ItemBuilder translateLohre(I18N translator, List<String> lohre) {
        for (int i = 0; i < lohre.size(); i++) {
            lohre.set(i, translator.get(lohre.get(i)));
        }
        return lohre(lohre);
    }

    public ItemBuilder lohre(List<String> lohre) {
        return metaOperation(itemMeta -> {
            List<String> lohres = itemMeta.getLore();
            if (lohres == null) {
                itemMeta.setLore(lohre);
            } else {
                lohres.addAll(lohre);
                itemMeta.setLore(lohres);
            }
        });
    }

    public ItemBuilder lohre(String... lohre) {
        if (lohre.length == 0) {
            return lohre(Collections.emptyList());
        }
        if (lohre.length == 1) {
            return lohre(Collections.singletonList(lohre[0]));
        }
        return lohre(Arrays.asList(lohre));
    }

    public ItemBuilder head(Player player) {
        return head(player.getName());
    }

    public ItemBuilder head(String player) {
        setType(Material.SKULL_ITEM);
        setDurability((short) 3);
        return metaOperation(itemMeta -> ((SkullMeta) itemMeta).setOwner(player));
    }

    /* See http://minecraftplayerheadsdatabase.weebly.com/mobsanimals.html */
    public ItemBuilder animal(String name) {
        return head("MHF_" + name);
    }

    public ItemBuilder animal(Animal animal) {
        return head(animal.getMHFName());
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

    public ItemStack setSize(int amount) {
        setAmount(amount);
        return this;
    }


    public ItemStack setMaterial(Material material) {
        setType(material);
        return this;
    }

    public ItemStack setMaterial(Material material, int amount) {
        setType(material);
        setAmount(amount);
        return this;
    }

}
