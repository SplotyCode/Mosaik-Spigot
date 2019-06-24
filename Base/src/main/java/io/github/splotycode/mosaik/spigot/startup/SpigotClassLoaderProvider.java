package io.github.splotycode.mosaik.spigot.startup;

import io.github.splotycode.mosaik.runtime.startup.ClassLoaderProvider;
import lombok.Setter;

public class SpigotClassLoaderProvider implements ClassLoaderProvider {

    @Setter
    private static ClassLoader classLoader;

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
