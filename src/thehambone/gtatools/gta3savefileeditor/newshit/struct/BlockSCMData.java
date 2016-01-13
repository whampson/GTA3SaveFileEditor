package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean8;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarShort;

/**
 * Created on Sep 21, 2015.
 * 
 * @author thehambone
 */
public class BlockSCMData extends Block
{
    public final VarInt pOnAMissionFlag = new VarInt();
    public final VarArray<ContactInfo> aContactInfo
            = new VarArray<>(ContactInfo.class, 16);
    public final VarArray<BuildingSwap> aBuildingSwap
            = new VarArray<>(BuildingSwap.class, 25);
    public final VarArray<InvisibilitySetting> aInvisibilitySetting
            = new VarArray<>(InvisibilitySetting.class, 20);
    public final VarBoolean bIsAlreadyRunningAMissionScript = new VarBoolean8();
    public final VarInt nMainScriptSize = new VarInt();
    public final VarInt nLargestMissionScriptSize = new VarInt();
    public final VarShort nNumberOfExclusiveMissionScripts = new VarShort();
    
    public BlockSCMData(int size)
    {
        super("SCMData", size);
    }
    
    @Override
    public void setSize(int size)
    {
        this.size = size;
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            pOnAMissionFlag, aContactInfo, aBuildingSwap, aInvisibilitySetting,
            bIsAlreadyRunningAMissionScript, nMainScriptSize,
            nLargestMissionScriptSize, nNumberOfExclusiveMissionScripts
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
        pOnAMissionFlag.load(buf, offset + 0x00);
        aContactInfo.load(buf, offset + 0x04, SaveFileNew.Platform.PC);
        aBuildingSwap.load(buf, offset + 0x188, SaveFileNew.Platform.PC);
        aInvisibilitySetting.load(buf, offset + 0x318, SaveFileNew.Platform.PC);
        bIsAlreadyRunningAMissionScript.load(buf, offset + 0x3B8);
        nMainScriptSize.load(buf, offset + 0x3BC);
        nLargestMissionScriptSize.load(buf, offset + 0x3C0);
        nNumberOfExclusiveMissionScripts.load(buf, offset + 0x3C4);
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
