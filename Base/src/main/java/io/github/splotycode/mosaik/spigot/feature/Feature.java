package io.github.splotycode.mosaik.spigot.feature;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.listener.ApplicationListener;

public abstract class Feature extends ApplicationListener {

    public void enable(SpigotApplicationType application) {
        setApplication(application);
        registerListener();
        onEnable();
    }

    public void disable() {
        unregisterListener();
        onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}

}
