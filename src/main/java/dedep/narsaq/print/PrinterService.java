package dedep.narsaq.print;

import java.nio.file.Path;

public interface PrinterService {
    Path print(Path file);
}
