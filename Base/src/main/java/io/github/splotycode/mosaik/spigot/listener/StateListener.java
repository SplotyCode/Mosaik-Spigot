package io.github.splotycode.mosaik.spigot.listener;

import io.github.splotycode.mosaik.spigot.feature.Feature;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class StateListener extends ApplicationListener {

    protected List<Feature> features;

    public StateListener() {
        this(new ArrayList<>());
    }

    public StateListener(Feature... features) {
        this.features = Arrays.asList(features);
    }

    @Override
    public void registerListener() {
        super.registerListener();
        features.forEach(feature -> feature.enable(application));
    }

    @Override
    public void unregisterListener() {
        super.unregisterListener();
        features.forEach(Feature::disable);
    }
}
