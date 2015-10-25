package dedep.narsaq.photo;

import edsdk.api.CanonCamera;

import javax.inject.Singleton;

@Singleton
public class Camera {

    private CanonCamera canonCamera;

    public Camera() {
        this.canonCamera = new CanonCamera();
    }

    public CanonCamera getCanonCamera() {
        return canonCamera;
    }
}
