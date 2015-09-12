package dedep.narsaq.print;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class ImagePrintable implements Printable {
    private BufferedImage image;

    public ImagePrintable(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex == 0) {
            int pWidth;
            int pHeight;

            if (pageFormat.getOrientation() == PageFormat.PORTRAIT) {
                pWidth = (int) Math.min(pageFormat.getPaper().getWidth(), (double) image.getWidth());
                pHeight = pWidth * image.getHeight() / image.getWidth();
            } else {
                pHeight = (int) Math.min(pageFormat.getPaper().getWidth(), (double) image.getHeight());
                pWidth = pHeight * image.getWidth() / image.getHeight();
            }
            g.drawImage(image, 0, 0, pWidth, pHeight, null);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
