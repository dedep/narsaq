package dedep.narsaq.photo;

import edsdk.utils.CanonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Singleton
public class PhotoServiceImpl implements PhotoService {

    @Inject
    private Camera camera;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final int MAX_ATTEMPTS = 1;

    @Override
    public Path shoot() {
        logger.info("Shooting image");
        File[] files = shootPhoto();
        return Stream.of(files).map(photo -> {
            try {
                Path img = photo.toPath();
                logger.info("Saved photo as: " + photo.getCanonicalPath());
                return img;
            } catch (IOException e) {
                logger.error("Photo shoot io exception", e);
                throw new PhotoShootException(e);
            }
        }).findFirst().orElseThrow(() -> new PhotoShootException("No taken photo"));
    }

    private File[] shootPhoto() {
        Future<File[]> futureFiles = Executors.newSingleThreadExecutor().submit(() ->
                camera.getCanonCamera().shoot(CanonConstants.EdsSaveTo.kEdsSaveTo_Host, MAX_ATTEMPTS));

        try {
            return futureFiles.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Photo shoot timeout", e);
            throw new PhotoShootException(e);
        }
    }
}
