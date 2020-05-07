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

    Box copy();

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
    
    void move(double x, double y, double z);
    Box createMove(double x, double y, double z);
    void move(Location location);
    Box createMove(Location location);

    void addCord(Location location);
    void addCord(double x, double y, double z);
    Box createAddCord(Location location);
    Box createAddCord(double x, double y, double z);

    default double calculateXOffset(Box other, double offsetX) {
        if(maxY() > minY() && other.minY() < maxY() && maxZ() > minZ() && minZ() < maxZ()) {
            if(offsetX > 0.0D && other.maxX() <= minX()) {
                double d1 = minX() - other.maxX();
                if(d1 < offsetX) {
                    offsetX = d1;
                }
            } else if(offsetX < 0.0D && minX() >= maxX()) {
                double d0 = maxX() - minX();
                if(d0 > offsetX) {
                    offsetX = d0;
                }
            }

            return offsetX;
        } else {
            return offsetX;
        }
    }

    default double calculateYOffset(Box other, double offsetY) {
        if(other.maxX() > minX() && minX() < maxX() && maxZ() > minZ() && minZ() < maxZ()) {
            if(offsetY > 0.0D && maxY() <= minY()) {
                double d1 = minY() - maxY();
                if(d1 < offsetY) {
                    offsetY = d1;
                }
            } else if(offsetY < 0.0D && other.minY() >= maxY()) {
                double d0 = maxY() - other.minY();
                if(d0 > offsetY) {
                    offsetY = d0;
                }
            }

            return offsetY;
        } else {
            return offsetY;
        }
    }

    default double calculateZOffset(Box other, double offsetZ) {
        if(other.maxX() > minX() && minX() < maxX() && maxY() > minY() && other.minY() < maxY()) {
            if(offsetZ > 0.0D && maxZ() <= minZ()) {
                double d1 = minZ() - maxZ();
                if(d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if(offsetZ < 0.0D && minZ() >= maxZ()) {
                double d0 = maxZ() - minZ();
                if(d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        } else {
            return offsetZ;
        }
    }
    
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
