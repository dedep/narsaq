package dedep.narsaq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL resource = getFXMLResource();
        Parent root = FXMLLoader.load(resource);
        setupStage(primaryStage, root);

        //todo: temp
        Injector injector = Guice.createInjector(new AppModule());
        PrinterService printerService = injector.getInstance(PrinterService.class);
        BufferedImage in = ImageIO.read(new URL("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcStDwNw870acjCQopwFS6CKUN2p8RWe_H9E8MGo80p72EVHJvD3sg"));
        printerService.print(in);
    }

    private URL getFXMLResource() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader == null){
            throw new RuntimeException("Cannot find classloader for class " + getClass());
        }

        URL resource = classLoader.getResource("gui.fxml");
        if (resource == null) {
            throw new FileNotFoundException("Cannot find FXML file");
        }

        return resource;
    }

    private void setupStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
