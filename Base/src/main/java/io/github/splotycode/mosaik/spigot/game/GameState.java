package io.github.splotycode.mosaik.spigot.game;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import io.github.splotycode.mosaik.spigot.listener.StateListener;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GameState extends StateListener {

    @Getter @Setter protected Game game;

    public GameState(List<Feature> features) {
        super(features);
    }

    public GameState() {
    }

    public GameState(Feature... features) {
        super(features);
    }

}
