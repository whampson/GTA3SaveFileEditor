
package thehambone.gtatools.gta3savefileeditor.util;

/**
 * A {@code QueueException} is an unchecked exception that indicates that an
 * invalid operation was performed on a queue.
 * <p>
 * Created on Jan 27, 2016.
 *
 * @author thehambone
 * @deprecated 
 */
public class QueueException extends RuntimeException
{
    /**
     * Creates a new {@code QueueException}.
     */
    public QueueException()
    {
        super();
    }
    
    /**
     * Creates a new {@code QueueException} with a message.
     * 
     * @param message exception details
     */
    public QueueException(String message)
    {
        super(message);
    }
    
    /**
     * Creates a new {@code QueueException} with the cause of the exception.
     * 
     * @param cause the underlying cause of the exception
     */
    public QueueException(Throwable cause)
    {
        super(cause);
    }
    
    /**
     * Creates a new {@code QueueException} with both a message and the cause of
     * the exception.
     * 
     * @param message exception details
     * @param cause the underlying cause of the exception
     */
    public QueueException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
