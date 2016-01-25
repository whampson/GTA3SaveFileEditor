package thehambone.gtatools.gta3savefileeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import thehambone.gtatools.gta3savefileeditor.game.Game;

/**
 * Program settings.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 06, 2015
 */
public class Settings
{
    public static final String SETTINGS_FILE_PATH = "./editor.properties";
    
    public static final String KEY_GTA3_USER_FILES
            = "gta3.user.files";
    public static final String KEY_LAST_FILE_SELECTED_PARENT_DIR
            = "last.file.selected.parent.dir";
    
    private static final Properties DEFAULTS = new Properties();
    static {
        DEFAULTS.put(KEY_GTA3_USER_FILES, Game.getGameUserDirectoryPath());
        DEFAULTS.put(KEY_LAST_FILE_SELECTED_PARENT_DIR, null);
        DEFAULTS.put("make.backups", "true");
        DEFAULTS.put("update.timestamp", "true");
    }
    
    private static final Properties CURRENT_SETTINGS = new Properties(DEFAULTS);
    
    public static void load() throws IOException
    {
        File f = new File(SETTINGS_FILE_PATH);
        if (!f.exists()) {
            return;
        }
        FileInputStream in = new FileInputStream(f);
        CURRENT_SETTINGS.clear();
        CURRENT_SETTINGS.load(in);
    }
    
    public static void loadDefaults()
    {
        CURRENT_SETTINGS.clear();
        CURRENT_SETTINGS.putAll(DEFAULTS);
    }
    
    public static void save() throws IOException
    {
        FileOutputStream out = new FileOutputStream(new File(SETTINGS_FILE_PATH));
        CURRENT_SETTINGS.store(out, Main.PROGRAM_TITLE + " Configuration");
    }
    
    public static String get(String key)
    {
        return CURRENT_SETTINGS.getProperty(key);
    }
    
    public static String getDefault(String key)
    {
        return DEFAULTS.getProperty(key);
    }
    
    public static void set(String key, String value)
    {
        CURRENT_SETTINGS.setProperty(key, value);
    }
}