package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.SpigotApplicationType;
import io.github.splotycode.mosaik.spigot.command.annotation.Command;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collection;

@AllArgsConstructor
public class CommandRegistration implements ClassRegister<Object> {

    private SpigotApplicationType application;

    @Override
    public void register(Object obj) {
        Class<?> clazz = obj.getClass();
        Command base = clazz.getAnnotation(Command.class);
        if (base != null) {
            String basePath = base.value();
            CommandGroup baseGroup = application.getCommandHead();
            boolean hasBase = !StringUtil.isEmpty(basePath);
            if (hasBase) {
                baseGroup = baseGroup.group(basePath);
                baseGroup.register(null, true);
            }

            for (Method method : ReflectionUtil.getAllMethods(clazz)) {
                Command command = method.getAnnotation(Command.class);
                if (command != null) {
                    CommandContext context = new CommandContext();
                    context.feed(method, obj);
                    if (StringUtil.isEmpty(command.value())) {
                        if (hasBase) {
                            context.data().setGroup(baseGroup);
                            baseGroup.setCommand(context);
                            context.data().buildUsage();
                        } else throw new IllegalStateException("Need base or sub command string");
                    } else {
                        CommandGroup group = baseGroup.group(command.value());
                        context.data().setGroup(group);
                        group.register(context, !hasBase);
                    }
                }
            }
        }
    }

    @Override
    public void unRegister(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getObjectClass() {
        return Object.class;
    }
}
