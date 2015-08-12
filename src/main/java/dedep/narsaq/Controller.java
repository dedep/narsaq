package dedep.narsaq;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("/home/dedep/Obrazy/download.jpg");
        Image image = new Image(file.toURI().toString(), 1500, 1500, false, true); //todo: set resolution
        imageView.setImage(image);
    }
}
