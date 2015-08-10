package dedep.narsaq;

import dedep.narsaq.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@Singleton
public class PhotoBoothServiceImpl implements PhotoBoothService{

    @Inject
    private PrinterService printerService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void executeAction() {
        try {
            BufferedImage in = ImageIO.read(new URL("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcStDwNw870acjCQopwFS6CKUN2p8RWe_H9E8MGo80p72EVHJvD3sg"));
            printerService.print(in);
        } catch (IOException e) {
            logger.error("Action error", e);
        }

    }
}
