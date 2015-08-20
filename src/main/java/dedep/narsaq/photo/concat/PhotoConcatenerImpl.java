package dedep.narsaq.photo.concat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PhotoConcatenerImpl implements PhotoConcatener{

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final int PHOTO_COLUMNS = 2;

    private static final String FILE_EXTENSION = "jpg";
    private static final String FILE_PREFIX = "photobooth";

    public static void main(String[] args) throws IOException {
        Path img1 = Paths.get(("C:\\Users\\localadmin\\AppData\\Local\\Temp\\IMG_0001.JPG"));
        Path img2 = Paths.get(("C:\\Users\\localadmin\\AppData\\Local\\Temp\\IMG_0002.JPG"));
        Path img3 = Paths.get(("C:\\Users\\localadmin\\AppData\\Local\\Temp\\IMG_0003.JPG"));
        Path img4 = Paths.get(("C:\\Users\\localadmin\\AppData\\Local\\Temp\\IMG_0004.JPG"));

        new PhotoConcatenerImpl().concat(Arrays.asList(img1, img2, img3, img4));
    }

    @Override
    public Path concat(List<Path> toConcat) {
        logger.info("Before image concatening");
        List<BufferedImage> imgList = toConcat.stream().map(this::toBufferedImage).collect(Collectors.toList());
        BufferedImage img = concatImages(imgList);
        try {
            File dest = File.createTempFile(FILE_PREFIX, "." + FILE_EXTENSION);
            ImageIO.write(img, FILE_EXTENSION, dest);
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
        Integer height = toConcat.stream().mapToInt(BufferedImage::getHeight).sum();
        Integer width = toConcat.stream().mapToInt(BufferedImage::getWidth).max().orElse(0) * PHOTO_COLUMNS;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int heightAgg = 0;
        for (BufferedImage img : toConcat) {
            int widthAgg = 0;
            for (int col = 0 ; col < PHOTO_COLUMNS ; col++) {
                result.createGraphics().drawImage(img, widthAgg, heightAgg, img.getWidth(), img.getHeight(), null);
                widthAgg += img.getWidth();
            }
            heightAgg += img.getHeight();
        }

        return result;
    }
}
