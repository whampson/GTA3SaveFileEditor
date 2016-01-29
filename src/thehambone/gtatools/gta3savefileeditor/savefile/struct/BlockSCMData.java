package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarBoolean;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarBoolean8;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarShort;

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
        pOnAMissionFlag.load(buf, offset + 0x00);
        aContactInfo.load(buf, offset + 0x04, SaveFile.Platform.ANDROID);
        aBuildingSwap.load(buf, offset + 0x188, SaveFile.Platform.ANDROID);
        aInvisibilitySetting.load(buf, offset + 0x318,
                SaveFile.Platform.ANDROID);
        bIsAlreadyRunningAMissionScript.load(buf, offset + 0x3B8);
        nMainScriptSize.load(buf, offset + 0x3BC);
        nLargestMissionScriptSize.load(buf, offset + 0x3C0);
        nNumberOfExclusiveMissionScripts.load(buf, offset + 0x3C4);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        pOnAMissionFlag.load(buf, offset + 0x00);
        aContactInfo.load(buf, offset + 0x04, SaveFile.Platform.IOS);
        aBuildingSwap.load(buf, offset + 0x188, SaveFile.Platform.IOS);
        aInvisibilitySetting.load(buf, offset + 0x318,
                SaveFile.Platform.IOS);
        bIsAlreadyRunningAMissionScript.load(buf, offset + 0x3B8);
        nMainScriptSize.load(buf, offset + 0x3BC);
        nLargestMissionScriptSize.load(buf, offset + 0x3C0);
        nNumberOfExclusiveMissionScripts.load(buf, offset + 0x3C4);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        pOnAMissionFlag.load(buf, offset + 0x00);
        aContactInfo.load(buf, offset + 0x04, SaveFile.Platform.PC);
        aBuildingSwap.load(buf, offset + 0x188, SaveFile.Platform.PC);
        aInvisibilitySetting.load(buf, offset + 0x318, SaveFile.Platform.PC);
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
