package thehambone.gtatools.gta3savefileeditor.savefile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * A {@code PCSaveSlot} represents one of eight save slots recognized by the PC
 * version of Grand Theft Auto III.
 * <p>
 * Created on Jan 24, 2016.
 *
 * @author thehambone
 */
public class PCSaveSlot
{
    private static final Pattern PC_FILE_NAME_PATTERN
            = Pattern.compile("^GTA3sf([0-9]).b$", Pattern.CASE_INSENSITIVE);
    
    // Replace # with a number when using
    private static final String PC_FILE_NAME_FORMAT = "GTA3sf#.b";
    
    private static final PCSaveSlot[] DEFINED_SAVE_SLOTS = new PCSaveSlot[] {
        new PCSaveSlot(1),
        new PCSaveSlot(2),
        new PCSaveSlot(3),
        new PCSaveSlot(4),
        new PCSaveSlot(5),
        new PCSaveSlot(6),
        new PCSaveSlot(7),
        new PCSaveSlot(8),
    };
    
    /**
     * Returns an array of 8 predefined save slots.
     * 
     * @return a collection of predefined save slots
     */
    public static PCSaveSlot[] getSaveSlots()
    {
        return DEFINED_SAVE_SLOTS;
    }
    
    private final int slotNumber;
    private File file;
    private String saveName;
    private String saveTimestamp;
    
    /**
     * Creates a new {@code PCSaveSlot} with the specified slot index.
     * 
     * @param slotNumber the slot index
     */
    public PCSaveSlot(int slotNumber)
    {
        this.slotNumber = slotNumber;
        file = null;
        saveName = "";
        saveTimestamp = "";
    }
    
    /**
     * Checks whether this slot is usable. A slot may be unusable if the file is
     * not a valid PC save file or if the file parent directory does not exist.
     * 
     * @return {@code true} if the save slot is usable, {@code false} otherwise
     */
    public boolean isUsable()
    {
        return file != null;
    }
    
    /**
     * Checks whether the file backed by this save slot exists and is usable.
     * 
     * @return {@code true} if the file exists and is usable, {@code false}
     *         otherwise
     */
    public boolean isEmpty()
    {
        return isUsable() && !file.exists();
    }
    
    /**
     * Gets the slot index number.
     * 
     * @return the slot number
     */
    public int getSlotNumber()
    {
        return slotNumber;
    }
    
    /**
     * Gets the file backed by this slot. {@code null} will be returned if the
     * slot is not usable.
     * 
     * @return the file backed by this save slot
     */
    public File getFile()
    {
        return file;
    }
    
    /**
     * Gets the path to the file backed by this save slot. The path is defined
     * by the {@code GTA3_USER_DIR} setting. This method will always return the
     * path to the file, even if the file does not exist.
     * 
     * @return the path to the file backed by this save slot
     */
    public String getFilePath()
    {
        File gta3SaveDir = new File(Settings.get(Settings.Key.GTA3_USER_DIR));
        String fileName = gta3SaveDir + File.separator + generateFileName();
        
        return fileName;
    }
    
    /**
     * Gets the name of the save file in this save slot. The slot must be usable
     * and must not be empty, otherwise {@code null} will be returned.
     * {@link #refresh()} should also be called before using this method to
     * ensure the most up-to-date save name is retrieved.
     * 
     * @return the name of the save file backed by this save slot
     */
    public String getSaveName()
    {
        return saveName;
    }
    
    /**
     * Gets the save timestamp from the file in this slot. The slot must be
     * usable and must not be empty, otherwise {@code null} will be returned.
     * {@link #refresh()} should also be called before using this method to
     * ensure the most up-to-date timestamp is retrieved.
     * 
     * @return the timestamp in the save file backed by this save slot
     */
    public String getSaveTimestamp()
    {
        return saveTimestamp;
    }
    
    /**
     * Refreshes the save slot. This method will attempt to reload the save slot
     * by checking the file found at the path specified by the
     * {@code GTA3_USER_DIR} setting. If the file exists, the save name and
     * timestamp will be extracted from the file.
     * 
     * @throws IOException if the path specified by the setting
     *         {@code GTA3_USER_DIR} is not a directory, or if the save file
     *         cannot be loaded/isn't a PC save file
     */
    public void refresh() throws IOException
    {
        // Get GTA3 User Files location from program settings
        File gta3SaveDir = new File(Settings.get(Settings.Key.GTA3_USER_DIR));
        
        // Check whether the GTA3 User Files location is valid
        if (!gta3SaveDir.exists()) {
            file = null;
            saveName = "";
            saveTimestamp = "";
            String message = "Cannot find GTA 3 save file directory! "
                    + "(" + gta3SaveDir + ")";
            Logger.warn(message);
            throw new IOException(message);
        } else if (!gta3SaveDir.isDirectory()) {
            file = null;
            saveName = "";
            saveTimestamp = "";
            String message = "The selected GTA 3 save file folder is not a "
                    + "directory! (" + gta3SaveDir + ")";
            Logger.warn(message);
            throw new IOException(message);
        }
        
        // Get all filenames in GTA3 User Files folder
        String[] fileNames = gta3SaveDir.list();
        
        /* If folder is empty, mark this slot as empty by generating a blank
           file object */
        if (fileNames.length == 0) {
            file = new File(generateFileName());
            saveName = "";
            saveTimestamp = "";
            return;
        }
        
        // Iterate over all filenames in the directory
        File f = null;
        boolean fileFound = false;
        boolean fileValid = false;
        for (String fileName : fileNames) {
            int foundSlotNumber;
            fileFound = false;
            fileValid = false;
            Matcher m = PC_FILE_NAME_PATTERN.matcher(fileName);
            
            // Skip if filename doesn't match GTA III save file name format
            if (!m.find()) {
                continue;
            }
            
            // We've got a match! Attempt to extract slot number from name
            try {
                foundSlotNumber = Integer.parseInt(m.group(1));
            } catch (NumberFormatException ex) {
                Logger.stackTrace(ex);
                continue;
            }
            
            /* Does the extracted number match the number of this slot? If not,
               skip this file */
            if (foundSlotNumber != slotNumber) {
                continue;
            }
            
            // The file name matches! Create a new File object from the filename
            f = new File(gta3SaveDir.getAbsolutePath()
                    + File.separator + fileName);
            fileFound = true;
            
            // Check whether the file is a valid GTA III PC save file
            try {
                SaveFile.Platform platform = SaveFile.getPlatform(f);
                if (platform == SaveFile.Platform.PC) {
                    // We've found a valid PC save file! End the loop
                    file = f;
                    fileValid = true;
                } else {
                    // The file is valid, but it's not a PC save file
                    // Mark slot as unusable to avoid accidental overwrite
                    file = null;
                    saveName = "";
                    saveTimestamp = "";
                }
            } catch (IOException ex) {
                // Not a valid save file
                // Mark slot as unusable to avoid accidental overwrite
                file = null;
                saveName = "";
                saveTimestamp = "";
            }
            
            /* A file has been found, valid or not, so end the loop. No need to
               continue searching. */
            break;
        }
        
        // No file was found
        // Mark this slot as empty by generating a blank file object
        if (!fileFound) {
            file = new File(generateFileName());
            saveName = "";
            saveTimestamp = "";
            return;
        }
        
        // Extract information from file
        if (fileValid) {
            SaveFile sf = new SaveFile(file, SaveFile.Platform.PC);
            String[] fileInfo = sf.getFileInfo();
            saveName = fileInfo[0];
            saveTimestamp = fileInfo[1];
        }
    }
    
    /**
     * Generates the filename for a GTA III PC save file for this slot
     */
    private String generateFileName()
    {
        // GTA3sf<SLOT#>.b
        return PC_FILE_NAME_FORMAT.replace("#", Integer.toString(slotNumber));
    }
}
