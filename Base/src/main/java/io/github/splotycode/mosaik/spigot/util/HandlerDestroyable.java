package io.github.splotycode.mosaik.spigot.util;

import io.github.splotycode.mosaik.util.commoni.Destroyable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface HandlerDestroyable extends Destroyable, Listener {

    @Override
    default void destroy() {
        HandlerList.unregisterAll(this);
    }
}
