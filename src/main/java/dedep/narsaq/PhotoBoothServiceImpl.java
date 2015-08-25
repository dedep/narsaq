package dedep.narsaq;

import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.photo.overlay.PhotoOverlayService;
import dedep.narsaq.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class PhotoBoothServiceImpl implements PhotoBoothService{

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
    public void executeAction() {
        Observable<Path> observable = Observable.interval(propertiesService.getInt(DELAY), TimeUnit.SECONDS)
                .take(propertiesService.getInt(PHOTOS))
                .map(i -> photoService.shoot()).retry(propertiesService.getInt(RETRIES)).toList()
                .map(this::preparePhoto);

        observable.subscribe(status -> {
            logger.info("Photo booth action executed successfully");
        }, error -> {
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
