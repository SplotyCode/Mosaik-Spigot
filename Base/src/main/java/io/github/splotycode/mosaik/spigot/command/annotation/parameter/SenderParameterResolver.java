package io.github.splotycode.mosaik.spigot.command.annotation.parameter;

import io.github.splotycode.mosaik.spigot.command.CommandContext;
import io.github.splotycode.mosaik.spigot.command.CommandRedirect;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;

public class SenderParameterResolver implements ParameterResolver<CommandSender, CommandContext> {
    @Override
    public boolean transformable(CommandContext context, Parameter parameter) {
        return CommandSender.class.isAssignableFrom(parameter.getType()) && parameter.getAnnotations().length == 0;
    }

    @Override
    public CommandSender transform(CommandContext context, Parameter parameter, DataFactory dataFactory) throws ParameterResolveException {
        CommandSender sender = dataFactory.getData(CommandRedirect.SENDER);
        if (parameter.getType().isAssignableFrom(sender.getClass())) {
            return sender;
        }
        throw new ParameterResolveException();
    }
}
