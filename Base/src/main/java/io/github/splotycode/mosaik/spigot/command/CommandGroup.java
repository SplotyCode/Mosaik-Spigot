package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

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

    public String commandString(char separator) {
        StringBuilder str = new StringBuilder();
        CommandGroup node = this;
        while (node != null) {
            str.insert(0, separator).insert(0, node.name);
            node = node.parent;
        }
        return str.deleteCharAt(0).toString();
    }

    public String commandAliasString(char separator) {
        StringBuilder str = new StringBuilder();
        CommandGroup node = this;
        while (node != null) {
            boolean first = true;
            for (String alias : node.getCommand().data().getAliases()) {
                if (first) {
                    first = false;
                } else {
                    str.insert(0, '/');
                }
                str.insert(0, alias);
            }
            str.insert(0, node.name).insert(0, separator);
            node = node.parent;
        }
        return str.deleteCharAt(0).toString();
    }

    public ArrayList<CommandGroup> realChilds() {
        ArrayList<CommandGroup> list = new ArrayList<>(Math.round(childs.size() * 0.85F));
        for (Map.Entry<String, CommandGroup> group : childs.entrySet()) {
            if (group.getKey().equals(group.getValue().name)) {
                list.add(group.getValue());
            }
        }
        return list;
    }

    public void register(CommandContext command, boolean createListener) {
        this.command = command;
        for (String aliases : command.data().getAliases()) {
            getParent().childs.put(aliases, this);
        }
        SpigotApplicationType application = getApplication();
        command.data().buildUsage(this);

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

        List<String> aliases = Collections.singletonList(getName());
        aliases.addAll(command.getData().getAliases());

        cmd.setAliases(aliases);
        cmd.setUsage(command.getData().getSimpleUsage());
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
