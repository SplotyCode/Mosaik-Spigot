package io.github.splotycode.mosaik.spigot;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import io.github.splotycode.mosaik.spigot.exception.PluginLoadException;
import io.github.splotycode.mosaik.spigot.gui.GuiListener;
import io.github.splotycode.mosaik.spigot.link.SpigotLinks;
import io.github.splotycode.mosaik.util.logger.JavaUtilLoggerFactory;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpigotPlugin extends JavaPlugin {

    private static boolean firstPlugin = true;

    private static Map<String, SpigotPlugin> instances = new HashMap<>();

    public static SpigotPlugin getInstance(String name) {
        return instances.get(name);
    }

    @Override
    public void onEnable() {
        instances.put(getName(), this);
        StartUpInvoke.inokeIfNotInitialised(new StartUpConfiguration()
                .withClassLoader(this::getClassLoader)
                .skipClassLoaderCheck()
                .skipInvokedCheck()
                .withBootLoggerFactory(JavaUtilLoggerFactory.class)
                .withRuntimeLoggerFactory(JavaUtilLoggerFactory.class));
        if (firstPlugin) {
            firstPlugin = false;
            firstPluginLoad();
        }

        Application rawApplication = LinkBase.getApplicationManager().getApplicationByName(getName());
        if (rawApplication == null) throw new PluginLoadException("Could not find Application with name " + getName());
        if (!(rawApplication instanceof SpigotApplicationType)) throw new PluginLoadException("Application '" + getName() + "' must implements SpigotPluginApplicationType");

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
