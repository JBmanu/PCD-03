package ex1.inspector.road;

import ex1.utils.ViewUtils;

import javax.swing.*;
import java.text.DecimalFormat;

public class RoadStatisticView extends JPanel {
    private static final int HGAP = 10;
    private static final String CURRENT_AVERAGE_SPEED = "Current average Speed: ";
    private static final String MAX_SPEED = "Max Speed: ";
    private static final String MIN_SPEED = "Min Speed: ";

    private final JLabel averageSpeedLabel;
    private final JLabel maxSpeedLabel;
    private final JLabel minSpeedLabel;
    private final BoxLayout layoutManager;

    public RoadStatisticView() {
        this.averageSpeedLabel = new JLabel();
        this.maxSpeedLabel = new JLabel();
        this.minSpeedLabel = new JLabel();
        this.layoutManager = new BoxLayout(this, BoxLayout.Y_AXIS);

        this.setLayout(this.layoutManager);
        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);

        this.add(this.averageSpeedLabel);
        this.add(this.maxSpeedLabel);
        this.add(this.minSpeedLabel);
    }

    public void updateStatistics(final RoadSimStatistics roadStatistics) {
        final DecimalFormat format = new DecimalFormat("#.####");
        this.averageSpeedLabel.setText(CURRENT_AVERAGE_SPEED + format.format(roadStatistics.currentAverageSpeed()));
        this.maxSpeedLabel.setText(MAX_SPEED + roadStatistics.maxSpeed());
        this.minSpeedLabel.setText(MIN_SPEED + roadStatistics.minSpeed());
    }




}
