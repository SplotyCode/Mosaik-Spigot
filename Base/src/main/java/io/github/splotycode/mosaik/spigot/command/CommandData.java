package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.command.annotation.Description;
import io.github.splotycode.mosaik.spigot.command.annotation.NeedPermission;
import io.github.splotycode.mosaik.util.reflection.annotation.data.DefaultMethodData;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandData extends DefaultMethodData {

    private List<String> aliases = new ArrayList<>();
    @Setter private String usage = "", description = "";
    @Setter private String permission;

    @Override
    public void buildData(Annotation[] annotations) {
        super.buildData(annotations);
        Class<?> clazz = getMethod().getDeclaringClass();
        Description description = clazz.getAnnotation(Description.class);
        if (description != null) {
            this.description = description.description();
            aliases.addAll(Arrays.asList(description.aliases()));
        }
        NeedPermission permission = clazz.getAnnotation(NeedPermission.class);
        if (permission != null) {
            this.permission = permission.value();
        }
    }
}
