package thehambone.gtatools.gta3savefileeditor.savefile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.util.Checksum;
import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockGangs;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockGarages;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockPedTypes;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockPlayerInfo;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockPlayerPeds;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.BlockSimpleVars;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Timestamp;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString16;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * This class represents a Grand Theft Auto III save file.
 * <p>
 * A save file is used to store the current state of a game so it can be resumed
 * at a later time. In the case of Grand Theft Auto III, the save file is a
 * complex binary file that is divided into many chunks, or blocks, where each 
 * block contains data pertaining to a particular part of the game. This class
 * is responsible for reading data from and writing data to the save file, as
 * well as maintaining information about which gaming platform the save file
 * originated from.
 * <p>
 * This class follows the singleton pattern, meaning only one instance can exist
 * at a time.
 * <p>
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class SaveFile
{
    private static final String TIMESTAMP_FORMAT = "dd MMM. yyyy HH:mm:ss";
    // Singleton object instance
    private static SaveFile currentSaveFile;
    
    public static Platform getPlatform(File src) throws IOException
    {
        SaveFile temp = new SaveFile(src);
        return temp.getPlatform();
    }
    
    /**
     * Loads data from the save file located at the path provided and returns a
     * new {@code SaveFile} object containing the loaded data.
     * 
     * @param srcPath the path of the file to be loaded
     * @return a new {@code SaveFile} object containing the loaded data
     * @throws IOException if the file loaded is not a valid GTA III save file
     *                     or if an I/O error occurs
     * @throws UnsupportedPlatformException if the file originated from a gaming
     *                                      platform that is not supported
     */
    public static SaveFile load(String srcPath) throws IOException
    {
        return load(new File(srcPath));
    }
    
    /**
     * Loads data from the save file represented by the {@code File} object
     * provided and returns a new {@code SaveFile} object containing the loaded
     * data.
     * 
     * @param src a {@code File} object representing the file to be loaded
     * @return a new {@code SaveFile} object containing the loaded data
     * @throws IOException if the file loaded is not a valid GTA III save file
     *                     or if an I/O error occurs
     * @throws UnsupportedPlatformException if the file originated from a gaming
     *                                      platform that is not supported
     */
    public static SaveFile load(File src) throws IOException
    {
        currentSaveFile = new SaveFile(src);
        currentSaveFile.load();
        return currentSaveFile;
    }
    
    /**
     * Loads data from the save file located at the path provided and returns a
     * new {@code SaveFile} object containing the loaded data. Data is loaded
     * according to the specified gaming platform.
     * 
     * @param srcPath the path of the file to be loaded
     * @param platform the gaming platform that from which this save file was
     *                 created
     * @return a new {@code SaveFile} object containing the loaded data
     * @throws IOException if the file loaded is not a valid GTA III save file
     *                     or if an I/O error occurs
     * @throws UnsupportedPlatformException if the file originated from a gaming
     *                                      platform that is not supported
     */
    public static SaveFile load(String srcPath, Platform platform)
            throws IOException
    {
        return load(new File(srcPath), platform);
    }
    
    /**
     * Loads data from the save file represented by the {@code File} object
     * provided and returns a new {@code SaveFile} object containing the loaded
     * data.
     * 
     * @param src a {@code File} object representing the file to be loaded
     * @param platform the gaming platform that from which this save file was
     *                 created
     * @return a new {@code SaveFile} object containing the loaded data
     * @throws IOException if the file loaded is not a valid GTA III save file
     *                     or if an I/O error occurs
     * @throws UnsupportedPlatformException if the file originated from a gaming
     *                                      platform that is not supported
     */
    public static SaveFile load(File src, Platform platform)
            throws IOException
    {
        currentSaveFile = new SaveFile(src, platform);
        currentSaveFile.load();
        return currentSaveFile;
    }
    
    /**
     * Removes the current {@code SaveFile} instance.
     */
    public static void close()
    {
        currentSaveFile = null;
    }
    
    /**
     * Checks whether a save file is loaded.
     * 
     * @return {@code true} if a file is loaded, {@code false} if a file is not
     *         loaded
     */
    public static boolean isFileLoaded()
    {
        return currentSaveFile != null;
    }
    
    /**
     * Gets the current {@code SaveFile} object instance.
     * 
     * @return the current {@code SaveFile} instance, {@code null} if a save
     *         file is not loaded
     */
    public static SaveFile getCurrentSaveFile()
    {
        return currentSaveFile;
    }
    
    /*
     * Data block objects; keep these public so file data can be accessed from
     * anywhere.
     */
    public BlockSimpleVars simpleVars;
    public BlockPlayerPeds playerPeds;
    public BlockGarages garages;
    public BlockGangs gangs;
    public BlockPlayerInfo playerInfo;
    public BlockPedTypes pedTypes;
    
    private final DataBuffer buf;
    private final Platform platform;
    
    private File src;
    
    // Don't allow instantiation outside of this package
    protected SaveFile(File src) throws IOException
    {
        this.src = src;
        buf = new DataBuffer(src);
        this.platform = detectPlatform();
    }
    
    // Don't allow instantiation outside of this package
    protected SaveFile(File src, Platform platform) throws IOException
    {
        this.src = src;
        buf = new DataBuffer(src);
        this.platform = platform;
    }
    
    /**
     * Returns the {@code DataBuffer} object associated with this
     * {@code SaveFile} instance. The {@code DataBuffer} contains the raw binary
     * data of the save file and can be used to modify bytes directly.
     * 
     * @return the {@code DataBuffer} object associated with this
     *         {@code SaveFile}
     */
    public DataBuffer getDataBuffer()
    {
        return buf;
    }
    
    /**
     * Gets the gaming platform from which this save file originated.
     * 
     * @return the platform associated with this save file.
     */
    public Platform getPlatform()
    {
        return platform;
    }
    
    public File getSourceFile()
    {
        return src;
    }
    
    public String[] getFileInfo()
    {
        if (platform != Platform.PC) {
            return new String[] { };
        }
        
        Date timestamp;
        String saveName;
        String[] fileInfo = new String[2];
        
        VarString saveNameVar = new VarString16(24);
        saveNameVar.load(buf, 0x04);
        saveName = saveNameVar.getValue();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, buf.readShort());
        cal.set(Calendar.MONTH, buf.readShort() - 1);
        cal.set(Calendar.DAY_OF_WEEK, buf.readShort() + 1);
        cal.set(Calendar.DAY_OF_MONTH, buf.readShort());
        cal.set(Calendar.HOUR_OF_DAY, buf.readShort());
        cal.set(Calendar.MINUTE, buf.readShort());
        cal.set(Calendar.SECOND, buf.readShort());
        cal.set(Calendar.MILLISECOND, buf.readShort());
        timestamp = cal.getTime();
        
        fileInfo[0] = saveName;
        fileInfo[1] = new SimpleDateFormat(TIMESTAMP_FORMAT).format(timestamp);
        
        return fileInfo;
    }
    
    public int getCRC32()
    {
        return Checksum.crc32(buf.toArray());
    }
    
    public int getSourceFileCRC32() throws IOException
    {
        DataBuffer tempBuf = new DataBuffer(src);
        return Checksum.crc32(tempBuf.toArray());
    }
    
    public void save() throws IOException
    {
        save(src);
    }
    
    public void save(File dest) throws IOException
    {
        /* Change source file so subsequent calls to save() will write to the
           same file */
        if (dest != src) {
            src = dest;
        }
        
        String timestampSetting = Settings.get(Settings.Key.TIMESTAMP_FILES);
        if (Boolean.parseBoolean(timestampSetting)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            Timestamp t = simpleVars.timestamp;
            t.nYear.setValue((short)cal.get(Calendar.YEAR));
            t.nMonth.setValue((short)(cal.get(Calendar.MONTH) + 1));
            t.nDayOfWeek.setValue((short)(cal.get(Calendar.DAY_OF_WEEK) - 1));
            t.nDay.setValue((short)cal.get(Calendar.DAY_OF_MONTH));
            t.nHour.setValue((short)cal.get(Calendar.HOUR_OF_DAY));
            t.nMinute.setValue((short)cal.get(Calendar.MINUTE));
            t.nSecond.setValue((short)cal.get(Calendar.SECOND));
            t.nMillisecond.setValue((short)cal.get(Calendar.MILLISECOND));
            Logger.info("File timestamp updated: %s\n", cal.getTime());
        }
        
        int checksum = Checksum.checksum32(buf.toArray(0, buf.getSize() - 4));
        Logger.debug("File checksum: 0x%08x\n", checksum);
        buf.seek(buf.getSize() - 4);
        Logger.debug("Writing checksum to offset 0x%08x...\n", buf.getOffset());
        buf.writeInt(checksum);        
        
        buf.writeFile(src);
    }
    
    /*
     * Loads block data.
     */
    private void load() throws UnsupportedPlatformException, IOException
    {
        int blockIndex;
        int blockSize;
        int offset;
        
        if (!platform.isSupported()) {
            throw new UnsupportedPlatformException("platform not supported: "
                    + platform);
        }
        
        blockIndex = 0;
        buf.seek(0);
        while (buf.available() > 4) {
            blockSize = buf.readInt();
            offset = buf.getOffset();
            switch (blockIndex++) {
                case 0:
                    Logger.debug("Loading block 0...");
                    simpleVars = new BlockSimpleVars(blockSize);
                    simpleVars.load(buf, offset, platform);
                    break;
                    
                case 1:
                    Logger.debug("Loading block 1...");
                    playerPeds = new BlockPlayerPeds(blockSize);
                    playerPeds.load(buf, offset, platform);
                    break;
                    
                case 2:
                    Logger.debug("Loading block 2...");
                    garages = new BlockGarages(blockSize);
                    garages.load(buf, offset, platform);
                    break;
                    
                case 12:
                    Logger.debug("Loading block 12...");
                    gangs = new BlockGangs(blockSize);
                    gangs.load(buf, offset, platform);
                    break;
                    
                case 16:
                    Logger.debug("Loading block 16...");
                    playerInfo = new BlockPlayerInfo(blockSize);
                    playerInfo.load(buf, offset, platform);
                    break;
                    
                case 19:
                    Logger.debug("Loading block 19...");
                    pedTypes = new BlockPedTypes(blockSize);
                    pedTypes.load(buf, offset, platform);
                    break;
            }
            buf.seek(offset);
            buf.skip(blockSize);
        }
        
        String makeBackupsSetting = Settings.get(Settings.Key.MAKE_BACKUPS);
        if (Boolean.parseBoolean(makeBackupsSetting)) {
            File original = src;
            File backupFile = new File(original.getAbsolutePath() + ".bak");
            
            Logger.info("Creating backup file at %s...\n", backupFile);
            save(backupFile);
            
            // Revert "src" back to original file because save() reassigns "src"
            src = original;
        }
    }
    
    /*
     * Detects the gaming platform from which this file originated using the
     * offset of the "SCR" block signature, the offset of the "201729" constant
     * that appears in every save file, and the size of block 1 (PlayerPeds).
     */
    private Platform detectPlatform() throws IOException
    {
        // This value is present in block 0 of every save file.
        // Its purpose is unknown.
        int unknownConstant = 201729;
        
        int scrOffset;
        int block1Size;
        boolean isPCorXbox = false;
        boolean isMobile = false;
        Platform p = null;
        
        // Get offset of "SCR" block signature.
        scrOffset = buf.searchFor(new byte[] {'S', 'C', 'R', '\0'});
        if (scrOffset == -1) {
            throw new IOException("invalid save file");
        }
        
        // Determine platform by "SCR" offset and unknown constant offset.
        if (scrOffset == 0xB8
                && buf.readInt(0x34) == unknownConstant) {
            isMobile = true;
        } else if (scrOffset == 0xC4
                && buf.readInt(0x44) == unknownConstant) {
            isPCorXbox = true;
        } else if (scrOffset == 0xB8
                && buf.readInt(0x04) == unknownConstant) {
            p = Platform.PS2;
        } else {
            throw new IOException("invalid save file");
        }
        
        // Determine platform by size of block 1.
        block1Size = buf.readInt(buf.readInt(0x00) + 0x04);
        if (isMobile) {
            if (block1Size == 0x064C) {
                p = Platform.ANDROID;
            } else if (block1Size == 0x0648) {
                p = Platform.IOS;
            }
        } else if (isPCorXbox) {
            if (block1Size == 0x0624) {
                p = Platform.PC;
            } else if (block1Size == 0x0628) {
                p = Platform.XBOX;
            }
        }
        
        if (p == null) {
            throw new IOException("invalid save file");
        }
        
        return p;
    }
    
    /**
     * Constants representing the possible gaming platforms from which a save
     * file can originate.
     */
    public static enum Platform
    {
        ANDROID(true, "Android"),
        IOS(true, "iOS"),
        PC(true, "PC"),
        PS2(false, "PS2"),
        XBOX(false, "Xbox");
        
        private final boolean isSupported;
        private final String friendlyName;
        
        private Platform(boolean isSupported, String friendlyName)
        {
            this.isSupported = isSupported;
            this.friendlyName = friendlyName;
        }
        
        /**
         * Checks whether the platform is supported.
         * 
         * @return {@code true} if the platform is supported, {@code false}
         *         otherwise
         */
        public boolean isSupported()
        {
            return isSupported;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
}
