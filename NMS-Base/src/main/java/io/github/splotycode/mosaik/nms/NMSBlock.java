package io.github.splotycode.mosaik.nms;

import io.github.splotycode.mosaik.box.Box;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public interface NMSBlock {

    Box getBox(Block block);
    List<Box> getIntersections(Block block, Box box);

    byte[] getNBT(Location location);
    void saveNBT(Location location, byte[] nbt);

}
