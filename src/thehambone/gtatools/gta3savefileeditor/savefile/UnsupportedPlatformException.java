package thehambone.gtatools.gta3savefileeditor.savefile;

/**
 * An {@code UnsupportedPlatformException} indicates that the operation is
 * not supported for saves originating from a particular platform.
 * <p>
 * Created on Nov 9, 2015.
 * 
 * @author thehambone
 */
public class UnsupportedPlatformException extends RuntimeException
{
    public UnsupportedPlatformException()
    {
        super();
    }
    
    public UnsupportedPlatformException(String message)
    {
        super(message);
    }
    
    public UnsupportedPlatformException(Throwable cause)
    {
        super(cause);
    }
    
    public UnsupportedPlatformException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
