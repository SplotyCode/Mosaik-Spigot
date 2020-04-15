package io.github.splotycode.mosaik.nms;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.HashMap;

public abstract class BlockRegistry<B> {

    @Getter protected HashMap<B, NMSBlock> blocks = new HashMap<>();

    public BlockRegistry() {
        for (B block : getAllBlocks()) {
            blocks.put(block, newBlock(block));
        }
    }

    public NMSBlock getBlock(Block block) {
        return blocks.get(byBlock(block));
    }

    public NMSBlock getBlock(Location location) {
        return blocks.get(byLocation(location));
    }

    protected abstract B byLocation(Location location);
    protected abstract B byBlock(Block block);

    protected abstract Iterable<B> getAllBlocks();
    protected abstract NMSBlock newBlock(B block);


}
