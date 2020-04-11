package io.github.splotycode.mosaik.spigot.countdown;

import io.github.splotycode.mosaik.spigot.SpigotPlugin;
import io.github.splotycode.mosaik.util.listener.DefaultListenerHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class Countdown {

    private BukkitTask task;
    private int secondsLeft, startSeconds;
    private SpigotPlugin plugin;
    private boolean paused = true;

    private DefaultListenerHandler<CountdownListener> listenerHandler = new DefaultListenerHandler<>();

    public Countdown(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    public float percentage() {
        return startSeconds / (float) (startSeconds - secondsLeft);
    }

    public void start(int seconds) {
        paused = false;
        secondsLeft = startSeconds = seconds;
        listenerHandler.call(CountdownListener::start);
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            float percentage = percentage();
            listenerHandler.call(listener -> listener.progress(secondsLeft, percentage));
            if (secondsLeft <= 0) {
                stop();
            } else {
                secondsLeft--;
            }
        }, 0, 20);
    }

    private void stopTask() {
        if (task != null) {
            task.cancel();
        }
    }

    public void stop() {
        secondsLeft = 0;
        stopTask();
        listenerHandler.call(CountdownListener::end);
    }

    public void pause(int seconds) {
        stopTask();
        paused = true;
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            listenerHandler.call(CountdownListener::pause);
        }, seconds * 20, seconds * 20);
    }

}
