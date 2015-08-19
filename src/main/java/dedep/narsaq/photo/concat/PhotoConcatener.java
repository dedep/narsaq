package dedep.narsaq.photo.concat;

import java.nio.file.Path;
import java.util.List;

public interface PhotoConcatener {
    Path concat(List<Path> toConcat);
}
