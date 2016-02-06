package thehambone.gtatools.gta3savefileeditor.savefile;

/**
 * An {@code UnspecifiedPlatformException} indicates that a gaming platform was
 * not indicated where it needed to be.
 * <p>
 * Created on Nov 9, 2015.
 * 
 * @author thehambone
 */
public class UnspecifiedPlatformException extends RuntimeException
{
    public UnspecifiedPlatformException()
    {
        super();
    }
    
    public UnspecifiedPlatformException(String message)
    {
        super(message);
    }
    
    public UnspecifiedPlatformException(Throwable cause)
    {
        super(cause);
    }
    
    public UnspecifiedPlatformException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
