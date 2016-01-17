package thehambone.gtatools.gta3savefileeditor.gui.observe;

/**
 * Marks a class as "observable" by other classes ("observers"). Observers and
 * observables are used to monitor events between objects.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 06, 2015
 */
public interface Observable
{
    /**
     * Add an observer to this class.
     * 
     * @param o the observer to add
     */
    public void register(Observer o);
    
    /**
     * Remove an observer from this class.
     * 
     * @param o the observer to remove
     */
    public void unregister(Observer o);
    
    /**
     * Notifies all observers of an incoming message by calling their update()
     * method. It is up to each specific observer to decide how to handle this
     * message.
     * 
     * @param message the message to send
     * @param args any additional data that goes along with the message
     */
    public void notifyObservers(String message, Object... args);
    
    /**
     * This method is called by an observer to get the message data.
     * 
     * @return message data
     */
    public Object[] getUpdate();
}