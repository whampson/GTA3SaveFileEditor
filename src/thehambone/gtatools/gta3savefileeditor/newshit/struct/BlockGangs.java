
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;

/**
 * Created on Jan 18, 2016.
 *
 * @author thehambone
 */
public class BlockGangs extends Block
{
    public final VarArray<Gang> aGang = new VarArray<>(Gang.class, 9);
    
    public BlockGangs(int size)
    {
        super("Gangs", size);
    }
    
    public void load(DataBuffer buf, SaveFileNew.Platform platform)
    {
        aGang.load(buf, offset + 0x0C, platform);
    }
    
    @Override
    public void setSize(int size)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            aGang
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        load(buf, SaveFileNew.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        load(buf, SaveFileNew.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        load(buf, SaveFileNew.Platform.PC);
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
