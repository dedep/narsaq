package dedep.narsaq;

import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

@Singleton
public class PrinterServiceImpl implements PrinterService {

    public void print(BufferedImage image) {
        Runnable r = () -> {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(new ImagePrintable(printJob, image));

            try {
                printJob.print();
            } catch (PrinterException prt) {
                prt.printStackTrace(); //todo: log
            }

            System.out.println("Printed"); //todo: log
        };

        new Thread(r, "Print thread").start();
    }
}
