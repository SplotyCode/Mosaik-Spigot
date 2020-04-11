package io.github.splotycode.mosaik.spigot.feature.rules;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class InvincibleRule extends Feature {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }

}
