package thehambone.gtatools.gta3savefileeditor.util;

/**
 * This class contains various methods for computing checksums on data.
 * <p>
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public class Checksum
{
    /**
     * Computes a 32-bit checksum of a byte buffer using a cyclic redundancy
     * check algorithm.
     * 
     * @param buf the byte buffer on which to compute the checksum
     * @return the computed 32-bit checksum
     */
    public static int crc32(byte[] buf)
    {
        int crc = 0xFFFFFFFF;
        int poly = 0xEDB88320;
        
        int temp;
        for (int i = 0; i < buf.length; i++) {
            temp = (crc ^ buf[i]) & 0xFF;
            for (int k = 0; k < 8; k++) {
                if ((temp & 1) == 1) {
                    temp = (temp >>> 1) ^ poly;
                } else {
                    temp = (temp >>> 1);
                }
            }
            crc = (crc >>> 8) ^ temp;
        }
        
        return crc ^ 0xFFFFFFFF;
    }
    
    /**
     * Calculates the sum of all of the bytes in the byte buffer and returns the
     * sum as a 32-bit integer.
     * 
     * @param buf the byte buffer on which the checksum will be calculated
     * @return the computed sum as a 32-bit integer
     */
    public static int checksum32(byte[] buf)
    {
        int sum = 0;
        for (int i = 0; i < buf.length; i++) {
            sum += (buf[i] & 0xFF);
        }
        return sum;
    }
}
