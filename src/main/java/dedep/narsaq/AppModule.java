package dedep.narsaq;

import com.google.inject.AbstractModule;
import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.PhotoServiceImpl;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.photo.concat.PhotoConcatenerImpl;
import dedep.narsaq.print.PrinterService;
import dedep.narsaq.print.PrinterServiceImpl;
import edsdk.api.CanonCamera;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PrinterService.class).to(PrinterServiceImpl.class);
        bind(PhotoBoothService.class).to(PhotoBoothServiceImpl.class);
        bind(CanonCamera.class).toInstance(createCamera());
        bind(PhotoService.class).to(PhotoServiceImpl.class);
        bind(PhotoConcatener.class).to(PhotoConcatenerImpl.class);
    }

    private CanonCamera createCamera() {
        CanonCamera camera = new CanonCamera();
        camera.openSession();

        return camera;
    }
}
