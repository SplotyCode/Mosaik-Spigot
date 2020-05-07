package io.github.splotycode.mosaik.box;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class LocationBox implements Box {

    private Location min, max;

    public LocationBox(Location min, Location max) {
        if (!min.getWorld().equals(max.getWorld())) throw new IllegalArgumentException("The locations need to be in the same world");
        if (min.getX() > max.getX() || min.getZ() > max.getY() || min.getZ() > max.getZ()) {
            this.min = new Location(min.getWorld(), Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
            this.max = new Location(min.getWorld(), Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));
        } else {
            this.min = min;
            this.max = max;
        }
    }

    @Override
    public World world() {
        return min.getWorld();
    }

    @Override
    public Location min() {
        return min;
    }

    @Override
    public Location max() {
        return max;
    }

    @Override
    public Vector minVec() {
        return min.toVector();
    }

    @Override
    public Vector maxVec() {
        return max.toVector();
    }

    @Override
    public Block minBlock() {
        return min.getBlock();
    }

    @Override
    public Block maxBlock() {
        return max.getBlock();
    }

    @Override
    public double minX() {
        return min.getX();
    }

    @Override
    public double minY() {
        return min.getY();
    }

    @Override
    public double minZ() {
        return min.getZ();
    }

    @Override
    public double maxX() {
        return max.getX();
    }

    @Override
    public double maxY() {
        return max.getY();
    }

    @Override
    public double maxZ() {
        return max.getZ();
    }

    @Override
    public LocationBox copy() {
        return new LocationBox(min.clone(), max.clone());
    }

    @Override
    public void expandByPosition(Location loc) {
        max.setX(Math.max(max.getX(), loc.getX()));
        max.setY(Math.max(max.getY(), loc.getY()));
        max.setZ(Math.max(max.getZ(), loc.getZ()));

        min.setX(Math.min(min.getX(), loc.getX()));
        min.setY(Math.min(min.getY(), loc.getY()));
        min.setZ(Math.min(min.getZ(), loc.getZ()));
    }

    @Override
    public Box createByPosition(Location loc) {
        return new RawBox(min.getWorld(), Math.min(min.getX(), loc.getX()), Math.min(min.getY(), loc.getY()), Math.min(min.getZ(), loc.getZ()),
                Math.max(max.getX(), loc.getX()), Math.max(max.getY(), loc.getY()), Math.max(max.getZ(), loc.getZ()));
    }

    @Override
    public void expandByPosition(double x, double y, double z) {
        max.setX(Math.max(max.getX(), x));
        max.setY(Math.max(max.getY(), y));
        max.setZ(Math.max(max.getZ(), z));

        min.setX(Math.min(min.getX(), x));
        min.setY(Math.min(min.getY(), y));
        min.setZ(Math.min(min.getZ(), z));
    }

    @Override
    public Box createByPosition(double x, double y, double z) {
        return new RawBox(min.getWorld(), Math.min(min.getX(), x), Math.min(min.getY(), y), Math.min(min.getZ(), z),
                Math.max(max.getX(), x), Math.max(max.getY(), y), Math.max(max.getZ(), z));
    }

    @Override
    public void expand(Location loc) {
        min.subtract(loc);
        max.add(loc);
    }

    @Override
    public Box createExpand(Location loc) {
        return new RawBox(min.getWorld(), minX() - loc.getX(), minY() - loc.getY(), minZ() - loc.getZ(),
                maxX() + loc.getX(), maxY() + loc.getY(), maxZ() + loc.getZ());
    }

    @Override
    public void expand(double x, double y, double z) {
        min.subtract(x, y, z);
        max.add(x, y, z);
    }

    @Override
    public Box createExpand(double x, double y, double z) {
        return new RawBox(min.getWorld(), minX() - x, minY() - y, minZ() - z,
                maxX() + x, maxY() + y, maxZ() + z);
    }

    @Override
    public void contract(Location loc) {
        min.add(loc);
        max.subtract(loc);
    }

    @Override
    public Box createContract(Location loc) {
        return new RawBox(min.getWorld(), minX() + loc.getX(), minY() + loc.getY(), minZ() + loc.getZ(),
                maxX() - loc.getX(), maxY() - loc.getY(), maxZ() - loc.getZ());
    }

    @Override
    public void contract(double x, double y, double z) {
        min.add(x, y, z);
        max.subtract(x, y, z);
    }

    @Override
    public Box createContract(double x, double y, double z) {
        return new RawBox(min.getWorld(), minX() + x, minY() + y, minZ() + z,
                maxX() - x, maxY() - y, maxZ() - z);
    }

    @Override
    public boolean intersectsWith(Box box) {
        return box.maxX() > minX() && box.minX() < maxX() &&
                box.maxY() > minY() && box.minY() < maxY() &&
                box.maxZ() > minZ() && box.minZ() < maxZ();
    }

    @Override
    public boolean isInside(Vector vector) {
        return isInsideXZ(vector) && vector.getY() >= min.getY() && vector.getY() <= max.getY();
    }

    @Override
    public boolean isInsideXZ(Vector vector) {
        return vector.getX() >= min.getX() && vector.getX() <= max.getX() &&
                vector.getZ() >= min.getZ() && vector.getZ() <= max.getZ();
    }

    @Override
    public double getAverageEdgeLength() {
        return ((max.getX() - min.getX()) + (max.getY() - min.getY()) + (max.getZ() - min.getZ())) / 3;
    }

    @Override
    public double getLength() {
        return min.distance(max);
    }

    @Override
    public double getLengthSquared() {
        return min.distanceSquared(max);
    }

    @Override
    public void move(double x, double y, double z) {
        min.add(x, y, z);
        max.add(x, y, z);
    }

    @Override
    public Box createMove(double x, double y, double z) {
        return new RawBox(world(), minX() + x, minY() + y, minZ() + z,
                maxX() + x, maxY() + y, maxZ() + z);
    }

    @Override
    public void move(Location location) {
        min.add(location);
        max.add(location);
    }

    @Override
    public Box createMove(Location location) {
        return createMove(location.getX(), location.getY(), location.getY());
    }

    @Override
    public void addCord(Location location) {
        addCord(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void addCord(double x, double y, double z) {
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
        min = new Location(world(), minX, minY, minZ);
        max = new Location(world(), maxX, maxY, maxZ);
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
    protected LocationBox clone() {
        try {
            return (LocationBox) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String toString() {
        return "LocationBox{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
