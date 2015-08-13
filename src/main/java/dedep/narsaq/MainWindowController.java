package dedep.narsaq;

import com.google.inject.Inject;
import edsdk.api.CanonCamera;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private ImageView imageView;

    @FXML
    private Button actionBtn;

    @Inject
    private CanonCamera camera;

    @Inject
    private PhotoBoothService photoBoothService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runnable r = () -> {
            if (camera.beginLiveView()) {
                liveViewLoop();
            }
        };

        new Thread(r).start();
    }

    private void liveViewLoop() {
        while (true) {
            final BufferedImage img = camera.downloadLiveView();
            if (img != null) {
                Image image = SwingFXUtils.toFXImage(img, null);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    private void onActionBtnClick(ActionEvent actionEvent) {
        photoBoothService.executeAction();
    }
}
