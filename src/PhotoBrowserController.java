public class PhotoBrowserController {
    private PhotoBrowserModel model;
    private PhotoBrowserView view;

    public PhotoBrowserController(PhotoBrowserModel model, PhotoBrowserView view) {
        this.model = model;
        this.view = view;
    }

    public void importPhoto(String path) {
        model.addPhoto(path);
        view.updatePhotos(model.getPhotos());
    }
}
