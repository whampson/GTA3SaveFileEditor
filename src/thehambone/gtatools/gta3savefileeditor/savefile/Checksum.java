package thehambone.gtatools.gta3savefileeditor.savefile;

/**
 * Contains a function for calculating the checksum of a save file. The checksum
 * is the last four bytes of the save file. If this value is incorrect, the
 * game will refuse to load the file.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 16, 2015
 * @deprecated 
 */
public class Checksum
{
    public static int calculateChecksum(byte[] buffer)
    {
        int checksum = 0;
        for (int i = 0; i < buffer.length; i++) {
            checksum += buffer[i] & 0xFF;
        }
        return checksum;
    }
}