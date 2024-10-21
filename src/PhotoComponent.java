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

    public PhotoComponent(Image image) {
        this.image = image;
        this.isTurned = false;
        this.drawnStrokes = new ArrayList<>();
        this.typedCharacters = new ArrayList<>();
        this.typedCharPositions = new ArrayList<>();

        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showAnnotations = !showAnnotations;
                    isTurned = !isTurned;
                    isTyping = false;
                    repaint();
                    System.out.println(isTurned);
                } else if (isTurned) {
                    isTyping = true;
                    typingClickedPosition = e.getPoint();
                    repaint();
                    requestFocusInWindow();
                }
                System.out.println(isTyping);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isTurned) {
                    drawnStrokes.add(e.getPoint());
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
            drawAnnotations(g2d);
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
