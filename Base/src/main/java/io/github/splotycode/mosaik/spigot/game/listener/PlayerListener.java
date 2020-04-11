package io.github.splotycode.mosaik.spigot.game.listener;

import io.github.splotycode.mosaik.spigot.game.Game;
import io.github.splotycode.mosaik.util.listener.Listener;
import org.bukkit.entity.Player;

public interface PlayerListener extends Listener {

    void onPlayerJoined(Game game, Player player);
    void onPlayerLeft(Game game, Player player);

    int onPlayerCountChanged(Game game, int oldCount, int newCount);


}
