import java.util.ArrayList;
import java.util.List;

public class PhotoBrowserModel {
    private List<String> photos;

    public PhotoBrowserModel() {
        photos = new ArrayList<>();
    }

    public void addPhoto(String path) {
        photos.add(path);
    }

    public List<String> getPhotos() {
        return photos;
    }
}
