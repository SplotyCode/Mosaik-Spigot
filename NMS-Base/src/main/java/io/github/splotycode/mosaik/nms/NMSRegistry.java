package io.github.splotycode.mosaik.nms;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class NMSRegistry {

    private static Logger LOGGER = Logger.getInstance(NMS.class);

    @Getter private static NMS nms = getCurrentNMS();

    private static String version() {
        String raw = Bukkit.getServer().getClass().getPackage().getName();
        return raw.substring(raw.lastIndexOf('.') + 2);
    }

    private static NMS getCurrentNMS() {
        String version = version();
        try {
            return (NMS) Class.forName("io.github.splotycode.mosaik.nms.impl.v" + version).newInstance();
        } catch (ClassNotFoundException e){
            LOGGER.error("You ServerVersion (" + version + ") is not Supported this will most probably result in Errors!!!");
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionUtil.throwRuntime(e, "Failed to get NMS instance");
        }
        return null;
    }

    public static NMSBlock getBlock(Block block) {
        return nms.getBlockRegistry().getBlock(block);
    }

    public static NMSBlock getBlock(Location location) {
        return nms.getBlockRegistry().getBlock(location);
    }

    public static NMSBlock getBlock(Material material) {
        return nms.getBlockRegistry().getBlock(material);
    }

}
