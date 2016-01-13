package thehambone.gtatools.gta3savefileeditor.savefile;

/**
 * Indicates that a save file format originating from a particular gaming
 * platform is not supported at this time and thus cannot be edited.
 *
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 06, 2015
 * @deprecated 
 */
public class UnsupportedPlatformException extends Exception
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