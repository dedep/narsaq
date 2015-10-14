package dedep.narsaq.photo.scale;

import com.google.inject.Singleton;
import dedep.narsaq.properties.PropertiesService;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

//todo: rename to service
@Singleton
public class PhotoScaleImpl implements PhotoScale {

    @Inject
    private PropertiesService propertiesService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String OUT_FILE_EXTENSION = "photo.output.file.extension";
    public static final String FILE_PREFIX = "photo.file.prefix";
    public static final String PHOTO_OUTPUT_FILE_WIDTH = "photo.output.file.width";
    public static final String PHOTO_OUTPUT_FILE_HEIGHT = "photo.output.file.height";

    @Override
    public Path scalePhoto(Path input) {
        logger.info("Before image scaling.");
        BufferedImage img = toBufferedImage(input);
        int width = propertiesService.getInt(PHOTO_OUTPUT_FILE_WIDTH);
        int height = propertiesService.getInt(PHOTO_OUTPUT_FILE_HEIGHT);
        BufferedImage output = Scalr.resize(img, width, height);

        try {
            return toPath(output);
        } catch (IOException e) {
            throw new RuntimeException("Image scale exception ", e);
        }
    }

    private BufferedImage toBufferedImage(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Image read exception ", e);
        }
    }

    private Path toPath(BufferedImage img) throws IOException {
        String prefix = propertiesService.get(FILE_PREFIX);
        String extension = propertiesService.get(OUT_FILE_EXTENSION);

        File dest = File.createTempFile(prefix, "." + extension);
        ImageIO.write(img, extension, dest);
        logger.info("Resized photo saved in " + dest.getPath());
        return dest.toPath();
    }
}
