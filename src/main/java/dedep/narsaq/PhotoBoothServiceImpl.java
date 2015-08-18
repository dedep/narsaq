package dedep.narsaq;

import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class PhotoBoothServiceImpl implements PhotoBoothService{

    @Inject
    private PrinterService printerService;

    @Inject
    private PhotoService photoService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final int DELAY = 8;
    private static final int PHOTOS = 1;
    private static final int RETRIES = 2;

    @Override
    public void executeAction() {
        Observable<Integer> observable = Observable.interval(DELAY, TimeUnit.SECONDS)
                .take(PHOTOS).map(i -> photoService.shoot()).retry(RETRIES).toList()
                .map(photos -> {
                    printerService.print(photos.get(PHOTOS - 1)); //todo: prepare photo to print
                    return 1; //todo: obmyslec co tu zwrocic
                });

        observable.subscribe(status -> {
            logger.info("Photobooth action executed successfully");
        }, error -> {
            logger.error("Failed to execute photobooth action due to: ", error);
        });
    }
}
