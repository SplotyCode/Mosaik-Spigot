package io.github.splotycode.mosaik.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AABBBox {

    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AABBBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AABBBox(Location pos1, Location pos2) {
        this.minX = pos1.getX();
        this.minY = pos1.getY();
        this.minZ = pos1.getZ();
        this.maxX = pos2.getX();
        this.maxY = pos2.getY();
        this.maxZ = pos2.getZ();
    }

    public AABBBox addCoord(double x, double y, double z) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;
        if(x < 0.0D) {
            d0 += x;
        } else if(x > 0.0D) {
            d3 += x;
        }

        if(y < 0.0D) {
            d1 += y;
        } else if(y > 0.0D) {
            d4 += y;
        }

        if(z < 0.0D) {
            d2 += z;
        } else if(z > 0.0D) {
            d5 += z;
        }

        return new AABBBox(d0, d1, d2, d3, d4, d5);
    }

    public AABBBox expand(double x, double y, double z) {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new AABBBox(d0, d1, d2, d3, d4, d5);
    }

    public AABBBox union(AABBBox other) {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new AABBBox(d0, d1, d2, d3, d4, d5);
    }

    public static AABBBox fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = Math.min(x1, x2);
        double d1 = Math.min(y1, y2);
        double d2 = Math.min(z1, z2);
        double d3 = Math.max(x1, x2);
        double d4 = Math.max(y1, y2);
        double d5 = Math.max(z1, z2);
        return new AABBBox(d0, d1, d2, d3, d4, d5);
    }

    public AABBBox offset(double x, double y, double z) {
        return new AABBBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public double calculateXOffset(AABBBox other, double offsetX) {
        if(other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if(offsetX > 0.0D && other.maxX <= this.minX) {
                double d1 = this.minX - other.maxX;
                if(d1 < offsetX) {
                    offsetX = d1;
                }
            } else if(offsetX < 0.0D && other.minX >= this.maxX) {
                double d0 = this.maxX - other.minX;
                if(d0 > offsetX) {
                    offsetX = d0;
                }
            }

            return offsetX;
        } else {
            return offsetX;
        }
    }

    public double calculateYOffset(AABBBox other, double offsetY) {
        if(other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if(offsetY > 0.0D && other.maxY <= this.minY) {
                double d1 = this.minY - other.maxY;
                if(d1 < offsetY) {
                    offsetY = d1;
                }
            } else if(offsetY < 0.0D && other.minY >= this.maxY) {
                double d0 = this.maxY - other.minY;
                if(d0 > offsetY) {
                    offsetY = d0;
                }
            }

            return offsetY;
        } else {
            return offsetY;
        }
    }

    public double calculateZOffset(AABBBox other, double offsetZ) {
        if(other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if(offsetZ > 0.0D && other.maxZ <= this.minZ) {
                double d1 = this.minZ - other.maxZ;
                if(d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if(offsetZ < 0.0D && other.minZ >= this.maxZ) {
                double d0 = this.maxZ - other.minZ;
                if(d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        } else {
            return offsetZ;
        }
    }

    public boolean intersectsWith(AABBBox other) {
        return (other.maxX > this.minX && other.minX < this.maxX) && ((other.maxY > this.minY && other.minY < this.maxY) && (other.maxZ > this.minZ && other.minZ < this.maxZ));
    }

    public boolean isVecInside(Vector vec) {
        return (vec.getX() > this.minX && vec.getX() < this.maxX) && ((vec.getY() > this.minY && vec.getY() < this.maxY) && (vec.getZ() > this.minZ && vec.getZ() < this.maxZ));
    }

    public double getAverageEdgeLength() {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0D;
    }

    public AABBBox contract(double x, double y, double z) {
        double d0 = this.minX + x;
        double d1 = this.minY + y;
        double d2 = this.minZ + z;
        double d3 = this.maxX - x;
        double d4 = this.maxY - y;
        double d5 = this.maxZ - z;
        return new AABBBox(d0, d1, d2, d3, d4, d5);
    }

    private boolean isVecInYZ(Vector vec) {
        return vec != null && (vec.getY() >= this.minY && vec.getY() <= this.maxY && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ);
    }

    private boolean isVecInXZ(Vector vec) {
        return vec != null && (vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ);
    }

    private boolean isVecInXY(Vector vec) {
        return vec != null && (vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getY() >= this.minY && vec.getY() <= this.maxY);
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
    
}
