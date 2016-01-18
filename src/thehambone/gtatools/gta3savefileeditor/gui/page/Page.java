package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableCheckBox;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableComboBox;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableComponent;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableSlider;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableTextField;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observable;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observer;

/**
 * Created on Mar 7, 2015.
 * 
 * @author thehambone
 */
public abstract class Page extends JPanel implements Observable
{
    protected volatile boolean isPageInitializing;
    
    protected VariableDefinitions vars;
    
    private final List<Observer> observers;
    private final Object lock;
    private final String pageTitle;
    private final Visibility visibility;
    
    public Page(String pageTitle, Visibility visibility)
    {
        super();
        observers = new ArrayList<>();
        lock = new Object();
        this.pageTitle = pageTitle;
        this.visibility = visibility;
    }
    
    public final String getPageTitle()
    {
        return pageTitle;
    }
    
    public final Visibility getVisibility()
    {
        return visibility;
    }
    
    protected final void addChangeNotifiersToComponents(JPanel root,
            Component... exclusions)
    {
        boolean exclude;
        for (Component c : root.getComponents()) {
            exclude = false;
            for (Component exclusion : exclusions) {
                if (c.equals(exclusion)) {
                    exclude = true;
                    break;
                }
            }
            
            if (exclude) {
                continue;
            }
            
            if (c instanceof VariableTextField) {
                addChangeNotifier((VariableTextField)c);
            } else if (c instanceof VariableCheckBox) {
                addChangeNotifier((VariableCheckBox)c);
            } else if (c instanceof VariableComboBox) {
                addChangeNotifier((VariableComboBox)c);
            } else if (c instanceof VariableSlider) {
                addChangeNotifier((VariableSlider)c);
            }/*else if (c instanceof JButton) {
                ((JButton)c).addActionListener(notifyChangeMadeActionListener);
            } */else if (c instanceof JPanel) {
                addChangeNotifiersToComponents((JPanel)c);
            }
        }
    }
    
    private void addChangeNotifier(final VariableTextField textField)
    {
        textField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void update()
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyChange(textField);
                    }
                });
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
    
    private void addChangeNotifier(final VariableCheckBox checkBox)
    {
        checkBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyChange(checkBox);
                    }
                });
            }
        });
    }
    
    private void addChangeNotifier(final VariableComboBox comboBox)
    {
        comboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyChange(comboBox);
                    }
                });
            }
        });
    }
    
    private void addChangeNotifier(final VariableSlider slider)
    {
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyChange(slider);
                    }
                });
            }
        });
    }
    
    private void notifyChange(VariableComponent comp)
    {
        if (!comp.hasVariable()) {
            return;
        }
        if (comp.getVariable().dataChanged()) {
            notifyObservers(Event.VARIABLE_CHANGED);
        } else {
            notifyObservers(Event.VARIABLE_UNCHANGED);
        }
    }
    
    public abstract void loadPage();
    
    @Override
    public void addObserver(Observer o)
    {
        synchronized (lock) {
            if (!observers.contains(o)) {
                observers.add(o);
            }
        }
    }
    
    @Override
    public void removeObserver(Observer o)
    {
        synchronized (lock) {
            observers.remove(o);
        }
    }
    
    @Override
    public void notifyObservers(Object message, Object... args)
    {
        if (isPageInitializing) {
            return;
        }
        synchronized (lock) {
            for (Observer o : observers) {
                o.update(message, args);
            }
        }
    }
    
    public static enum Visibility
    {
        ALWAYS_VISIBLE,
        VISIBLE_WHEN_FILE_LOADED_ONLY,
        VISIBLE_WHEN_FILE_NOT_LOADED_ONLY;
    }
    
    public static enum Event
    {
        VARIABLE_CHANGED,
        VARIABLE_UNCHANGED,
        FILE_LOAD,
        FILE_DELETE,
        REFRESH_SLOTS;
    }
}
