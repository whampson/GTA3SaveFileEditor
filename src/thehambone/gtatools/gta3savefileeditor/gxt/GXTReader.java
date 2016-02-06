package thehambone.gtatools.gta3savefileeditor.gxt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;

/**
 * The purpose of this class is to read text data found within a Grand Theft
 * Auto III GXT file. GXT files contain all of the strings used in a given GTA
 * game. This reader is designed to handle GTA III-format GXT files only. The
 * file follows a key-value format where a unique, 8-character long key maps to
 * a variable-length UTF-16 string.
 * <p>
 * Created on Jan 16, 2016.
 *
 * @author thehambone
 */
public class GXTReader
{
    private static final byte[] TKEY_SIG = new byte[] { 'T', 'K', 'E', 'Y' };
    private static final byte[] TDAT_SIG = new byte[] { 'T', 'D', 'A', 'T' };
    private static final int SIZE_OF_TKEY_ENTRY = 0x0C;
    
    private final DataBuffer gxtBuf;
    
    /**
     * Create a new {@code GXTReader} instance from the specified file.
     * 
     * @param f the {@code File} object representing the file to read
     * @throws IOException if an I/O error occurs while loading the file
     */
    public GXTReader(File f) throws IOException
    {
        gxtBuf = new DataBuffer(f); // Loads file data into memory
    }
    
    /**
     * Creates a new {@code GXTReader} instance from the data in the specified
     * stream.
     * 
     * @param stream the data stream
     * @throws IOException if an I/O error occurs while reading the data stream
     */
    public GXTReader(InputStream stream) throws IOException
    {
        gxtBuf = new DataBuffer(stream); // Loads stream data into memory
    }
    
    /**
     * Extracts text data from the GXT file and returns it as a {@code Map}.
     * 
     * @return a {@code HashMap} containing the GXT keys and values
     * @throws IOException if the file is invalid
     */
    public Map<String, String> readFile() throws IOException
    {
        byte[] sigBuf = new byte[4];
        byte[] entryNameBuf = new byte[8];
        Map<String, String> gxt = new HashMap<>();
        
        gxtBuf.seek(0);
        
        // Read TKEY section header
        gxtBuf.read(sigBuf);
        int sizeOfTKEYSection = gxtBuf.readInt();
        
        // Check if section signature is correct
        if (!Arrays.equals(sigBuf, TKEY_SIG)) {
            throw new IOException("invalid GXT file");
        }
        
        // Read TDAT section header
        gxtBuf.mark();
        gxtBuf.seek(sizeOfTKEYSection + 0x08);
        gxtBuf.read(sigBuf);
        
        // Check if section signature is correct
        if (!Arrays.equals(sigBuf, TDAT_SIG)) {
            throw new IOException("invalid GXT file");
        }
        gxtBuf.reset();
        
        // Iterate through all TKEY entries and dereference GXT string
        int numEntries = sizeOfTKEYSection / SIZE_OF_TKEY_ENTRY;
        for (int i = 0; i < numEntries; i++) {
            int pValue = gxtBuf.readInt();
            gxtBuf.read(entryNameBuf);
            String key = new String(entryNameBuf).trim();
            String value = readGXTString(sizeOfTKEYSection, pValue);
            gxt.put(key, value);
        }
        
        // Return GXT hashmap
        return gxt;
    }
    
    /**
     * Dereferences a GXT string from the pointer found in the TKEY section.
     */
    private String readGXTString(int sizeOfTKEYSection, int pString)
    {
        // Move to offset of string in file buffer; preserve previous offset
        gxtBuf.mark();
        gxtBuf.seek(sizeOfTKEYSection + 0x10);
        gxtBuf.skip(pString);
        
        // Search for UTF-16 null terminator (0x00 0x00)
        int startOffset = gxtBuf.getOffset();
        while (gxtBuf.readShort() != 0) {
            continue;   // Keep looping until a 0 (null terminator) is found
        }
        
        // Determine size of UTF-16 string buffer
        int sizeOfEntry = gxtBuf.getOffset() - startOffset;
        byte[] entryBuf = new byte[sizeOfEntry];
        
        // Read UTF-16 string buffer
        gxtBuf.seek(startOffset);
        gxtBuf.read(entryBuf);
        
        // Convert UTF-16 string buffer into a String object
        String entry = new String(entryBuf, Charset.forName("UTF-16LE")).trim();
        
        // Move back to previous offset in file
        gxtBuf.reset();
        
        // Return the string
        return entry;
    }
}
