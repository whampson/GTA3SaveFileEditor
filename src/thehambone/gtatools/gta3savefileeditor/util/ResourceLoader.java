
package thehambone.gtatools.gta3savefileeditor.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * This class handles the loading of embedded files.
 * <p>
 * Created on Feb 1, 2016.
 *
 * @author thehambone
 */
public class ResourceLoader
{
    /**
     * Gets an {@code InputStream} for the embedded resource. The InputStream
     * can then be used to load the resource data.
     * 
     * @param resourcePath the path to the resource
     * @return the {@code InputStream} pointing to the resource, {@code null} if
     *         the resource path is invalid
     */
    public static InputStream getResourceStream(String resourcePath)
    {
        return ClassLoader.getSystemClassLoader()
                .getResourceAsStream(resourcePath);
    }
    
    /**
     * Loads an embedded image file.
     * 
     * @param resourcePath the path to the embedded image
     * @return an {@code Image} object containing the loaded image data
     * @throws IOException if the resource path is invalid or if the resource
     *         is not an image
     */
    public static Image getImageResource(String resourcePath)
            throws IOException
    {
        InputStream stream = getResourceStream(resourcePath);
        return ImageIO.read(stream);
    }
}
