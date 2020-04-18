package io.github.splotycode.mosaik.nms.impl.nms18R3;

import io.github.splotycode.mosaik.nms.BlockRegistry;
import io.github.splotycode.mosaik.nms.NMSBlock;
import net.minecraft.server.v1_8_R3.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class BlockRegistryImpl extends BlockRegistry<Block> {

    @Override
    protected Block byLocation(Location location) {
        return BlockImpl.getData0(location).getBlock();
    }

    @Override
    protected Block byBlock(org.bukkit.block.Block block) {
        return CraftMagicNumbers.getBlock(block);
    }

    @Override
    protected Block byMaterial(Material material) {
        return Block.getById(material.getId());
    }

    @Override
    protected Iterable<Block> getAllBlocks() {
        return Block.REGISTRY;
    }

    @Override
    protected NMSBlock newBlock(Block block) {
        return new BlockImpl(block);
    }
}
