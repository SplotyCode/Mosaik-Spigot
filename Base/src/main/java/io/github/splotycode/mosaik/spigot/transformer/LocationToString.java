package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.CodecUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;
import org.bukkit.Location;

import java.nio.ByteBuffer;

public class LocationToString extends ValueTransformer<Location, String> {

    @Override
    public String transform(Location location, DataFactory dataFactory) throws Exception {
        if (location == null) return "";
        String world = location.getWorld().getName();
        ByteBuffer buffer = ByteBuffer.allocate(BinaryUtil.calculateStringSize(world) + 3 * 8 + 2 * 4);
        buffer.put(BinaryUtil.writeString(world));
        buffer.putDouble(location.getX());
        buffer.putDouble(location.getY());
        buffer.putDouble(location.getZ());
        buffer.putFloat(location.getYaw());
        buffer.putFloat(location.getPitch());
        return CodecUtil.bytesToHex(buffer.array());
    }
}
