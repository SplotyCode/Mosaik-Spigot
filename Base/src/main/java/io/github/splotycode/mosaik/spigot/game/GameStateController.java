package io.github.splotycode.mosaik.spigot.game;

import io.github.splotycode.mosaik.spigot.listener.StateListenerController;
import io.github.splotycode.mosaik.spigot.listener.ToggleableListener;
import io.github.splotycode.mosaik.util.listener.Listener;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameStateController extends StateListenerController {

    private Game game;

    @Override
    protected void preEnable(ToggleableListener listener) {
        if (listener instanceof GameState) {
            GameState state = (GameState) listener;

            GameState old = current instanceof GameState ? (GameState) current : null;
            game.updateState(old, state);

            state.setGame(game);
            if (listener instanceof Listener) {
                game.handler.addListener((Listener) listener);
            }
        }
    }
}
