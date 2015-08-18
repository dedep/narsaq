package dedep.narsaq.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String HOT_FOLDER_URI = "C:\\DNP\\Hot Folder\\Prints\\4x6";

    public static void main(String[] args) throws IOException, URISyntaxException {
        PrinterServiceImpl service = new PrinterServiceImpl();
        service.print(Paths.get("C:\\Users\\localadmin\\Pictures\\images.jpg"));
    }

    public void print(Path image) {
        try {
            Path dest = Paths.get(HOT_FOLDER_URI).resolve("temp.jpg"); //todo: some ID
            Files.copy(image, dest);
            logger.info("File " + image + "copied to " + dest);
        } catch (IOException e) {
            throw new PrintException(e);
        }
    }
}
