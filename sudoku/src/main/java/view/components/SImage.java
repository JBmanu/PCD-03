package view.components;

import javax.swing.*;
import java.awt.*;

public class SImage extends JLabel {
    private static final int DEFAULT_SPACE = 50;
    private final Dimension imageDimension;
    private final String path;
    private int padding;

    public SImage(final String path, final Dimension dimension) {
        super();
        this.padding = DEFAULT_SPACE;
        this.imageDimension = new Dimension();
        this.setImageDimension(dimension);
        this.path = path;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }

    public void setImageDimension(final Dimension imageDimension) {
        this.setPreferredSize(imageDimension);
        this.imageDimension.setSize(new Dimension(imageDimension.width - this.padding, imageDimension.height - this.padding));
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension dimension = this.getSize();
        final int x = (dimension.width - this.imageDimension.width) / 2;
        final int y = (dimension.height - this.imageDimension.height) / 2;
        final Image backgroundImage = new ImageIcon(ClassLoader.getSystemResource(this.path)).getImage();
        g2.drawImage(backgroundImage, x, y, this.imageDimension.width, this.imageDimension.height, this);
    }

}
