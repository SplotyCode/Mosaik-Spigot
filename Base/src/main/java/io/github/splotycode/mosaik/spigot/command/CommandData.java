package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.command.annotation.parameter.Arg;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.i18n.I18N;
import io.github.splotycode.mosaik.util.reflection.annotation.data.DefaultMethodData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CommandData extends DefaultMethodData {

    private List<String> aliases = new ArrayList<>();
    @Setter private String description = "";
    private String simpleUsage;
    @Setter private String permission;
    private HashMap<Integer, Parameter> fields = new HashMap<>();
    @Setter private CommandGroup group;

    @Override
    public void buildData(Annotation[] annotations) {
        super.buildData(annotations);
        for (Parameter parameter : getMethod().getParameters()) {
            Arg arg = parameter.getAnnotation(Arg.class);
            if (arg != null) {
                fields.put(arg.value(), parameter);
            }
        }
    }

    public void buildUsage() {
        I18N i18N = group.getApplication().getI18N();
        StringBuilder builder = new StringBuilder(group.commandAliasString(' '));

        int max = fields.keySet().stream().max(Integer::compareTo).orElse(-1);
        for (int i = 0; i < max + 1; i++) {
            builder.append(" <");
            Parameter parameter = fields.get(i);
            if (parameter == null) {
                builder.append(i18N.get("core.command.args.unknown"));
            } else {
                Arg arg = parameter.getAnnotation(Arg.class);
                if (StringUtil.isEmpty(arg.displayName())) {
                    if (OfflinePlayer.class.isAssignableFrom(parameter.getType())) {
                        if (Player.class.isAssignableFrom(parameter.getType())) {
                            builder.append(i18N.get("core.command.args.online"));
                        } else {
                            builder.append(i18N.get("core.command.args.player"));
                        }
                    } else {
                        builder.append(parameter.getName());
                    }

                } else {
                    String displayName = arg.displayName();
                    if (displayName.startsWith(".")) {
                        builder.append(i18N.get("arguments." + group.commandString('.') + displayName));
                    } else {
                        builder.append(displayName);
                    }
                }
            }
            builder.append('>');
        }
        simpleUsage = builder.toString();
    }

    public boolean hasPermission(CommandSender sender) {
        return StringUtil.isEmpty(permission) || sender.hasPermission(permission);
    }
}
