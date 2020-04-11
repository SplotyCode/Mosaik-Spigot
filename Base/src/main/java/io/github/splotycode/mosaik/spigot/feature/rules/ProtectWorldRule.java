package io.github.splotycode.mosaik.spigot.feature.rules;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ProtectWorldRule extends Feature {

    public static ProtectWorldRule allowRedstone() {
        return new ProtectWorldRule(false);
    }

    public static ProtectWorldRule disallowRedstone() {
        return new ProtectWorldRule(true);
    }

    private boolean disallowRedstone;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Action action = event.getAction();
            if (action == Action.PHYSICAL && disallowRedstone) {
                event.setCancelled(true);
            }
            Material material = event.getClickedBlock().getType();
            if (!disallowRedstone && material == Material.STONE_BUTTON || material == Material.WOOD_BUTTON ||
                    material == Material.LEVER) {
                return;
            }
            if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

}
