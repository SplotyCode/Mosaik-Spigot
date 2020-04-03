package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerToString extends ValueTransformer<OfflinePlayer, String> {

    @Override
    public String transform(OfflinePlayer offlinePlayer, DataFactory info) throws Exception {
        return offlinePlayer.getUniqueId().toString();
    }

}
