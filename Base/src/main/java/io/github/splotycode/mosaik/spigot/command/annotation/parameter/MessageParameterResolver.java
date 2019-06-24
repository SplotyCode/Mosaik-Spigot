package io.github.splotycode.mosaik.spigot.command.annotation.parameter;

import io.github.splotycode.mosaik.spigot.command.CommandContext;
import io.github.splotycode.mosaik.spigot.command.CommandRedirect;
import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.i18n.MessageContext;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;

import java.lang.reflect.Parameter;

public class MessageParameterResolver implements ParameterResolver<SpigotMessageContext, CommandContext> {
    @Override
    public boolean transformable(CommandContext context, Parameter parameter) {
        return MessageContext.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public SpigotMessageContext transform(CommandContext context, Parameter parameter, DataFactory dataFactory) throws ParameterResolveException {
        return dataFactory.getData(CommandRedirect.MESSAGE_CONTEXT);
    }
}
