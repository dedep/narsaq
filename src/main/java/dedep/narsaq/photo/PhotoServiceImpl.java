package dedep.narsaq.photo;

import edsdk.api.CanonCamera;
import edsdk.utils.CanonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Singleton
public class PhotoServiceImpl implements PhotoService {

    @Inject
    private CanonCamera camera;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final int MAX_ATTEMPTS = 1;

    @Override
    public BufferedImage shoot() {
        logger.info("Shooting image");
        File[] files = shootPhoto();
        return Stream.of(files).map(photo -> {
            try {
                BufferedImage img = ImageIO.read(photo);
                logger.info("Saved photo as: " + photo.getCanonicalPath());
                return img;
            } catch (IOException e) {
                logger.error("Photo shoot io exception", e);
                throw new PhotoShootException(e);
            }
        }).findFirst().orElseThrow(() -> new PhotoShootException("No taken photo."));
    }

    private File[] shootPhoto() {
        Future<File[]> futureFiles = Executors.newSingleThreadExecutor().submit(() ->
                camera.shoot(CanonConstants.EdsSaveTo.kEdsSaveTo_Host, MAX_ATTEMPTS));

        try {
            return futureFiles.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Photo shoot timeout", e);
            throw new PhotoShootException(e);
        }
    }
}
