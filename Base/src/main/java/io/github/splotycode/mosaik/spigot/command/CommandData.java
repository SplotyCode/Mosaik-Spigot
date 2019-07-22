package io.github.splotycode.mosaik.spigot.command;

import io.github.splotycode.mosaik.spigot.command.annotation.parameter.Arg;
import io.github.splotycode.mosaik.util.reflection.annotation.data.DefaultMethodData;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CommandData extends DefaultMethodData {

    private List<String> aliases = new ArrayList<>();
    @Setter private String usage = "", description = "";
    @Setter private String permission;
    private HashMap<Integer, Class<?>> fields = new HashMap<>();

    @Override
    public void buildData(Annotation[] annotations) {
        super.buildData(annotations);
        for (Class<?> parameter : getMethod().getParameterTypes()) {
            Arg arg = parameter.getAnnotation(Arg.class);
            if (arg != null) {
                fields.put(arg.value(), parameter);
            }
        }
    }
}
