
package thehambone.gtatools.gta3savefileeditor.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import thehambone.gtatools.gta3savefileeditor.Main;

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
    
    // Replace "%s" with timestamp (FILE_NAME_TIMESTAMP_FORMAT)
    private static final String LOG_FILE_NAME_FORMAT
            = "gta3-save-editor_%s.log";
    private static final String CRASH_DUMP_FILE_NAME_FORMAT
            = "crash-dump_%s.log";
    
    private static final String FILE_NAME_TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    
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
     * Writes a crash report to a file. A crash report contains all of the
     * program's log entries.
     * 
     * @return the path to the crash dump file
     * @throws IOException if an error occurs while writing the file
     */
    public static String generateCrashDump() throws IOException
    {
        String timestamp = new SimpleDateFormat(FILE_NAME_TIMESTAMP_FORMAT)
                        .format(new Date());
        String fileName = String.format(CRASH_DUMP_FILE_NAME_FORMAT, timestamp);
        
        File crashdumpFile = new File(fileName);
        instance.storeLog(crashdumpFile);
        
        return crashdumpFile.getAbsolutePath();
    }
    
    /**
     * Writes the stack trace of a {@code Throwable} object to the log using the
     * {@code DEBUG} level.
     * 
     * @param t the {@code Throwable} whose stack trace will be written
     */
    public static void stackTrace(Throwable t)
    {
        stackTrace(Level.DEBUG, t);
    }
    
    public static void stackTrace(Level level, Throwable t)
    {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        t.printStackTrace(pw);
        
        instance.log(level, writer.getBuffer().toString());
    }
    
    /**
     * Writes a message to the log using the {@code DEBUG} level.
     * 
     * @param message the message to be written to the log
     */
    public static void debug(Object message)
    {
        debug("%s\n", message == null ? "null" : message.toString());
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
    public static void info(Object message)
    {
        info("%s\n", message == null ? "null" : message.toString());
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
    public static void warn(Object message)
    {
        warn("%s\n", message == null ? "null" : message.toString());
    }
    
    /**
     * Writes a message to the log using the {@code WARN} level. Information
     * about the specified {@code Throwable} object will be appended to the
     * message. Information about the Throwable object is appended in the
     * following format: {@code [throwable.ClassName: throwable message]}.
     * 
     * @param message the message to be written to the log
     * @param t the {@code Throwable} object to be described in the message
     */
    public static void warn(Object message, Throwable t)
    {
        if (t == null) {
            warn(message);
        } else {
            warn("%s [%s: %s]\n",
                    message == null ? "null" : message.toString(),
                    t.getClass().getName(),
                    t.getMessage());
        }
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
    public static void error(Object message)
    {
        error("%s\n", message == null ? "null" : message.toString());
    }
    
    /**
     * Writes a message to the log using the {@code ERROR} level. Information
     * about the specified {@code Throwable} object will be appended to the
     * message. Information about the Throwable object is appended in the
     * following format: {@code [throwable.ClassName: throwable message]}.
     * 
     * @param message the message to be written to the log
     * @param t the {@code Throwable} object to be described in the message
     */
    public static void error(Object message, Throwable t)
    {
        if (t == null) {
            error(message);
        } else {
            error("%s [%s: %s]\n",
                    message == null ? "null" : message.toString(),
                    t.getClass().getName(),
                    t.getMessage());
        }
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
    public static void fatal(Object message)
    {
        fatal("%s\n", message == null ? "null" : message.toString());
    }
    
    /**
     * Writes a message to the log using the {@code FATAL} level. Information
     * about the specified {@code Throwable} object will be appended to the
     * message. Information about the Throwable object is appended in the
     * following format: {@code [throwable.ClassName: throwable message]}.
     * 
     * @param message the message to be written to the log
     * @param t the {@code Throwable} object to be described in the message
     */
    public static void fatal(Object message, Throwable t)
    {
        if (t == null) {
            fatal(message);
        } else {
            fatal("%s [%s: %s]\n",
                    message == null ? "null" : message.toString(),
                    t.getClass().getName(),
                    t.getMessage());
        }
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
    private boolean logToFile;
    private File logFile;
    private PrintWriter logFileWriter;
    
    /**
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
     * Turns logging to a file on or off. If enabled, each log entry will be
     * written to an auto-generated file.
     * <p>
     * The log file will show up in the working directory and will be named in
     * the following format: {@code gta3-save-editor_yyyymmddHHmmss.log}.
     * 
     * @param logToFile a boolean indicating whether file logging should be
     *        enabled
     * @throws FileNotFoundException if the log file cannot be created
     */
    public void logToFile(boolean logToFile) throws FileNotFoundException
    {
        this.logToFile = logToFile;
        
        if (!logToFile) {
            return;
        }
        
        if (logFile == null) {
            String timestamp = new SimpleDateFormat(FILE_NAME_TIMESTAMP_FORMAT)
                    .format(new Date());
            String fileName = String.format(LOG_FILE_NAME_FORMAT, timestamp);
            logFile = new File(fileName);
        }
        
        logFileWriter = storeLog(logFile);
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
        if (logToFile) {
            logFileWriter.print(logEntry);
            logFileWriter.flush();
        }
        if (level == Level.ERROR || level == Level.FATAL) {
            System.err.print(logEntry);
        } else {
            System.out.print(logEntry);
        }
    }
    
    /**
     * Returns a string containing the current date and time as specified by
     * the string constant TIMESTAMP_FORMAT.
     */
    private String timestamp()
    {
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
    }
    
    /**
     * Writes the log to a file.
     * 
     * @param f the file to write
     * @return the PrintWriter used to write the file
     * @throws FileNotFoundException if the file cannot be created
     */
    private PrintWriter storeLog(File f) throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
        
        String title = Main.PROGRAM_TITLE + " Log";
        pw.println(title);
        pw.println(StringUtilities.repeatChar('=', title.length()));
        pw.println("Log created on " + new Date());
        pw.println();
        pw.append(LOG_BUFFER);
        pw.flush();
        
        return pw;
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
