package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import thehambone.gtatools.gta3savefileeditor.util.DataBuffer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.UnsupportedPlatformException;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarString8;

/**
 * Created on Jan 14, 2016.
 *
 * @author thehambone
 */
public class PlayerPed extends Record
{
    public final CPlayerPed cPlayerPed = new CPlayerPed();
    public final VarInt nMaxWantedLevel = new VarInt();
    public final VarInt nMaxChaosLevel = new VarInt();
    public final VarString8 szModelName = new VarString8(24);
    
    public PlayerPed()
    {
        super(1);   // Size varies by platform
    }
    
    @Override
    public DataStructure[] getMembers()
    {
        return new DataStructure[] {
            nMaxWantedLevel, nMaxChaosLevel, szModelName
        };
    }
    
    @Override
    protected void loadAndroid(DataBuffer buf)
    {
        this.size = 0x0644;
        
        cPlayerPed.load(buf, offset + 0x0A, SaveFile.Platform.ANDROID);
        nMaxWantedLevel.load(buf, offset + 0x0622);
        nMaxChaosLevel.load(buf, offset + 0x0626);
        szModelName.load(buf, offset + 0x062A);
    }
    
    @Override
    protected void loadIOS(DataBuffer buf)
    {
        this.size = 0x0640;
        
        cPlayerPed.load(buf, offset + 0x0A, SaveFile.Platform.IOS);
        nMaxWantedLevel.load(buf, offset + 0x061E);
        nMaxChaosLevel.load(buf, offset + 0x0622);
        szModelName.load(buf, offset + 0x0626);
    }
    
    @Override
    protected void loadPC(DataBuffer buf)
    {
        this.size = 0x061C;
        
        cPlayerPed.load(buf, offset + 0x0A, SaveFile.Platform.PC);
        nMaxWantedLevel.load(buf, offset + 0x05FA);
        nMaxChaosLevel.load(buf, offset + 0x05FE);
        szModelName.load(buf, offset + 0x0602);
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
