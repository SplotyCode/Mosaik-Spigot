package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.command.annotation.DescriptionHandler;
import io.github.splotycode.mosaik.spigot.command.annotation.NeedPermissionHandler;
import io.github.splotycode.mosaik.spigot.command.annotation.parameter.ArgParameterResolver;
import io.github.splotycode.mosaik.spigot.command.annotation.parameter.MessageParameterResolver;
import io.github.splotycode.mosaik.spigot.command.annotation.parameter.SenderParameterResolver;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.SingleAnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.valuetransformer.CommonData;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CommandContext extends SingleAnnotationContext<CommandContext, CommandData> {

    @Override
    protected Collection<ParameterResolver> additionalParameterResolver() {
        return new ArrayList<>(Arrays.asList(new ArgParameterResolver(), new MessageParameterResolver(),
                new SenderParameterResolver()));
    }

    @Override
    public Object rawTransform(String input, Class<?> clazz, Collection<ValueTransformer> transformers) {
        DataFactory info = new DataFactory();
        info.putData(CommonData.TRANSLATION_PREFIX, "core.command.transformers.");
        info.putData(CommonData.I18N, data().getGroup().getApplication().getI18N());
        return TransformerManager.getInstance().transformWithAdditional(info, input, clazz, transformers);
    }

    @Override
    protected Collection<ValueTransformer> additionalTransformers() {
        return super.additionalTransformers();
    }

    @Override
    public Collection<AnnotationHandler<CommandContext, ? extends Annotation, CommandData>> getAnnotationHandlers() {
        return new ArrayList<>(Arrays.asList(new DescriptionHandler(), new NeedPermissionHandler()));
    }

    @Override
    public Class<? extends CommandData> elementClass() {
        return CommandData.class;
    }
}
