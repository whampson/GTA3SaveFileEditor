package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * a set of 3D Cartesian coordinates. The coordinates stored in this object
 * describe a position in the in-game world.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 * @deprecated 
 */
public class Coord extends GTAObject
{
    public static final int SIZE = 12;
    
    private float x, y, z;
    
    public Coord()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public Coord(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float[] getCoords()
    {
        return new float[] {x, y, z};
    }
    
    public float getX()
    {
        return x;
    }
    
    public void setX(float x)
    {
        this.x = x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public void setY(float y)
    {
        this.y = y;
    }
    
    public float getZ()
    {
        return z;
    }
    
    public void setZ(float z)
    {
        this.z = z;
    }
    @Override
    public int getSize()
    {
        return SIZE;
    }

    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
        return out.getPointer() - startOffset;
    }
}