package io.github.splotycode.mosaik.spigot.gui;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.exception.GuiLoadException;
import io.github.splotycode.mosaik.util.StringUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class GuiManager {

    @Getter private static HashMap<Class<? extends Gui>, Gui> initialised = new HashMap<>();

    private HashMap<UUID, InventoryData> openInventorys = new HashMap<>();
    private HashMap<UUID, List<InventoryData>> inventoryHistory = new HashMap<>();

    public InventoryData getInvenctory(UUID uuid) {
        return openInventorys.get(uuid);
    }

    protected Gui initialise(Class<? extends Gui> gui) {
        return initialised.computeIfAbsent(gui, clazz -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new GuiLoadException("Failed to construct gui", e);
            }
        });
    }

    public void closeNormally(InventoryData data) {
        Player player = data.getPlayer();
        player.closeInventory();
        openInventorys.remove(player.getUniqueId());
        data.getGui().onClose(player, data);
    }

    public void openGui(Class<? extends Gui> clazz, SpigotApplicationType application, Player player) {
        Gui gui = initialise(clazz);
        InventoryData data = new InventoryData(player, gui, application);
        gui.onPreOpen(player, data);
        openInventory(data);
        gui.onPostOpen(player, data.getInventory());
    }

    public void openInventory(InventoryData inventory) {
        Player player = inventory.getPlayer();
        String permission = inventory.getGui().getPermission();
        if (!StringUtil.isEmpty(permission) && !player.hasPermission(permission)) {
            inventory.getApplication().getMessageContext().message(player, "core.gui.access");
            return;
        }
        if (inventory.getSound() != null) {
            player.playSound(player.getLocation(), inventory.getSound(), 3, 0);
        }
        player.openInventory(inventory.getInventory());
    }

    public void endSession(InventoryData session) {
        closeNormally(session);
        inventoryHistory.remove(session.getPlayer().getUniqueId());
    }

    public InventoryData getLastInventory(Player player) {
        List<InventoryData> list = inventoryHistory.get(player.getUniqueId());
        if (list == null || list.isEmpty()) return null;

        return list.get(list.size() - 1);
    }

    public InventoryData removeLastInventory(Player player) {
        List<InventoryData> list = inventoryHistory.get(player.getUniqueId());
        if (list == null || list.isEmpty()) return null;

        return list.remove(list.size() - 1);
    }

}
