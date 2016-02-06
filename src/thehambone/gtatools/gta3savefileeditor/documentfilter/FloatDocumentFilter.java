package thehambone.gtatools.gta3savefileeditor.documentfilter;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import thehambone.gtatools.gta3savefileeditor.util.NumberUtilities;

/**
 * An extension of {@link javax.swing.text.DocumentFilter} that is designed to
 * filter text input by the user such that the text input will remain a valid
 * floating-point decimal number.
 * <p>
 * Created on Mar 30, 2015.
 * 
 * @author thehambone
 */
public class FloatDocumentFilter extends DocumentFilter
{
    @Override
    public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);
        
        if (NumberUtilities.isDecimal(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep();
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
        
        if (NumberUtilities.isDecimal(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException
    {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);
        
        super.remove(fb, offset, length);
    }
}
