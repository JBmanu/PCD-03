package view.color;

import java.awt.*;

public interface Palette {
    
    private static Color alpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    } 

    static Palette light() {
        return new PaletteImpl(
                Color.decode("#1D5DDE"),
                Color.decode("#27282D"),
                Color.decode("#FCFDFE"),
                Color.decode("#8AAFFF"),
                Color.decode("#2859BE"));
    }

    static Palette dark() {
        return new PaletteImpl(
                Color.decode("#2489FF"),
                Color.decode("#FCFDFE"),
                Color.decode("#14181B"),
                Color.decode("#2859BE"),
                Color.decode("#8AAFFF"));
    }


    Color primary();

    Color secondary();

    Color neutral();

    Color feedback();

    Color interaction();
    
    Color primaryWithAlpha(int alpha);
    
    Color secondaryWithAlpha(int alpha);
    
    Color neutralWithAlpha(int alpha);
    
    Color feedbackWithAlpha(int alpha);
    
    Color interactionWithAlpha(int alpha);


    record PaletteImpl(Color primary, Color secondary, Color neutral,
                       Color feedback, Color interaction) implements Palette {
        @Override
        public Color primaryWithAlpha(final int alpha) {
            return Palette.alpha(this.primary, alpha);
        }
        @Override
        public Color secondaryWithAlpha(final int alpha) {
            return Palette.alpha(this.secondary, alpha);
        }
        @Override
        public Color neutralWithAlpha(final int alpha) {
            return Palette.alpha(this.neutral, alpha);
        }
        @Override
        public Color feedbackWithAlpha(final int alpha) {
            return Palette.alpha(this.feedback, alpha);
        }
        @Override
        public Color interactionWithAlpha(final int alpha) {
            return Palette.alpha(this.interaction, alpha);
        }
    }

}
