package io.github.splotycode.mosaik.spigot.exception;

public class GuiLoadException extends RuntimeException {

    public GuiLoadException() {
    }

    public GuiLoadException(String message) {
        super(message);
    }

    public GuiLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuiLoadException(Throwable cause) {
        super(cause);
    }

    public GuiLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
