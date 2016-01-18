package thehambone.gtatools.gta3savefileeditor.gui.observe;

/**
 * Created on Apr 6, 2015.
 * 
 * @author thehambone
 */
public interface Observable
{
    public void addObserver(Observer o);
    
    public void removeObserver(Observer o);
    
    public void notifyObservers(Object message, Object... args);
}