package thehambone.gtatools.gta3savefileeditor.savefile.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A SaveFileInputStream provides functionality for reading raw data from a
 * GTA III save file buffer. The data in a GTA III save file is stored with a
 * little endian byte order. This means that the least significant byte is
 * stored first in an array of bytes. A SaveFileInputStream accounts for the
 * byte order of the file format and allows data structures of various types to
 * be extracted from the file buffer.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 06, 2015
 * @deprecated 
 */
public class SaveFileInputStream
{
    private final byte[] buffer;
    
    private int pointer = 0;
    
    /**
     * Creates a new SaveFileInputStream using the supplied buffer.
     * 
     * @param buffer the file buffer from which data will be read
     */
    public SaveFileInputStream(byte[] buffer)
    {
        this.buffer = buffer;
    }
    
    /**
     * Creates a new SaveFileInputStream from a file.
     * 
     * @param f the file to load
     * @throws IOException if an I/O error occurs
     */
    public SaveFileInputStream(File f) throws IOException
    {
        this(f, f.length());
        
    }
    
    /**
     * Creates a new SaveFileInputStream with the specified size from a file.
     * 
     * @param f the file to load
     * @param bufferSize the number of bytes from the start of the file to load
     * into the buffer
     * @throws IOException if an I/O error occurs
     */
    public SaveFileInputStream(File f, long bufferSize) throws IOException
    {
        if (bufferSize > Integer.MAX_VALUE) {
            // Should never happen, but in case it does...
            throw new IOException("buffer is too large");
        }
        this.buffer = new byte[(int)bufferSize];
        try (FileInputStream fIn = new FileInputStream(f)) {
            int bytesRead = fIn.read(buffer);
            if (bytesRead != bufferSize) {
                throw new IOException("file was only partially read");
            }
        }
    }
    
    /**
     * Gets the number of bytes available to read in the buffer.
     * 
     * @return the number of bytes available to read from the current offset
     */
    public int available()
    {
        return buffer.length - pointer;
    }
    
    /**
     * Gets the size of the buffer in bytes.
     * 
     * @return the size of the buffer in bytes
     */
    public int getBufferSize()
    {
        return buffer.length;
    }
    
    /**
     * Gets the current value of the input stream pointer. The input stream
     * pointer is a value containing the offset of the next byte to be read
     * from the buffer. Each time one of the read() methods is called, the
     * pointer is incremented n bytes. The pointer can be adjusted using the
     * seek() and skip() functions.
     * 
     * @return the buffer offset
     */
    public int getPointer()
    {
        return pointer;
    }
    
    /**
     * Sets the buffer pointer to point to a specific offset in the buffer.
     * 
     * @param pos the desired buffer offset
     * @throws IOException if the position is less than zero or greater than or
     *         equal to the size of the buffer
     */
    public void seek(int pos) throws IOException
    {
        if (pos < 0) {
            throw new IOException("negative seek offset");
        }
        if (pos >= buffer.length) {
            throw new EOFException("attempted to seek past end of buffer");
        }
        pointer = pos;
    }
    
    /**
     * Skips n bytes from the current offset. This is done by adding n bytes to
     * the current buffer pointer. A negative skip value is allowed so long as
     * the resulting buffer offset isn't negative.
     * 
     * @param numBytes the number of bytes to skip over from the current buffer
     *        offset
     * @return the number of bytes skipped
     * @throws IOException if the resulting buffer pointer is negative or
     *         greater than or equal to the size of the buffer
     */
    public int skip(int numBytes) throws IOException
    {
        if ((pointer + numBytes) > buffer.length) {
            throw new EOFException("attempted to skip past end of buffer");
        }
        if ((pointer + numBytes) < 0) {
            throw new IOException("attempted to skip past start of buffer");
        }
        pointer += numBytes;
        return numBytes;
    }
    
    /**
     * Reads the next byte from the buffer at the current offset. A byte is an
     * 8-bit signed type. After the byte is read, the buffer pointer is
     * incremented by 1.
     * 
     * @return the byte read from the buffer
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public byte read() throws IOException
    {
        if (pointer >= buffer.length) {
            throw new EOFException("attempted to read past end of buffer");
        }
        byte b = buffer[pointer++];
        return b;
    }
    
    /**
     * Reads the the next set of bytes from the buffer and stores them in the
     * supplied array. Any values initially contained within the array will be
     * overwritten. The number of bytes read will be equal to the size of the
     * supplied array.
     * 
     * @param bBuf a byte array that will store the bytes read by the input
     *        stream
     * @return the number of bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int read(byte[] bBuf) throws IOException
    {
        int bytesRead;
        for (bytesRead = 0; bytesRead < bBuf.length; bytesRead++) {
            bBuf[bytesRead] = read();
        }
        return bytesRead;
    }
    
    /**
     * Reads the next single byte from the buffer and interprets its value as
     * either true or false. A value of 0 is false, while anything else is true.
     * 
     * @return a boolean whose status reflects the value of the byte read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public boolean readBoolean8() throws IOException
    {
        byte b = read();
        return b != 0;
    }
    
    /**
     * Reads the next 2 bytes from the buffer and interprets their combined
     * value as either true or false. A value of 0 is false, while anything else
     * is true.
     * 
     * @return a boolean whose status reflects the value of the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public boolean readBoolean16() throws IOException
    {
        short s = readShort();
        return s != 0;
    }
    
    /**
     * Reads the next 4 bytes from the buffer and interprets their combined
     * value as either true or false. A value of 0 is false, while anything else
     * is true.
     * 
     * @return a boolean whose status reflects the value of the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public boolean readBoolean32() throws IOException
    {
        int i = readInt();
        return i != 0;
    }
    
    /**
     * Reads the next 2 bytes from the buffer and returns their combined value
     * as a char. A char is a 16-bit unsigned type.
     * 
     * @return a char containing the combined value of the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public char readChar() throws IOException
    {
        char c = (char)
                (((read() & 0xFF) << 0)
                | ((read() & 0xFF) << 8));
        return c;
    }
    
    /**
     * Reads the the next set of chars from the buffer and stores them in the
     * supplied array. Any values initially contained within the array will be
     * overwritten. Since a char is 16 bits wide, the number of bytes read will
     * be twice the size of the supplied array.
     * 
     * @param cBuf a char array that will store the chars read by the input
     *        stream
     * @return the number of bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int readChar(char[] cBuf) throws IOException
    {
        int bytesRead = 0;
        for (int i = 0; i < cBuf.length; i++) {
            cBuf[i] = readChar();
            bytesRead += 2;                 // A char is 16-bits wide (2 bytes)
        }
        return bytesRead;
    }
    
    /**
     * Reads the next 4 bytes from the buffer and interprets their combined
     * value as a floating-point number. In a GTA III save file, floating-point
     * values are represented in binary using the IEEE 754 floating-point
     * standard. The bytes read are converted to a floating-point value using
     * Java's built-in IEEE 754 floating-point arithmetic functions.
     * 
     * @return a float type containing the floating-point value represented by
     *         the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public float readFloat() throws IOException
    {
        float f = Float.intBitsToFloat(readInt());
        return f;
    }
    
    /**
     * Reads the the next set of floats from the buffer and stores them in the
     * supplied array. Any values initially contained within the array will be
     * overwritten. Since a float is 32 bits wide, the number of bytes read is
     * four times the size of the supplied array.
     * 
     * @param fBuf a float array that will store the floats read by the input
     *        stream
     * @return the number of bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int readFloat(float[] fBuf) throws IOException
    {
        int bytesRead = 0;
        for (int i = 0; i < fBuf.length; i++) {
            fBuf[i] = readFloat();
            bytesRead += 4;                 // A float is 32-bits wide (4 bytes)
        }
        return bytesRead;
    }
    
    /**
     * Reads the next 4 bytes from the buffer and returns their combined value
     * as an int. An int is a 32-bit signed type.
     * 
     * @return an int containing the combined value of the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int readInt() throws IOException
    {
        int i = ((read() & 0xFF) << 0)
                | ((read() & 0xFF) << 8)
                | ((read() & 0xFF) << 16)
                | ((read() & 0xFF) << 24);
        return i;
    }
    
    /**
     * Reads the the next set of ints from the buffer and stores them in the
     * supplied array. Any values initially contained within the array will be
     * overwritten. Because an int is 32 bits wide, the number of bytes read
     * will be four times the size of the supplied array.
     * 
     * @param iBuf a int array that will store the ints read by the input
     *        stream
     * @return the number of bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int readInt(int[] iBuf) throws IOException
    {
        int bytesRead = 0;
        for (int i = 0; i < iBuf.length; i++) {
            iBuf[i] = readInt();
            bytesRead += 4;                 // An int is 32-bits wide (4 bytes)
        }
        return bytesRead;
    }
    
    /**
     * Reads the next 2 bytes from the buffer and interprets their combined
     * value as a short. A short is a 16-bit signed type.
     * 
     * @return a short containing the combined value of the bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public short readShort() throws IOException
    {
        short s = (short)
                (((read() & 0xFF) << 0)
                | ((read() & 0xFF) << 8));
        return s;
    }
    
    /**
     * Reads the the next set of shorts from the buffer and stores them in the
     * supplied array. Any values initially contained within the array will be
     * overwritten. Because a short is 16 bits wide, the number of bytes read
     * will be four times the size of the supplied array.
     * 
     * @param sBuf a short array that will store the shorts read by the input
     *        stream 
     * @return the number of bytes read
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public int readShort(short[] sBuf) throws IOException
    {
        int bytesRead = 0;
        for (int i= 0; i < sBuf.length; i++) {
            sBuf[i] = readShort();
            bytesRead += 2;                 // A short is 16-bits wide (2 bytes)
        }
        return bytesRead;
    }
    
    /**
     * Reads a sequence of bytes from the buffer and interprets their values as
     * characters in a UTF-8 string. After the bytes have been read, the byte
     * sequence is truncated after the first occurrence of a null character
     * (0x00). The bytes are then joined to create a null-terminated UTF-8
     * string.
     * 
     * Characters in a UTF-8 string are 8-bits wide, so the number of characters
     * read will always be equal to the number of actual bytes read by the
     * stream.
     * 
     * @param charsToRead the number of characters to read from the current
     *        buffer offset
     * @return a UTF-8-encoded String
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public String readStringUTF8(int charsToRead) throws IOException
    {
        byte[] bBuf = new byte[charsToRead];
        read(bBuf);
        return new String(bBuf, 0, indexOfFirstNull(bBuf), "UTF-8");
    }
    
    /**
     * Reads a sequence of bytes from the buffer and interprets their values as
     * characters in a UTF-16 string. After the bytes have been read, the byte
     * sequence is truncated after the first occurrence of a null character
     * (0x00 0x00). The bytes are then joined to create a null-terminated UTF-16
     * string.
     * 
     * Characters in a UTF-16 string are 16-bits wide, so the number of
     * characters read will be equal to twice the number of actual bytes read by
     * the stream.
     * 
     * @param charsToRead the number of characters (not bytes) to read from the
     *        current buffer offset
     * @return a UTF-16-encoded String
     * @throws IOException if an attempt is made to read past the end of the
     *         buffer
     */
    public String readStringUTF16(int charsToRead) throws IOException
    {
        char[] cBuf = new char[charsToRead];
        readChar(cBuf);
        return new String(cBuf, 0, indexOfFirstNull(cBuf));
    }
    
    /**
     * Gets the index of the first byte from the supplied array with a value of
     * 0. If no null bytes exist in the array, the index returned is equal to
     * the length of the array.
     * 
     * @param bBuf the byte array to be searched
     * @return the index of the first null byte
     */
    private int indexOfFirstNull(byte[] bBuf)
    {
        int index;
        for (index = 0; index < bBuf.length; index++) {
            if (bBuf[index] == 0) {
                return index;
            }
        }
        return index;
    }
    
    /**
     * Gets the index of the first char from the supplied array with a value of
     * 0. If no null chars exist in the array, the index returned is equal to
     * the length of the array.
     * 
     * @param cBuf the char array to be searched
     * @return the index of the first null char
     */
    private int indexOfFirstNull(char[] cBuf)
    {
        int index;
        for (index = 0; index < cBuf.length; index++) {
            if (cBuf[index] == 0) {
                return index;
            }
        }
        return index;
    }
}