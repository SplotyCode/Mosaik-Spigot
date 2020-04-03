package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.CodecUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.io.BinaryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.nio.ByteBuffer;

public class StringToLocation extends ValueTransformer<String, Location> {

    @Override
    public Location transform(String hex, DataFactory dataFactory) throws Exception {
        if (StringUtil.isEmpty(hex)) return null;
        byte[] data = CodecUtil.decodeHex(hex);
        String world = BinaryUtil.readString(data);
        ByteBuffer buffer = ByteBuffer.wrap(data, BinaryUtil.calculateStringSize(world), data.length);
        return new Location(Bukkit.getWorld(world),
                buffer.getDouble(),
                buffer.getDouble(),
                buffer.getDouble(),
                buffer.getFloat(),
                buffer.getFloat());
    }
}
