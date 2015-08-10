package dedep.narsaq;

import com.google.inject.AbstractModule;
import dedep.narsaq.print.PrinterService;
import dedep.narsaq.print.PrinterServiceImpl;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PrinterService.class).to(PrinterServiceImpl.class);
        bind(PhotoBoothService.class).to(PhotoBoothServiceImpl.class);
    }
}
