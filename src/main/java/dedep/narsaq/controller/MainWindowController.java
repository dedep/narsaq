package dedep.narsaq.controller;

import com.google.inject.Inject;
import dedep.narsaq.photo.Camera;
import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.photo.overlay.PhotoOverlayService;
import dedep.narsaq.photo.scale.PhotoScale;
import dedep.narsaq.photo.storage.PhotoStorageService;
import dedep.narsaq.print.PrinterService;
import dedep.narsaq.properties.PropertiesService;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainWindowController implements Initializable {

    private static final String DELAY = "photos.delay";
    private static final String PHOTOS = "photos.count";
    private static final String RETRIES = "photos.max.retries";

    @FXML
    private ImageView imageView;
    @FXML
    private GridPane grid;
    @FXML
    private Label counterLabel;

    @Inject
    private Camera camera;
    @Inject
    private PrinterService printerService;
    @Inject
    private PhotoService photoService;
    @Inject
    private PhotoConcatener photoConcatener;
    @Inject
    private PropertiesService propertiesService;
    @Inject
    private PhotoOverlayService photoOverlayService;
    @Inject
    private PhotoScale photoScale;
    @Inject
    private PhotoStorageService photoStorageService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Optional<Path> lastPhoto = Optional.empty();
    private Boolean isRunning = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void liveStream() {
        Runnable r = () -> {
            camera.getCanonCamera().openSession();
            if (camera.getCanonCamera().beginLiveView()) {
                liveViewLoop();
            }
        };

        new Thread(r).start();
    }

    private void liveViewLoop() {
        while (isRunning) {
            final BufferedImage img = camera.getCanonCamera().downloadLiveView();
            if (img != null) {
                setBackground(img);
            }
        }

        setBackground(null);
        camera.getCanonCamera().closeSession();
    }

    @FXML
    private void onActionBtnClick(ActionEvent actionEvent) {
        startPhotoBoothSession();
    }

    private void reprintLastPhoto() {
        lastPhoto.ifPresent(printerService::print);
    }

    private void startPhotoBoothSession() {
        grid.setVisible(false);
        isRunning = true;
        liveStream();
        executeAction();
    }

    private void stopPhotoBoothAction() {
        grid.setVisible(true);
        isRunning = false;
    }

    private void setBackground(BufferedImage img) {
        Image image = null;
        if (img != null) {
            image = SwingFXUtils.toFXImage(img, null);
        }

        imageView.setImage(image);
    }

    private void executeAction() {
        Observable<Path> observable = createPhotoTrigger()
                .take(propertiesService.getInt(PHOTOS))
                .map(i -> photoService.shoot())
                .retry(propertiesService.getInt(RETRIES))
                .toList()
                .map(this::preparePhoto)
                .map(printerService::print)
                .map(photoStorageService::storeFile);

        observable.subscribe(status -> {
            stopPhotoBoothAction();
            logger.info("Photo booth action executed successfully");
        }, error -> {
            stopPhotoBoothAction();
            //todo: some alert
            logger.error("Failed to execute photo booth action due to: ", error);
        });
    }

    private Observable<Long> createPhotoTrigger() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .map((x) -> {
                    long index = (x % propertiesService.getInt(DELAY)) + 1;
                    Platform.runLater(() ->
                            counterLabel.setText(String.valueOf(propertiesService.getInt(DELAY) - index)));

                    return index;
                })
                .filter((i) -> i % propertiesService.getInt(DELAY) == 0);
    }

    private Path preparePhoto(List<Path> inputPhotos) {
        Platform.runLater(() -> counterLabel.setText(""));

        Path photo = photoConcatener.concat(inputPhotos);
        photo = photoStorageService.storeFile(photo);
        photo = photoScale.scalePhoto(photo);
        photo = photoOverlayService.overlayPhoto(photo);
        lastPhoto = Optional.of(photo);

        return photo;
    }
}
