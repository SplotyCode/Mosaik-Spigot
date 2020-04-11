package io.github.splotycode.mosaik.spigot.game;

import io.github.splotycode.mosaik.spigot.game.listener.GameStateListener;
import io.github.splotycode.mosaik.spigot.game.listener.PlayerListener;
import io.github.splotycode.mosaik.spigot.listener.ApplicationListener;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Game extends ApplicationListener {

    @Getter protected GameStateController stateController = new GameStateController(this);
    @Getter protected MultipleListenerHandler<Listener> handler = new MultipleListenerHandler<>();

    protected int minPlayers, maxPlayers;

    private ArrayList<Player> playingPlayers = new ArrayList<>();

    public boolean isPlaying(Player player) {
        return playingPlayers.contains(player);
    }

    public void addPlaying(Player player) {
        playingPlayers.add(player);
    }

    public void startGame() {
        registerListener();
    }

    public void stopGame() {
        unregisterListener();
    }

    public abstract Collection<? extends Player> allPlayers();
    public abstract boolean isInGame(Player player);

    public void addPlayer(Player player) {
        handler.call(PlayerListener.class, listener -> listener.onPlayerJoined(this, player));
        addPlayer0(player);
        triggerPlayerCount(1);
    }

    public void removePlayer(Player player) {
        handler.call(PlayerListener.class, listener -> listener.onPlayerLeft(this, player));
        removePlayer0(player);
        triggerPlayerCount(-1);
    }

    private void triggerPlayerCount(int change) {
        int players = allPlayers().size();
        handler.call(PlayerListener.class, listener -> listener.onPlayerCountChanged(this, players + change, players));
    }

    protected void addPlayer0(Player player) {}
    protected void removePlayer0(Player player) {}

    void updateState(GameState oldState, GameState newState) {
        handler.call(GameStateListener.class, listener -> listener.onStateUpdate(this, oldState, newState));
    }

}
