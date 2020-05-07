package io.github.splotycode.mosaik.box;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

@AllArgsConstructor
public class RawBox implements Box {

    public static RawBox createAuto(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new RawBox(world, Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2),
                Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }

    private World world;
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    @Override
    public World world() {
        return world;
    }

    @Override
    public Location min() {
        return new Location(world, minX, minY, minZ);
    }

    @Override
    public Location max() {
        return new Location(world, maxX, maxY, maxZ);
    }

    @Override
    public Vector minVec() {
        return new Vector(minX, minY, minZ);
    }

    @Override
    public Vector maxVec() {
        return new Vector(maxX, maxY, maxZ);
    }

    @Override
    public Block minBlock() {
        return world.getBlockAt((int) minX, (int) minY, (int) minZ);
    }

    @Override
    public Block maxBlock() {
        return world.getBlockAt((int) maxX, (int) maxY, (int) maxZ);
    }

    @Override
    public double minX() {
        return minX;
    }

    @Override
    public double minY() {
        return minY;
    }

    @Override
    public double minZ() {
        return minZ;
    }

    @Override
    public double maxX() {
        return maxX;
    }

    @Override
    public double maxY() {
        return maxY;
    }

    @Override
    public double maxZ() {
        return maxZ;
    }

    @Override
    public RawBox copy() {
        return new RawBox(world(), minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void expandByPosition(Location loc) {
        expandByPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public Box createByPosition(Location loc) {
        return createByPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void expandByPosition(double x, double y, double z) {
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);
        maxZ = Math.max(maxZ, z);

        minX = Math.min(minX, x);
        minY = Math.min(minY, y);
        minZ = Math.min(minZ, z);
    }

    @Override
    public Box createByPosition(double x, double y, double z) {
        return new RawBox(world, Math.min(minX, x), Math.min(minY, y), Math.min(minZ, z),
                Math.max(maxX, x), Math.max(maxY, y), Math.max(maxZ, z));
    }

    @Override
    public void expand(Location loc) {
        expand(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public Box createExpand(Location loc) {
        return createExpand(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void expand(double x, double y, double z) {
        minX -= x;
        minY -= y;
        minZ -= z;

        maxX += x;
        maxY += y;
        maxZ += z;
    }

    @Override
    public Box createExpand(double x, double y, double z) {
        return new RawBox(world, minX - x, minY() - y, minZ - z,
                maxX + x, maxY + y, maxZ + z);
    }

    @Override
    public void contract(Location loc) {
        contract(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public Box createContract(Location loc) {
        return createContract(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void contract(double x, double y, double z) {
        minX += x;
        minY += y;
        minZ += z;

        maxX -= x;
        maxY -= y;
        maxZ -= z;
    }

    @Override
    public Box createContract(double x, double y, double z) {
        return new RawBox(world, minX + x, minY + y, minZ + z,
                maxX - x, maxY - y, maxZ - z);
    }

    @Override
    public boolean intersectsWith(Box box) {
        return box.maxX() > minX && box.minX() < maxX &&
                box.maxY() > minY && box.minY() < maxY &&
                box.maxZ() > minZ && box.minZ() < maxZ;
    }

    @Override
    public boolean isInside(Vector vector) {
        return isInsideXZ(vector) && vector.getY() >= minY && vector.getY() <= maxY;
    }

    @Override
    public boolean isInsideXZ(Vector vector) {
        return vector.getX() >= minX && vector.getX() <= maxX &&
                vector.getZ() >= minZ && vector.getZ() <= maxZ;
    }

    @Override
    public double getAverageEdgeLength() {
        return ((maxX - minX) + (maxY - minY) + (maxZ - minZ)) / 3;
    }

    @Override
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }

    @Override
    public double getLengthSquared() {
        return Math.pow(minX - maxX, 2) + Math.pow(minY - maxY, 2) + Math.pow(minZ - maxZ, 2);
    }

    @Override
    public void move(double x, double y, double z) {
        minX += x;
        maxX += x;

        minY += y;
        maxY += y;

        minZ += z;
        maxZ += z;
    }

    @Override
    public Box createMove(double x, double y, double z) {
        return new RawBox(world, minX + x, minY + y, minZ + z,
                maxX + x, maxY + y, maxZ + z);
    }

    @Override
    public void move(Location location) {
        move(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public Box createMove(Location location) {
        return createMove(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void addCord(Location location) {
        addCord(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void addCord(double x, double y, double z) {
        if(x < 0.0D) {
            minX += x;
        } else if(x > 0.0D) {
            maxX += x;
        }

        if(y < 0.0D) {
            minY += y;
        } else if(y > 0.0D) {
            maxY += y;
        }

        if(z < 0.0D) {
            minZ += z;
        } else if(z > 0.0D) {
            maxZ += z;
        }
    }

    @Override
    public Box createAddCord(Location location) {
        return createAddCord(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public Box createAddCord(double x, double y, double z) {
        double minX = minX();
        double minY = minY();
        double minZ = minZ();
        double maxX = maxX();
        double maxY = maxY();
        double maxZ = maxZ();
        if(x < 0.0D) {
            minX += x;
        } else if(x > 0.0D) {
            maxX += x;
        }

        if(y < 0.0D) {
            minY += y;
        } else if(y > 0.0D) {
            maxY += y;
        }

        if(z < 0.0D) {
            minZ += z;
        } else if(z > 0.0D) {
            maxZ += z;
        }
        return new RawBox(world(), minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    protected RawBox clone() {
        try {
            return (RawBox) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String toString() {
        return "RawBox{" +
                "world=" + world +
                ", minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }
}
