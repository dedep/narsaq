package dedep.narsaq.module;

import com.google.inject.AbstractModule;
import dedep.narsaq.photo.PhotoService;
import dedep.narsaq.photo.PhotoServiceImpl;
import dedep.narsaq.photo.concat.PhotoConcatener;
import dedep.narsaq.photo.concat.PhotoConcatenerImpl;
import dedep.narsaq.photo.overlay.PhotoOverlayService;
import dedep.narsaq.photo.overlay.PhotoOverlayServiceImpl;
import dedep.narsaq.photo.scale.PhotoScale;
import dedep.narsaq.photo.scale.PhotoScaleImpl;
import dedep.narsaq.photo.storage.PhotoStorageService;
import dedep.narsaq.photo.storage.PhotoStorageServiceImpl;
import dedep.narsaq.print.PrinterService;
import dedep.narsaq.print.PrinterServiceImpl;
import dedep.narsaq.properties.PropertiesService;
import dedep.narsaq.properties.PropertiesServiceImpl;
import dedep.narsaq.util.StdoutLoggingProxy;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StdoutLoggingProxy.class).asEagerSingleton();
        bind(PrinterService.class).to(PrinterServiceImpl.class);
        bind(PhotoService.class).to(PhotoServiceImpl.class);
        bind(PhotoConcatener.class).to(PhotoConcatenerImpl.class);
        bind(PhotoOverlayService.class).to(PhotoOverlayServiceImpl.class);
        bind(PropertiesService.class).to(PropertiesServiceImpl.class);
        bind(PhotoScale.class).to(PhotoScaleImpl.class);
        bind(PhotoStorageService.class).to(PhotoStorageServiceImpl.class);
    }
}
