package io.github.splotycode.mosaik.spigot.listener;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerEvent;

@Getter
@Setter
public class ApplicationListener implements ToggleableListener {

    protected SpigotApplicationType application;

    public SpigotMessageContext message(PlayerEvent event) {
        return message(event.getPlayer());
    }

    public SpigotMessageContext message(CommandSender sender) {
        SpigotMessageContext ctx = new SpigotMessageContext(application.getI18N(), sender);
        ctx.setPrefix(application.getPrefix());
        return ctx;
    }

    @Override
    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, application.getPlugin());
    }
}
