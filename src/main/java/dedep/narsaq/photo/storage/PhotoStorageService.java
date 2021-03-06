package dedep.narsaq.photo.storage;

import java.nio.file.Path;

public interface PhotoStorageService {
    Path storeFile(Path file);
    Path storeFile(Path file, String destName);
}
