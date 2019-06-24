package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.exception.CommandExcpetion;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import io.github.splotycode.mosaik.spigot.permission.Permissions;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.valuetransformer.TransformException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRedirect implements CommandExecutor, TabCompleter {

    public static final DataKey<CommandSender> SENDER = new DataKey<>("command.sender");
    public static final DataKey<String[]> ARGS = new DataKey<>("command.args");
    public static final DataKey<SpigotMessageContext> MESSAGE_CONTEXT = new DataKey<>("message.context");

    private SpigotApplicationType application;
    private SpigotMessageContext messageContext;

    public CommandRedirect(SpigotApplicationType application) {
        this.application = application;
    }
    
    private void prepareMessageContext(CommandSender sender, String usage) {
        if (messageContext == null || !messageContext.isReuse()) {
            messageContext = new SpigotMessageContext(application.getI18N(), sender);
        } else {
            messageContext.reuse(sender);
        }
        messageContext.setUsage(usage);
        messageContext.setPrefix(application.getPrefix());
        messageContext.setAllLinePrefix(true);
        messageContext.addReplacement("%line%", "\n");
    }

    private String[] appendArgs(String label, String[] args) {
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = label;
        System.arraycopy(args, 0, allArgs, 1, args.length);
        return allArgs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandGroup group = application.getGroup(appendArgs(label, args));
        CommandContext command = group.getCommand();
        if (command == null) return false;

        DataFactory additionally = new DataFactory();
        additionally.putData(SENDER, sender);
        additionally.putData(ARGS, args);

        prepareMessageContext(sender, command.getData().getUsage());
        messageContext.addReplacement("%executor%", sender.getName());
        for (int i = 0; i < args.length; i++) {
            messageContext.addReplacement("%arg" + i + "%", args[i]);
        }
        additionally.putData(MESSAGE_CONTEXT, messageContext);

        if (!messageContext.checkAccess(command.getData().getPermission())) return true;
        try {
            command.callmethod(additionally);
        } catch (Throwable ex) {
            CommandExcpetion cmdEx = ExceptionUtil.getInstanceOfCause(ex, CommandExcpetion.class);
            TransformException transformException = ExceptionUtil.getInstanceOfCause(ex, TransformException.class);
            if (cmdEx != null) {
                messageContext.messageRaw(cmdEx.getMessage());
                return true;
            }
            if (transformException != null) {
                TransformException raw = (TransformException) transformException.getCause();
                String transformKey = "transforms." + raw.getStackTrace()[0].getClassName();
                if (messageContext.hasTranslation(transformKey)) {
                    messageContext.message(transformKey);
                } else {
                    if (sender.hasPermission(Permissions.DEBUG)) {
                        messageContext.messageRaw(transformKey);
                    }
                    messageContext.message("core.command.notranslation_transform", transformException.getMessage());
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
        CommandGroup group = application.getGroup(appendArgs(label, args));
        CommandContext command = group.getCommand();
        if (command == null) return Collections.emptyList();

        prepareMessageContext(sender, null);
        
        messageContext.message("core.command.notab");
        return Collections.emptyList();
    }

}
