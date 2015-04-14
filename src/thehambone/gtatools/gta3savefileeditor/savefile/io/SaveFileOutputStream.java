package thehambone.gtatools.gta3savefileeditor.savefile.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A SaveFileOutputStream writes data of various types to a GTA III save file
 * buffer. The data written to the buffer is formatted such that it matches up
 * with the formatting of a save file created by the GTA III game executable.
 * Data is written with little-endian byte order, meaning the least significant
 * byte is stored first.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 15, 2015
 */
public class SaveFileOutputStream
{
    private final byte[] buffer;
    
    private int pointer = 0;
    
    /**
     * Creates a new SaveFileOutputStream with the specified buffer size.
     * 
     * @param size the size of the buffer where data will be written
     */
    public SaveFileOutputStream(int size)
    {
        this.buffer = new byte[size];
    }
    
    /**
     * Gets the number of bytes available to write in the buffer.
     * 
     * @return the number of bytes available to write from the current offset
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
     * Gets the current value of the buffer pointer. The input stream pointer is
     * a value containing the offset of the next byte to be written to the
     * buffer. Each time one of the write() methods is called, the pointer is
     * incremented n bytes. The pointer can be adjusted using the seek() and
     * skip() functions.
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
     * Gets the buffer as an array of bytes.
     * 
     * @return the buffer as an array of bytes
     */
    public byte[] toByteArray()
    {
        return buffer;
    }
    
    /**
     * Writes this gamesave buffer to a file.
     * 
     * @param f the file to write
     * @throws IOException if an I/O error occurs
     */
    public void writeToFile(File f) throws IOException
    {
        try (FileOutputStream fOut = new FileOutputStream(f)) {
            fOut.write(buffer);
        }
    }
    
    /**
     * Writes a byte to the buffer at the current offset. A byte is an 8-bit
     * signed type. After the byte is written, the buffer pointer is incremented
     * by 1.
     * 
     * @param b the byte value to write
     * @return the number of bytes written
     * @throws IOException if the current buffer pointer is greater than or
     *         equal to the size of the buffer
     */
    public int write(byte b) throws IOException
    {
        if (pointer >= buffer.length) {
            throw new EOFException("attempted to write past end of buffer");
        }
        buffer[pointer++] = b;
        return 1;
    }
    
     /**
     * Writes an array of bytes to the buffer. The number of bytes written will
     * be equal to the size of the array.
     * 
     * @param bBuf an array of bytes write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int write(byte[] bBuf) throws IOException
    {
        int bytesWritten;
        for (bytesWritten = 0; bytesWritten < bBuf.length; bytesWritten++) {
            write(bBuf[bytesWritten]);
        }
        return bytesWritten;
    }
    
    /**
     * Writes a boolean value to the buffer as a single byte. False is written
     * as 0, true is written as 1.
     * 
     * @param bool the boolean value to be written to the buffer
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeBoolean8(boolean bool) throws IOException
    {
        return write(bool ? (byte)1 : (byte)0);
    }
    
    /**
     * Writes a boolean value to the buffer as 2 bytes. False is written as 0,
     * true is written as 1.
     * 
     * @param bool the boolean value to be written to the buffer
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeBoolean16(boolean bool) throws IOException
    {
        return writeShort(bool ? (short)1 : (short)0);
    }
    
    /**
     * Writes a boolean value to the buffer as 4 bytes. False is written as 0,
     * true is written as 1.
     * 
     * @param bool the boolean value to be written to the buffer
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeBoolean32(boolean bool) throws IOException
    {
        return writeInt(bool ? 1 : 0);
    }
    
    /**
     * Writes a char to the buffer. A char is a 16-bit unsigned type.
     * 
     * @param c the char value to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeChar(char c) throws IOException
    {
        int bytesWritten = 0;
        bytesWritten += write((byte)(c >>> 0));
        bytesWritten += write((byte)(c >>> 8));
        return bytesWritten;
    }
    
    /**
     * Writes an array of chars to the buffer. The number of bytes written will
     * be equal to twice the size of the array. This is because a char is 16
     * bits wide.
     * 
     * @param cBuf an array of chars to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeChar(char[] cBuf) throws IOException
    {
        int bytesWritten = 0;
        for (int i = 0; i < cBuf.length; i++) {
            bytesWritten += writeChar(cBuf[i]);
        }
        return bytesWritten;
    }
    
    /**
     * Writes a float to the buffer. In a GTA III save file, floating-point
     * values are represented in binary using the IEEE 754 floating-point
     * standard. The supplied float-point value is converted to a binary value
     * using Java's built-in IEEE 754 floating-point arithmetic functions.
     * 
     * @param f the float value to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeFloat(float f) throws IOException
    {
        return writeInt(Float.floatToRawIntBits(f));
    }
    
    /**
     * Writes an array of floats to the buffer. The number of bytes written will
     * be equal to four times the size of the array. This is because a float is
     * 32 bits wide.
     * 
     * @param fBuf an array of floats to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeFloat(float[] fBuf) throws IOException
    {
        int bytesWritten = 0;
        for (int i = 0; i < fBuf.length; i++) {
            bytesWritten += writeFloat(fBuf[i]);
        }
        return bytesWritten;
    }
    
    /**
     * Writes an int to the buffer. An int is a 32-bit unsigned type.
     * 
     * @param i the int value to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeInt(int i) throws IOException
    {
        int bytesWritten = 0;
        bytesWritten += write((byte)(i >>> 0));
        bytesWritten += write((byte)(i >>> 8));
        bytesWritten += write((byte)(i >>> 16));
        bytesWritten += write((byte)(i >>> 24));
        return bytesWritten;
    }
    
    /**
     * Writes an array of ints to the buffer. The number of bytes written will
     * be equal to four times the size of the array. This is because an int is
     * 32 bits wide.
     * 
     * @param iBuf an array of ints to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeInt(int[] iBuf) throws IOException {
        int bytesWritten = 0;
        for (int i = 0; i < iBuf.length; i++) {
            bytesWritten += writeInt(iBuf[i]);
        }
        return bytesWritten;
    }
    
    /**
     * Writes a short to the buffer. A short is a 16-bit signed type.
     * 
     * @param s the short value to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeShort(short s) throws IOException
    {
        int bytesWritten = 0;
        bytesWritten += write((byte)(s >>> 0));
        bytesWritten += write((byte)(s >>> 8));
        return bytesWritten;
    }
    
    /**
     * Writes an array of shorts to the buffer. The number of bytes written will
     * be equal to twice the size of the array. This is because a short is 16
     * bits wide.
     * 
     * @param sBuf an array of shorts to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeShort(short[] sBuf) throws IOException
    {
        int bytesWritten = 0;
        for (int i = 0; i < sBuf.length; i++) {
            bytesWritten += writeShort(sBuf[i]);
        }
        return bytesWritten;
    }
    
    /**
     * Writes a null-terminated string to the buffer, encoding the characters
     * using the UTF-8 character encoding. Because each string in a GTA III
     * save file is stored with a fixed size, the number of characters to write
     * needs to be supplied in order to preserve the correct formatting and
     * prevent a buffer overflow.
     * 
     * If the string does not contain a null terminator (0x00), the string will
     * be null terminated while still preserving the size of the string.
     * 
     * @param s the String to write
     * @param charsToWrite the number of characters from the string to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeStringUTF8(String s, int charsToWrite) throws IOException
    {
        byte[] sBuf = new byte[charsToWrite];
        if (s.length() >= charsToWrite) {
            s = nullTerminate(s.substring(0, charsToWrite - 1));
            sBuf = s.getBytes("UTF-8");
        } else {
            s = nullTerminate(s);
            byte[] utf8Buf = s.getBytes("UTF-8");
            for (int i = 0; i < utf8Buf.length; i++) {
                sBuf[i] = utf8Buf[i];
            }
            // Pad the rest of the string buffer with 0s
            for (int i = 0; i < charsToWrite - utf8Buf.length; i++) {
                sBuf[i + utf8Buf.length] = 0;
            }
        }
        return write(sBuf);
    }
    
    /**
     * Writes a null-terminated string to the buffer, encoding the characters
     * using the UTF-16 character encoding. Because each string in a GTA III
     * save file is stored with a fixed size, the number of characters to write
     * needs to be supplied in order to preserve the correct formatting and
     * prevent a buffer overflow.
     * 
     * If the string does not contain a null terminator (0x00 0x00), the string
     * will be null terminated while still preserving the size of the string.
     * 
     * @param s the String to write
     * @param charsToWrite the number of characters from the string to write
     * @return the number of bytes written
     * @throws IOException if an attempt is made to write past the end of the
     *         buffer
     */
    public int writeStringUTF16(String s, int charsToWrite) throws IOException
    {
        char[] sBuf = new char[charsToWrite];
        if (s.length() >= charsToWrite) {
            s = nullTerminate(s.substring(0, charsToWrite - 1));
            sBuf = s.toCharArray();
        } else {
            s = nullTerminate(s);
            char[] utf16Buf = s.toCharArray();
            for (int i = 0; i < utf16Buf.length; i++) {
                sBuf[i] = utf16Buf[i];
            }
            // Pad the rest of the string buffer with 0s
            for (int i = 0; i < charsToWrite - utf16Buf.length; i++) {
                sBuf[i + utf16Buf.length] = 0;
            }
        }
        return writeChar(sBuf);
    }
    
    /**
     * Adds a null character to the end of a string if there isn't already one
     * present.
     * 
     * @param s the String to null-terminate
     * @return the null-terminated String
     */
    private String nullTerminate(String s)
    {
        if (s.charAt(s.length() - 1) != '\0') {
            s += '\0';
        }
        return s;
    }
}