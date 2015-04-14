package thehambone.gtatools.gta3savefileeditor.io;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Writes data to a {@link javax.swing.text.Document}, which can later be used
 * in a JTextArea or other text interface.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 12, 2015
 */
public class DocumentOutputStream extends OutputStream
{
    private final Document doc;
    
    public DocumentOutputStream(Document doc)
    {
        this.doc = doc;
    }
    
    public Document getDocument()
    {
        return doc;
    }

    @Override
    public void write(int b) throws IOException
    {
        try {
            doc.insertString(doc.getLength(), String.valueOf((char)b), null);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}