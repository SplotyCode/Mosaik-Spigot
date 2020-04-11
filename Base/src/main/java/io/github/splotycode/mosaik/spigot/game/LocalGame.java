package io.github.splotycode.mosaik.spigot.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class LocalGame extends Game {

    private ArrayList<Player> allPlayers = new ArrayList<>();

    @Override
    public Collection<? extends Player> allPlayers() {
        return allPlayers;
    }

    @Override
    public boolean isInGame(Player player) {
        return allPlayers.contains(player);
    }
}
