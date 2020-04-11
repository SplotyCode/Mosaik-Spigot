package io.github.splotycode.mosaik.spigot.feature.rules;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class NoDropRule extends Feature {

    public static NoDropRule noPickup() {
        return new NoDropRule(true, false);
    }

    public static NoDropRule noDrop() {
        return new NoDropRule(false, true);
    }

    public static NoDropRule disallowAll() {
        return new NoDropRule(true, true);
    }

    private boolean disallowPickup, disallowDrop;

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if (disallowPickup) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerPickupItemEvent event) {
        if (disallowDrop) {
            event.setCancelled(true);
        }
    }

}
