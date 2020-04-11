package io.github.splotycode.mosaik.spigot.listener;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StateListenerController {

    protected ToggleableListener current;

    public StateListenerController(ToggleableListener start) {
        next(start);
    }

    public boolean isCurrent(Class<? extends ToggleableListener> listener) {
        return listener.isInstance(current);
    }

    public <L extends ToggleableListener> L getCurrent(Class<L> listener) {
        if (listener.isInstance(current)) {
            //noinspection unchecked
            return (L) current;
        }
        return null;
    }

    public void unregister() {
        if (current != null) {
            current.unregisterListener();
        }
    }

    public void next(ToggleableListener next) {
        unregister();
        preEnable(next);
        current = next;
        next.registerListener();
        postEnable(next);
    }

    protected void preEnable(ToggleableListener state) {}
    protected void postEnable(ToggleableListener state) {}

}
