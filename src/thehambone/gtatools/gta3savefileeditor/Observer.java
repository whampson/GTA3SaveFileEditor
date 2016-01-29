package thehambone.gtatools.gta3savefileeditor;

/**
 * The {@code Observer} interface should be inherited by any class that wishes
 * to observe other classes (known as {@code Observables}). This "observer
 * pattern" is used for handling events between multiple objects. An event
 * message is created by an {@code Observable} object and sent to the object's
 * {@code Observers}, which subsequently decide how to handle the event.
 * <p>
 * Created on Apr 6, 2015.
 * 
 * @author thehambone
 * @see Observable
 */
public interface Observer
{
    /**
     * Alerts the observer that an event has occurred.
     * <p>
     * This method should be called by
     * {@link Observable#notifyObservers(Object, Object...)}.
     * 
     * @param message event message
     * @param args event arguments
     */
    public void update(Object message, Object... args);
}