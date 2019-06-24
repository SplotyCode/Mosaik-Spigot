package io.github.splotycode.mosaik.spigot.exception;

public class CommandExcpetion extends RuntimeException {

    public CommandExcpetion() {
    }

    public CommandExcpetion(String message) {
        super(message);
    }

    public CommandExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExcpetion(Throwable cause) {
        super(cause);
    }

    public CommandExcpetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
