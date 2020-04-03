package io.github.splotycode.mosaik.spigot.util;

import io.github.splotycode.mosaik.util.StringUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Animal {

    PIG,
    COW,
    CHICKEN,
    WOLF,
    OCELOT,
    SHEEP,
    SQUID,
    VILLAGER,
    SLIME,
    MAGMA_CUBE("LavaSlime"),
    CREEPER,
    ZOMBIE,
    SKELETON,
    WITHER_SKELETON("WSkeleton"),
    WITHER,
    ENDERMAN,
    IRON_GOLEM("Golem"),
    BLAZE,
    GHAST,
    GUARDIAN,
    ELDER_GUARDIAN("EGuardian"),
    WITCH,
    PUFFERFISH("!Luci");

    Animal() {
        this.mHFName = StringUtil.camelCase(name());
    }

    private final String mHFName;

    public String getMHFName() {
        return mHFName.startsWith("!") ? mHFName.substring(1) : "MHF_" + mHFName;
    }
}
