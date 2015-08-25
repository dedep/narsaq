package dedep.narsaq.photo.concat;

import dedep.narsaq.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PhotoConcatenerImpl implements PhotoConcatener{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private PropertiesService propertiesService;

    public static final String PHOTO_COLUMNS = "photo.columns";

    private static final String FILE_EXTENSION = "photo.input.file.extension";
    private static final String FILE_PREFIX = "photo.file.prefix";

    @Override
    public Path concat(List<Path> toConcat) {
        logger.info("Before image concatening");
        String fileExtension = propertiesService.get(FILE_EXTENSION);
        String filePrefix = propertiesService.get(FILE_PREFIX);
        List<BufferedImage> imgList = toConcat.stream().map(this::toBufferedImage).collect(Collectors.toList());
        BufferedImage img = concatImages(imgList);
        try {
            File dest = File.createTempFile(filePrefix, "." + fileExtension);
            ImageIO.write(img, fileExtension, dest);
            logger.info("Concatened photo saved in " + dest.getPath());
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

    private BufferedImage concatImages(List<BufferedImage> toConcat) {
        Integer photoColumns = propertiesService.getInt(PHOTO_COLUMNS);
        Integer height = toConcat.stream().mapToInt(BufferedImage::getHeight).sum();
        Integer width = toConcat.stream().mapToInt(BufferedImage::getWidth).max().orElse(0) * photoColumns;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int heightAgg = 0;
        for (BufferedImage img : toConcat) {
            int widthAgg = 0;
            for (int col = 0 ; col < photoColumns ; col++) {
                result.createGraphics().drawImage(img, widthAgg, heightAgg, img.getWidth(), img.getHeight(), null);
                widthAgg += img.getWidth();
            }
            heightAgg += img.getHeight();
        }

        return result;
    }
}
