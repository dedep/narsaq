package dedep.narsaq.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    public static void main(String[] args) {
        new PrinterServiceImpl().print(Paths.get("/home/dedep/Obrazy/The_Pale_Blue_Dot.jpg"));
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void print(Path image) {
        Runnable r = () -> {
            try {
                BufferedImage img = ImageIO.read(image.toFile());

                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPrintable(new ImagePrintable(printJob, img));

                try {
                    printJob.print();
                } catch (PrinterException e) {
                    logger.error("Print error", e);
                }

                logger.info("Printed"); //todo: some id
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(r, "Print thread").start();
    }
}
