package io.github.splotycode.mosaik.spigot.util;

import org.bukkit.Bukkit;

public final class MCVersion {

    public static String[] VERSION = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
    public static int MAJOR = Integer.parseInt(VERSION[0]);
    public static int MINOR = Integer.parseInt(VERSION[1]);
    public static int MINOR2 = VERSION.length > 2 ? Integer.parseInt(VERSION[2]) : 0;

    public static boolean isAtLeast8() {
        return MAJOR >= 1 && MINOR >= 8;
    }

    public static boolean isAtLeast9() {
        return MAJOR >= 1 && MINOR >= 9;
    }

    public static boolean isAtLeast12() {
        return MAJOR >= 1 && MINOR >= 12;
    }

    public static boolean isAtLeast13() {
        return MAJOR >= 1 && MINOR >= 13;
    }

}
