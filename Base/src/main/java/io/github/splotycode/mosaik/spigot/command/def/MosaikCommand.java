package io.github.splotycode.mosaik.spigot.command.def;

import io.github.splotycode.mosaik.spigot.command.annotation.Command;
import io.github.splotycode.mosaik.spigot.command.annotation.NeedPermission;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import org.bukkit.command.CommandSender;

import java.util.HashSet;

@Command("mosaik")
public class MosaikCommand {

    public static MosaikCommand INSTANCE;

    private HashSet<String> debugPlayers = new HashSet<>();

    public MosaikCommand() {
        INSTANCE = this;
    }

    public boolean isDebugging(CommandSender sender) {
        return debugPlayers.contains(sender.getName());
    }


    @Command("debug")
    @NeedPermission("mosaik.debug")
    void toogleDebug(CommandSender sender, SpigotMessageContext ctx) {
        if (debugPlayers.contains(sender.getName())) {
            debugPlayers.remove(sender.getName());
            ctx.message("core.defcommand.debug.off");
        } else {
            debugPlayers.add(sender.getName());
            ctx.message("core.defcommand.debug.on");
        }
    }

}
