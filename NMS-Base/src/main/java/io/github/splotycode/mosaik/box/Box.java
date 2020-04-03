package io.github.splotycode.mosaik.box;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.function.Consumer;

public interface Box extends Serializable, Cloneable {

    World world();

    Location min();
    Location max();

    Vector minVec();
    Vector maxVec();

    Block minBlock();
    Block maxBlock();

    double minX();
    double minY();
    double minZ();

    double maxX();
    double maxY();
    double maxZ();

    default double higth() {
        return maxY() - minY();
    }

    default double xLength() {
        return maxX() - minX();
    }

    default double zLength() {
        return maxZ() - minZ();
    }

    void expandByPosition(Location loc);
    Box createByPosition(Location loc);
    void expandByPosition(double x, double y, double z);
    Box createByPosition(double x, double y, double z);

    void expand(Location loc);
    Box createExpand(Location loc);
    void expand(double x, double y, double z);
    Box createExpand(double x, double y, double z);

    void contract(Location loc);
    Box createContract(Location loc);
    void contract(double x, double y, double z);
    Box createContract(double x, double y, double z);

    boolean intersectsWith(Box box);
    boolean isInside(Vector vector);
    boolean isInsideXZ(Vector vector);

    double getAverageEdgeLength();
    double getLength();
    double getLengthSquared();

    default void blockProcessor(Consumer<Block> processor) {
        for (int x = (int) minX(); x < maxX(); x++) {
            for (int z = (int) minZ(); z < maxZ(); z++) {
                for (int y = (int) minY(); y < maxY(); y++) {
                    processor.accept(world().getBlockAt(x, y, z));
                }
            }
        }
    }

    default void locationProcessor(Consumer<Location> processor) {
        for (int x = (int) minX(); x < maxX(); x++) {
            for (int z = (int) minZ(); z < maxZ(); z++) {
                for (int y = (int) minY(); y < maxY(); y++) {
                    processor.accept(new Location(world(), x, y, z));
                }
            }
        }
    }


}
