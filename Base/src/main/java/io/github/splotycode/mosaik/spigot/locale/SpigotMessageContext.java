package io.github.splotycode.mosaik.spigot.locale;

import io.github.splotycode.mosaik.spigot.exception.CommandExcpetion;
import io.github.splotycode.mosaik.spigot.permission.Permissions;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.i18n.I18N;
import io.github.splotycode.mosaik.util.i18n.MessageContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

public class SpigotMessageContext extends MessageContext {

    @Getter @Setter
    private boolean reuse = true;
    private CommandSender sender;
    @Setter @Getter private String usage;

    public SpigotMessageContext(I18N translator, CommandSender sender) {
        super(translator, "Not set", false);
        this.sender = sender;
    }

    public boolean hasTranslation(String key) {
        return translator.getMap().containsKey(key);
    }

    public void throwUsage(String message, Object... objects) {
        throwErrorRaw(translate(message, objects) + "\n" + translate("core.command.usage", usage));
    }

    public void throwUsage() {
        throwError("core.command.usage", usage);
    }

    public void reuse(CommandSender sender) {
        if (!reuse) throw new IllegalStateException("Reusing without reuse flag");
        this.sender = sender;
        replacements.clear();
    }

    public void message(CommandSender sender, String key, Object... objects) {
        sendMessage(sender, translate(key, objects));
    }

    protected void sendMessage(CommandSender sender, String message) {
        for (String line : message.split("\n")) {
            sender.sendMessage(line);
        }
    }

    public void message(String key, Object... objects) {
        message(sender, key, objects);
    }

    public void messageRaw(String message) {
        messageRaw(sender, message);
    }

    public void messageRaw(CommandSender sender, String message) {
        sendMessage(sender, transformRaw(message));
    }

    public boolean checkAccess(String permission) {
        if (!StringUtil.isEmpty(permission) && !sender.hasPermission(permission)) {
            if (sender.hasPermission(Permissions.DEBUG)) {
                message("core.access_debug", permission);
            } else {
                message("core.access_denied");
            }
            return false;
        }
        return true;
    }

    @Override
    protected RuntimeException getErrorException(String message) {
        return new CommandExcpetion(message);
    }
}
