package view.color;

import java.awt.*;

public interface Colorable {

    static Colorable create(final Color text, final Color hoverText, final Color background,
                            final Color hover, final Color disabled, final Color onClick) {
        return new ColorableImpl(text, hoverText, background, hover, disabled, onClick);
    }


    Color text();

    Color hoverText();

    Color background();

    Color hover();

    Color disabled();

    Color onClick();


    record ColorableImpl(Color text, Color hoverText, Color background, Color hover, Color disabled, Color onClick)
            implements Colorable {
    }

}
