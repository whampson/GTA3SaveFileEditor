
package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;

/**
 * Created on Jan 19, 2016.
 *
 * @author thehambone
 */
public class BlockPedTypes extends Block
{
    public final VarArray<PedType> aPedType = new VarArray<>(PedType.class, 23);
    
    public BlockPedTypes(int blockSize)
    {
        super("PedTypes", blockSize);
    }
    
    private void load(DataBuffer buf, SaveFile.Platform platform)
    {
        aPedType.load(buf, offset + 0x0C, platform);
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
            aPedType
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.PC);
    }
    
    @Override
    protected void loadPS2(DataBuffer buf)
    {
        throw new UnsupportedPlatformException("PS2 not supported yet.");
    }
    
    @Override
    protected void loadXbox(DataBuffer buf)
    {
        load(buf, SaveFile.Platform.XBOX);
    }
}
