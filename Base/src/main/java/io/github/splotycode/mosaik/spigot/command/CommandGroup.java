package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CommandGroup {

    public static class Head extends CommandGroup {

        private SpigotApplicationType application;

        public Head(String name, SpigotApplicationType application) {
            super(name);
            this.application = application;
        }
    }

    public Head getHead() {
        CommandGroup current = this;
        while (current.parent != null) {
            current = current.parent;
        }
        if (current instanceof Head) {
            return (Head) current;
        }
        throw new IllegalStateException("Head is not instanceof tail");
    }

    public SpigotApplicationType getApplication() {
        return getHead().application;
    }

    protected CommandGroup(String name) {
        this.name = name;
    }

    @Getter private final String name;

    @Getter @Setter private CommandGroup parent;

    @Getter private CommandContext command;

    @Getter private HashMap<String, CommandGroup> childs = new HashMap<>();

    public CommandGroup group(String name) {
        CommandGroup parent = this;
        CommandGroup group = null;

        for (String each : name.split(" ")) {
            group = new CommandGroup(each);
            group.setParent(parent);
            parent.childs.put(each, group);
            parent = group;
        }
        return group;
    }

    public void register(CommandContext command, boolean createListener) {
        this.command = command;
        SpigotApplicationType application = getApplication();

        if (createListener) {
            try {
                PluginCommand cmd = createCommand(command, application);

                Field map = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                map.setAccessible(true);
                CommandMap commandMap = (CommandMap) map.get(Bukkit.getServer());
                commandMap.register(application.spigotName(), cmd);
            } catch (ReflectiveOperationException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    private PluginCommand createCommand(CommandContext command, SpigotApplicationType application) throws ReflectiveOperationException {
        Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        c.setAccessible(true);

        PluginCommand cmd = c.newInstance(name, application.getPlugin());

        ArrayList<String> aliases = new ArrayList<>(Collections.singletonList(getHead().getName()));
        aliases.addAll(command.getData().getAliases());

        cmd.setAliases(aliases);
        cmd.setUsage(command.getData().getUsage());
        cmd.setDescription(command.getData().getDescription());
        cmd.setExecutor(application.getData(SpigotApplicationType.COMMAND_REDIRECT));
        cmd.setTabCompleter(application.getData(SpigotApplicationType.COMMAND_REDIRECT));
        return cmd;
    }

    @Override
    public String toString() {
        return "CommandGroup{" +
                "name='" + name + '\'' +
                ", command=" + command +
                ", childs=" + childs +
                '}';
    }
}
