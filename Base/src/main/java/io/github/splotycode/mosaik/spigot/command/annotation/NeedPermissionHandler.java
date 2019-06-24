package io.github.splotycode.mosaik.spigot.command.annotation;

import io.github.splotycode.mosaik.spigot.command.CommandContext;
import io.github.splotycode.mosaik.spigot.command.CommandData;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;

public class NeedPermissionHandler implements AnnotationHandler<CommandContext, NeedPermission, CommandData> {

    @Override
    public Class<? extends NeedPermission> annotation() {
        return NeedPermission.class;
    }

    @Override
    public void init(CommandContext context, NeedPermission permission, CommandData data) throws Exception {
        data.setPermission(permission.value());
    }

    @Override
    public void preCall(CommandContext context, NeedPermission needPermission, CommandData data) throws Exception {

    }

    @Override
    public void postCall(CommandContext context, NeedPermission needPermission, CommandData data) throws Exception {

    }
}
