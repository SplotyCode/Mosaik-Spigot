package io.github.splotycode.mosaik.spigot.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface ToggleableListener extends Listener {

    default void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

    void registerListener();

}
