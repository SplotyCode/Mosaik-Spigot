package io.github.splotycode.mosaik.nms.impl.nms18R3;

import io.github.splotycode.mosaik.box.Box;
import io.github.splotycode.mosaik.box.RawBox;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public final class BoxUtil {

    public static Box convert(World world, AxisAlignedBB boundingBox) {
        if (boundingBox == null) return null;
        return new RawBox(world, boundingBox.a, boundingBox.b, boundingBox.c, boundingBox.d, boundingBox.e, boundingBox.f);
    }

    public static List<Box> convert(World world, List<AxisAlignedBB> boundingBoxes) {
        ArrayList<Box> boxes = new ArrayList<>(boundingBoxes.size());
        for (AxisAlignedBB box : boundingBoxes) {
            boxes.add(convert(world, box));
        }
        return boxes;
    }

    public static AxisAlignedBB convert(Box box) {
        return new AxisAlignedBB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

}
