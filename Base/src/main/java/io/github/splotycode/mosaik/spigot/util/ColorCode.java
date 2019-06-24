package io.github.splotycode.mosaik.spigot.util;

import lombok.Getter;

@Getter
public enum ColorCode {

    WHITE(0, "white", 0xFFe4e4e4),
    ORANGE(1, "orange", 0xFFea7e35),
    MAGENTA(2, "magenta", 0xFFbe49c9),
    LIGHT_BLUE(3, "light_blue", 0xFF6387d2),
    YELLOW(4, "yellow", 0xFFc2b51c),
    LIME(5, "lime", 0xFF39ba2e),
    PINK(6, "pink", 0xFFd98199),
    GRAY(7, "gray", 0xFF414141),
    LIGHT_GRAY(8, "light_gray", 0xFFa0a7a7),
    CYAN(9, "cyan", 0xFF267191),
    PURPLE(10, "purple", 0xFF7e34bf),
    BLUE(11, "blue", 0xFF253193),
    BROWN(12, "brown", 0xFF56331c),
    GREEN(13, "green", 0xFF364b18),
    RED(14, "red", 0xFF9e2b27),
    BLACK(15, "black", 0xFF181414);

    public static ColorCode nearest(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);

        int dist = -1;
        ColorCode current = null;
        for (ColorCode code : values()) {
            int cDist = Math.abs(r - code.red) + Math.abs(g - code.green) + Math.abs(b - code.blue);
            if (current == null || dist > cDist) {
                current = code;
                dist = cDist;
            }
        }
        return current;
    }

    private final int id;
    private final String color;
    private final int hex;
    private final int red, blue, green;

    ColorCode(int id, String color, int hex) {
        this.id = id;
        this.color = color;
        this.hex = hex;
        red = (hex & 0xFF0000) >> 16;
        green = (hex & 0xFF00) >> 8;
        blue = (hex & 0xFF);
    }

}
