package dedep.narsaq.photo.overlay;

import dedep.narsaq.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhotoOverlayServiceImpl implements PhotoOverlayService {

    @Inject
    private PropertiesService propertiesService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String OVERLAY_PATH = "overlay.url";
    private static final String FILE_PREFIX = "photo.file.prefix";
    private static final String FILE_EXTENSION = "photo.file.extension";

    @Override
    public Path overlayPhoto(Path toOverlay) {
        logger.info("Overlaying photo " + toOverlay);
        BufferedImage image = toBufferedImage(toOverlay);
        Path overlayPath = Paths.get(propertiesService.get(OVERLAY_PATH));
        BufferedImage overlay = toBufferedImage(overlayPath);

        Graphics g = image.getGraphics();
        g.drawImage(overlay, 0, 0, null);

        return savePhoto(image);
    }

    private Path savePhoto(BufferedImage img) {
        try {
            File dest = File.createTempFile(propertiesService.get(FILE_PREFIX), ".png");
            ImageIO.write(img, "png", dest);
            logger.info("Overlay photo saved in " + dest.getPath());
            return dest.toPath();
        } catch (IOException e) {
            throw new RuntimeException("Photo concatenation exception ", e);
        }
    }

    private BufferedImage toBufferedImage(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Image read exception ", e);
        }
    }
}
