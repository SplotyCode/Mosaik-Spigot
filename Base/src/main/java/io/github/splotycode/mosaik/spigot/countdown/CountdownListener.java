package io.github.splotycode.mosaik.spigot.countdown;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface CountdownListener extends Listener {

    void end();
    void start();
    void progress(int secondsLeft, float percentage);
    void pause();

}
