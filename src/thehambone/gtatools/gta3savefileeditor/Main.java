package thehambone.gtatools.gta3savefileeditor;

import thehambone.gtatools.gta3savefileeditor.io.IO;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import thehambone.gtatools.gta3savefileeditor.gui.EditorWindow;
import thehambone.gtatools.gta3savefileeditor.gui.GUIUtils;
import thehambone.gtatools.gta3savefileeditor.gui.UncaughtExceptionHandler;
import thehambone.gtatools.gta3savefileeditor.util.Logger;
import thehambone.gtatools.gta3savefileeditor.util.StringUtilities;

/**
 * Program initialization and constants.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 06, 2015
 */
public class Main
{
    public static final String PROGRAM_TITLE        = "GTA III Save File Editor";
    public static final String PROGRAM_VERSION      = "0.2";
    public static final String PROGRAM_AUTHOR       = "thehambone";
    public static final String PROGRAM_AUTHOR_URL   = "http://gtaforums.com/user/907241-thehambone/";
    public static final String PROGRAM_AUTHOR_EMAIL = "thehambone93@gmail.com";
    public static final String PROGRAM_UPDATE_URL   = "http://www.gtagarage.com/mods/show.php?id=27101";
    
    private static final String PROGRAM_BUILD_PROPERTIES_PATH = "META-INF/build.properties";
    private static final String PROGRAM_ICON_IMAGE_PATH       = "META-INF/res/icon.png";
    
    private static Date programBuildDate        = new Date(0);
    private static int programBuildNumber       = 0; 
    private static OperatingSystem os           = OperatingSystem.UNKNOWN;
    private static boolean isDebugModeEnabled   = false;
    
    /*
     * Sets the default uncaught exception handler for all threads.
     */
    private static void initUncaughtExceptionHandler()
    {
        Thread.setDefaultUncaughtExceptionHandler(
                new UncaughtExceptionHandler());
    }
    
    private static void initLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            IO.error("Failed to initialize UI look and feel.", ex);
        }
    }
    
    private static void determineOS()
    {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            os = OperatingSystem.WINDOWS;
        } else if (osName.contains("mac")) {
            os = OperatingSystem.MAC_OS_X;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            os = OperatingSystem.LINUX_FAMILY;
        } else {
            os = OperatingSystem.UNKNOWN;
        }
    }
    
    private static void readBuildProperties()
    {
        Properties buildProperties = new Properties();
        try {
            InputStream in = Main.class.getClassLoader().getResourceAsStream(PROGRAM_BUILD_PROPERTIES_PATH);
            if (in == null) {
                throw new IOException("file not found - " + PROGRAM_BUILD_PROPERTIES_PATH);
            }
            buildProperties.load(in);
            String buildDateFormat = buildProperties.getProperty("build.date.format");
            String buildDateRaw = buildProperties.getProperty("build.date");
            String buildNumberRaw = buildProperties.getProperty("build.number");
            if (buildDateFormat != null && buildDateRaw != null) {
                programBuildDate = new SimpleDateFormat(buildDateFormat).parse(buildDateRaw);
            }
            if (buildNumberRaw != null) {
                programBuildNumber = Integer.parseInt(buildNumberRaw);
            }
        } catch (IOException | ParseException ex) {
            String errMsg = "Failed to read program build properties. "
                    + "Program title and build dates will render incorrectly.";
            IO.error(errMsg, ex);
            GUIUtils.showErrorMessage(null, errMsg, "Program Error", ex);
        }
    }
    
    private static void initShutdownHooks()
    {
        Runnable saveSettingsHook = new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    IO.info("Saving program settings...");
                    Settings.save();
                } catch (IOException ex) {
                    IO.error("An error occured while saving program settings.", ex);
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(saveSettingsHook));
    }
    
    private static void parseArgs(String[] args)
    {
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                continue;
            }
            switch (arg.substring(2)) {
                case "debug":
                    isDebugModeEnabled = true;
                    IO.debug("Debug mode enabled.");
                    break;
            }
        }
    }
    
    private static void loadSettings()
    {
        try {
            IO.info("Loading program settings...");
            Settings.load();
        } catch (IOException ex) {
            String errMsg = "Failed to load program settings. Default settings will be used.";
            IO.error(errMsg, ex);
            GUIUtils.showErrorMessage(null, errMsg, "IO Error", ex);
            Settings.loadDefaults();
        }
    }
    
    private static void createAndShowGUI(final ImageIcon icon)
    {
        IO.info("Loading UI...");
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                EditorWindow frame = new EditorWindow();
                frame.setTitle(PROGRAM_TITLE + " " + PROGRAM_VERSION);
                frame.setIconImage(icon.getImage());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(630, 515);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    public static void main(String[] args)
    {
        // TODO: cmd options --log-level=<levels[level2[,...]]>, --log-to-file, --debug=<TRUE|FALSE>
        initUncaughtExceptionHandler();
        initLookAndFeel();
        determineOS();
        readBuildProperties();
        
        IO.infof("%s\n", PROGRAM_TITLE);
        IO.info(StringUtilities.repeatChar('=', PROGRAM_TITLE.length()));
        IO.infof("Version %s build %d\n", PROGRAM_VERSION, programBuildNumber);
        IO.infof("Compiled by %s on %s.\n", PROGRAM_AUTHOR, new SimpleDateFormat("MMM. dd, yyyy").format(programBuildDate));
        
//        initShutdownHooks();
        parseArgs(args);
        
        IO.infof("Operating system: %s (%s)\n", System.getProperty("os.name"), System.getProperty("os.version"));
        IO.infof("Architecture: %s\n", System.getProperty("os.arch"));
        IO.infof("Java version: %s\n", System.getProperty("java.version"));
        IO.infof("JVM architecture: %s\n", System.getProperty("sun.arch.data.model"));
        
        ImageIcon icon = null;
        try {
            icon = IO.loadImageResource(PROGRAM_ICON_IMAGE_PATH);
        } catch (IOException ex) {
            IO.error("Failed to load icon image resource.", ex);
        }
        
//        loadSettings();
//        createAndShowGUI(icon);
        
        Logger l = Logger.newInstance(Logger.Level.INFO, Logger.Level.WARN,
                Logger.Level.ERROR, Logger.Level.FATAL);
        System.out.println(l.isLevelEnabled(Logger.Level.INFO));
        System.out.println(l);
        Logger.warn("abc");
    }
    
    public static boolean isDebugModeEnabled()
    {
        return isDebugModeEnabled;
    }
    
    public static OperatingSystem getOperatingSystem()
    {
        return os;
    }
    
    public static Date getProgramBuildDate()
    {
        return programBuildDate;
    }
    
    public static int getProgramBuildNumber()
    {
        return programBuildNumber;
    }
    
    public static enum OperatingSystem
    {
        LINUX_FAMILY,
        MAC_OS_X,
        WINDOWS,
        UNKNOWN;
    }
}