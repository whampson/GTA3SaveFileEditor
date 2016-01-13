package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarShort;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Created on Sep 17, 2015.
 * 
 * @author thehambone
 */
public class Timestamp extends Record
{
    public final VarShort nYear = new VarShort();
    public final VarShort nMonth = new VarShort();
    public final VarShort nDayOfWeek = new VarShort();
    public final VarShort nDay = new VarShort();
    public final VarShort nHour = new VarShort();
    public final VarShort nMinute = new VarShort();
    public final VarShort nSecond = new VarShort();
    public final VarShort nMillisecond = new VarShort();
    
    public Timestamp()
    {
        super(0x10);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new VarShort[] {
            nYear, nMonth, nDayOfWeek, nDay, nHour, nMinute, nSecond,
            nMillisecond
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("Android not supported yet.");
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("iOS not supported yet.");
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        int localOffset = 0;
        for (Variable var : (Variable[]) getMembers()) {
            var.load(buf, offset + localOffset);
            localOffset += var.getSize();
        }
    }
    
    @Override
    protected void loadPS2(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("PS2 not supported yet.");
    }
    
    @Override
    protected void loadXbox(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("Xbox not supported yet.");
    }
}
