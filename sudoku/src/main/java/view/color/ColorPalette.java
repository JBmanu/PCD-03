package view.color;

import java.awt.*;

public interface ColorPalette {

    static ColorPalette light() {
        return new ColorPaletteImpl(
                Color.decode("#1F90FF"),
                Color.decode("#D3FBD8"),
                Color.decode("#2C3650"),
                Color.decode("#FF0000"),
                Color.decode("#00FF00"));
    }

    static ColorPalette dark() {
        return new ColorPaletteImpl(
                Color.decode("#1F90FF"),
                Color.decode("#D3FBD8"),
                Color.decode("#2C3650"),
                Color.decode("#FF0000"),
                Color.decode("#00FF00"));
    }


    Color primary();

    Color secondary();

    Color neutral();

    Color feedback();

    Color interaction();


    record ColorPaletteImpl(Color primary, Color secondary, Color neutral,
                            Color feedback, Color interaction) implements ColorPalette {
    }

}
