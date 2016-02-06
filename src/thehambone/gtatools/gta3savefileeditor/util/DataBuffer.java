package thehambone.gtatools.gta3savefileeditor.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A {@code DataBuffer} is a fixed-length collection of bytes. This class allows
 * for the reading and writing of simple data structures to the buffer, which
 * makes it useful for editing a file.
 * <p>
 * A {@code DataBuffer} cannot be resized.
 * <p>
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public final class DataBuffer
{
    private final byte[] buf;
    
    private int offset;
    private int mark;
    
    /**
     * Creates a new {@code DataBuffer} object with an empty buffer of the
     * specified size.
     * 
     * @param size the size in bytes of the new buffer
     * @throws IllegalArgumentException if the buffer size is negative
     */
    public DataBuffer(int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException(
                    "buffer size cannot be negative");
        }
        
        buf = new byte[size];
        offset = 0;
        mark = 0;
    }
    
    /**
     * Creates a new {@code DataBuffer} and populates it with the data from the
     * specified file.
     * 
     * @param file the file to load
     * @throws IOException if an error occurs while reading the file
     */
    public DataBuffer(File file) throws IOException
    {
        if (!file.exists()) {
            throw new FileNotFoundException("file not found - " + file);
        }
        if (file.length() > Integer.MAX_VALUE) {
            throw new IOException("file is too large");
        }
        
        try {
            buf = new byte[(int)file.length()];
            offset = 0;
            mark = 0;
        } catch (OutOfMemoryError ex) {
            // Not sure if this is the correct way to handle large files...
            throw new IOException("file is too large", ex);
        }
        
        // Load the file into memory
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(buf, 0, buf.length);
        }
    }
    
    /**
     * Creates a new {@code DataBuffer} and populates it with the data from the
     * specified stream.
     * 
     * @param stream the stream to read from
     * @throws IOException if an error occurs while reading the stream
     */
    public DataBuffer(InputStream stream) throws IOException
    {
        buf = getStreamBytes(stream);
        offset = 0;
        mark = 0;
    }
    
    /**
     * Loads the stream into a byte array.
     * 
     * @param stream the stream to load
     * @return the stream data as a byte array
     * @throws IOException if an error occurs while reading the stream
     */
    private byte[] getStreamBytes(InputStream stream) throws IOException
    {
        BufferedInputStream bin = new BufferedInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int bytesRead;
        byte[] readBuffer = new byte[4096];
        
        try {
            while ((bytesRead = bin.read(readBuffer)) != -1) {
                baos.write(readBuffer, 0, bytesRead);
            }
        } catch (OutOfMemoryError ex) {
            // Not sure if this is the correct way to handle large streams...
            throw new IOException("stream is too large", ex);
        }
        
        return baos.toByteArray();
    }
    
    /**
     * Writes the contents of the buffer to the specified file. If the file
     * already exists, its <b>data will be overwritten</b>.
     * 
     * @param f the {@code File} object representing the file to be written
     * @throws IOException if an error occurs while writing the file
     */
    public void writeFile(File f) throws IOException
    {
        writeFile(f, 0, buf.length);
    }
    
    /**
     * Writes {@code len} bytes from the specified offset in the buffer to the
     * specified file. If the file already exists, its <b>data will be
     * overwritten</b>.
     * 
     * @param f the {@code File} object representing the file to be written
     * @param off the position in the buffer of the first byte that will be
     *        written to the file
     * @param len the number of bytes to write to the file
     * @throws IOException if an error occurs while writing the file
     * @throws IndexOutOfBoundsException if {@code off + len} exceeds the
     *         boundaries of the buffer
     */
    public void writeFile(File f, int off, int len) throws IOException
    {
        rangeCheck(off + len);
        
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        
        try (FileOutputStream out = new FileOutputStream(f)) {
            out.write(buf, off, len);
        }
    }
    
    /**
     * Searches the buffer for a specific byte sequence.
     * 
     * @param toFind the byte sequence to look for
     * @return the offset of the first byte of the byte sequence if found,
     *         {@code -1} of the sequence is not found
     */
    public int searchFor(byte[] toFind)
    {
        int pos = -1;
        boolean found = false;
        
        for (int i = 0; i < buf.length; i++) {
            if (toFind[0] != buf[i]) {
                continue;
            }
            for (int j = 1; j < toFind.length; j++) {
                if (toFind[j] != buf[i + j]) {
                    break;
                } else if (j == toFind.length - 1) {
                    found = true;
                }
            }
            if (found) {
                pos = i;
                break;
            }
        }
        
        return pos;
    }
    
    /**
     * Returns the number of bytes remaining in the buffer from the current
     * position.
     * 
     * @return the number of bytes left in the buffer from the current offset
     */
    public int available()
    {
        return buf.length - offset;
    }
    
    /**
     * Sets the buffer mark at the current position. When {@link #reset()} is
     * called, the buffer position will be reset to this mark.
     */
    public void mark()
    {
        mark = offset;
    }
    
    /**
     * Moves the buffer position to the current mark. Use {@link #mark()} to set
     * the mark.
     */
    public void reset()
    {
        seek(mark);
    }
    
    /**
     * Sets the mark to 0 (the start of the buffer).
     */
    public void resetMark()
    {
        mark = 0;
    }
    
    /**
     * Moves to a specific position in the buffer. Data will be read from this
     * offset the next time {@link #read()} (or any of its derivatives) is
     * called, or written to this offset the next time {@link #write(byte)} (or
     * any of its derivatives) is called.
     * 
     * @param pos the new position; cannot be negative or greater than the
     *        size of the buffer
     * @return the number of bytes skipped; will be negative if the pointer is
     *         moved backwards from its current position
     * @throws IndexOutOfBoundsException if the new position is out of bounds
     */
    public int seek(int pos)
    {
        rangeCheck(pos);
        
        int diff = pos - offset;
        offset = pos;
        return diff;
    }
    
    /**
     * Moves the data pointer forward or back {@code n} bytes. Data will be
     * written to this offset the next time {@code read()} or {@code write()} is
     * called.
     * 
     * @param n the number of bytes to skip; negative values will skip
     *        backwards; the resulting offset cannot be negative or exceed the
     *        buffer size
     * @return the new buffer position
     * @throws IndexOutOfBoundsException if the pointer is moved to an offset
     *         that is out of bounds
     */
    public int skip(int n)
    {
        if (n > available() || offset + n < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMessage(offset + n));
        }
        offset += n;
        return offset;
    }
    
    /**
     * Sets the data pointer to 0 (the start of the buffer).
     * <p>
     * This is a convenience method that is equivalent to calling
     * {@code seek(0)}. 
     * 
     * @return the buffer position before rewinding
     */
    public int rewind()
    {
        int oldOffset = offset;
        offset = 0;
        return oldOffset;
    }
    
    /**
     * Returns the data contained within the buffer as an array of bytes.
     * <p>
     * <b>Because this method returns a reference to the buffer data, any
     * changes made to the returned array will be reflected on the buffer, and
     * vice versa.</b>
     * 
     * @return the data contained within the buffer as an array of bytes
     */
    public byte[] toArray()
    {
        return buf;
    }
    
    /**
     * Returns a chunk of the buffer as an array of bytes.
     * <p>
     * <b>Unlike {@link #toArray()}, the array returned is not backed by the
     * buffer, so changes made to this array will <u>not</u> be reflected in the
     * buffer.</b>
     * 
     * @param off the starting offset of the buffer chunk
     * @param len the length of the chunk
     * @return a chunk of the buffer as an array of bytes
     */
    public byte[] toArray(int off, int len)
    {
        if (off < 0 || len > buf.length) {
            throw new IllegalArgumentException("offset out of bounds: " + len);
        }
        if (len < 0 || len > buf.length) {
            throw new IllegalArgumentException("length out of bounds: " + len);
        }
        
        byte[] arr = new byte[len];
        System.arraycopy(buf, off, arr, 0, len);
        return arr;
    }
    
    /**
     * Returns the current position in the buffer.
     * 
     * @return the current offset in the buffer
     */
    public int getOffset()
    {
        return offset;
    }
    
    /**
     * Returns the number of bytes contained within the buffer.
     * 
     * @return the size of the buffer in bytes
     */
    public int getSize()
    {
        return buf.length;
    }
    
    /**
     * Returns the value of the byte at the current position in the buffer, then
     * moves the position forward by 1.
     * 
     * @return the byte value at the current position
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public byte read()
    {
        rangeCheck(offset + 1);
        
        return buf[offset++];
    }
    
    /**
     * Reads the next {@code n} bytes into the specified array. {@code n} is
     * defined as the size of the specified array. {@code n} cannot exceed the
     * number of bytes available in the buffer.
     * 
     * @param b the byte buffer into which the next {@code n} bytes will be read
     * @return the number of bytes read
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public int read(byte[] b)
    {
        int bytesRead = 0;
        for (int i = 0; i < b.length; i++) {
            b[i] = read();
            bytesRead++;
        }
        return bytesRead;
    }
    
    /**
     * See {@link #readBoolean8()}.
     * 
     * @return the next four bytes as a {@code boolean} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public boolean readBoolean()
    {
        return readBoolean8();
    }
    
    /**
     * Reads the next byte (8 bits) and interprets its value as a boolean value.
     * A byte value of 0 is defined as {@code false}, while anything else
     * (typically 1) is defined as {@code true}.
     * 
     * @return the next byte as a {@code boolean} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public boolean readBoolean8()
    {
        return read() != 0;
    }
    
    /**
     * Reads the next two bytes (16 bits) from the buffer and interprets their
     * combined value as a boolean value. A value of 0 is defined as
     * {@code false}, while anything else (typically 1) is defined as
     * {@code true}.
     * 
     * @return the next two bytes as a {@code boolean} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public boolean readBoolean16()
    {
        return readShort() != 0;
    }
    
    /**
     * Reads the next four bytes (32 bits) from the buffer and interprets their
     * combined value as a boolean value. A value of 0 is defined as
     * {@code false}, while anything else (typically 1) is defined as
     * {@code true}.
     * 
     * @return the next four bytes as a {@code boolean} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public boolean readBoolean32()
    {
        return readInt() != 0;
    }
    
    /**
     * Reads the next four bytes from the buffer and interprets their combined
     * value as a 32-bit floating-point number. In a Grand Theft Auto save file,
     * floating-point numbers are stored using the IEEE 754 floating-point
     * standard.
     * 
     * @return the next four bytes as a {@code float} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     * @see <a href="https://en.wikipedia.org/wiki/IEEE_floating_point">
     *      IEEE 754 on Wikipedia</a>
     */
    public float readFloat()
    {
        return Float.intBitsToFloat(readInt());
    }
    
    /**
     * Reads the next four bytes from the buffer and interprets their combined
     * value as a signed integer.
     * 
     * @return the next four bytes as an {@code int} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public int readInt()
    {
        // Little endian
        return read() & 0xFF
                | (read() & 0xFF) << 8
                | (read() & 0xFF) << 16
                | (read() & 0xFF) << 24;
    }
    
    /**
     * Reads four bytes from the buffer at the specified offset and interprets
     * their combined value as a signed integer. The current position in the
     * buffer will not be changed after reading.
     * <p>
     * This is a convenience method equivalent to the following, where
     * {@code off} is an {@code int} representing an index in the buffer:<br>
     * &nbsp;{@code mark();}<br>
     * &nbsp;{@code seek(off);}<br>
     * &nbsp;{@code readInt();}<br>
     * &nbsp;{@code reset();}
     * 
     * @param off the position in the buffer where the {@code int} will be read
     *            from
     * @return the next four bytes as an {@code int} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public int readInt(int off)
    {
        int mark0;
        int value;
        
        mark0 = offset;
        offset = off;
        value = readInt();
        offset = mark0;
        
        return value;
    }
    
    /**
     * Reads the next two bytes from the buffer and interprets their combined
     * value as a signed short integer.
     * 
     * @return the next two bytes as a {@code short} value
     * @throws IndexOutOfBoundsException if an attempt is made to read past the
     *         end of the buffer
     */
    public short readShort()
    {
        // Little endian
        return (short) (read() & 0xFF
                | (read() & 0xFF) << 8);
    }
    
    /**
     * Puts the specified byte value at the current position, moves the position
     * forward by 1.
     * 
     * @param b the byte to write
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void write(byte b)
    {
        rangeCheck(offset + 1);
        
        buf[offset++] = b;
    }
    
    /**
     * Writes {@code n} bytes from the specified array to the buffer starting at
     * the current position. {@code n} is defined as the size of the array.
     * {@code n} cannot exceed the number of bytes available in the buffer.
     * 
     * @param b the byte array from which the next {@code n} bytes will be
     *          written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void write(byte[] b)
    {
        for (int i = 0; i < b.length; i++) {
            write(b[i]);
        }
    }
    
    /**
     * See {@link #writeBoolean8(boolean)}.
     * 
     * @param bool the {@code boolean} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeBoolean(boolean bool)
    {
        writeBoolean8(bool);
    }
    
    /**
     * Writes a boolean value to the buffer at the current offset as a single
     * byte (8 bits). {@code false} is written as 0, {@code true} is written as
     * 1.
     * 
     * @param bool the {@code boolean} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeBoolean8(boolean bool)
    {
        write((byte) (bool ? 1 : 0));
    }
    
    /**
     * Writes a boolean value to the buffer at the current offset as two bytes
     * (16 bits). {@code false} is written as 0, {@code true} is written as 1.
     * 
     * @param bool the {@code boolean} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeBoolean16(boolean bool)
    {
        writeShort((short) (bool ? 1 : 0));
    }
    
    /**
     * Writes a boolean value to the buffer at the current offset as four bytes
     * (32 bits). {@code false} is written as 0, {@code true} is written as 1.
     * 
     * @param bool the {@code boolean} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeBoolean32(boolean bool)
    {
        writeInt(bool ? 1 : 0);
    }
    
    /**
     * Writes a 32-bit floating-point value to the buffer at the current offset
     * as four bytes. In a Grand Theft Auto save file, floating-point numbers
     * are stored using the IEEE 754 floating-point standard.
     * 
     * @param f the {@code float} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     * @see <a href="https://en.wikipedia.org/wiki/IEEE_floating_point">
     *      IEEE 754 on Wikipedia</a>
     */
    public void writeFloat(float f)
    {
        writeInt(Float.floatToRawIntBits(f));
    }
    
    /**
     * Writes a signed integer value to the buffer at the current offset as four
     * bytes.
     * 
     * @param i the {@code int} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeInt(int i)
    {
        // Little endian
        write((byte) i);
        write((byte) (i >>> 8));
        write((byte) (i >>> 16));
        write((byte) (i >>> 24));
    }
    
    /**
     * Writes a signed short integer value to the buffer at the current offset
     * as two bytes.
     * 
     * @param s the {@code short} value to be written
     * @throws IndexOutOfBoundsException if an attempt is made to write past the
     *         end of the buffer
     */
    public void writeShort(short s)
    {
        // Little endian
        write((byte) s);
        write((byte) (s >>> 8));
    }
    
    /**
     * Checks if an index exists within the buffer. Throws an
     * IndexOutOfBoundsException if the index is out of range.
     */
    private void rangeCheck(int index)
    {
        if (index < 0) {
            throw new IndexOutOfBoundsException("negative offset not allowed: "
                    + index);
        } if (index > buf.length) {
            throw new IndexOutOfBoundsException(outOfBoundsMessage(index));
        }
    }
    
    /**
     * Creates an "out of bounds" message given the specified index.
     */
    private String outOfBoundsMessage(int index)
    {
        return "Index: " + index + ", Size: " + buf.length;
    }
}
