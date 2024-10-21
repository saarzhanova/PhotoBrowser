import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Annotation {
    private List<Point> points;
    private Color color;

    public Annotation(List<Point> points, Color color) {
        this.points = new ArrayList<>(points);
        this.color = color;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        for (Point point : points) {
            g2d.fillOval(point.x, point.y, 5, 5);
        }
    }

    public void move(int dx, int dy) {
        for (Point point : points) {
            point.x += dx;
            point.y += dy;
        }
    }

    public boolean contains(Point p) {
        for (Point point : points) {
            if (p.distance(point) < 5) {
                return true;
            }
        }
        return false;
    }
}