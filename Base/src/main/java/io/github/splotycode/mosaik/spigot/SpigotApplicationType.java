package io.github.splotycode.mosaik.spigot;

import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.spigot.command.CommandGroup;
import io.github.splotycode.mosaik.spigot.command.CommandRedirect;
import io.github.splotycode.mosaik.spigot.command.CommandRegistration;
import io.github.splotycode.mosaik.spigot.gui.Gui;
import io.github.splotycode.mosaik.spigot.gui.GuiManager;
import io.github.splotycode.mosaik.spigot.listener.ListenerClassRegister;
import io.github.splotycode.mosaik.spigot.locale.SpigotLocale;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import io.github.splotycode.mosaik.spigot.storage.YamlFile;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.i18n.I18N;
import io.github.splotycode.mosaik.util.i18n.MessageContext;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.File;

public interface SpigotApplicationType extends ApplicationType {

    DataKey<SpigotPlugin> PLUGIN = new DataKey<>("spigot.plugin");
    DataKey<String> PREFIX = new DataKey<>("spigot.prefix");

    DataKey<CommandGroup> COMMAND_HEAD = new DataKey<>("spigot.command_head");
    DataKey<CommandRedirect> COMMAND_REDIRECT = new DataKey<>("spigot.command_redirect");
    DataKey<CommandRegistration> COMMAND_REGISTRATION = new DataKey<>("spigot.command_reg");

    DataKey<I18N> I18N = new DataKey<>("spigot.i18n");
    DataKey<SpigotMessageContext> MESSAGE_CONTEXT = new DataKey<>("spigot.message_context");
    DataKey<YamlFile> CONFIG = new DataKey<>("spigot.config");

    DataKey<ClassRegister<Listener>> LISTENER_REGISTRY = new DataKey<>("spigot.listener_registry");

    GuiManager GUI_MANAGER = new GuiManager();

    default CommandGroup getGroup(String[] name) {
        CommandGroup current = getCommandHead();
        for (String sub : name) {
            CommandGroup subGroup = current.getChilds().get(sub);
            if (subGroup == null) return current;
            current = subGroup;
        }
        return current;
    }

    default void initType(BootContext context, SpigotApplicationType dummy) {
        putData(COMMAND_HEAD, new CommandGroup.Head("", this));
        putData(COMMAND_REDIRECT, new CommandRedirect(this));
        putData(COMMAND_REGISTRATION, new CommandRegistration(this));
        putData(LISTENER_REGISTRY, new ListenerClassRegister(this));

        resourceToFile("message.txt");
        useLanguageFile("message.txt");

        putData(SpigotApplicationType.PLUGIN, SpigotPlugin.getInstance(getName()));

        getLocalShutdownManager().addShutdownTask(() -> HandlerList.unregisterAll(getPlugin()));
    }

    default void printCommandMap() {
        printCommandMap(getCommandHead());
    }

    default void printCommandMap(CommandGroup group) {
        if (group.getCommand() != null) {
            getLogger().info("/" + group.getCommand().data().getSimpleUsage());
        }
        for (CommandGroup child : group.realChilds()) {
            printCommandMap(child);
        }
    }

    default SpigotMessageContext constructMessageContext() {
        SpigotMessageContext ctx = new SpigotMessageContext(getI18N(), null);
        ctx.setPrefix(getPrefix());
        putData(MESSAGE_CONTEXT, ctx);
        return ctx;
    }

    default void openGui(Class<? extends Gui> clazz, Player player) {
        GUI_MANAGER.openGui(clazz, this, player);
    }

    default SpigotMessageContext getMessageContext() {
        if (!Bukkit.isPrimaryThread()) throw new IllegalStateException("The Main MessageContext is not thread save");
        SpigotMessageContext ctx = getData(MESSAGE_CONTEXT);
        if (ctx == null) {
            ctx = constructMessageContext();
        }
        return ctx;
    }

    default ClassRegister<Listener> getListenerRegistry() {
        return getData(LISTENER_REGISTRY);
    }

    default void resourceToFile(String name) {
        File file = getFile(name);
        if (!file.exists()) {
            FileUtil.writeToFile(file, IOUtil.resourceToText(name));
        }
    }

    default File getFile(String name) {
        return new File(getPlugin().getDataFolder(), name);
    }

    default void loadResourceConfig() {
        loadResourceConfig("config.yml");
    }

    default void loadResourceConfig(String name) {
        resourceToFile(name);
        loadConfig(name);
    }

    default void loadConfig() {
        loadConfig("config.yml");
    }

    default void loadConfig(String name) {
        putData(CONFIG, new YamlFile(getFile(name)));
    }

    default File getDefaultMessageFile() {
        return getFile("message.txt");
    }

    default void useLanguageFile(String name) {
        putData(I18N, new I18N().setLocale(new SpigotLocale(getFile(name))));
        constructMessageContext();
    }

    default String spigotName() {
        return getPlugin().getDescription().getName();
    }

    default CommandGroup getCommandHead() {
        return getData(COMMAND_HEAD);
    }

    default SpigotPlugin getPlugin() {
        return getData(PLUGIN);
    }

    default String getPrefix() {
        String prefix = getData(PREFIX);
        if (prefix == null) {
            return getI18N().get("core.prefix");
        }
        return prefix;
    }

    default CommandRegistration getRegistraction() {
        return getData(COMMAND_REGISTRATION);
    }

    default YamlFile getConfiguration() {
        return getData(CONFIG);
    }

    default I18N getI18N() {
        return getData(I18N);
    }

    default void setPrefix(String prefix) {
        putData(PREFIX, prefix);
    }

    default void registerListeners(Listener... listeners) {
        getPlugin().registerListeners(listeners);
    }

}
