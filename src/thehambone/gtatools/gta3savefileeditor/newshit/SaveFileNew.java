package thehambone.gtatools.gta3savefileeditor.newshit;

import java.io.File;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockPlayerInfo;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockPlayerPeds;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockSimpleVars;
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
public class SaveFileNew
{
    // Singleton object instance
    private static SaveFileNew currentSaveFile;
    
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
    public static SaveFileNew load(String srcPath) throws IOException
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
    public static SaveFileNew load(File src) throws IOException
    {
        currentSaveFile = new SaveFileNew(src);
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
    public static SaveFileNew load(String srcPath, Platform platform)
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
    public static SaveFileNew load(File src, Platform platform)
            throws IOException
    {
        currentSaveFile = new SaveFileNew(src, platform);
        currentSaveFile.load();
        return currentSaveFile;
    }
    
    public static void close()
    {
        currentSaveFile = null;
    }
    
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
    public static SaveFileNew getCurrentSaveFile()
    {
        return currentSaveFile;
    }
    
    /*
     * Block objects; keep these public so file data can be accessed from
     * anywhere.
     */
    public BlockSimpleVars simpleVars;
    public BlockPlayerPeds playerPeds;
    public BlockPlayerInfo playerInfo;
    
    private final DataBuffer buf;
    private final Platform platform;
    
    private File src;
    
    // Singleton class; keep this private
    private SaveFileNew(File src) throws IOException
    {
        this.src = src;
        buf = new DataBuffer(src);
        this.platform = detectPlatform();
    }
    
    // Singleton class; keep this private
    private SaveFileNew(File src, Platform platform) throws IOException
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
    private void load() throws UnsupportedPlatformException
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
                
                case 16:
                    Logger.debug("Loading block 16...");
                    playerInfo = new BlockPlayerInfo(blockSize);
                    playerInfo.load(buf, offset, platform);
                    break;
            }
            buf.seek(offset);
            buf.skip(blockSize);
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
            // TODO: rethink
            throw new IOException("unable to detect platform origin");
        } else {
            Logger.debug("Detected platfotm: " + p);
        }
        
        return p;
    }
    
    /**
     * Constants representing the possible gaming platforms from which a save
     * file can originate.
     */
    public static enum Platform
    {
        ANDROID(true),
        IOS(true),
        PC(true),
        PS2(false),
        XBOX(false);
        
        private final boolean isSupported;
        
        private Platform(boolean isSupported)
        {
            this.isSupported = isSupported;
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
    }
}
