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
            storeCurrentPhotoComponent(image);
        }

        photoPanel.revalidate();
        photoPanel.repaint();
    }

    private void storeCurrentPhotoComponent(ImageIcon image) {
        currentPhotoComponent = new PhotoComponent(image.getImage());
        currentPhotoComponent.requestFocusInWindow();
        photoPanel.add(currentPhotoComponent);
    }

    public PhotoComponent getCurrentPhotoComponent() {
        return currentPhotoComponent;
    }
}