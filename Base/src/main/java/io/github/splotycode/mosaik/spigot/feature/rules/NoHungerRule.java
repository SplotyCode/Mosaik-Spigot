package io.github.splotycode.mosaik.spigot.feature.rules;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoHungerRule extends Feature {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
