package dedep.narsaq.photo.concat;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Singleton
public class PhotoConcatenerImpl implements PhotoConcatener{

    public static void main(String[] args) throws IOException {
        Path img1 = Paths.get(("/home/dedep/Obrazy/charyn-canyon-kazakhstan-wallpaper-3.jpg"));
        Path img2 = Paths.get(("/home/dedep/Obrazy/Finland-Wallpaper-HD-Free-Download-62.jpg"));

        new PhotoConcatenerImpl().concat(Arrays.asList(img1, img2));
    }

    @Override
    public Path concat(List<Path> toConcat) {
        BufferedImage img = toConcat.stream().map(this::toBufferedImage).collect(new PhotoConcatenerCollector());
        try {
            File dest = File.createTempFile("1", "jpg"); //todo: some id
            ImageIO.write(img, "jpg", dest);
            return dest.toPath();
        } catch (IOException e) {
            throw new RuntimeException(); //todo: handle it
        }
    }

    private BufferedImage toBufferedImage(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(); //todo: handle it
        }
    }

    class PhotoConcatenerCollector implements Collector<BufferedImage, BufferedImage, BufferedImage> {

        @Override
        public Supplier<BufferedImage> supplier() {
            return () -> new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public BiConsumer<BufferedImage, BufferedImage> accumulator() {
            return (acc, elem) ->
                    acc.createGraphics().drawImage(elem, 0, acc.getHeight(), null);
        }

        @Override
        public BinaryOperator<BufferedImage> combiner() {
            return (left, right) -> {
                left.createGraphics().drawImage(right, 0, left.getHeight(), null);
                return left;
            };
        }

        @Override
        public Function<BufferedImage, BufferedImage> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.IDENTITY_FINISH);
        }
    }
}
