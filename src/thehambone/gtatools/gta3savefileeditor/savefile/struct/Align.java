package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * The function of an Align is twofold: to position the file buffer pointer to a
 * specific offset based on the file's platform origin as well as preserve
 * unknown data.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 12, 2015
 */
public class Align implements DataStructure
{
    private final int sizeAndroid, sizeiOS, sizePC, sizePS2, sizeXbox;
    private final SaveFile.Platform platform;
    
    private byte[] buffer;
    
    /**
     * Creates a new Align structure. The parameters specify the size of the
     * align buffer for each platform, as the gamesave structure varies slightly
     * between platforms. A negative offset indicates that the platform is not
     * yet supported.
     * 
     * @param platform the platform specifier
     * @param sizeAndroid the size of the buffer for the Android platform
     * @param sizeiOS the size of the buffer for the iOS platform
     * @param sizePC the size of the buffer for the PC platform
     * @param sizePS2 the size of the buffer for the PS2 platform
     * @param sizeXbox the size of the buffer for the Xbox platform
     */
    public Align(SaveFile.Platform platform,
            int sizeAndroid,
            int sizeiOS,
            int sizePC,
            int sizePS2,
            int sizeXbox)
    {
        this.sizeAndroid = sizeAndroid;
        this.sizeiOS = sizeiOS;
        this.sizePC = sizePC;
        this.sizePS2 = sizePS2;
        this.sizeXbox = sizeXbox;
        this.platform = platform;
        setBufferSize();
    }
    
    @Override
    public int getSize()
    {
        return buffer.length;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        return in.read(buffer);
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        return out.write(buffer);
    }
    
    /**
     * Sets the buffer size based on the platform.
     */
    private void setBufferSize()
    {
        int bufferSize = 0;
        switch (platform) {
            case ANDROID:
                if (sizeAndroid > -1) {
                    bufferSize = sizeAndroid;
                }
                break;
            case iOS:
                if (sizeiOS > -1) {
                    bufferSize = sizeiOS;
                }
                break;
            case PC:
                if (sizePC > -1) {
                    bufferSize = sizePC;
                }
                break;
            case PS2:
                if (sizePS2 > -1) {
                    bufferSize = sizePS2;
                }
                break;
            case XBOX:
                if (sizeXbox > -1) {
                    bufferSize = sizeXbox;
                }
                break;
        }
        buffer = new byte[bufferSize];
    }
}