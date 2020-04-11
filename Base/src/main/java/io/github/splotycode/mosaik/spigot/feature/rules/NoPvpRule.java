package io.github.splotycode.mosaik.spigot.feature.rules;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NoPvpRule extends Feature {

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }

}
