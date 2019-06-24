package io.github.splotycode.mosaik.spigot.transformer;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.valuetransformer.TransformException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationTransformer extends ValueTransformer<String, Long> {

    private static final Pattern PATTERN = Pattern.compile("([1-9][0-9]*)([a-zA-Z])");

    @Override
    public Long transform(String input, DataFactory dataFactory) throws Exception {
        Matcher matcher = PATTERN.matcher(input);
        if (matcher.find()) {
            int time = Integer.valueOf(matcher.group(1));
            String id = matcher.group(2);
            switch (id) {
                case "s":
                    return time * 1000L;
                case "m":
                    return time * 1000L * 60L;
                case "h":
                    return time * 1000L * 60L * 60L;
                case "d":
                    return time * 1000L * 60L * 60L * 24L;
                case "w":
                    return time * 1000L * 60L * 60L * 24L * 7L;
                case "M":
                    return time * 1000L * 60L * 60L * 24L * 30L;
                case "y":
                    return time * 1000L * 60L * 60L * 24L * 365L;
                default:
                    throw new TransformException("Invalid time id");
            }
        } else {
            throw new TransformException("Invalid time format");
        }
    }
}
