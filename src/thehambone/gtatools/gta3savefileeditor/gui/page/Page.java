package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observable;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observer;

/**
 *
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 07, 2015
 */
public abstract class Page extends JPanel implements Observable
{
    protected final ArrayList<Observer> observers = new ArrayList<>();
    protected final Object lock = new Object();
    protected final String title;
    protected final Visibility visibility;
    protected final ActionListener notifyChangeMadeActionListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            notifyObservers("change.made");
        }
    };
    protected final DocumentListener notifyChangesMadeDocumentListener = new DocumentListener()
    {
        private void update()
        {
            notifyObservers("change.made");
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
            update();
        }
    };
    
    protected List<Object> update = new ArrayList<>();
    protected VariableDefinitions vars;
    protected boolean isPageInitializing;
    
    public Page(String title, Visibility visibility)
    {
        this.title = title;
        this.visibility = visibility;
    }
    
    protected void addNotifiersToComponents(JPanel root, Component... exclusions)
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
            if (c instanceof JTextField) {
                ((JTextField)c).getDocument().addDocumentListener(notifyChangesMadeDocumentListener);
            } else if (c instanceof JCheckBox) {
                ((JCheckBox)c).addActionListener(notifyChangeMadeActionListener);
            } else if (c instanceof JComboBox) {
                ((JComboBox)c).addActionListener(notifyChangeMadeActionListener);
            } else if (c instanceof JButton) {
                ((JButton)c).addActionListener(notifyChangeMadeActionListener);
            } else if (c instanceof JPanel) {
                addNotifiersToComponents((JPanel)c);
            }
        }
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public Visibility getVisibility()
    {
        return visibility;
    }
    
    public abstract void loadPage();
    
    @Override
    public void register(Observer o)
    {
        synchronized (lock) {
            if (!observers.contains(o)) {
                observers.add(o);
            }
        }
    }

    @Override
    public void unregister(Observer o)
    {
        synchronized (lock) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers(String message, Object... args)
    {
        if (isPageInitializing) {
            return;
        }
        update = new ArrayList<>();
        update.add(message);
        update.addAll(Arrays.asList(args));
        List<Observer> localObservers = null;
        synchronized (lock) {
            localObservers = new ArrayList<>(observers);
            for (Observer o : localObservers) {
                o.update();
            }
        }
    }

    @Override
    public Object[] getUpdate()
    {
        Object[] o = update.toArray();
        update.clear();
        return o;
    }
    
    public static enum Visibility
    {
        ALWAYS_VISIBLE,
        VISIBLE_WHEN_GAMESAVE_LOADED_ONLY,
        VISIBLE_WHEN_GAMESAVE_NOT_LOADED_ONLY;
    }
}
