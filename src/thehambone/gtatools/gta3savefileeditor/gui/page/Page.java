package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableComponent;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observable;
import thehambone.gtatools.gta3savefileeditor.gui.observe.Observer;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Created on Mar 7, 2015.
 * 
 * @author thehambone
 */
public abstract class Page extends JPanel implements Observable
{
    protected VariableDefinitions vars;
    
    private final List<Observer> observers;
    private final String pageTitle;
    private final Visibility visibility;
    
    /**
     * Creates a new {@code Page} object.
     * 
     * @param pageTitle the name of the page
     * @param visibility the page visibility type
     */
    public Page(String pageTitle, Visibility visibility)
    {
        super();
        observers = new ArrayList<>();
        this.pageTitle = pageTitle;
        this.visibility = visibility;
    }
    
    /**
     * Returns the name of this page.
     * 
     * @return the page title
     */
    public final String getPageTitle()
    {
        return pageTitle;
    }
    
    /**
     * Gets the page visibility type.
     * 
     * @return the page visibility
     * @see Visibility
     */
    public final Visibility getVisibility()
    {
        return visibility;
    }
    
    protected void notifyChange(Variable var)
    {
        if (var.dataChanged()) {
            notifyObservers(Event.VARIABLE_CHANGED);
        } else {
            notifyObservers(Event.VARIABLE_UNCHANGED);
        }
    }
    
    /**
     * Adds variable change event notifiers to all {@code VariableComponent}
     * objects found within the specified {@code Container}. These event
     * notifiers consist of additional {@code ActionListeners},
     * {@code DocumentListeners}, and {@code ChangeListeners} (depending on the
     * component type) which send a notification whenever a variable value is
     * changed. This is used for detecting whether unsaved changes have been
     * made to the file.
     * 
     * @param root a {@code Container} containing {@code VariableComponents}
     * @param exclusions components to exclude from the process
     * @see Event
     */
    private void addObserverToVariableComponents(Container root, Observer o)
    {
        for (Component c : root.getComponents()) {
            if (c instanceof VariableComponent) {
                VariableComponent vc = (VariableComponent)c;
                vc.addObserver(o);
            } else if (c instanceof Container) {
                addObserverToVariableComponents((Container)c, o);   // Recurse
            }
        }
    }
    
    /**
     * Associates variables with {@code VariableComponents} and updates the
     * components' states so they reflect the current values of variables.
     */
    public abstract void loadPage();
    
    @Override
    public void addObserver(Observer o)
    {
        if (!observers.contains(o)) {
            observers.add(o);
            addObserverToVariableComponents(this, o);
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
    
    /**
     * Constants defining Page visibility types.
     */
    public static enum Visibility
    {
        /**
         * The Page is always visible.
         */
        ALWAYS_VISIBLE,
        
        /**
         * The Page is only visible when a save file is loaded.
         */
        VISIBLE_WHEN_FILE_LOADED_ONLY,
        
        /**
         * The Page is only visible when a save file is not loaded.
         */
        VISIBLE_WHEN_FILE_NOT_LOADED_ONLY;
    }
    
    /**
     * Constants defining page events.
     */
    public static enum Event
    {
        /**
         * Signifies that a variable's value has been changed.
         */
        VARIABLE_CHANGED,
        
        /**
         * Indicates that a variable's value has been restored to its original
         * state.
         */
        VARIABLE_UNCHANGED,
        
        /**
         * Signals that a file should be loaded.
         */
        FILE_LOAD,
        
        /**
         * Signals that a file should be deleted.
         */
        FILE_DELETE,
        
        /**
         * Signals that the save slots should be refreshed.
         */
        REFRESH_SLOTS;
    }
}
