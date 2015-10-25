package dedep.narsaq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dedep.narsaq.controller.MainWindowController;
import dedep.narsaq.module.AppModule;
import dedep.narsaq.photo.Camera;
import dedep.narsaq.util.GuiceLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainWindow extends Application {

    public static final String STYLES_FILE = "style.css";
    public static final String WINDOW_TITLE = "window.title";

    private static Injector injector;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = getRoot();
        setupStage(primaryStage, root);
    }

    private Parent getRoot() throws FileNotFoundException {
        Injector injector = getInjector();
        GuiceLoader guiceLoader = injector.getInstance(GuiceLoader.class);

        return (Parent) guiceLoader.load("mainWindow.fxml", MainWindowController.class);
    }

    private void setupStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle(getWindowTitle());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(STYLES_FILE).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        addShutdownHook(primaryStage);
        primaryStage.show();
    }

    private void addShutdownHook(Stage primaryStage) {
        Injector injector = getInjector();
        Camera camera = injector.getInstance(Camera.class);
        primaryStage.setOnCloseRequest(we -> {
            try {
                camera.getCanonCamera().closeSession();
                camera.getCanonCamera().close();
            } catch (Exception e) {
                logger.error("Cannot close camera", e);
            } finally {
                System.exit(0);
            }
        });
    }

    private static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new AppModule());
        }

        return injector;
    }

    private String getWindowTitle() {
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle", Locale.getDefault());
        return bundle.getString(WINDOW_TITLE);
    }
}
