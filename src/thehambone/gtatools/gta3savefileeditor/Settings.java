package thehambone.gtatools.gta3savefileeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;
import thehambone.gtatools.gta3savefileeditor.game.Game;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * This class handles the program settings.
 * <p>
 * Created on Apr 6, 2015.
 * 
 * @author thehambone
 */
public final class Settings
{
    /**
     * The path to the program configuration file.
     */
    public static final String CONFIG_FILE_PATH = "./.gta3-save-editor";
    
    /**
     * The maximum number of recent files to store.
     */
    public static final int MAX_RECENT_FILES = 10;
    
    // Replace the # character with a number when using
    private static final String RECENT_FILE_KEY_FORMAT = "recent.file#";
    
    private static final TreeMap<String, String> DEFAULTS = new TreeMap<>();
    static {
        // Populate defaults list
        DEFAULTS.put(Key.DEBUG_LOGGING.propertyName, "false");
        DEFAULTS.put(Key.GTA3_USER_DIR.propertyName,
                Game.getUserDirectoryPath());
        DEFAULTS.put(Key.LAST_LOCATION.propertyName, "");
        DEFAULTS.put(Key.MAKE_BACKUPS.propertyName, "true");
        DEFAULTS.put(Key.TIMESTAMP_FILES.propertyName, "true");
    }
    
    // A TreeMap is used to keep properties in alphabetical order
    private static final TreeMap<String, String> CURRENT_CONFIG
            = new TreeMap<>(DEFAULTS);
    
    /**
     * Gets a property from the current program configuration.
     * 
     * @param key the property name
     * @return the property value; {@code null} if the property has not been
     *         set
     * @see Key for a list of keys and their descriptions
     */
    public static String get(Key key)
    {
        return CURRENT_CONFIG.get(key.propertyName);
    }
    
    /**
     * Gets a property from the default program configuration.
     * 
     * @param key the property name
     * @return the property value; {@code null} if the property has not been
     *         set
     * @see Key for a list of keys and their descriptions
     */
    public static String getDefault(Key key)
    {
        return DEFAULTS.get(key.propertyName);
    }
    
    /**
     * Returns a {@code Map} of strings containing all of the current settings.
     * 
     * @return the current configuration as a {@code Map}
     */
    public static Map<String, String> getAll()
    {
        return Collections.unmodifiableMap(CURRENT_CONFIG);
    }
    
    /**
     * Returns a {@code Map} of strings containing all of the default settings.
     * 
     * @return the default configuration as a {@code Map}
     */
    public static Map<String, String> getDefaults()
    {
        return Collections.unmodifiableMap(DEFAULTS);
    }
    
    /**
     * Sets a property.
     * 
     * @param key the property name
     * @param value the desired value for the property specified by the key
     * @see Key for a list of keys and their descriptions
     */
    public static void set(Key key, String value)
    {
        CURRENT_CONFIG.put(key.propertyName, value);
        
        Logger.debug("Property updated: %s = %s\n", key, value);
    }
    
    /**
     * Gets a collection containing the paths of recently-accessed files.
     * 
     * @return a {@code List} containing the paths of
     *         recently-accessed files
     */
    public static List<String> getRecentFiles()
    {
        List<String> recentFiles = new ArrayList<>();
        
        for (int i = 0; i < MAX_RECENT_FILES; i++) {
            char indexChar = Character.forDigit(i + 1, 10);
            String key = RECENT_FILE_KEY_FORMAT.replace('#', indexChar);
            
            String path = CURRENT_CONFIG.get(key);
            if (path == null) {
                continue;
            }
            
            recentFiles.add(path);
        }
        
        return recentFiles;
    }
    
    /**
     * Stores the paths of recently-accessed files to the current program
     * configuration.
     * 
     * @param recentFiles a {@code List} containing the paths of
     *        recently-accessed files
     */
    public static void storeRecentFiles(List<String> recentFiles)
    {
        int i = 0;
        for (String path : recentFiles) {
            if (i > MAX_RECENT_FILES) {
                break;
            }
            
            char indexChar = Character.forDigit(++i, 10);
            String key = RECENT_FILE_KEY_FORMAT.replace('#', indexChar);
            CURRENT_CONFIG.put(key, path);
        }
    }
    
    /**
     * Loads the default configuration into memory.
     */
    public static  void loadDefaults()
    {
        CURRENT_CONFIG.clear();
        CURRENT_CONFIG.putAll(DEFAULTS);
    }
    
    /**
     * Loads configuration data from the file found at {@code CONFIG_FILE_PATH}
     * into memory. If the file exists and has been loaded successfully, {@code
     * true} will be returned.
     * 
     * @return {@code true} if the configuration file exists and has been
     *         loaded, {@code false} if the files does not exist
     * @throws IOException if an I/O error occurs while loading the
     *         configuration file
     */
    public static boolean load() throws IOException
    {
        File f = new File(CONFIG_FILE_PATH);
        if (!f.exists()) {
            return false;
        }
        
        Properties p = new Properties();
        
        InputStream in = new FileInputStream(f);
        p.load(in);
        
        CURRENT_CONFIG.clear();
        for (String key : p.stringPropertyNames()) {
            CURRENT_CONFIG.put(key, p.getProperty(key));
        }
        
        return true;
    }
    
    /**
     * Saves the current configuration to the file found at
     * {@code CONFIG_FILE_PATH}.
     * 
     * @throws IOException if an I/O error occurs while writing the
     *         configuration file
     */
    public static void save() throws IOException
    {
        // Create a Properties object that returns keys in alphabetical order
        Properties p = new Properties()
        {
            @Override
            public synchronized Enumeration<Object> keys()
            {
                return Collections.enumeration(new TreeSet<>(super.keySet()));
            }
        };
        
        p.putAll(CURRENT_CONFIG);
        
        String comment = String.format("%s %s Configuration",
                Main.PROGRAM_TITLE, Main.PROGRAM_VERSION);
        
        OutputStream out = new FileOutputStream(CONFIG_FILE_PATH);
        p.store(out, comment);
    }
    
    /**
     * Definitions of all valid properties.
     */
    public static enum Key
    {
        /**
         * Indicates whether the logger should use the {@code DEBUG} level and
         * log files be created each time the program is run.
         */
        DEBUG_LOGGING("debug.logging"),
        
        /**
         * Contains the location of the "GTA3 User Files" directory, which holds
         * all PC saves files read by the game.
         * <p>
         * Acceptable values: any valid directory path
         */
        GTA3_USER_DIR("gta3.user.dir"),
        
        /**
         * Holds the most-recently browsed to directory.
         * <p>
         * Acceptable values: any valid directory path
         */
        LAST_LOCATION("last.location"),
        
        /**
         * Dictates whether a backup should be made before loading a save file.
         * <p>
         * Acceptable values: {@code "true"}, {@code "false"}
         */
        MAKE_BACKUPS("make.backups"),
        
        /**
         * Dictates whether the timestamp of the working file should be
         * updated when saved.
         * <p>
         * Acceptable values: {@code "true"}, {@code "false"}
         */
        TIMESTAMP_FILES("timestamp.files");
        
        private final String propertyName;
        
        private Key(String propertyName)
        {
            this.propertyName = propertyName;
        }
    }
}
