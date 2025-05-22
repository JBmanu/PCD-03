package view.color;

import java.awt.*;

public interface Palette {

    static Palette light() {
        return new PaletteImpl(
                Color.decode("#1F90FF"),
                Color.decode("#D3FBD8"),
                Color.decode("#2C3650"),
                Color.decode("#FF0000"),
                Color.decode("#00FF00"));
    }

    static Palette dark() {
        return new PaletteImpl(
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


    record PaletteImpl(Color primary, Color secondary, Color neutral,
                       Color feedback, Color interaction) implements Palette {
    }

}
