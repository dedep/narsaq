package dedep.narsaq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class StdoutLoggingProxy {
    private static Logger logger = LoggerFactory.getLogger(StdoutLoggingProxy.class);

    public StdoutLoggingProxy() {
        tieSystemOutAndErrToLog();
    }

    public static void tieSystemOutAndErrToLog() {
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
        logger.info("Proxying stdout to file.");
    }

    public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                realPrintStream.print(string);
                logger.info(string);
            }
        };
    }
}
