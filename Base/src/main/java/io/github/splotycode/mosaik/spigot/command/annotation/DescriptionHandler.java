package io.github.splotycode.mosaik.spigot.command.annotation;

import io.github.splotycode.mosaik.spigot.command.CommandContext;
import io.github.splotycode.mosaik.spigot.command.CommandData;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class DescriptionHandler implements AnnotationHandler<CommandContext, Description, CommandData> {

    @Override
    public Class<? extends Description> annotation() {
        return Description.class;
    }

    @Override
    public void init(CommandContext context, Description description, CommandData data) throws Exception {
        if (!StringUtil.isEmpty(description.description())) {
            data.setDescription(description.description());
        }
        data.getAliases().addAll(Arrays.asList(description.aliases()));
    }

    @Override
    public void preCall(CommandContext commandContext, Description description, CommandData commandData) throws Exception {

    }

    @Override
    public void postCall(CommandContext commandContext, Description description, CommandData commandData) throws Exception {

    }
}
