import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PhotoComponent extends JComponent {
    private Image image;
    public boolean isTurned;
    private List<Point> drawnStrokes;
    private List<Character> typedCharacters;
    private List<Point> typedCharPositions;
    private Point typingClickedPosition;
    private boolean isTyping = false;

    private boolean showAnnotations = false;

    private List<Annotation> annotations;
    private Annotation currentAnnotation;
    private Annotation selectedAnnotation;
    private Point lastDragPoint;
    private boolean isDrawing = false;

    private Color selectedColor = Color.BLACK;

    public PhotoComponent(Image image) {
        this.image = image;
        this.isTurned = false;
        this.drawnStrokes = new ArrayList<>();
        this.typedCharacters = new ArrayList<>();
        this.typedCharPositions = new ArrayList<>();

        this.annotations = new ArrayList<>();
        this.currentAnnotation = null;

        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showAnnotations = !showAnnotations;
//                    isTurned = !isTurned;
//                    isTyping = false;
                    repaint();
                    System.out.println("Annotation is " +  showAnnotations);
//                } else if (isTurned) {
//                    isTyping = true;
//                    typingClickedPosition = e.getPoint();
//                    repaint();
//                    requestFocusInWindow();
                } else if (e.getClickCount() == 1) {
                    selectedAnnotation = findAnnotationAt(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // check if clicked on annotation
                selectedAnnotation = findAnnotationAt(e.getPoint());
                if (selectedAnnotation != null) {
                    lastDragPoint = e.getPoint();  // prepare to move
                } else {
                    // start drawing
                    if (showAnnotations) {
                        currentAnnotation = new Annotation(new ArrayList<>(), Color.BLACK);
                        currentAnnotation.getPoints().add(e.getPoint());
                        isDrawing = true;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDrawing && currentAnnotation != null) {
                    // stop drawing and add annotation to the list
                    annotations.add(currentAnnotation);
                    currentAnnotation = null;
                    isDrawing = false;
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                if (isTurned) {
//                    drawnStrokes.add(e.getPoint());
//                    repaint();
//                }
                if (isDrawing && currentAnnotation != null) {
                    // drawing
                    currentAnnotation.getPoints().add(e.getPoint());
                    repaint();
                } else if (showAnnotations && selectedAnnotation != null) {
                    // move annotation
                    int dx = e.getX() - lastDragPoint.x;
                    int dy = e.getY() - lastDragPoint.y;
                    selectedAnnotation.move(dx, dy);
                    lastDragPoint = e.getPoint();
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (isTyping && isTurned) {
                    char keyChar = e.getKeyChar();
                    if (keyChar == KeyEvent.VK_BACK_SPACE) {
                        if (!typedCharacters.isEmpty()) {
                            typedCharacters.remove(typedCharacters.size() - 1);
                            typedCharPositions.remove(typedCharPositions.size() - 1);
                            updateTypingClickedPositionAfterBackspace();
                        }
                    } else {
                        typedCharacters.add(keyChar);
                        typedCharPositions.add(new Point(typingClickedPosition.x, typingClickedPosition.y));
                        updateTypingClickedPosition(keyChar);
                    }
                    repaint();
                }
            }
        });

//        setPreferredSize(new Dimension(400, 400));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        if (showAnnotations) {
            // draw all prev annotations
            for (Annotation annotation : annotations) {
                annotation.draw(g2d);
            }
            // draw current annotation if exists
            if (currentAnnotation != null) {
                currentAnnotation.draw(g2d);
            }
            // highlight selected annotation
            if (selectedAnnotation != null) {
                g2d.setColor(Color.BLUE);
                Rectangle bounds = selectedAnnotation.getBounds();
                g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    private void drawAnnotations(Graphics2D g2d) {
        draw(g2d);
        typeText(g2d);
    }

    private void typeText(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();

        for (int i = 0; i < typedCharacters.size(); i++) {
            char c = typedCharacters.get(i);
            Point pos = typedCharPositions.get(i);
            g2d.drawString(String.valueOf(c), pos.x, pos.y);
        }
    }

    private void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);

        for (Point point : drawnStrokes) {
            g2d.fillOval(point.x, point.y, 5, 5);
        }
    }

    private Annotation findAnnotationAt(Point p) {
        for (Annotation annotation : annotations) {
            if (annotation.contains(p)) {
                return annotation;
            }
        }
        return null;
    }

    private void updateTypingClickedPosition(char typedChar) {
        FontMetrics fm = getFontMetrics(getFont());
        int charWidth = fm.charWidth(typedChar);

        typingClickedPosition.x += charWidth;

        if (typingClickedPosition.x + charWidth > getWidth() - 20) {
            typingClickedPosition.x = 10;
            typingClickedPosition.y += fm.getHeight();
        }
    }

    private void updateTypingClickedPositionAfterBackspace() {
        if (!typedCharPositions.isEmpty()) {
            Point lastPosition = typedCharPositions.get(typedCharPositions.size() - 1);
            typingClickedPosition = new Point(lastPosition.x + 7, lastPosition.y); // number 7 helps avoiding letters to overlap
        }
    }
}
