package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.command.def.MosaikCommand;
import io.github.splotycode.mosaik.spigot.exception.CommandException;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import io.github.splotycode.mosaik.spigot.permission.Permissions;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.valuetransformer.TransformException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRedirect implements CommandExecutor, TabCompleter {

    public static final DataKey<CommandSender> SENDER = new DataKey<>("command.sender");
    public static final DataKey<String[]> ARGS = new DataKey<>("command.args");
    public static final DataKey<SpigotMessageContext> MESSAGE_CONTEXT = new DataKey<>("message.context");

    private SpigotApplicationType application;
    private SpigotMessageContext messageContext;

    public CommandRedirect(SpigotApplicationType application) {
        this.application = application;
    }
    
    private void prepareMessageContext(CommandSender sender, CommandGroup group, String usage) {
        if (messageContext == null || !messageContext.isReuse()) {
            messageContext = new SpigotMessageContext(application.getI18N(), sender);
        } else {
            messageContext.reuse(sender);
            messageContext.setAllLinePrefix(true);
        }
        messageContext.setUsage(usage);
        messageContext.setPrefix(application.getPrefix());
        messageContext.setTranslationPrefix("commands." + group.commandString('.'));
    }

    private void printHelp(CommandGroup group) {
        printHelp(messageContext, group);
    }

    public static void printHelp(SpigotMessageContext ctx, CommandGroup group) {
        CommandData data;
        if (group.getCommand() != null && (data = group.getCommand().data()).hasPermission(ctx.getSender())) {
            ctx.messageRaw("/" + data.getSimpleUsage());
            if (!StringUtil.isEmpty(data.getDescription())) {
                ctx.message(data.getDescription());
            }
        }
        for (CommandGroup child : group.realChilds()) {
            printHelp(ctx, child);
        }
    }

    private boolean isHelp(String last) {
        return "help".equals(last) || "hilfe".equals(last);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean help = isHelp(ArrayUtil.last(args));
        if (help) {
            args = ArrayUtil.resize(args, args.length - 1);
        }

        CommandGroup group = application.getGroup(ArrayUtil.prepend(args, label));
        if (help) {
            CommandGroup helpGroup = group.getChilds().get("help");
            if (helpGroup != null) {
                group = helpGroup;
            }
        }
        CommandContext command = group.getCommand();
        prepareMessageContext(sender, group, null);
        if (command == null) {
            messageContext.message("core.command.notfound");
        }
        if (help || command == null) {
            if (group.realChilds().isEmpty() && command == null)  {
                messageContext.message("core.command.nohelp");
            } else {
                messageContext.sendHeader("core.command.help");
                printHelp(group);
                messageContext.sendHeader("core.command.help");
            }
            return true;
        }

        args = Arrays.copyOfRange(args, group.parentSize(), args.length);

        messageContext.setUsage(command.data().getSimpleUsage());

        DataFactory additionally = new DataFactory();
        additionally.putData(SENDER, sender);
        additionally.putData(ARGS, args);


        messageContext.addReplacement("%executor%", sender.getName());
        for (int i = 0; i < args.length; i++) {
            messageContext.addReplacement("%arg" + i + "%", args[i]);
        }
        additionally.putData(MESSAGE_CONTEXT, messageContext);

        if (!messageContext.checkAccess(command.getData().getPermission())) return true;
        try {
            command.callmethod(additionally);
        } catch (Throwable ex) {
            RuntimeException userException = ExceptionUtil.getInstanceOfCause(ex, CommandException.class);
            if (userException == null) {
                userException = ExceptionUtil.getInstanceOfCause(ex, TransformException.class);
                if (userException != null && userException.getCause() instanceof TransformException) {
                    userException = (RuntimeException) userException.getCause();
                }
            }
            if (userException != null) {
                messageContext.messageRaw(userException.getMessage());
                if (MosaikCommand.INSTANCE.isDebugging(sender)) {
                    messageContext.messageRaw(ExceptionUtil.toString(ex));
                }
                return true;
            }
            messageContext.message("core.command.error");
            if (sender.hasPermission(Permissions.DEBUG)) {
                messageContext.messageRaw(ExceptionUtil.toString(ex));
            } else {
                messageContext.message("core.command.nodebug");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        CommandGroup group = application.getGroup(args);
        CommandContext command = group.getCommand();
        prepareMessageContext(sender, group, null);
        if (command == null) {
            messageContext.message("core.command.notfound");
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>(group.getChilds().keySet());

        String arg = args[args.length - 1].toLowerCase();
        Parameter parameter = command.getData().getFields().get(args.length - 2); // For label and to get the index

        if (parameter != null && OfflinePlayer.class.isAssignableFrom(parameter.getType())) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(name -> name.toLowerCase().contains(arg)).collect(Collectors.toList()));
        }

        if (completions.isEmpty()) {
            messageContext.message("core.command.notab");
        } else {
            completions.add("help");
        }
        return completions;
    }

}
