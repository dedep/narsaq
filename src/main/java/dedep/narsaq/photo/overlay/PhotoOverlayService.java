package dedep.narsaq.photo.overlay;

import java.nio.file.Path;

public interface PhotoOverlayService {
    Path overlayPhoto(Path toOverlay);
}
