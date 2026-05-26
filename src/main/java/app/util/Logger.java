package app.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * logger for the service layer.
 */
// TODO: Refactor to use proper logging framework for future UI integration.
public class Logger {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static boolean debugEnabled = false;

    private Logger() {}

    /**
     * Enables ot disables debug logging.
     * @param enabled true to enable debug logging, false to disable
     */
    public static void setDebug(boolean enabled) { debugEnabled = enabled; }

    /**
     * Check if debug is enabled.
     */
    public static boolean isDebug() { return debugEnabled; }

    /**
     * Logs a debug message if debug logging is enabled.
     */
    public static void debug(String message) {
        if (debugEnabled) System.out.println(stamp() + " [DEBUG] " + message);
    }

    /** 
     * Logs an info message.
     */
    public static void info(String message) {
        System.out.println(stamp() + " [INFO ] " + message);
    }

    /**
     * Logs a warning message.
     */
    public static void warn(String message) {
        System.out.println(stamp() + " [WARN] " + message);
    }

    /**
     * Logs an error message.
     */
    public static void error(String message) {
        System.err.println(stamp() + " [ERROR] " + message);
    }

    /**
     * Logs an error message with a throwable.
     */
    public static void error(String message, Throwable t) {
        System.err.println(stamp() + " [ERROR] " + message + " - " + t.getMessage());
    }

    /**
     * Returns the current time formatted as a string.
     */
    private static String stamp() {
        return LocalTime.now().format(FMT);
    }
}
