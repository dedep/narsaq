package dedep.narsaq.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.Path;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void print(Path image) {
        Runnable r = () -> {
            try {
                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPageable(createPageable(image, printJob));
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

    private Pageable createPageable(Path image, PrinterJob printJob) throws IOException {
        BufferedImage img = ImageIO.read(image.toFile());
        PageFormat pf = printJob.defaultPage();
        Paper p = pf.getPaper();
        p.setImageableArea(0, 0, p.getWidth(), p.getHeight());
        pf.setPaper(p);

        Printable printable = new ImagePrintable(img);

        return new OpenBook(pf, printable);
    }
}
