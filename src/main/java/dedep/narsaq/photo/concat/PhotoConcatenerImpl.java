package dedep.narsaq.photo.concat;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class PhotoConcatenerImpl implements PhotoConcatener{

    public static void main(String[] args) {

    }

    @Override
    public Path concat(List<Path> toConcat) {
        return toConcat.get(0); //todo: temp
    }
}
