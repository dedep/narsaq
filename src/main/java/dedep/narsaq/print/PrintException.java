package dedep.narsaq.print;

public class PrintException extends RuntimeException {
    public PrintException() {
    }

    public PrintException(String message) {
        super(message);
    }

    public PrintException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintException(Throwable cause) {
        super(cause);
    }

    public PrintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
