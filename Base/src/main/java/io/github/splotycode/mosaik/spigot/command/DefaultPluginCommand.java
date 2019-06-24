package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.ApplicationHandle;
import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.command.annotation.Command;
import io.github.splotycode.mosaik.spigot.locale.SpigotLocale;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.IOUtil;

public class DefaultPluginCommand {

    private SpigotApplicationType application;

    public DefaultPluginCommand(SpigotApplicationType application) {
        application.getRegistraction().register(this);
    }

    @Command("help")
    void help(SpigotMessageContext ctx) {
        String usage = ctx.getUsage();
        if (StringUtil.isEmpty(usage)) {
            ctx.message("core.defcommand.nohelp");
        } else {
            ctx.message("core.defcommand.help", usage);
        }
    }

    @Command("messages reload")
    void messageReload(SpigotMessageContext ctx) {
        application.getI18N().setLocale(new SpigotLocale(application.getDefaultMessageFile()));
        ctx.message("core.defcommand.message.reload");
    }

    @Command("messages stash")
    void messageStash(SpigotMessageContext ctx) {
        ctx.message("core.defcommand.message.stash");
        messageStashSilent(ctx);
        messageReload(ctx);
    }

    @Command("messages stashsilent")
    void messageStashSilent(SpigotMessageContext ctx) {
        FileUtil.writeToFile(application.getDefaultMessageFile(), IOUtil.resourceToText("message.txt"));
        ctx.message("core.defcommand.message.stashsilent");
    }

    @Command("reload")
    void reload(SpigotMessageContext ctx) {
        ctx.message("core.defcommand.reload.stopping");
        application.getLocalShutdownManager().simulateShutdown();
        ctx.message("core.defcommand.reload.stopped");

        ApplicationHandle handle = LinkBase.getApplicationManager().getHandleByName(application.getName());
        ctx.message("core.defcommand.reload.config");
        handle.configurise();
        ctx.message("core.defcommand.reload.starting");
        handle.start();
        ctx.message("core.defcommand.reload.finished");
    }

}
