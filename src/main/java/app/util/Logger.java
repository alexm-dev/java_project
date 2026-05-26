package app.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Lightweight logger for the CLI and service layer.
 */
public class Logger {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static boolean debugEnabled = false;

    private Logger() {}

    public static void setDebug(boolean enabled) { debugEnabled = enabled; }
    public static boolean isDebug() { return debugEnabled; }

    public static void debug(String message) {
        if (debugEnabled) System.out.println(stamp() + " [DEBUG] " + message);
    }

    public static void info(String message) {
        System.out.println(stamp() + " [INFO ] " + message);
    }

    public static void warn(String message) {
        System.out.println(stamp() + " [WARN ] " + message);
    }

    public static void error(String message) {
        System.err.println(stamp() + " [ERROR] " + message);
    }

    public static void error(String message, Throwable t) {
        System.err.println(stamp() + " [ERROR] " + message + " - " + t.getMessage());
    }

    private static String stamp() {
        return LocalTime.now().format(FMT);
    }
}
