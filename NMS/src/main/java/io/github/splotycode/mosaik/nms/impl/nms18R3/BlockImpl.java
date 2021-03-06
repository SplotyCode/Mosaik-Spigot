package io.github.splotycode.mosaik.nms.impl.nms18R3;

import io.github.splotycode.mosaik.box.Box;
import io.github.splotycode.mosaik.nms.NMSBlock;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.ArrayList;
import java.util.List;

public class BlockImpl implements NMSBlock {

    private Block block;

    public BlockImpl(Block block) {
        this.block = block;
    }

    public static World getWorld0(Location location) {
        return ((CraftWorld) location.getWorld()).getHandle();
    }

    public static IBlockData getData0(Location location) {
        return getWorld0(location).getType(getPos0(location));
    }

    public static BlockPosition getPos0(Location location) {
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static void getBoxes0(World world, IBlockData data, BlockPosition blockPosition, AxisAlignedBB box, ArrayList<AxisAlignedBB> output) {
        data.getBlock().a(world, blockPosition, data, box, output, null);
    }

    public static List<Box> getBoxes0(org.bukkit.World bukkitWorld, World world, IBlockData data, BlockPosition blockPosition, AxisAlignedBB box) {
        ArrayList<AxisAlignedBB> output = new ArrayList<>();
        getBoxes0(world, data, blockPosition, box, output);
        return BoxUtil.convert(bukkitWorld, output);
    }

    @Override
    public Box getBox(org.bukkit.block.Block block) {
        BlockPosition loc = new BlockPosition(block.getX(), block.getY(), block.getZ());
        World world = getWorld0(block.getLocation());
        IBlockData data = getData0(block.getLocation());
        if (data == null) return null;
        net.minecraft.server.v1_8_R3.Block nmsBlock = data.getBlock();

        if (nmsBlock == null) return null;
        return BoxUtil.convert(block.getWorld(), nmsBlock.a(world, loc, data));
    }

    @Override
    public List<Box> getIntersections(org.bukkit.block.Block block, Box box) {
        Location location = block.getLocation();

        AxisAlignedBB nmsBox = BoxUtil.convert(box);
        BlockPosition blockPosition = getPos0(location);
        World world = getWorld0(location);

        IBlockData data = world.getType(blockPosition);
        return getBoxes0(location.getWorld(), world, data, blockPosition, nmsBox);
    }

    @Override
    public List<Box> getIntersections(Location location, int data, Box box) {
        AxisAlignedBB nmsBox = BoxUtil.convert(box);
        BlockPosition blockPosition = getPos0(location);
        World world = getWorld0(location);
        IBlockData stateData = block.fromLegacyData(data);
        return getBoxes0(location.getWorld(), world, stateData, blockPosition, nmsBox);
    }

    @Override
    public byte[] getNBT(Location location) {
        if (block.isTileEntity()) {
            CraftWorld world = (CraftWorld) location.getWorld();
            TileEntity tileEntity = world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            NBTTagCompound nbt = new NBTTagCompound();
            tileEntity.b(nbt);
            return NBTUtil.fromNBT(nbt);
        }
        return ArrayUtil.EMPTY_BYTE_ARRAY;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void saveNBT(Location location, byte[] nbt) {
        if (block.isTileEntity()) {
            CraftWorld world = (CraftWorld) location.getWorld();
            TileEntity tileEntity = world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            tileEntity.a(NBTUtil.toNBT(nbt));
            tileEntity.update();
        }
    }

    @Override
    public float slipperiness() {
        /*
         * In MCP it is named slipperiness craftbukkit named it frictionFactor
         * If this field changes we can get the name from the BlockIce constructor
         */
        return block.frictionFactor;
    }

}
