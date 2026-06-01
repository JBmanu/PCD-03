package ui.color;

import java.awt.*;

public interface GenerateColor {
    
    static Color from(final int index) {
        final float hue = (float) ((index * 0.618033988749895) % 1.0);
        final float saturation = 0.75f;
        final float brightness = 0.95f;
        return Color.getHSBColor(hue, saturation, brightness);
    }
    
}
