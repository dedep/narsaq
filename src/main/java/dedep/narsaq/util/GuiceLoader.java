package dedep.narsaq.util;

import com.google.inject.Injector;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import javax.inject.Inject;

public class GuiceLoader implements Callback<Class<?>, Object> {

    public static final String BUNDLE_NAME = "Bundle";

    private final Injector injector;

    @Inject
    public GuiceLoader(Injector injector) {
        this.injector = injector;
    }

    public Object load(String fxmlName, Class<?> controller) {
        try {
            URL url = controller.getClassLoader().getResource(fxmlName);
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()));
            loader.setControllerFactory(this);
            return loader.load(url.openStream());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object call(Class<?> controller) {
        return injector.getInstance(controller);
    }
}
