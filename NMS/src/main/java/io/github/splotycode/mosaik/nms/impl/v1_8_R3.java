package io.github.splotycode.mosaik.nms.impl;

import io.github.splotycode.mosaik.box.Box;
import io.github.splotycode.mosaik.box.RawBox;
import io.github.splotycode.mosaik.nms.NMS;
import io.github.splotycode.mosaik.nms.impl.nms18R3.BlockImpl;
import io.github.splotycode.mosaik.nms.impl.nms18R3.BlockRegistryImpl;
import io.github.splotycode.mosaik.nms.impl.nms18R3.BoxUtil;
import io.github.splotycode.mosaik.nms.impl.nms18R3.NBTUtil;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class v1_8_R3 implements NMS {

    @Getter private BlockRegistryImpl blockRegistry = new BlockRegistryImpl();

    private EntityPlayer getHandle(Player player) {
       return ((CraftPlayer) player).getHandle();
   }

    @Override
    public Box getBox(Player player) {
        return BoxUtil.convert(player.getWorld(), getHandle(player).getBoundingBox());
    }

    @Override
    public List<Box> getIntersections(Box box) {
        World world = ((CraftWorld) box.world()).getHandle();
        List<AxisAlignedBB> intersections = world.a(BoxUtil.convert(box));
        return BoxUtil.convert(box.world(), intersections);
    }

    @Override
    public List<Box> getBlockIntersection(int combinedID, Location location, Box box) {
        IBlockData data = Block.getByCombinedId(combinedID);
        World world = BlockImpl.getWorld0(location);
        BlockPosition position = BlockImpl.getPos0(location);

        ArrayList<AxisAlignedBB> boxes = new ArrayList<>();
        BlockImpl.getBoxes0(world, data, position, BoxUtil.convert(box), boxes);
        return BoxUtil.convert(location.getWorld(), boxes);
    }

    @Override
    public int getPing(Player player) {
        return getHandle(player).ping;
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
        return NBTUtil.fromNBT(nbt);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Inventory loadInventory(byte[] data, Inventory inventory) {
        NBTTagCompound nbt = NBTUtil.toNBT(data);
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
