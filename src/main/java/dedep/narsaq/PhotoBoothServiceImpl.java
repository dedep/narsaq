package dedep.narsaq;

import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DELAY = "photos.delay";
    private static final String PHOTOS = "photos.count";
    private static final String RETRIES = "photos.max.retries";

    @Override
    public void executeAction() {
        Observable<Path> observable = Observable.interval(propertiesService.getInt(DELAY), TimeUnit.SECONDS)
                .take(propertiesService.getInt(PHOTOS))
                .map(i -> photoService.shoot()).retry(propertiesService.getInt(RETRIES)).toList()
                .map(photos -> {
                    Path toPrint = photoConcatener.concat(photos);
                    printerService.print(toPrint);
                    return toPrint;
                });

        observable.subscribe(status -> {
            logger.info("Photo booth action executed successfully");
        }, error -> {
            logger.error("Failed to execute photo booth action due to: ", error);
        });
    }
}
