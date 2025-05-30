package view.color;

import java.awt.*;
import java.util.Optional;

public interface Colorable {

    static Colorable create(final Color text, final Color hoverText, final Color disableText, final Color background,
                            final Color hover, final Color click, final Color disabled) {
        final Background bg = createBackground(background, hover, click, disabled);
        final Text tx = createText(text, hoverText, disableText);
        return new ColorableImpl(bg, tx);
    }

    static Colorable test() {
        return create(Color.black, Color.white, Color.black, Color.cyan,
                Color.blue, Color.magenta, Color.gray);
    }
    
    static Colorable createMainButton(final Palette palette) {
        final Colorable.Background background = Colorable.createBackground(
                palette.primary(),
                palette.primaryWithAlpha(150),
                palette.interaction(),
                palette.secondaryWithAlpha(50));

        final Colorable.Text text = Colorable.createText(
                palette.neutral(),
                palette.neutral(),
                palette.neutral());
        return new ColorableImpl(background, text);
    }

    private static Background createBackground(final Color base, final Color hover, final Color click,
                                       final Color disabled) {
        return new Background(Optional.ofNullable(base), Optional.ofNullable(hover),
                Optional.ofNullable(click), Optional.ofNullable(disabled));
    }

    private static Text createText(final Color base, final Color hover, final Color disable) {
        return new Text(Optional.ofNullable(base), Optional.ofNullable(hover), Optional.ofNullable(disable));
    }


    Optional<Color> currentText();

    Optional<Color> currentBackground();

    Background background();

    Text text();

    void setBackground(Background background);

    void setText(Text text);

    void setDefault();

    void setHover();

    void setClick();

    void setDisabled();


    record Background(Optional<Color> background, Optional<Color> hover, Optional<Color> click,
                      Optional<Color> disabled) {
    }

    record Text(Optional<Color> text, Optional<Color> action, Optional<Color> disabled) {
    }

    final class ColorableImpl implements Colorable {
        private Background background;
        private Text text;

        private Optional<Color> currentBackground;
        private Optional<Color> currentText;

        public ColorableImpl(final Background background, final Text text) {
            this.background = background;
            this.text = text;
            this.setDefault();
        }

        public Optional<Color> currentBackground() {
            return this.currentBackground;
        }

        @Override
        public Background background() {
            return this.background;
        }

        @Override
        public Text text() {
            return this.text;
        }

        @Override
        public void setBackground(final Background background) {
            this.background = background;
            this.setDefault();
        }

        @Override
        public void setText(final Text text) {
            this.text = text;
            this.setDefault();
        }

        public Optional<Color> currentText() {
            return this.currentText;
        }

        public void setDefault() {
            this.currentBackground = this.background.background;
            this.currentText = this.text.text;
        }

        public void setHover() {
            this.currentBackground = this.background.hover;
            this.currentText = this.text.action;
        }

        public void setClick() {
            this.currentBackground = this.background.click;
            this.currentText = this.text.action;
        }

        public void setDisabled() {
            this.currentBackground = this.background.disabled;
            this.currentText = this.text.disabled;
        }
    }

}
