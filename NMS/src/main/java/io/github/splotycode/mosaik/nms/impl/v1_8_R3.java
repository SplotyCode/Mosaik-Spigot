package io.github.splotycode.mosaik.nms.impl;

import io.github.splotycode.mosaik.box.Box;
import io.github.splotycode.mosaik.box.RawBox;
import io.github.splotycode.mosaik.nms.NMS;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class v1_8_R3 implements NMS {

   private EntityPlayer getHandle(Player player) {
       return ((CraftPlayer) player).getHandle();
   }

    @Override
    public Box getBox(Player player) {
        return convert(player.getWorld(), getHandle(player).getBoundingBox());
    }

    private Box convert(World world, AxisAlignedBB boundingBox) {
        if (boundingBox == null) return null;
        return new RawBox(world, boundingBox.a, boundingBox.b, boundingBox.c, boundingBox.d, boundingBox.e, boundingBox.f);
    }

    @Override
    public int getPing(Player player) {
        return getHandle(player).ping;
    }

    @Override
    public Box getBox(Block block) {
        BlockPosition loc = new BlockPosition(block.getX(), block.getY(), block.getZ());
        WorldServer world = ((CraftWorld) block.getWorld()).getHandle();

        IBlockData data = world.getType(loc);
        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block);

        if (data == null || nmsBlock == null) return null;
        return convert(block.getWorld(), nmsBlock.a(world, loc, data));
   }

   private byte[] fromNBT(NBTTagCompound nbt) {
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
       try {
           NBTCompressedStreamTools.a(nbt, bos);
       } catch (IOException ex) {
           ExceptionUtil.throwRuntime(ex);
       }
       return bos.toByteArray();
   }

   private NBTTagCompound toNBT(byte[] data) {
       try {
           return NBTCompressedStreamTools.a(new ByteArrayInputStream(data));
       } catch (IOException ex) {
           ExceptionUtil.throwRuntime(ex);
           return null;
       }
   }

    @Override
    public byte[] getNBT(org.bukkit.block.BlockState state) {
        CraftBlockState craftState = (CraftBlockState) state;
        NBTTagCompound nbt = new NBTTagCompound();
        craftState.getTileEntity().b(nbt);
        return fromNBT(nbt);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void saveNBT(org.bukkit.block.BlockState state, byte[] data) {
        CraftBlockState craftState = (CraftBlockState) state;
        craftState.getTileEntity().b(toNBT(data));
        craftState.getTileEntity().update();
    }

    @Override
    public byte[] storeInventory(Inventory inventory) {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(int index = 0; index < inventory.getContents().length; index++){
            ItemStack itemStack = inventory.getItem(index);
            if(itemStack != null){
                NBTTagCompound itemCompound = CraftItemStack.asNMSCopy(itemStack).getTag();
                itemCompound.set("index", new NBTTagInt(index));
                list.add(itemCompound);
            }
        }
        nbt.set("inventory", list);
        nbt.setInt("size", list.size());
        return fromNBT(nbt);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Inventory loadInventory(byte[] data, Inventory inventory) {
        NBTTagCompound nbt = toNBT(data);
        NBTTagList list = nbt.getList("inventory", nbt.getInt("size"));

        if (inventory == null) {
            inventory = Bukkit.createInventory(null, list.size());
        }

        for(int i = 0; i < list.size(); i++){
            NBTTagCompound item = list.get(i);
            int index = item.getInt("index");
            inventory.setItem(index, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack.createStack(item)));
        }
        return inventory;
    }

}
