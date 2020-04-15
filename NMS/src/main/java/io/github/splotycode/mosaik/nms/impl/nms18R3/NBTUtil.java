package io.github.splotycode.mosaik.nms.impl.nms18R3;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class NBTUtil {

    public static byte[] fromNBT(NBTTagCompound nbt) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            NBTCompressedStreamTools.a(nbt, bos);
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
        return bos.toByteArray();
    }

    public static NBTTagCompound toNBT(byte[] data) {
        try {
            return NBTCompressedStreamTools.a(new ByteArrayInputStream(data));
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
            return null;
        }
    }

}
