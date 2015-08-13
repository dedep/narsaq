package dedep.narsaq.photo;

public class PhotoShootException extends RuntimeException {

    public PhotoShootException(String message) {
        super(message);
    }

    public PhotoShootException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhotoShootException() {
    }

    public PhotoShootException(Throwable cause) {
        super(cause);
    }

    public PhotoShootException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
