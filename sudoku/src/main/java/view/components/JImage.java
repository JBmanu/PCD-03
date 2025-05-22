package view.components;

import javax.swing.*;
import java.awt.*;

public class JImage extends JLabel {
    private static final int DEFAULT_SPACE = 50;
    private final Dimension imageDimension;
    private final String path;
    private int space;

    public JImage(final String path) {
        this(path, new Dimension(100, 100));
    }

    public JImage(final String path, final Dimension dimension) {
        super();
        this.space = DEFAULT_SPACE;
        this.imageDimension = new Dimension();
        this.setImageDimension(dimension);
        this.path = path;
    }

    public void setSpace(final int space) {
        this.space = space;
    }

    public void setImageDimension(final Dimension imageDimension) {
        this.setPreferredSize(imageDimension);
        this.imageDimension.setSize(new Dimension(imageDimension.width - this.space, imageDimension.height - this.space));
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
