package dedep.narsaq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader == null){
            throw new RuntimeException("Cannot find classloader for class " + getClass());
        }

        URL resource = classLoader.getResource("gui.fxml");
        if (resource == null) {
            throw new FileNotFoundException("Cannot find FXML file");
        }

        Parent root = FXMLLoader.load(resource);
        setupStage(primaryStage, root);
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
