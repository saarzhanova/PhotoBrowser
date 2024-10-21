import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PhotoComponent extends JComponent {
    private Image image;
    private boolean isShowAnnotations = false;
    private List<Annotation> annotations;
    private Annotation currentAnnotation;
    private Annotation selectedAnnotation;
    private Point lastDragPoint;
    private boolean isDrawing = false;

    public PhotoComponent(Image image) {
        this.image = image;
        this.annotations = new ArrayList<>();
        this.currentAnnotation = null;

        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        setPreferredSize(new Dimension(imageWidth, imageHeight));
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int numberOfClicks = e.getClickCount();
                if (numberOfClicks == 2) {
                    switchAnnotationsDemonstration();
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                selectedAnnotation = findAnnotationAt(e.getPoint());
                boolean isClickedOnAnnotation = selectedAnnotation != null;

                if (isClickedOnAnnotation) {
                    prepareToMoveFrom(e);
                } else if (isShowAnnotations) {
                    startDrawingOn(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean isCurrentlyDrawing = isDrawing && currentAnnotation != null;

                if (isCurrentlyDrawing) {
                    annotations.add(currentAnnotation);
                    stopDrawing();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                boolean isCurrentlyDrawing = isDrawing && currentAnnotation != null;
                boolean isAnnotationSelected = isShowAnnotations && selectedAnnotation != null;

                if (isCurrentlyDrawing) {
                    saveDrawingPoints(e);
                    repaint();
                } else if (isAnnotationSelected) {
                    moveAnnotationTo(e);
                    prepareToMoveFrom(e);
                    repaint();
                }
            }
        });
    }

    private void switchAnnotationsDemonstration() {
        isShowAnnotations = !isShowAnnotations;
    }

    private Annotation findAnnotationAt(Point p) {
        for (Annotation annotation : annotations) {
            if (annotation.contains(p)) {
                return annotation;
            }
        }
        return null;
    }

    private void prepareToMoveFrom(MouseEvent e) {
        lastDragPoint = e.getPoint();
    }

    private void startDrawingOn(MouseEvent e) {
        currentAnnotation = new Annotation(new ArrayList<>(), Color.BLACK);
        currentAnnotation.getPoints().add(e.getPoint());
        isDrawing = true;
    }

    private void saveDrawingPoints(MouseEvent e) {
        currentAnnotation.getPoints().add(e.getPoint());
    }

    private void stopDrawing() {
        currentAnnotation = null;
        isDrawing = false;
    }

    private void moveAnnotationTo(MouseEvent e) {
        int dx = e.getX() - lastDragPoint.x;
        int dy = e.getY() - lastDragPoint.y;
        selectedAnnotation.move(dx, dy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        if (isShowAnnotations) {
            boolean isCurrentlyDrawing = currentAnnotation != null;
            boolean isAnnotationSelected = selectedAnnotation != null;

            drawAllPreviousAnnotations(g2d);
            if (isCurrentlyDrawing) drawCurrentAnnotation(g2d);
            if (isAnnotationSelected) highlightSelectedAnnotation(g2d);
        }
    }

    private void drawAllPreviousAnnotations(Graphics2D g2d) {
        for (Annotation annotation : annotations) {
            annotation.draw(g2d);
        }
    }

    private void drawCurrentAnnotation(Graphics2D g2d) {
        currentAnnotation.draw(g2d);
    }

    private void highlightSelectedAnnotation(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        Rectangle bounds = selectedAnnotation.getBounds();
        g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Annotation getSelectedAnnotation() {
        return selectedAnnotation;
    }

    public void changeSelectedAnnotationColor(Color newColor) {
        boolean isAnnotationSelected = selectedAnnotation != null;

        if (isAnnotationSelected) {
            selectedAnnotation.setColor(newColor);
            repaint();
        }
    }
}
