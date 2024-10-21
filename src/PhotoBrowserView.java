import javax.swing.*;
import java.util.List;
import java.awt.*;

public class PhotoBrowserView {
    private JPanel photoPanel;
    private PhotoComponent currentPhotoComponent;

    public PhotoBrowserView(JPanel photoPanel) {
        this.photoPanel = photoPanel;
    }

    public void updatePhotos(List<String> photoPaths) {
        photoPanel.removeAll();
        for (String path : photoPaths) {
            ImageIcon image = new ImageIcon(path);
//            PhotoComponent photoComponent = new PhotoComponent(image.getImage());
//            photoComponent.requestFocusInWindow();
            currentPhotoComponent = new PhotoComponent(image.getImage()); // Store current photo component
            currentPhotoComponent.requestFocusInWindow();
            photoPanel.add(currentPhotoComponent);
        }
        photoPanel.revalidate();
        photoPanel.repaint();
    }
    public PhotoComponent getCurrentPhotoComponent() {
        return currentPhotoComponent;
    }
}