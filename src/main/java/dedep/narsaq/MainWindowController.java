package dedep.narsaq;

import com.google.inject.Inject;
import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.photo.overlay.PhotoOverlayService;
import dedep.narsaq.print.PrinterService;
import edsdk.api.CanonCamera;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainWindowController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private GridPane grid;

    @Inject
    private CanonCamera camera;

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

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DELAY = "photos.delay";
    private static final String PHOTOS = "photos.count";
    private static final String RETRIES = "photos.max.retries";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runnable r = () -> {
            if (camera.beginLiveView()) { //todo: moze to ustawic juz przy starcie?
                liveViewLoop();
            }
        };

        new Thread(r).start();
    }

    private void liveViewLoop() {
        while (true) {
            final BufferedImage img = camera.downloadLiveView(); //todo: do osobnego serwisu
            if (img != null) {
                Image image = SwingFXUtils.toFXImage(img, null);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    private void onActionBtnClick(ActionEvent actionEvent) {
        grid.setVisible(false);
        executeAction();
    }

    private void executeAction() {
        Observable<Path> observable = Observable.interval(propertiesService.getInt(DELAY), TimeUnit.SECONDS)
                .take(propertiesService.getInt(PHOTOS))
                .map(i -> photoService.shoot()).retry(propertiesService.getInt(RETRIES)).toList()
                .map(this::preparePhoto);

        observable.subscribe(status -> {
            grid.setVisible(true);
            logger.info("Photo booth action executed successfully");
        }, error -> {
            grid.setVisible(true);
            //todo: some alert
            logger.error("Failed to execute photo booth action due to: ", error);
        });
    }

    private Path preparePhoto(List<Path> inputPhotos) {
        Path concatened = photoConcatener.concat(inputPhotos);
        Path overlayed = photoOverlayService.overlayPhoto(concatened);
        printerService.print(overlayed);
        return overlayed;
    }
}
