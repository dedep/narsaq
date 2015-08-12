package dedep.narsaq;

import dedep.narsaq.print.PrinterService;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Singleton
public class PhotoBoothServiceImpl implements PhotoBoothService{

    @Inject
    private PrinterService printerService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void executeAction() {
        try {
            MDC.put("id", UUID.randomUUID());
            BufferedImage in = ImageIO.read(new URL("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcStDwNw870acjCQopwFS6CKUN2p8RWe_H9E8MGo80p72EVHJvD3sg"));
            printerService.print(in);
            logger.info("Action performed");
        } catch (IOException e) {
            logger.error("Action error", e);
        }

    }
}
