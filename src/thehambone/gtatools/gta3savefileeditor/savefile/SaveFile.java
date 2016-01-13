package thehambone.gtatools.gta3savefileeditor.savefile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * A generic save file created by the GTA III executable.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 06, 2015
 * @deprecated 
 */
public abstract class SaveFile
{
    private static SaveFile currentlyLoadedFile;
    
    public static boolean isFileLoaded()
    {
        return currentlyLoadedFile != null;
    }
    
    public static SaveFile getCurrentlyLoadedFile()
    {
        return currentlyLoadedFile;
    }
    
    /**
     * Loads a file and parses it as a GTA III save file. The platform from
     * which the file originated is detected automatically and the file is
     * loaded based on that platform. (not yet implemented)
     * 
     * @param saveFile the file to load
     * @return a SaveFile object holding the loaded data
     * @throws IOException if an I/O error occurs
     * @throws UnsupportedPlatformException if the platform from which the file
     *         originated is not supported
     */
    public static SaveFile loadFile(File saveFile) throws IOException, UnsupportedPlatformException
    {
        String makeBackupsProperty = Settings.get("make.backups");
        if (makeBackupsProperty != null && Boolean.parseBoolean(makeBackupsProperty)) {
            File backupFile = new File(saveFile.getAbsolutePath() + ".bak");
            Logger.debug("Backing up %s to %s...\n", saveFile, backupFile);
            IO.copyFile(saveFile, backupFile);
        }
        
        // todo: develop a way to identify platform origin
        
        // For now, loadFile a PC gamesave by default
        PCSaveFile pcSaveFile = new PCSaveFile(new VariableDefinitions());
        Logger.debug("Reading data from file: %s...\n", saveFile.getAbsolutePath());
        int bytesRead = pcSaveFile.load(saveFile);
        Logger.debug("Finished reading file. Bytes read: 0x%04x\n", bytesRead);
        currentlyLoadedFile = pcSaveFile;
        return pcSaveFile;
    }
    
    public static void closeFile()
    {
        currentlyLoadedFile = null;
    }
    
    protected final Map<Integer, Align[]> unknownData = new HashMap<>();
    protected final Map<Integer, Align> skippedData = new HashMap<>();
    
    protected final Platform platform;
    protected final VariableDefinitions vars;
    
    protected File currentFile;

    protected SaveFile(Platform platform, VariableDefinitions vars)
    {
        this.platform = platform;
        this.vars = vars;
    }
    
    public Platform getPlatform()
    {
        return platform;
    }
    
    public VariableDefinitions getVariables()
    {
        return vars;
    }
    
    public File getCurrentFile()
    {
        return currentFile;
    }
    
    /**
     * Loads the specified file. The blocks specified in the constructor are
     * loaded recursively. Data values found in each block are stored to the 
     * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions}
     * object supplied in the constructor.
     * 
     * @param saveFile the file to load
     * @return the number of bytes read
     * @throws IOException if an I/O error occurs
     */
    public abstract int load(File saveFile) throws IOException;
    
    /**
     * Writes the file buffer to a file. Values from the
     * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions}
     * object are written to a binary file. The file is formatted in the same
     * way as a save file created by the GTA III executable. Files will differ
     * slightly between platforms.
     * 
     * @param saveFile the file to write
     * @return the number of bytes written
     * @throws IOException if an I/O error occurs
     */
    public abstract int save(File saveFile) throws IOException;
    
    protected abstract Block[] generateFileStructure();
    
    /**
     * Platform definitions.
     */
    public static enum Platform
    {
        ANDROID(false),
        iOS(false),
        PC(true),
        PS2(false),
        XBOX(false);
        
        private final boolean isSupported;
        
        private Platform(boolean isSupported)
        {
            this.isSupported = isSupported;
        }
        
        public boolean isSupported()
        {
            return isSupported;
        }
    }
}