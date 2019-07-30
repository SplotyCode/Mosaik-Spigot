package io.github.splotycode.mosaik.spigot.command.annotation.parameter;

import io.github.splotycode.mosaik.spigot.command.CommandContext;
import io.github.splotycode.mosaik.spigot.command.CommandRedirect;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.AnnotatedParameterResolver;

import java.lang.reflect.Parameter;

public class ArgParameterResolver extends AnnotatedParameterResolver<Arg, Object, CommandContext> {

    public ArgParameterResolver() {
        super(Arg.class);
    }

    @Override
    protected Object transformAnnotation(CommandContext context, Arg arg, Parameter parameter, DataFactory dataFactory) {
        try {
            return context.parameterValue(parameter, dataFactory.getData(CommandRedirect.ARGS)[arg.value()]);
        } catch (IndexOutOfBoundsException ex) {
            if (arg.required()) {
                dataFactory.getData(CommandRedirect.MESSAGE_CONTEXT).throwUsage("core.command.nopara", arg.value() + 1);
            }
            return null;
        }
    }
}
