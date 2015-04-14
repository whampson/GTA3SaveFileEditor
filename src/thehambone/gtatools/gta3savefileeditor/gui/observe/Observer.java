package thehambone.gtatools.gta3savefileeditor.gui.observe;

/**
 * Marks a class as an "observer" which can observe "observable" classes.
 * Observers and observables are used to monitor events between objects.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 06, 2015
 */
public interface Observer
{
    /**
     * Adds an observable object to the list objects being observed by this
     * class.
     * 
     * @param o the observable object to add
     */
    public void addSubject(Observable o);
    
    /**
     * Indicates to the observer that a message has been received.
     */
    public void update();
}