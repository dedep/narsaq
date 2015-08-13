package dedep.narsaq;

import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.util.List;
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

    @Override
    public void executeAction() {
        Observable<List<BufferedImage>> observable = Observable.interval(DELAY, TimeUnit.SECONDS)
                .take(PHOTOS).map(i -> photoService.shoot()).retry(2).toList();

        observable.subscribe(photo -> {
            photo.forEach(printerService::print);
        }, error -> {
            logger.error("Failed to execute photobooth action due to: ", error);
        });
    }
}
