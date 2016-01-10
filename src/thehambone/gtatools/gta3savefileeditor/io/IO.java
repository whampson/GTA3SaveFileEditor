package thehambone.gtatools.gta3savefileeditor.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import thehambone.gtatools.gta3savefileeditor.Main;

/**
 * Methods for input and output.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 28, 2015
 */
public class IO
{
    private static final Document OUTPUT_DOCUMENT = new JTextArea().getDocument();
    private static final TeeableOutputStream ERR_STREAM = new TeeableOutputStream(System.err, new DocumentOutputStream(OUTPUT_DOCUMENT));
    private static final TeeableOutputStream OUT_STREAM = new TeeableOutputStream(System.out, new DocumentOutputStream(OUTPUT_DOCUMENT));
    private static final PrintWriter stderr = new PrintWriter(ERR_STREAM, true);
    private static final PrintWriter stdout = new PrintWriter(OUT_STREAM, true);
    private static final int FILE_COPY_BUFFER_SIZE = 1024 * 8;
    
    public static Document getOutputDocument()
    {
        return OUTPUT_DOCUMENT;
    }
    
    public static PrintWriter getStderr()
    {
        return stderr;
    }
    
    public static PrintWriter getStdout()
    {
        return stdout;
    }
    
//    public static void debug(String message)
//    {
//        debugf("%s\n", message);
//    }
    
//    public static void debugf(String format, Object... args)
//    {
//        if (Main.isDebugModeEnabled()) {
//            String s = String.format(format, args);
//            stdout.printf("[DEBUG]: %s", s);
//        }
//    }
//    
//    public static void error(String message)
//    {
//        errorf("%s\n", message);
//    }
//    
//    public static void error(String message, Throwable cause)
//    {
//        errorf("%s [%s]\n", message, cause);
//    }
//    
//    public static void errorf(String format, Object... args)
//    {
//        String s = String.format(format, args);
//        stderr.printf("[ERROR]: %s", s);
//    }
//    
//    public static void info(String message)
//    {
//        infof("%s\n", message);
//    }
//    
//    public static void infof(String format, Object... args)
//    {
//        String s = String.format(format, args);
//        stdout.printf("[ INFO]: %s",s);
//    }
    
    
//    public static void repeatChar(char ch, int n, PrintWriter out)
//    {
//        out.printf("%s", repeatChar(ch, n));
//    }
    
    public static String intToBinaryString(int num)
    {
        StringBuilder binaryBuilder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            binaryBuilder.append((num >>> (31 - i)) & 1);
            if ((i + 1) % 8 == 0) {
                binaryBuilder.append(" ");
            }
        }
        return binaryBuilder.toString().trim();
    }
    
    public static String hexDump(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        int offset = 0;
        do {
            sb.append(String.format("0x%04x  ", offset));
            int rowLength = Math.min(16, b.length - offset);
            for (int i = 0; i < rowLength; i++) {
                sb.append(String.format("%02x", b[offset + i]));
                if (i != rowLength - 1) {
                    sb.append(" ");
                }
            }
            for (int i = 0; i < (16 - rowLength) * 3; i++) {
                sb.append(" ");
            }
            sb.append("  ");
            for (int i = 0; i < rowLength; i++) {
                char c = (char)b[offset + i];
                if (c < 0x20 || c > 0x7f) {
                    sb.append(".");
                } else {
                    sb.append(c);
                }
            }
            offset += rowLength;
            if (offset < b.length) {
                sb.append("\n");
            }
        } while (offset < b.length);
        return sb.toString();
    }
    
    public static void hexDump(byte[] b, PrintWriter out)
    {
        String dump = hexDump(b);
        out.println(dump);
    }
    
    public static boolean copyFile(File src, File dest) throws IOException
    {
        if (!dest.exists()) {
            dest.getParentFile().mkdirs();
        }
        
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
        
        long srcFileSize = src.length();
        long totalBytesRead = 0;
        int bufferBytesRead = 0;
        
        byte[] buffer = new byte[FILE_COPY_BUFFER_SIZE];
        
        while ((bufferBytesRead = in.read(buffer)) != -1) {
            totalBytesRead += bufferBytesRead;
            out.write(buffer, 0, bufferBytesRead);
        }
        
        out.close();
        in.close();
        
        return totalBytesRead == srcFileSize;
    }
    
    public static ImageIcon loadImageResource(String path) throws IOException
    {
        InputStream in = IO.class.getClassLoader().getResourceAsStream(path);
        if (in == null) {
            throw new IOException("image not found - " + path);
        }
        return new ImageIcon(ImageIO.read(in));
    }
    
    public static void saveOutput(File f) throws IOException
    {
        try (PrintWriter pw = new PrintWriter(f)) {
            String outputBuffer = OUTPUT_DOCUMENT.getText(0, OUTPUT_DOCUMENT.getLength());
            pw.print(outputBuffer.replaceAll("\\n", System.lineSeparator()));
            pw.flush();
        } catch (BadLocationException ex) {
            throw new IOException(ex);
        }
    }
}
