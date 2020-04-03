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

public interface NMS {

    Logger LOGGER = Logger.getInstance(NMS.class);
    NMS current = current0();

    static NMS current() {
        return current;
    }

    static String version() {
        String raw = Bukkit.getServer().getClass().getPackage().getName();
        return raw.substring(raw.lastIndexOf('.') + 2);
    }

    static NMS current0() {
        try {
            return (NMS) Class.forName("io.github.splotycode.mosaik.nms.impl.v" + version()).newInstance();
        }catch (ClassNotFoundException e){
            LOGGER.error("You ServerVersion is not Supported this will most probably result in Errors!!!");
        }catch (InstantiationException | IllegalAccessException e) {
            ExceptionUtil.throwRuntime(e);
        }
        return null;
    }

    int getPing(Player player);

    Box getBox(Player player);

    Box getBox(Block block);

    byte[] getNBT(BlockState state);
    void saveNBT(BlockState state, byte[] nbt);

    byte[] storeInventory(Inventory inventory);
    Inventory loadInventory(byte[] data, Inventory inventory);

    default Inventory loadInventory(byte[] data) {
        return loadInventory(data, null);
    }


    default Box getBox(Location location) {
        return getBox(location.getBlock());
    }

}
