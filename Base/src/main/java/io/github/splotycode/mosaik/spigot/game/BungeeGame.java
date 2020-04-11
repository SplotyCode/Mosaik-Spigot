package io.github.splotycode.mosaik.spigot.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class BungeeGame extends Game {

    @Override
    public Collection<? extends Player> allPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public boolean isInGame(Player player) {
        return player.isOnline();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

}
