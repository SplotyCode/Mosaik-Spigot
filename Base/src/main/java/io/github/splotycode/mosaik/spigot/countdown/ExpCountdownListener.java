package io.github.splotycode.mosaik.spigot.countdown;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public class ExpCountdownListener implements CountdownListener {

    protected Collection<Player> players;

    private void reset() {
        players.forEach(player -> player.setTotalExperience(0));
    }

    @Override
    public void progress(int secondsLeft, float percentage) {
        for (Player player : players) {
            player.setLevel(secondsLeft);
            player.setExp(percentage);
        }
    }

    @Override
    public void end() {
        reset();
    }

    @Override
    public void start() {
        reset();
    }

    @Override
    public void pause() {
        reset();
    }
}
