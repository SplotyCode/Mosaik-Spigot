package io.github.splotycode.mosaik.nms.impl;

import io.github.splotycode.mosaik.nms.AABBBox;
import io.github.splotycode.mosaik.nms.NMS;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

public class v1_8_R3 implements NMS {

   private EntityPlayer getHandle(Player player) {
       return ((CraftPlayer) player).getHandle();
   }

    @Override
    public AABBBox getBox(Player player) {
        return convert(getHandle(player).getBoundingBox());
    }

    private AABBBox convert(AxisAlignedBB boundingBox) {
        if (boundingBox == null) return null;
        return new AABBBox(boundingBox.a, boundingBox.b, boundingBox.c, boundingBox.d, boundingBox.e, boundingBox.f);
    }

    @Override
    public int getPing(Player player) {
        return getHandle(player).ping;
    }

    @Override
    public AABBBox getBox(Block block) {
        BlockPosition loc = new BlockPosition(block.getX(), block.getY(), block.getZ());
        WorldServer world = ((CraftWorld) block.getWorld()).getHandle();

        IBlockData data = world.getType(loc);
        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block);

        if (data == null || nmsBlock == null) return null;
        return convert(nmsBlock.a(world, loc, data));
   }

}
