package io.github.splotycode.mosaik.spigot.listener;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.Collection;

public class ListenerClassRegister implements ClassRegister<Listener> {

    protected SpigotApplicationType application;
    protected Plugin plugin;

    public ListenerClassRegister(SpigotApplicationType application) {
        this.application = application;
        plugin = application.getPlugin();
    }

    @Override
    public void register(Listener listener) {
        if (listener instanceof ApplicationListener) {
            ((ApplicationListener) listener).setApplication(application);
        }
        if (listener instanceof ToggleableListener) {
            ((ToggleableListener) listener).registerListener();
        } else {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void unRegister(Listener listener) {
        if (listener instanceof ToggleableListener) {
            ((ToggleableListener) listener).unregisterListener();
        } else {
            HandlerList.unregisterAll(listener);
        }
    }

    @Override
    public Collection<Listener> getAll() {
        ArrayList<Listener> listeners = new ArrayList<>();
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            listeners.add(listener.getListener());
        }
        return listeners;
    }

}
