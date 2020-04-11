package io.github.splotycode.mosaik.spigot.game.listener;

import io.github.splotycode.mosaik.spigot.game.Game;
import io.github.splotycode.mosaik.spigot.game.GameState;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface GameStateListener extends Listener {

    void onStateUpdate(Game game, GameState oldState, GameState newState);

}
