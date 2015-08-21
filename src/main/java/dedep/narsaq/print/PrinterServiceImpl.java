package dedep.narsaq.print;

import dedep.narsaq.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String HOT_FOLDER_URL = "hot.folder.url";

    @Inject
    private PropertiesService propertiesService;

    public void print(Path image) {
        try {
            String hotFolderUrl = propertiesService.get(HOT_FOLDER_URL);
            Path dest = Paths.get(hotFolderUrl).resolve(image.getFileName());
            Files.copy(image, dest);
            logger.info("File " + image + "copied to " + dest);
        } catch (IOException e) {
            logger.error("Photo print error", e);
            throw new PrintException(e);
        }
    }
}
