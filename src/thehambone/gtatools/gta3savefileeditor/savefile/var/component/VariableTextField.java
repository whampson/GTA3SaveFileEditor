
package thehambone.gtatools.gta3savefileeditor.savefile.var.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import thehambone.gtatools.gta3savefileeditor.util.GUIUtilities;
import thehambone.gtatools.gta3savefileeditor.Observer;
import thehambone.gtatools.gta3savefileeditor.savefile.var.Variable;

/**
 * Created on Jan 11, 2016.
 *
 * @author thehambone
 * @param <T>
 */
public abstract class VariableTextField<T extends Variable>
        extends JTextField implements VariableComponent<T>
{
    protected volatile boolean isComponentRefreshing;
    protected boolean doUpdateOnChange;
    
    private final List<Observer> observers;
    private final List<T> supplementaryVars;
    
    private T var;
    private String displayFormat;
    
    protected VariableTextField(T var, T... supplementaryVars)
    {
        this.var = var;
        this.observers = new ArrayList<>();
        this.supplementaryVars
                = new ArrayList<>(Arrays.asList(supplementaryVars));
        
        isComponentRefreshing = false;
        displayFormat = null;
        doUpdateOnChange = true;
        
        initDocumentListener();
        initFocusListener();
        refreshComponent();
        
    }
    
    public String getDisplayFormat()
    {
        return displayFormat;
    }
    
    public void setDisplayFormat(String format)
    {
        displayFormat = format;
    }
    
    private void initDocumentListener()
    {
        getDocument().addDocumentListener(new DocumentListener()
        {
            private void update()
            {
                if (isInputValid()
                        && !isComponentRefreshing
                        && doUpdateOnChange) {
                    updateVariable();
                }
            }
            
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                update();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                // Nop
            }
        });
    }
        
    private void initFocusListener()
    {
        addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                Runnable doFocusGained = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        selectAll();
                    }
                };
                SwingUtilities.invokeLater(doFocusGained);
            }
            
            @Override
            public void focusLost(FocusEvent e)
            {
                Runnable doFocusLost = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (getText().equals("-") || getText().equals("+")
                                || getText().isEmpty()) {
                            setText("0");
                        }
                        if (!isInputValid()) {
                            GUIUtilities.showErrorMessageBox(getTopLevelAncestor(),
                                    "Invalid input - " + getText(),
                                    "Invalid Input");
                            selectAll();
                            requestFocus();
                        } else {
                            refreshComponent();
                        }
                    }
                };
                SwingUtilities.invokeLater(doFocusLost);
            }
        });
    }
    
    protected abstract boolean isInputValid();
    
    @Override
    public boolean hasVariable()
    {
        return var != null;
    }
    
    @Override
    public T getVariable()
    {
        return var;
    }
    
    @Override
    public final void setVariable(T var, T... supplementaryVars)
    {
        this.var = var;
        
        this.supplementaryVars.clear();
        this.supplementaryVars.addAll(Arrays.asList(supplementaryVars));
        
        refreshComponent();
    }
    
    @Override
    public List<T> getSupplementaryVariables()
    {
        return Collections.unmodifiableList(supplementaryVars);
    }
    
    @Override
    public void updateVariableOnChange(boolean doUpdate)
    {
        doUpdateOnChange = doUpdate;
    }
    
    @Override
    public void addObserver(Observer o)
    {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }
    
    @Override
    public void removeObserver(Observer o)
    {
        observers.remove(o);
    }
    
    @Override
    public void notifyObservers(Object message, Object... args)
    {
        for (Observer o : observers) {
            o.update(message, args);
        }
    }
}
