package io.github.splotycode.mosaik.nms;

import io.github.splotycode.mosaik.box.Box;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface NMS {

    BlockRegistry<?> getBlockRegistry();

    List<Box> getIntersections(Box box);
    List<Box> getBlockIntersection(int combinedID, Location location, Box box);

    int getPing(Player player);
    Box getBox(Player player);

    byte[] storeInventory(Inventory inventory);

    Inventory loadInventory(byte[] data, Inventory inventory);
    default Inventory loadInventory(byte[] data) {
        return loadInventory(data, null);
    }

}
