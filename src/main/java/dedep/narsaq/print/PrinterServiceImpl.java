package dedep.narsaq.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void print(BufferedImage image) {
        Runnable r = () -> {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(new ImagePrintable(printJob, image));

            try {
                printJob.print();
                logger.info("Printed");
            } catch (PrinterException e) {
                throw new PrintException(e);
            }
        };

        new Thread(r, "Print thread").start();
    }
}
