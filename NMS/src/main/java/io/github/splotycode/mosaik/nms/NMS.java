package io.github.splotycode.mosaik.nms;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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

    AABBBox getBox(Player player);

    AABBBox getBox(Block block);

    default AABBBox getBox(Location location) {
        return getBox(location.getBlock());
    }

}
