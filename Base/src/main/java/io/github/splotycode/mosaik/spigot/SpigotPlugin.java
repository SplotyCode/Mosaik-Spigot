package io.github.splotycode.mosaik.spigot;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import io.github.splotycode.mosaik.spigot.exception.PluginLoadException;
import io.github.splotycode.mosaik.spigot.gui.GuiListener;
import io.github.splotycode.mosaik.spigot.link.SpigotLinks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SpigotPlugin extends JavaPlugin {

    private static boolean firstPlugin = true;

    @Override
    public void onEnable() {
        StartUpInvoke.inokeIfNotInitialised();
        if (firstPlugin) {
            firstPlugin = false;
            firstPluginLoad();
        }

        Application rawApplication = LinkBase.getApplicationManager().getApplicationByName(getName());
        if (rawApplication == null) throw new PluginLoadException("Could not find Application with name " + getName());
        if (rawApplication instanceof SpigotApplicationType) throw new PluginLoadException("Application '" + getName() + "' must implements SpigotPluginApplicationType");

        SpigotApplicationType application = (SpigotApplicationType) rawApplication;
        application.getDataFactory().putData(SpigotApplicationType.PLUGIN, this);
    }

    private void firstPluginLoad() {
        LinkBase.getInstance().registerLink(SpigotLinks.MAIN_PLUGIN, this);
        registerListeners(new GuiListener());
    }

    public void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

}
