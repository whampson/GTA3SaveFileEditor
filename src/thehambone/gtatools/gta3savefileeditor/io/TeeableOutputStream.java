package thehambone.gtatools.gta3savefileeditor.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows data to be written to multiple streams.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 12, 2015
 * @deprecated 
 */
public class TeeableOutputStream extends OutputStream
{
    private final StringBuffer buffer;
    private final List<OutputStream> tees;
    
    private boolean isClosed = false;
    
    public TeeableOutputStream()
    {
        buffer = new StringBuffer();
        tees = new ArrayList<>();
    }
    
    public TeeableOutputStream(OutputStream... tees)
    {
        buffer = new StringBuffer();
        this.tees = new ArrayList<>();
        this.tees.addAll(Arrays.asList(tees));
    }
    
    public void addTee(OutputStream tee)
    {
        if (!tees.contains(tee)) {
            tees.add(tee);
        }
    }
    
    public String getBuffer()
    {
        return buffer.toString();
    }
    
    @Override
    public void close() throws IOException
    {
        isClosed = true;
        flush();
        tees.clear();
    }
    
    @Override
    public void flush() throws IOException
    {
        for (OutputStream tee : tees) {
            tee.write(buffer.toString().getBytes("UTF-8"), 0, buffer.length());
            tee.flush();
        }
        buffer.setLength(0);
    }

    @Override
    public void write(int b) throws IOException
    {
        if (isClosed) {
            throw new IOException("stream is closed");
        }
        buffer.append((char)b);
    }
}