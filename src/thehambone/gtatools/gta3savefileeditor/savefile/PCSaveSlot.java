package thehambone.gtatools.gta3savefileeditor.savefile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import thehambone.gtatools.gta3savefileeditor.Settings;

/**
 * A PCSaveSlot represents one of the eight save slots for the PC version of
 * GTA III. PCSaveSlots can be used to get the title and timestamp of a save
 * file without having to load the entire file.
 * 
 * File detection is a still a little iffy; files can be found, but not are
 * verified for validity.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 05, 2015
 */
public class PCSaveSlot
{
    private static final PCSaveSlot[] pcSaveSlots = new PCSaveSlot[] {
        new PCSaveSlot(1),
        new PCSaveSlot(2),
        new PCSaveSlot(3),
        new PCSaveSlot(4),
        new PCSaveSlot(5),
        new PCSaveSlot(6),
        new PCSaveSlot(7),
        new PCSaveSlot(8),
    };
    
    public static PCSaveSlot[] getSaveSlots()
    {
        return pcSaveSlots;
    }
    
    public static File getSaveFileForSlot(int slotNumber) throws FileNotFoundException
    {
        File gta3SaveDir = new File(Settings.get("gta3.save.dir"));
        String[] fileNames = gta3SaveDir.list();
        if (fileNames == null || fileNames.length == 0) {
            return null;
        }
        for (String fileName : fileNames) {
            if (!fileName.matches(PCSaveFile.FILE_NAME_REGEX)) {
                continue;
            }
            Matcher m = Pattern.compile(PCSaveFile.FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE).matcher(fileName);
            int fileSlotNumber = -1;
            
            // Get second number in file name
            if (m.find()) {
                fileSlotNumber = Integer.parseInt(m.group(1));
                if (fileSlotNumber == slotNumber) {
                    return new File(gta3SaveDir.getAbsolutePath() + File.separator + fileName);
                }
            }
        }
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(gta3SaveDir.getAbsolutePath())
                .append(File.separator)
                .append(PCSaveFile.FILE_NAME_SIMPLE_FORMAT.replace("#", Integer.toString(slotNumber)));
        return new File(pathBuilder.toString());
    }
    
    private final int slotNumber;
    
    private File saveFile;
    private boolean isEmpty;
    private String saveTitle;
    private String saveTimestampString;
    
    public PCSaveSlot(int slotNumber)
    {
        this.slotNumber = slotNumber;
        saveFile = null;
        isEmpty = true;
        saveTitle = "";
        saveTimestampString = "";
    }
    
    public File getSaveFile()
    {
        return saveFile;
    }
    
    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public int getSlotNumber()
    {
        return slotNumber;
    }
    
    public String getSaveTitle()
    {
        return saveTitle;
    }
    
    public String getSaveTimeStampString()
    {
        return saveTimestampString;
    }
    
    public void load() throws IOException, UnsupportedPlatformException
    {
        if (isEmpty) {
            throw new IOException("slot is empty");
        }
        saveFile = getSaveFileForSlot(slotNumber);
        SaveFile.loadFile(saveFile);
    }
    
    public void save() throws IOException
    {
        if (SaveFile.isFileLoaded()) {
            saveFile = getSaveFileForSlot(slotNumber);
            SaveFile.getCurrentlyLoadedFile().save(saveFile);
        }
    }
    
    public void delete() throws IOException
    {
        saveFile = getSaveFileForSlot(slotNumber);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }
    
    public void refresh() throws IOException
    {
        saveFile = getSaveFileForSlot(slotNumber);
        if (saveFile == null || !saveFile.exists()) {
            isEmpty = true;
            saveTitle = "";
            saveTimestampString = "";
            return;
        }
        isEmpty = false;
        String[] saveFileInfo = PCSaveFile.getSaveFileInfo(saveFile);
        saveTitle = saveFileInfo[0];
        saveTimestampString = saveFileInfo[1];
    }
}