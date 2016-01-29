package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarShort;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString16;

/**
 * Created on Sep 21, 2015.
 * 
 * @author thehambone
 */
public class BlockSimpleVars extends Block
{
    public final VarString szSaveName = new VarString16(24);
    public final Timestamp timestamp = new Timestamp();
    public final VarInt nCurrentLevel = new VarInt();
    public final RWv3D vCameraPosition = new RWv3D();
    public final VarInt nGameMinuteLengthMillis = new VarInt();
    public final VarInt nLastClockTick = new VarInt();
    public final VarByte nGameHours = new VarByte();
    public final VarByte nGameMinutes = new VarByte();
    public final VarInt nTimeInMillis = new VarInt();
    public final VarInt nFrameCounter = new VarInt();
    public final VarShort nPreviousWeatherType = new VarShort();
    public final VarShort nCurrentWeatherType = new VarShort();
    public final VarShort nForcedWeatherType = new VarShort();
    public final VarFloat fWeatherInterpolationValue = new VarFloat();
    
    public final BlockTheScripts theScripts = new BlockTheScripts(0x00);
    
    public BlockSimpleVars(int size)
    {
        super("SimpleVars", size);
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
            szSaveName, timestamp, nCurrentLevel, vCameraPosition,
            nGameMinuteLengthMillis, nLastClockTick, nGameHours,
            nGameMinutes, nTimeInMillis, nFrameCounter,
            nPreviousWeatherType, nCurrentWeatherType, nForcedWeatherType,
            fWeatherInterpolationValue, theScripts
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        szSaveName.load(buf, offset + 0x00);
        nCurrentLevel.load(buf, offset + 0x34);
        vCameraPosition.load(buf, offset + 0x38, SaveFile.Platform.ANDROID);
        nGameMinuteLengthMillis.load(buf, offset + 0x44);
        nLastClockTick.load(buf, offset + 0x48);
        nGameHours.load(buf, offset + 0x4C);
        nGameMinutes.load(buf, offset + 0x50);
        nTimeInMillis.load(buf, offset + 0x58);
        nFrameCounter.load(buf, offset + 0x68);
        nPreviousWeatherType.load(buf, offset + 0x78);
        nCurrentWeatherType.load(buf, offset + 0x7C);
        nForcedWeatherType.load(buf, offset + 0x80);
        fWeatherInterpolationValue.load(buf, offset + 0x84);
        
        buf.seek(offset + 0xB0);
        theScripts.setSize(buf.readInt());
        theScripts.load(buf, offset + 0xB4, SaveFile.Platform.ANDROID);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        szSaveName.load(buf, offset + 0x00);
        nCurrentLevel.load(buf, offset + 0x34);
        vCameraPosition.load(buf, offset + 0x38, SaveFile.Platform.IOS);
        nGameMinuteLengthMillis.load(buf, offset + 0x44);
        nLastClockTick.load(buf, offset + 0x48);
        nGameHours.load(buf, offset + 0x4C);
        nGameMinutes.load(buf, offset + 0x50);
        nTimeInMillis.load(buf, offset + 0x58);
        nFrameCounter.load(buf, offset + 0x68);
        nPreviousWeatherType.load(buf, offset + 0x78);
        nCurrentWeatherType.load(buf, offset + 0x7C);
        nForcedWeatherType.load(buf, offset + 0x80);
        fWeatherInterpolationValue.load(buf, offset + 0x84);
        
        buf.seek(offset + 0xB0);
        theScripts.setSize(buf.readInt());
        theScripts.load(buf, offset + 0xB4, SaveFile.Platform.IOS);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        szSaveName.load(buf, offset + 0x00);
        timestamp.load(buf, offset + 0x30, SaveFile.Platform.PC);
        nCurrentLevel.load(buf, offset + 0x44);
        vCameraPosition.load(buf, offset + 0x48, SaveFile.Platform.PC);
        nGameMinuteLengthMillis.load(buf, offset + 0x54);
        nLastClockTick.load(buf, offset + 0x58);
        nGameHours.load(buf, offset + 0x5C);
        nGameMinutes.load(buf, offset + 0x60);
        nTimeInMillis.load(buf, offset + 0x68);
        nFrameCounter.load(buf, offset + 0x78);
        nPreviousWeatherType.load(buf, offset + 0x88);
        nCurrentWeatherType.load(buf, offset + 0x8C);
        nForcedWeatherType.load(buf, offset + 0x90);
        fWeatherInterpolationValue.load(buf, offset + 0x94);
        
        buf.seek(offset + 0xBC);
        theScripts.setSize(buf.readInt());
        theScripts.load(buf, offset + 0xC0, SaveFile.Platform.PC);
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
