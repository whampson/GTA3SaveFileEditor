package thehambone.gtatools.gta3savefileeditor.savefile.var.component;

import javax.swing.text.PlainDocument;
import thehambone.gtatools.gta3savefileeditor.documentfilter.StringDocumentFilter;
import thehambone.gtatools.gta3savefileeditor.page.Page;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Jan 12, 2016.
 *
 * @author thehambone
 */
public class StringVariableTextField extends VariableTextField<VarString>
{
    private int maxChars;
    
    public StringVariableTextField()
    {
        this(null);
    }
    
    public StringVariableTextField(VarString var,
            VarString... supplementaryVars)
    {
        super(var, supplementaryVars);
        maxChars = -1;
        
        initDocumentFilter();
    }
    
    public int getMaxInputLength()
    {
        return maxChars;
    }
    
    public void setMaxInputLength(int maxChars)
    {
        this.maxChars = maxChars;
        initDocumentFilter();
    }
    
    private void initDocumentFilter()
    {
        PlainDocument doc = (PlainDocument)getDocument();
        if (maxChars < 0) {
            doc.setDocumentFilter(null);
        } else {
            doc.setDocumentFilter(new StringDocumentFilter(maxChars));
        }
    }
    
    @Override
    protected boolean isInputValid()
    {
        return true;
    }
    
    @Override
    public void refreshComponent()
    {
        VarString v = getVariable();
        if (v == null) {
            return;
        }
        
        boolean temp = doUpdateOnChange;
        doUpdateOnChange = false;
        isComponentRefreshing = true;
        
        setText(v.getValue());
        
        isComponentRefreshing = false;
        doUpdateOnChange = temp;
    }
    
    @Override
    public void updateVariable()
    {
        VarString v = getVariable();
        if (v == null) {
            return;
        }
        
        v.setValue(getText());
        Logger.debug("Variable updated: " + v);
        
        for (VarString v1 : getSupplementaryVariables()) {
            v1.parseValue(getText());
            Logger.debug("Variable updated: " + v1);
        }
        
        notifyObservers(Page.Event.VARIABLE_CHANGED);
    }
}
