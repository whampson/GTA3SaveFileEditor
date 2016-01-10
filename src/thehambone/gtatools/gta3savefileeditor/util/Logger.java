
package thehambone.gtatools.gta3savefileeditor.util;

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
     * @param levels the logging levels to be enabled
     * @return the new instance
     */
    public static Logger newInstance(Level... levels)
    {
        instance = new Logger(levels);
        return instance;
    }
    
    public static void debug(String message)
    {
        debug("%s\n", message);
    }
    
    public static void debug(String format, Object... args)
    {
        instance.log(Level.DEBUG, String.format(format, args));
    }
    
    public static void info(String message)
    {
        info("%s\n", message);
    }
    
    public static void info(String format, Object... args)
    {
        instance.log(Level.INFO, String.format(format, args));
    }
    
    public static void warn(String message)
    {
        warn("%s\n", message);
    }
    
    public static void warn(String format, Object... args)
    {
        instance.log(Level.WARN, String.format(format, args));
    }
    
    public static void error(String message)
    {
        error("%s\n", message);
    }
    
    public static void error(String format, Object... args)
    {
        instance.log(Level.ERROR, String.format(format, args));
    }
    
    public static void fatal(String message)
    {
        fatal("%s\n", message);
    }
    
    public static void fatal(String format, Object... args)
    {
        instance.log(Level.FATAL, String.format(format, args));
    }
    
    private final StringBuilder LOG_BUFFER;
    
    private int levels;     // bitstring
    
    /*
     * Creates a new Logger with the specified levels enabled.
     */
    // Logger is a singleton class; keep this constructor private
    private Logger(Level... levels)
    {
        LOG_BUFFER = new StringBuilder();
        setLevels(levels);
    }
    
    /**
     * Checks whether a specific logging level is enabled.
     * 
     * @param level the level to check for
     * @return {@code true} if the specified logging level is enabled,
     *         {@code false} otherwise
     */
    public boolean isLevelEnabled(Level level)
    {
        return (levels & level.getMask()) == level.getMask();
    }
    
    /**
     * Sets the logging levels to be shown in the log.
     * 
     * @param levels the levels to be shown
     */
    public void setLevels(Level... levels)
    {
        this.levels = 0;
        
        for (Level l : levels) {
            this.levels |= l.getMask();
        }
    }
    
    /**
     * Adds an entry to the log.
     * 
     * @param level the logging level
     * @param message the message to be logged
     */
    public void log(Level level, String message)
    {
        if (!isLevelEnabled(level)) {
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
        StringBuilder buf = new StringBuilder("Logger: { levels = ");
        for (Level l : Level.values()) {
            if (isLevelEnabled(l)) {
                buf.append(l.name()).append(" | ");
            }
        }
        buf.delete(buf.length() - 2, buf.length() - 1);
        buf.append("}");
        
        return buf.toString();
    }
    
    /**
     * Defines the various logging levels. Logging levels are used to categorize
     * messages sent to the log.
     */
    public enum Level
    {
        /**
         * Designates information useful for debugging.
         */
        DEBUG(1),
        
        /**
         * Specifies an informational message.
         */
        INFO(2),
        
        /**
         * Denotes that a potentially harmful event has occurred.
         */
        WARN(4),
        
        /**
         * Indicates that an error has occurred, but the program may continue to
         * run.
         */
        ERROR(8),
        
        /**
         * Denotes a severe error that could stop the program from running.
         */
        FATAL(16);
        
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
