package io.github.splotycode.mosaik.spigot.gui;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.util.ColorCode;
import io.github.splotycode.mosaik.spigot.util.ItemStackUtil;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventoryData {

    public static final DataKey<Integer> PAGE = new DataKey<>("lib.page");
    public static final DataKey<Integer> HOT_BAR_SLOT = new DataKey<>("lib.hotbar.slot");

    private Player player;
    private Gui gui;
    private List<ItemStack> content;
    private List<ItemStack> hotBar;
    private String displayName = null;
    private int size = -1;
    @Setter @Getter private Sound sound = Sound.LEVEL_UP;
    private DataFactory factory;
    private Inventory inventory;
    private Closeable closeable;
    private AlmostBoolean page = AlmostBoolean.MAYBE;

    public InventoryData(Player player, Gui gui) {
        this.player = player;
        this.gui = gui;
    }

    public boolean itemClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return false;
        }
        DataFactory factory = new DataFactory();
        if (isPage() && event.getRawSlot() > 46 && event.getRawSlot() < event.getClickedInventory().getSize() - 2) {
            factory.putData(HOT_BAR_SLOT, event.getRawSlot() - 46);
        }
        if (closeable.isCloseButton() && event.getRawSlot() == event.getClickedInventory().getSize() - 1) {
            GuiManager manager = SpigotApplicationType.GUI_MANAGER;

            player.closeInventory();
            InventoryData inv = manager.removeLastInventory(player);
            manager.endSession(this);

            if (inv != null) {
                manager.openInventory(inv);
            }
            return true;
        }
        if (isPage()) {
            if (event.getRawSlot() == 46) {
                factory.putData(PAGE, factory.getData(PAGE) - 1);
            } else if (event.getRawSlot() == event.getClickedInventory().getSize() - 2) {
                factory.putData(PAGE, factory.getData(PAGE) + 1);
            }
            event.getClickedInventory().setContents(getContent());
            return true;
        }
        return gui.onItemClick(player, this, factory, event);
    }

    public ItemStack[] getContent() {
        if(!factory.containsData(PAGE)) factory.putData(PAGE, 0);
        int currentPage = factory.getData(PAGE);
        boolean page = isPage();
        int size = getSize();

        int startCount = currentPage*45;

        ItemStack[] inv = new ItemStack[size];

        int i = 0;
        for (int count = startCount;count < startCount+size && count != content.size();count++) {
            inv[i] = content.get(count);
            i++;
        }


        if (page) {
            if (currentPage != 0) inv[46] = ItemStackUtil.createbasic("§6Letzte Seite", currentPage-1, Material.ARROW);
            if (content.size() > startCount + size) inv[size - 2] = ItemStackUtil.createbasic("§6Nägste Seite", currentPage+1, Material.ARROW);
            int place = 47;
            for (ItemStack item : hotBar) {
                inv[place] = item;
                place++;
                if (place == size - 2) break;
            }
        }

        Closeable closeable = getCloseable();

        if (closeable.isCloseButton()) {
            InventoryData last = SpigotApplicationType.GUI_MANAGER.getLastInventory(player);
            inv[size - 1] = ItemStackUtil.createColoredWoolLohre(last == null ? "Exit" : "Zurück", 1, ColorCode.RED.getId(), last == null ? "Schliessen" : "Zurück zu '" + last.getDisplayName() + "'");
        }

        return inv;
    }

    public void setPage(boolean page) {
        this.page = AlmostBoolean.fromBoolean(page);
    }

    public InventoryData setCloseable(Closeable closeable) {
        this.closeable = closeable;
        return this;
    }

    public Closeable getCloseable() {
        if (closeable == null) {
            closeable = Closeable.ALL;
        }
        return closeable;
    }

    public int getSize() {
        if (size == -1) {
            recaclcSize();
        }
        return size;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = "Unamed";
        }
        return displayName;
    }

    public void recaclcSize() {
        size = isPage() ? 45 : (int) Math.ceil((content.size()+1D) / 9D)*9;
        if (size < 9) size = 9;
    }

    public boolean isPage() {
        if (page == AlmostBoolean.MAYBE) {
            page = AlmostBoolean.fromBoolean(content.size() + 1 > 54);
        }
        return page.decide(false);
    }

    public Inventory getInventory() {
        if (inventory == null) {
            recaclcSize();
            inventory = Bukkit.createInventory(null, getSize(), getDisplayName());
            inventory.setContents(getContent());
        }
        return inventory;
    }

    public InventoryData setItem(int index, ItemStack itemStack) {
        if (content == null) content = new ArrayList<>();
        content.set(index, itemStack);
        return this;
    }

    public InventoryData addItems(ItemStack... items) {
        if (content == null) {
            content = Arrays.asList(items);
            return this;
        }
        Collections.addAll(content, items);
        return this;
    }

    public InventoryData setHotBar(int index, ItemStack itemStack) {
        if (hotBar == null) hotBar = new ArrayList<>();
        hotBar.set(index, itemStack);
        return this;
    }

    public InventoryData addHotBar(ItemStack... items) {
        if (hotBar == null) {
            hotBar = Arrays.asList(items);
            return this;
        }
        Collections.addAll(hotBar, items);
        return this;
    }

    public InventoryData setList(List<ItemStack> content) {
        this.content = content;
        return this;
    }

    public DataFactory getFactory() {
        if (factory == null) factory = new DataFactory();
        return factory;
    }

    public Player getPlayer() {
        return player;
    }

    public Gui getGui() {
        return gui;
    }

}
