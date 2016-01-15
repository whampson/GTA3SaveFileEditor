package thehambone.gtatools.gta3savefileeditor.newshit.struct;

import thehambone.gtatools.gta3savefileeditor.newshit.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarBoolean8;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarShort;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarString;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarString8;


/**
 * A {@code RunningScript} is a mission script or SCM thread that is currently
 * running.
 * <p>
 * Created on Sep 21, 2015.
 * 
 * @author thehambone
 */
public class RunningScript extends Record
{
    public final VarInt pNextScript = new VarInt();
    public final VarInt pPreviousScript = new VarInt();
    public final VarString szName = new VarString8(8);
    public final VarInt pCurrentInstruction = new VarInt();
    public final VarArray<VarInt> aReturnStack
            = new VarArray<>(VarInt.class, 6);
    public final VarShort nReturnStackCount = new VarShort();
    public final VarArray<VarInt> aLocalVariable
            = new VarArray<>(VarInt.class, 16);
    public final VarInt nTimerA = new VarInt();
    public final VarInt nTimerB = new VarInt();
    public final VarBoolean bIfResult = new VarBoolean8();
    public final VarBoolean bIsMissionScript = new VarBoolean8();
    public final VarBoolean bIsActive = new VarBoolean8();
    public final VarInt nWakeTime = new VarInt();
    public final VarShort nIfNumber = new VarShort();
    public final VarBoolean bNotFlag = new VarBoolean8();
    public final VarBoolean bIsWastedBustedCheckEnabled = new VarBoolean8();
    public final VarBoolean bWastedBustedCheckResult = new VarBoolean8();
    public final VarBoolean bIsMissionFlag = new VarBoolean8();
    
    public RunningScript()
    {
        super(0x88);
    }
    
    private void load(DataBuffer buf)
    {
        pNextScript.load(buf, offset + 0x00);
        pPreviousScript.load(buf, offset + 0x04);
        szName.load(buf, offset + 0x08);
        pCurrentInstruction.load(buf, offset + 0x10);
        aReturnStack.load(buf, offset + 0x14);
        nReturnStackCount.load(buf, offset + 0x2C);
        aLocalVariable.load(buf, offset + 0x30);
        nTimerA.load(buf, offset + 0x70);
        nTimerB.load(buf, offset + 0x74);
        bIfResult.load(buf, offset + 0x78);
        bIsMissionScript.load(buf, offset + 0x79);
        bIsActive.load(buf, offset + 0x7A);
        nWakeTime.load(buf, offset + 0x7C);
        nIfNumber.load(buf, offset + 0x80);
        bNotFlag.load(buf, offset + 0x82);
        bIsWastedBustedCheckEnabled.load(buf, offset + 0x83);
        bWastedBustedCheckResult.load(buf, offset + 0x84);
        bIsMissionFlag.load(buf, offset + 0x85);
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            pNextScript, pPreviousScript, szName, pCurrentInstruction,
            aReturnStack, nReturnStackCount, aLocalVariable, nTimerA, nTimerB,
            bIfResult, bIsMissionScript, bIsActive, nWakeTime, nIfNumber,
            bNotFlag, bIsWastedBustedCheckEnabled, bWastedBustedCheckResult,
            bIsMissionFlag
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        load(buf);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        load(buf);
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
