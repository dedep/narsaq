package dedep.narsaq.photo.storage;

import dedep.narsaq.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class PhotoStorageServiceImpl implements PhotoStorageService {

    @Inject
    private PropertiesService propertiesService;

    public static final String OUTPUT_FILE_DIR_KEY = "photo.output.file.directory";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Path storeFile(Path file) { //todo: w razie czego mozna w nowym watku
        String outputDir = propertiesService.get(OUTPUT_FILE_DIR_KEY);
        Path destDir = Paths.get(outputDir).resolve(getDestinationDirectoryName());
        Path dest = destDir.resolve(file.getFileName());
        logger.info("Copying file {} -> {}", file, dest);

        try {
            Files.createDirectories(destDir);
            return Files.copy(file, dest);
        } catch (IOException e) {
            logger.warn("Could not move file " + file + " -> " + dest, e);
            return file;
        }
    }

    private String getDestinationDirectoryName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}
