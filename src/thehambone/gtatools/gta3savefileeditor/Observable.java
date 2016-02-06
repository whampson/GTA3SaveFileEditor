package thehambone.gtatools.gta3savefileeditor;

/**
 * The {@code Observable} interface should be inherited by any class that wishes
 * to be observed by another class (an {@code Observer}). This "observer
 * pattern" is used for handling events between multiple objects. An event
 * message is created by an {@code Observable} object and sent to the object's
 * {@code Observers}, which subsequently decide how to handle the event.
 * <p>
 * Created on Apr 6, 2015.
 * 
 * @author thehambone
 * @see Observer
 */
public interface Observable
{
    /**
     * Registers an observer on this object.
     * 
     * @param o the {@code Observer} to add
     */
    public void addObserver(Observer o);
    
    /**
     * Removes the specified observer from the list of observers.
     * 
     * @param o the {@code Observer} to remove
     */
    public void removeObserver(Observer o);
    
    /**
     * Alerts all observers that an event has occurred.
     * 
     * @param message event message
     * @param args event arguments
     */
    public void notifyObservers(Object message, Object... args);
}
