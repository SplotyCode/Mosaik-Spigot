package io.github.splotycode.mosaik.spigot.startup;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

@Priority(priority = StartUpPriorities.POST_LINKBASE)
public class RegisterTransformer implements StartupTask {
    @Override
    public void execute(StartUpEnvironmentChanger startUpEnvironmentChanger) throws Exception {
        TransformerManager.getInstance().registerPackage("io.github.splotycode.mosaik.spigot.transformer");
    }
}
