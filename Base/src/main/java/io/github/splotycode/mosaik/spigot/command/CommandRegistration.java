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
            boolean sup = !StringUtil.isEmpty(basePath);
            for (Method method : ReflectionUtil.getAllMethods(clazz)) {
                Command command = method.getAnnotation(Command.class);
                if (command != null) {
                    String path = sup ? (basePath + " ") : "" + command.value();
                    CommandContext context = new CommandContext();
                    context.feed(method, obj);
                    application.getCommandHead().group(path).register(context, !sup);
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
