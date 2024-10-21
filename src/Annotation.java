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

    public Rectangle getBounds() {
        if (points.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);  // Return an empty rectangle if there are no points
        }

        // Initialize min and max values with the first point
        int minX = points.get(0).x;
        int minY = points.get(0).y;
        int maxX = points.get(0).x;
        int maxY = points.get(0).y;

        // Iterate over all points to find the min and max coordinates
        for (Point point : points) {
            if (point.x < minX) {
                minX = point.x;
            }
            if (point.y < minY) {
                minY = point.y;
            }
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
        }

        // Create and return the rectangle that bounds the annotation
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}