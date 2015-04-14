package thehambone.gtatools.gta3savefileeditor.savefile.variable;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTABoolean32;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAStringUTF16;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTABoolean8;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAShort;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.BuildingSwap;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.Contact;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.Coord;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.Gang;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.GlobalVariable;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.InvisibilitySetting;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.PedType;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.PlayerPed;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.RunningScript;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.SaveGarageSlot;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.WeaponSlot;

/**
 * A collection of {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}s
 * that are found in a GTA III save file.
 * 
 * I hope to replace this class with a more versatile variable system through
 * XML.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 12, 2015
 */
public class VariableDefinitions
{
    // Ugly as hell. I hope to define these externally at some point (XML?)
    
    // Block 0
    public final Variable<GTAStringUTF16>      sz16Title                      = new Variable<>("title", false, false, true, false, false, new GTAStringUTF16(24));
    public final Variable<GTAShort>            aTimestamp                     = new Variable<>("timestamp", false, false, true, false, false, new GTAShort(), 8);
    public final Variable<GTAInteger>          iCurrentIsland                 = new Variable<>("currentIsland", false, false, true, false, false, new GTAInteger());
    public final Variable<Coord>               fCameraPosition                = new Variable<>("cameraPosition", false, false, true, false, false, new Coord());
    public final Variable<GTAInteger>          iMillisPerGameMinute           = new Variable<>("millisPerGameMinute", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iWeatherTimer                  = new Variable<>("weatherTimer", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAByte>             bGameTimeHours                 = new Variable<>("gameTimeHours", false, false, true, false, false, new GTAByte());
    public final Variable<GTAByte>             bGameTimeMinutes               = new Variable<>("gameTimeMinutes", false, false, true, false, false, new GTAByte());
    public final Variable<GTAShort>            sCurrentPadMode                = new Variable<>("currentPadMode", false, false, true, false, false, new GTAShort());
    public final Variable<GTAInteger>          iGlobalTimer                   = new Variable<>("globalTimer", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAFloat>            fTimeScale                     = new Variable<>("timeScale", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAFloat>            fTimerTimeStep                 = new Variable<>("timerTimeStep", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAFloat>            fTimerTimeStepNonClipped       = new Variable<>("timerTimeStepNonClipped", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAInteger>          iFrameCounter                  = new Variable<>("frameCounter", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAFloat>            fTimeStep                      = new Variable<>("timeStep", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAFloat>            fFramesPerUpdate               = new Variable<>("framesPerUpdate", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAFloat>            fTimeScale2                    = new Variable<>("timeScale2", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAShort>            sWeatherTypeOld                = new Variable<>("weatherTypeOld", false, false, true, false, false, new GTAShort());
    public final Variable<GTAShort>            sWeatherTypeNew                = new Variable<>("weatherTypeNew", false, false, true, false, false, new GTAShort());
    public final Variable<GTAShort>            sWeatherTypeForced             = new Variable<>("weatherTypeForced", false, false, true, false, false, new GTAShort());
    public final Variable<GTAFloat>            fWeatherInterpolationValue     = new Variable<>("weatherInterpolationValue", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAByte>             aCompilationTime               = new Variable<>("compilationTime", false, false, true, false, false, new GTAByte(), 24);
    public final Variable<GTAInteger>          iWeatherTypeInList             = new Variable<>("weatherTypeInList", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iGlobalVariablesSpaceSize      = new Variable<>("globalVariablesSpaceSize", false, false, true, false, false, new GTAInteger());
    public final Variable<GlobalVariable>      aGlobalVariables               = new Variable<>("globalVariables", false, false, true, false, false, new GlobalVariable(), 0);
    public final Variable<GTAInteger>          iOnAMissionFlag                = new Variable<>("onAMissionFlag", false, false, true, false, false, new GTAInteger());
    public final Variable<Contact>             aContactInfo                   = new Variable<>("contactInfo", false, false, true, false, false, new Contact(), 16);
    public final Variable<GTAInteger>          iLastMissionPassedTime         = new Variable<>("lastMissionPassedTime", false, false, true, false, false, new GTAInteger());
    public final Variable<BuildingSwap>        aBuildingSwaps                 = new Variable<>("buildingSwaps", false, false, true, false, false, new BuildingSwap(), 25);
    public final Variable<InvisibilitySetting> aInvisibliltySettings          = new Variable<>("invisibilitySettings", false, false, true, false, false, new InvisibilitySetting(), 20);
    public final Variable<GTABoolean8>         b8AlreadyRunningAMissionScript = new Variable<>("alreadyRunningAMissionScript", false, false, true, false, false, new GTABoolean8());
    public final Variable<GTAInteger>          iMainScriptSize                = new Variable<>("mainScriptSize", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iLargestMissionScriptSize      = new Variable<>("largestMissionScriptSize", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAShort>            sExclusiveMissionScriptsCount  = new Variable<>("exclusiveMissionScriptsCount", false, false, true, false, false, new GTAShort());
    public final Variable<GTAInteger>          iRunningScriptsCount           = new Variable<>("runningScriptsCount", false, false, true, false, false, new GTAInteger());
    public final Variable<RunningScript>       aRunningScripts                = new Variable<>("runningScripts", false, false, true, false, false, new RunningScript(), 0);
    
    // Block 1
    public final Variable<PlayerPed>           aPlayerPed                     = new Variable<>("playerPeds", false, false, true, false, false, new PlayerPed(), 1);
    public final Variable<GTAInteger>          iNumPlayers                    = new Variable<>("numPlayers", false, false, true, false, false, new GTAInteger());
    public final Variable<Coord>               aPlayerCoords                  = new Variable<>("playerCoords", false, false, true, false, false, new Coord());
    public final Variable<GTAFloat>            fPlayerHealth                  = new Variable<>("playerHealth", false, false, true, false, false, new GTAFloat());
    public final Variable<GTAFloat>            fPlayerArmor                   = new Variable<>("playerArmor", false, false, true, false, false, new GTAFloat());
    public final Variable<WeaponSlot>          aWeaponSlots                   = new Variable<>("weaponSlots", false, false, true, false, false, new WeaponSlot(), 13);
    public final Variable<GTAInteger>          iMaxWantedLevel                = new Variable<>("maxWantedLevel", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iMaxChaosLevel                 = new Variable<>("maxChaosLevel", false, false, true, false, false, new GTAInteger());
    
    // Block 2
    public final Variable<GTABoolean32>        bFreeBombs                     = new Variable<>("freeBombs", false, false, true, false, false, new GTABoolean32());
    public final Variable<GTABoolean32>        bFreeResprays                  = new Variable<>("freeResprays", false, false, true, false, false, new GTABoolean32());
    public final Variable<SaveGarageSlot>      aSaveGarageSlots               = new Variable<>("saveGarageSlots", false, false, true, false, false, new SaveGarageSlot(), 6);
    
    // Block 12
    public final Variable<Gang>                aGangs                         = new Variable<>("gangs", false, false, true, false, false, new Gang(), 9);
    
    // Block 16
    public final Variable<GTAInteger>          iMoney                         = new Variable<>("money", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iMoney2                        = new Variable<>("money2", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iHiddenPackagesCollected       = new Variable<>("hiddenPackagesCollected", false, false, true, false, false, new GTAInteger());
    public final Variable<GTAInteger>          iHiddenPackagesTotal           = new Variable<>("hiddenPackagesTotal", false, false, true, false, false, new GTAInteger());
    public final Variable<GTABoolean8>         bInfiniteSprint                = new Variable<>("infiniteSprint", false, false, true, false, false, new GTABoolean8());
    public final Variable<GTABoolean8>         bFastReload                    = new Variable<>("fastReload", false, false, true, false, false, new GTABoolean8());
    public final Variable<GTABoolean8>         bGetOutOfJailFree              = new Variable<>("getOutOfJailFree", false, false, true, false, false, new GTABoolean8());
    public final Variable<GTABoolean8>         bFreeHealthcare                = new Variable<>("freeHealthcare", false, false, true, false, false, new GTABoolean8());
    
    // Block 19
    public final Variable<PedType>             aPedTypes                      = new Variable<>("pedTypes", false, false, true, false, false, new PedType(), 23);
}