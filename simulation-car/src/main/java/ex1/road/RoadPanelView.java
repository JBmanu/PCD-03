package ex1.road;

import ex1.car.CarAgentInfo;
import ex1.road.core.V2d;
import ex1.road.trafficLight.TrafficLight;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static ex1.utils.ViewUtils.CAR_SIZE;

public class RoadPanelView extends JPanel {

    List<CarAgentInfo> cars;
    List<Road> roads;
    List<TrafficLight> sems;

    public RoadPanelView(final int width, final int height){
        this.setSize(width, height);
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0,0,this.getWidth(),this.getHeight());

        if (this.roads != null) {
            for (final var r: this.roads) {
                g2.drawLine((int)r.getFrom().x(), (int)r.getFrom().y(), (int)r.getTo().x(), (int)r.getTo().y());
            }
        }

        if (this.sems != null) {
            for (final var s: this.sems) {
                if (s.isGreen()) {
                    g.setColor(new Color(0, 255, 0, 255));
                } else if (s.isRed()) {
                    g.setColor(new Color(255, 0, 0, 255));
                } else {
                    g.setColor(new Color(255, 255, 0, 255));
                }
                g2.fillRect((int)(s.getPos().x()-5), (int)(s.getPos().y()-5), 10, 10);
            }
        }

        g.setColor(new Color(0, 0, 0, 255));

        if (this.cars != null) {
            for (final var c: this.cars) {
                final double pos = c.getPos();
                final Road r = c.getRoad();
                final V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);

                g2.drawOval((int)(r.getFrom().x() + dir.x() - CAR_SIZE/2), (int)(r.getFrom().y() + dir.y() - CAR_SIZE/2), CAR_SIZE , CAR_SIZE);
            }
        }
    }

    public void update(final List<Road> roads,
                       final List<CarAgentInfo> cars,
                       final List<TrafficLight> sems) {
        this.roads = roads;
        this.cars = cars;
        this.sems = sems;
        this.repaint();
    }
}