
package thehambone.gtatools.gta3savefileeditor.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on Jan 9, 2016.
 *
 * @author thehambone
 */
public final class Logger
{
    private static final String TIMESTAMP_FORMAT = "HH:mm:ss";
    private static final String LINE_SEPARATOR
            = System.getProperty("line.separator");
    private static final int MAX_LEVEL_NAME_LENGTH = 5;
    
    // Singleton instance
    private static Logger instance;
    
    /**
     * Creates a new {@code Logger} instance.
     * 
     * @param level the logging level
     * @return the new instance
     */
    public static Logger newInstance(Level level)
    {
        instance = new Logger(level);
        return instance;
    }
    
    /**
     * Gets the current {@code Logger} instance.
     * 
     * @return the current logger instance
     */
    public static Logger getLogger()
    {
        return instance;
    }
    
    /**
     * Writes the stack trace of a {@code Throwable} object to the log using the
     * {@code DEBUG} level.
     * 
     * @param t the {@code Throwable} whose stack trace will be written
     */
    public static void stackTrace(Throwable t)
    {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        t.printStackTrace(pw);
        
        instance.log(Level.DEBUG, writer.getBuffer().toString());
    }
    
    /**
     * Writes a message to the log using the {@code DEBUG} level.
     * 
     * @param message the message to be written to the log
     */
    public static void debug(String message)
    {
        debug("%s\n", message);
    }
    
    /**
     * Writes a formatted message to the log using the {@code DEBUG} level.
     * 
     * @param format the message format
     * @param args format arguments
     */
    public static void debug(String format, Object... args)
    {
        instance.log(Level.DEBUG, String.format(format, args));
    }
    
    /**
     * Writes a message to the log using the {@code INFO} level.
     * 
     * @param message the message to be written to the log
     */
    public static void info(String message)
    {
        info("%s\n", message);
    }
    
    /**
     * Writes a formatted message to the log using the {@code INFO} level.
     * 
     * @param format the message format
     * @param args format arguments
     */
    public static void info(String format, Object... args)
    {
        instance.log(Level.INFO, String.format(format, args));
    }
    
    /**
     * Writes a message to the log using the {@code WARN} level.
     * 
     * @param message the message to be written to the log
     */
    public static void warn(String message)
    {
        warn("%s\n", message);
    }
    
    /**
     * Writes a formatted message to the log using the {@code WARN} level.
     * 
     * @param format the message format
     * @param args format arguments
     */
    public static void warn(String format, Object... args)
    {
        instance.log(Level.WARN, String.format(format, args));
    }
    
    /**
     * Writes a message to the log using the {@code ERROR} level.
     * 
     * @param message the message to be written to the log
     */
    public static void error(String message)
    {
        error("%s\n", message);
    }
    
    /**
     * Writes a formatted message to the log using the {@code ERROR} level.
     * 
     * @param format the message format
     * @param args format arguments
     */
    public static void error(String format, Object... args)
    {
        instance.log(Level.ERROR, String.format(format, args));
    }
    
    /**
     * Writes a message to the log using the {@code FATAL} level.
     * 
     * @param message the message to be written to the log
     */
    public static void fatal(String message)
    {
        fatal("%s\n", message);
    }
    
    /**
     * Writes a formatted message to the log using the {@code FATAL} level.
     * 
     * @param format the message format
     * @param args format arguments
     */
    public static void fatal(String format, Object... args)
    {
        instance.log(Level.FATAL, String.format(format, args));
    }
    
    private final StringBuilder LOG_BUFFER;
    
    private Level currentLevel;
    
    /*
     * Creates a new Logger with the specified logging level.
     */
    // Logger is a singleton class; keep this constructor private
    private Logger(Level level)
    {
        LOG_BUFFER = new StringBuilder();
        currentLevel = level;
    }
    
    /**
     * Gets the current logging level.
     * 
     * @return the current logging level
     */
    public Level getLevel()
    {
        return currentLevel;
    }
    
    /**
     * Sets the current logging level.
     * 
     * @param level the logging level to be set as current
     */
    public void setLevel(Level level)
    {
        currentLevel = level;
    }
    
    /**
     * Adds an entry to the log.
     * 
     * @param level the logging Level
     * @param message the message to be logged
     */
    public void log(Level level, String message)
    {
        if (level.getMask() < currentLevel.getMask()) {
            return;
        }
        
        // Calculate padding on level name
        String namePadding = "";
        if (level.name().length() < MAX_LEVEL_NAME_LENGTH) {
            int numSpaces = MAX_LEVEL_NAME_LENGTH
                    - (level.name().length() % MAX_LEVEL_NAME_LENGTH);
            namePadding = StringUtilities.repeatChar(' ', numSpaces);
        }
        
        // Create new log entry with timestamp, level, and message
        String logEntry = "";
        logEntry += "<" + timestamp() + "> ";
        logEntry += "[" + namePadding + level.name() + "]: ";
        logEntry += message.replaceAll("\\n", LINE_SEPARATOR);
        
        // Append entry to log and output to console
        LOG_BUFFER.append(logEntry);
        if (level == Level.ERROR || level == Level.FATAL) {
            System.err.print(logEntry);
        } else {
            System.out.print(logEntry);
        }
    }
    
    /*
     * Returns a string containing the current date and time as specified by
     * the string constant TIMESTAMP_FORMAT.
     */
    private String timestamp()
    {
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
    }
    
    @Override
    public String toString()
    {
        return "Logger: { level = " + currentLevel.name() + " }";
    }
    
    /**
     * Defines the various logging levels. Logging levels are used to categorize
     * messages sent to the log. Each logging level has precedence over the
     * other. The precedence order is as follows:
     *     {@code FATAL > ERROR > WARN > INFO > DEBUG}.
     * When a logging level is enabled, all levels of higher precedence are also
     * enabled. For instance, when {@code INFO} is enabled, {@code WARN, ERROR}
     * and {@code FATAL} are enabled as well.
     */
    public enum Level
    {
        /**
         * Indicates a message useful for debugging.
         */
        DEBUG(1),
        
        /**
         * Specifies an informational message.
         */
        INFO(2),
        
        /**
         * Denotes that a potentially harmful event has occurred.
         */
        WARN(3),
        
        /**
         * Indicates that an error has occurred, but the program may continue to
         * run.
         */
        ERROR(4),
        
        /**
         * Denotes a severe error that could stop the program from running.
         */
        FATAL(5);
        
        private final int mask;
        
        private Level(int id)
        {
            this.mask = id;
        }
        
        private int getMask()
        {
            return mask;
        }
    }
}
