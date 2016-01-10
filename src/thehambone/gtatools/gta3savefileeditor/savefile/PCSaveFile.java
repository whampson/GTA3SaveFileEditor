package thehambone.gtatools.gta3savefileeditor.savefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.Settings;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Align;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.Block;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block00;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block01;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block02;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block12;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block16;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.blockdefs.Block19;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAShort;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * This class represents a save file originating from the PC version of GTA III.
 * The load/save functions and well as the block structure are programmed to
 * match up with save files created by the PC version of GTA III. Attempting to
 * load a save file that was created by a different platform will most likely
 * result in failure.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 06, 2015
 */
public class PCSaveFile extends SaveFile
{
    public static final String FILE_NAME_REGEX = "^GTA3sf([0-9]).b$";
    public static final String FILE_NAME_SIMPLE_FORMAT = "GTA3sf#.b";
    
    protected static final int FILE_SIZE                = 201820;
    protected static final int NUMBER_OF_DATA_BLOCKS    = 20;
    protected static final int MAX_PADDING_BLOCK_SIZE   = 55000;
    protected static final int MAX_NUMBER_OF_PAD_BLOCKS = 4;
    
    public static String[] getSaveFileInfo(File file) throws IOException
    {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getName() + " doesn't exist");
        }
        // todo: better detection of valid save files
        if (file.length() < FILE_SIZE) {
            throw new IOException(file.getName() + " is not a valid save file");
        }
        byte[] buffer = new byte[68];   // Name and timestamp
        try (FileInputStream fIn = new FileInputStream(file)) {
            fIn.read(buffer);
        }
        
        SaveFileInputStream in = new SaveFileInputStream(buffer);
        in.skip(4);     // skip block size
        String name = in.readStringUTF16(24);
        short[] timestampRaw = new short[8];
        in.readShort(timestampRaw);
        Date timestamp = timestampToDate(timestampRaw);
        
        String[] fileInfo = new String[2];
        fileInfo[0] = name;
        fileInfo[1] = new SimpleDateFormat("dd MMM. yyyy HH:mm:ss").format(timestamp);
        return fileInfo;
    }
    
    public static Date timestampToDate(short[] timestamp)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, timestamp[0]);
        cal.set(Calendar.MONTH, timestamp[1] - 1);
        cal.set(Calendar.DAY_OF_WEEK, timestamp[2] + 1);
        cal.set(Calendar.DAY_OF_MONTH, timestamp[3]);
        cal.set(Calendar.HOUR_OF_DAY, timestamp[4]);
        cal.set(Calendar.MINUTE, timestamp[5]);
        cal.set(Calendar.SECOND, timestamp[6]);
        cal.set(Calendar.MILLISECOND, timestamp[7]);
        return cal.getTime();
    }
    
    protected PCSaveFile(VariableDefinitions vars)
    {
        super(Platform.PC, vars);
    }
    
    @Override
    public int load(File saveFile) throws IOException
    {
        int bytesRead = 0;
        int blockInFileIndex = -1;
        int definedBlockIndex = 0;
        Block[] definedBlocks = generateFileStructure();
        SaveFileInputStream in = new SaveFileInputStream(saveFile);
        
        while (in.available() > 4) {        // skip checksum
            if (++blockInFileIndex < NUMBER_OF_DATA_BLOCKS) {
                // Data block
                if (definedBlockIndex < definedBlocks.length) {
                    Block block = definedBlocks[definedBlockIndex];
                    if (block.getIdentifier().getIndex() == blockInFileIndex) {
                        Logger.info("0x%08x: Reading block %d (\"%s\")...\n", in.getPointer(), blockInFileIndex, block.getIdentifier().getFriendlyName());
                        block.setExpectedSize(in.readInt());
                        bytesRead += 4;
                        bytesRead += block.load(in);
                        unknownData.put(blockInFileIndex, block.getAlignData());
                        definedBlockIndex++;
                        continue;
                    }
                }
                int expectedSize = in.readInt();
                bytesRead += 4;
                Align skippedBlock = new Align(platform, expectedSize, expectedSize, expectedSize, expectedSize, expectedSize);
                Logger.info("0x%08x: Reading block %d...\n", in.getPointer(), blockInFileIndex);
                int skippedBytes = skippedBlock.load(in);
                bytesRead += skippedBytes;
                skippedData.put(blockInFileIndex, skippedBlock);
            } else {
                // Padding block
                int expectedSize = in.readInt();
                bytesRead += 4;
                Logger.debug("0x%08x: Reading block %d (padding)...\n", in.getPointer(), blockInFileIndex);
                int skippedBytes = in.skip(expectedSize);
                bytesRead += skippedBytes;
            }
        }
        in.readInt();     // checksum; not needed
        bytesRead += 4;
        currentFile = saveFile;
        return bytesRead;
    }
    
    @Override
    public int save(File saveFile) throws IOException
    {
        // Pre-save operations
//        vars.iGlobalVariablesSpaceSize.setValue(new GTAInteger(vars.aGlobalVariables.getSizeInBytes()));
        String updateTimestampProperty = Settings.get("update.timestamp");
        if (updateTimestampProperty != null && Boolean.parseBoolean(updateTimestampProperty)) {
            Calendar now = new GregorianCalendar();
            vars.aTimestamp.setValueAt(0, new GTAShort((short)now.get(Calendar.YEAR)));
            vars.aTimestamp.setValueAt(1, new GTAShort((short)(now.get(Calendar.MONTH) + 1)));
            vars.aTimestamp.setValueAt(2, new GTAShort((short)(now.get(Calendar.DAY_OF_WEEK) - 1)));
            vars.aTimestamp.setValueAt(3, new GTAShort((short)now.get(Calendar.DAY_OF_MONTH)));
            vars.aTimestamp.setValueAt(4, new GTAShort((short)now.get(Calendar.HOUR_OF_DAY)));
            vars.aTimestamp.setValueAt(5, new GTAShort((short)now.get(Calendar.MINUTE)));
            vars.aTimestamp.setValueAt(6, new GTAShort((short)now.get(Calendar.SECOND)));
            vars.aTimestamp.setValueAt(7, new GTAShort((short)now.get(Calendar.MILLISECOND)));
        }
        
        int blockInFileIndex = 0;
        int bytesWritten = 0;
        int checksum = 0;
        int dataSize = 0;
        int definedBlockIndex = 0;
        int fileSize = 0;
        int paddingSize = 0;
        int padBytesRemaining = 0;
        Block[] definedBlocks = generateFileStructure();
        
        for (Block block : definedBlocks) {
            fileSize += 4;  // size of block
            fileSize += block.getSize();
        }
        for (Map.Entry e : skippedData.entrySet()) {
            Align skippedBlock = (Align)e.getValue();
            fileSize += 4;  // size of block
            fileSize += skippedBlock.getSize();
        }
        dataSize = fileSize;
        fileSize += 4;      // checksum
        if (fileSize < FILE_SIZE) {
            paddingSize = FILE_SIZE - (fileSize);
            fileSize += paddingSize;
        }
        
        paddingSize -= (4 * (int)Math.ceil((double)paddingSize / MAX_PADDING_BLOCK_SIZE));      // make room for size of pad block

        Logger.debug("Writing data to file: %s...\n", saveFile);
        SaveFileOutputStream out = new SaveFileOutputStream(fileSize);
        
        // Write data blocks
        for (blockInFileIndex = 0; blockInFileIndex < NUMBER_OF_DATA_BLOCKS; blockInFileIndex++) {
            if (definedBlockIndex < definedBlocks.length) {
                Block block = definedBlocks[definedBlockIndex];
                if (block.getIdentifier().getIndex() == blockInFileIndex) {
                    block.setAlignData(unknownData.get(blockInFileIndex));
                    bytesWritten += out.writeInt(block.getSize());
                    Logger.debug("0x%08x: Writing block %d (\"%s\")...\n", out.getPointer(), blockInFileIndex, block.getIdentifier().getFriendlyName());
                    bytesWritten += block.save(out);
                    definedBlockIndex++;
                    continue;
                }
            }
            Align skippedBlock = skippedData.get(blockInFileIndex);
            bytesWritten += out.writeInt(skippedBlock.getSize());
            Logger.debug("0x%08x: Writing block %d...\n", out.getPointer(), blockInFileIndex);
            bytesWritten += skippedBlock.save(out);
        }
        if (bytesWritten != dataSize) {
            throw new IOException(String.format("Actual number of bytes written does not match expected number."
                    + "Expected: 0x%04x, Actual: 0x%04x. At offset: 0x%04x", dataSize, bytesWritten, out.getPointer()));
        }
        
        // Write pad blocks
        padBytesRemaining = paddingSize;
        while (out.available() > 4) {
            int padBytesWritten = 0;
            byte[] padBlock = new byte[padBytesRemaining > MAX_PADDING_BLOCK_SIZE ? MAX_PADDING_BLOCK_SIZE : padBytesRemaining];
            bytesWritten += out.writeInt(padBlock.length);
            Logger.debug("0x%08x: Writing block %d (padding)...\n", out.getPointer(), blockInFileIndex++);
            padBytesWritten += out.write(padBlock);
            bytesWritten += padBytesWritten;
            padBytesRemaining -= padBytesWritten;
        }
        
        // Write checksum
        checksum = Checksum.calculateChecksum(out.toByteArray());
        Logger.debug("0x%08x: Writing checksum...\n", out.getPointer());
        bytesWritten += out.writeInt(checksum);
        
        out.writeToFile(saveFile);
        Logger.debug("Finished writing file. Bytes written: 0x%04x\n", bytesWritten);
        
        currentFile = saveFile;
        
        return bytesWritten;
    }
    
    @Override
    protected Block[] generateFileStructure()
    {
        Block[] blocks = new Block[] {
            new Block00(platform, vars),
            new Block01(platform, vars),
            new Block02(platform, vars),
            new Block12(platform, vars),
            new Block16(platform, vars),
            new Block19(platform, vars)
        };
        return blocks;
    }
}