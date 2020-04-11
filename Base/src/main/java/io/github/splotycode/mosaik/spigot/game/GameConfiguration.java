package io.github.splotycode.mosaik.spigot.game;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameConfiguration {

    private final SpigotApplicationType application;
    private boolean bungee;

    public Game createGame() {
        Game game = bungee ? new BungeeGame() : new LocalGame();
        game.setApplication(application);
        return game;
    }

}
