import javax.swing.*;
import java.util.List;
import java.awt.*;

public class PhotoBrowserView {
    private JPanel photoPanel;

    public PhotoBrowserView(JPanel photoPanel) {
        this.photoPanel = photoPanel;
    }

    public void updatePhotos(List<String> photoPaths) {
        photoPanel.removeAll();
        for (String path : photoPaths) {
            ImageIcon image = new ImageIcon(path);
            PhotoComponent photoComponent = new PhotoComponent(image.getImage());
            photoComponent.requestFocusInWindow();
            photoPanel.add(photoComponent);
        }
        photoPanel.revalidate();
        photoPanel.repaint();
    }
}