
package thehambone.gtatools.gta3savefileeditor.documentfilter;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 10, 2016.
 *
 * @author thehambone
 */
public class StringDocumentFilter extends DocumentFilter
{
    private final int maxChars;
    
    public StringDocumentFilter(int maxChars)
    {
        if (maxChars < 0) {
            throw new IllegalArgumentException("input out of range: "
                    + maxChars);
        }
        this.maxChars = maxChars;
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);
        
        if (sb.toString().length() > maxChars) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            super.insertString(fb, offset, string, attr);
        }
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);
        
        if (sb.toString().length() > maxChars) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
