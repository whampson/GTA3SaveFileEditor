package thehambone.gtatools.gta3savefileeditor.game;

import java.awt.Color;

/**
 * Contains a set of enums pertaining to unchangeable aspects of GTA III, such
 * as weapons, vehicles, peds, etc.
 * 
 * At some point in the future, I want to have this program read some of the
 * game files which define these values. That way, if the user has, say, a
 * custom vehicle installed or a custom car color defined, this editor will be
 * able to work with those modifications to the game.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 16, 2015
 */

// todo: define these in their own classes (looks much cleaner!)
public class GameConstants
{
    public static enum Gang
    {
        GANG01(0, "Mafia"),
        GANG02(1, "Triads"),
        GANG03(2, "Diablos"),
        GANG04(3, "Yakuza"),
        GANG05(4, "Yardies"),
        GANG06(5, "Colombians"),
        GANG07(6, "Hoods"),
        GANG08(7, "<unused>"),
        GANG09(8, "<unused>");
        
        private final int id;
        private final String friendlyName;
        
        private Gang(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
        
        @Override
        public String toString()
        {
            return friendlyName;
        }
    }
    
    public static enum Vehicle
    {
        _EMPTY(-1, "<empty>"),
        LANDSTK(90, "Landstalker"),
        IDAHO(91, "Idaho"),
        STINGER(92, "Stinger"),
        LINERUN(93, "Linerunner"),
        PEREN(94, "Perennial"),
        SENTINL(95, "Sentinel"),
        PATRIOT(96, "Patriot"),
        FIRETRK(97, "Firetruck"),
        TRASHM(98, "Trashmaster"),
        STRETCH(99, "Stretch"),
        MANANA(100, "Manana"),
        INFERNS(101, "Infernus"),
        BLISTA(102, "Blista"),
        PONY(103, "Pony"),
        MULE(104, "Mule"),
        CHEETAH(105, "Cheetah"),
        AMBULAN(106, "Ambulance"),
        FBICAR(107, "Fbi Car"),
        MOONBM(108, "Moonbeam"),
        ESPERAN(109, "Esperanto"),
        TAXI(110, "Taxi"),
        KURUMA(111, "Kuruma"),
        BOBCAT(112, "Bobcat"),
        WHOOPEE(113, "Mr Whoopee"),
        BFINJC(114, "BF Injection"),
        MANANA2(115, "Manana (corpse)"),
        POLICAR(116, "Police"),
        ENFORCR(117, "Enforcer"),
        SECURI(118, "Securicar"),
        BANSHEE(119, "Banshee"),
        PREDATR(120, "Predator"),
        BUS(121, "Bus"),
        RHINO(122, "Rhino"),
        BARRCKS(123, "Barracks OL"),
//        TRAIN(124, "Train"),
//        HELI(125, "Helicopter"),
        DODO(126, "Dodo"),
        COACH(127, "Coach"),
        CABBIE(128, "Cabbie"),
        STALION(129, "Stallion"),
        RUMPO(130, "Rumpo"),
        RCBANDT(131, "RC Bandit"),
        BELLYUP(132, "Triad Fish Van"),
        MRWONGS(133, "Mr Wongs"),
        MAFIACR(134, "Mafia Sentinel"),
        YARDICR(135, "Yardie Lobo"),
        YAKUZCR(136, "Yakuza Stinger"),
        DIABLCR(137, "Diablo Stallion"),
        COLOMCR(138, "Cartel Cruiser"),
        HOODSCR(139, "Hoods Rumpo XL"),
//        AEROPL(140, "Aeroplane"),
//        DODO2(141, "Deaddodo"),
        SPEEDER(142, "Speeder"),
        REEFER(143, "Reefer"),
        PANLANT(144, "Panlantic"),
        FLATBED(145, "Flatbed"),
        YANKEE(146, "Yankee"),
//        HELI2(147, "Escape"),
        BORGNIN(148, "Borgnine"),
        TOYZ(149, "TOYZ"),
        GHOST(150, "Ghost");
        
        private final int id;
        private final String friendlyName;
        
        private Vehicle(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
        
        @Override
        public String toString()
        {
            return friendlyName;
        }
    }
    
    public static enum Weapon {
        FISTS(0, "Fists"),
        BAT(1, "Bat"),
        PISTOL(2, "Pistol"),
        UZI(3, "Uzi"),
        SHOTGUN(4, "Shotgun"),
        AK47(5, "AK47"),
        M16(6, "M16"),
        SNIPER(7, "Sniper Rifle"),
        ROCKET(8, "Rocket Launcher"),
        FLAME(9, "Flamethrower"),
        MOLOTOV(10, "Molotov Cocktail"),
        GRENADE(11, "Grenade"),
        DETONATOR(12, "Detonator");
        
        private final int id;
        private final String friendlyName;
        
        private Weapon(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
        
        @Override
        public String toString()
        {
            return friendlyName;
        }
    }
    
    public static enum Island 
    {
        PORTLAND(1, "Portland"),
        STAUNTON(2, "Staunton Island"),
        SHORESIDE(3, "Shoreside Vale");
        
        private final int id;
        private final String friendlyName;
        
        private Island(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public static enum WeatherType
    {
        SUNNY(0, "Sunny"),
        CLOUDY(1, "Cloudy"),
        RAINY(2, "Rainy"),
        FOGGY(3, "Foggy");
        
        private final int id;
        private final String friendlyName;
        
        private WeatherType(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public static enum PedType
    {
        PLAYER01(0, 1, "Player 1"),
        PLAYER02(1, 2, "Player 2"),
        PLAYER03(2, 4, "Player 3"),
        PLAYER04(3, 8, "Player 4"),
        CIVMALE(4, 16, "Male Civilian"),
        CIVFEMALE(5, 32, "Female Civilian"),
        COP(6, 64, "Cop"),
        GANG01(7, 128, "Mafia"),
        GANG02(8, 256, "Triads"),
        GANG03(9, 512, "Diablos"),
        GANG04(10, 1024, "Yakuza"),
        GANG05(11, 2048, "Yardies"),
        GANG06(12, 4096, "Colombians"),
        GANG07(13, 8192, "Hoods"),
        GANG08(14, 16384, "<unused gang>"),
        GANG09(15, 32768, "<unused gang>"),
        EMERGENCY(16, 65536, "Paramedic"),
        FIREMAN(17, 131072, "Firefighter"),
        CRIMINAL(18, 262144, "Criminal"),
        SPECIAL01(19, 524288, "Special"),
        PROSTITUTE(20, 1048576, "Prostitute"),
        SPECIAL02(21, 2097152, "Special 2");
        
        private final int id;
        private final int threatNumber;
        private final String friendlyName;
        
        private PedType(int id, int threatNumber, String friendlyName)
        {
            this.id = id;
            this.threatNumber = threatNumber;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public int getThreatNumber()
        {
            return threatNumber;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public enum BombType
    {
        _NONE(0, "<none>"),
        TIMER(1, "Timer"),
        IGNITION(2, "Ignition"),
        REMOTE(3, "Remote"),
        TIMER_ARMED(4, "Timer (armed)"),
        IGNITION_ARMED(5, "Ignition (armed)");
        
        private final int id;
        private final String friendlyName;
        
        private BombType(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public enum RadioStation
    {
        _RADIO_OFF(11, "<radio off>"),
        HEAD_RADIO(0, "Head Radio"),
        DOUBLE_CLEF_FM(1, "Double Clef FM"),
        JAH_RADIO(2, "Jah Radio"),
        RISE_FM(3, "Rise FM"),
        LIPS_106(4, "Lips 106"),
        GAME_FM(5, "Game FM"),
        MSX_FM(6, "MSX FM"),
        FLASHBACK_95_6(7, "Flashback 95.6"),
        CHATTERBOX_109(8, "Chatterbox 109"),
        USER_TRACK_PLAYER(9, "User track player"),
        POLICE_RADIO(10, "Police Radio");
        
        private final int id;
        private final String friendlyName;
        
        private RadioStation(int id, String friendlyName)
        {
            this.id = id;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public enum CarColor
    {
        // This looks so ugly! Should be reading directly from carcols.dat...
        BLACK(0, 5, 5, 5, "Black"),
        WHITE(1, 245, 245, 245, "White"),
        POLICE_CAR_BLUE(2, 42, 119, 161, "Police Car Blue"),
        CHERRY_RED(3, 179, 54, 58, "Cherry Red"),
        MIDNIGHT_BLUE(4, 38, 55, 57, "Midnight Blue"),
        TEMPLE_CURTAIN_PURPLE(5, 134, 68, 110, "Temple Curtain Purple"),
        TAXI_YELLOW(6, 243, 237, 71, "Taxi Yellow"),
        STRIKING_BLUE(7, 76, 117, 183, "Striking Blue"),
        LIGHT_BLUE_GREY(8, 102, 114, 146, "Light Blue Grey"),
        HOODS(9, 94, 112, 114, "Hoods"),
        RED1(10, 53, 34, 36, "Red 1"),
        RED2(11, 90, 33, 36, "Red 2"),
        RED3(12, 102, 43, 43, "Red 3"),
        RED4(13, 99, 50, 46, "Red 4"),
        RED5(14, 132, 40, 39, "Red 5"),
        RED6(15, 138, 58, 66, "Red 6"),
        RED7(16, 104, 39, 49, "Red 7"),
        RED8(17, 139, 60, 68, "Red 8"),
        RED9(18, 158, 47, 43, "Red 9"),
        RED10(19, 163, 58, 47, "Red 10"),
        ORANGE1(20, 210, 86, 51, "Orange 1"),
        ORANGE2(21, 146, 86, 53, "Orange 2"),
        ORANGE3(22, 244, 114, 58, "Orange 3"),
        ORANGE4(23, 211, 87, 51, "Orange 4"),
        ORANGE5(24, 226, 90, 89, "Orange 5"),
        ORANGE6(25, 119, 42, 37, "Orange 6"),
        ORANGE7(26, 225, 119, 67, "Orange 7"),
        ORANGE8(27, 196, 70, 54, "Orange 8"),
        ORANGE9(28, 225, 120, 68, "Orange 9"),
        ORANGE10(29, 195, 89, 56, "Orange 10"),
        YELLOW1(30, 70, 72, 64, "Yellow 1"),
        YELLOW2(31, 116, 119, 97, "Yellow 2"),
        YELLOW3(32, 117, 119, 99, "Yellow 3"),
        YELLOW4(33, 145, 138, 61, "Yellow 4"),
        YELLOW5(34, 148, 140, 102, "Yellow 5"),
        YELLOW6(35, 153, 141, 121, "Yellow 6"),
        YELLOW7(36, 216, 165, 52, "Yellow 7"),
        YELLOW8(37, 201, 189, 125, "Yellow 8"),
        YELLOW9(38, 201, 197, 145, "Yellow 9"),
        YELLOW10(39, 212, 200, 78, "Yellow 10"),
        GREEN1(40, 26, 51, 46, "Green 1"),
        GREEN2(41, 36, 47, 43, "Green 2"),
        GREEN3(42, 29, 55, 63, "Green 3"),
        GREEN4(43, 60, 74, 59, "Green 4"),
        GREEN5(44, 45, 80, 55, "Green 5"),
        GREEN6(45, 58, 108, 96, "Green 6"),
        GREEN7(46, 58, 98, 60, "Green 7"),
        GREEN8(47, 124, 162, 130, "Green 8"),
        GREEN9(48, 76, 82, 78, "Green 9"),
        GREEN10(49, 86, 119, 91, "Green 10"),
        BLUE1(50, 28, 70, 80, "Blue 1"),
        BLUE2(51, 72, 94, 132, "Blue 2"),
        BLUE3(52, 28, 39, 69, "Blue 3"),
        BLUE4(53, 31, 52, 104, "Blue 4"),
        BLUE5(54, 43, 72, 120, "Blue 5"),
        BLUE6(55, 71, 92, 131, "Blue 6"),
        BLUE7(56, 68, 124, 146, "Blue 7"),
        BLUE8(57, 61, 103, 171, "Blue 8"),
        BLUE9(58, 75, 125, 130, "Blue 9"),
        BLUE10(59, 128, 176, 183, "Blue 10"),
        PURPLE1(60, 61, 35, 51, "Purple 1"),
        PURPLE2(61, 28, 41, 72, "Purple 2 / Blue"),
        PURPLE3(62, 52, 57, 65, "Purple 3"),
        PURPLE4(63, 64, 69, 76, "Purple 4"),
        PURPLE5(64, 74, 45, 43, "Purple 5"),
        PURPLE6(65, 86, 62, 51, "Purple 6"),
        PURPLE7(66, 65, 70, 76, "Purple 7 / Grey"),
        PURPLE8(67, 103, 39, 49, "Purple 8"),
        PURPLE9(68, 131, 90, 117, "Purple 9"),
        PURPLE10(69, 134, 133, 135, "Purple 10"),
        GREY1(70, 23, 23, 23, "Grey 1"),
        GREY2(71, 46, 46, 46, "Grey 2"),
        GREY3(72, 69, 69, 69, "Grey 3"),
        GREY4(73, 92, 92, 92, "Grey 4"),
        GREY5(74, 115, 115, 115, "Grey 5"),
        GREY6(75, 138, 138, 138, "Grey 6"),
        GREY7(76, 161, 161, 161, "Grey 7"),
        GREY8(77, 184, 184, 184, "Grey 8"),
        GREY9(78, 207, 207, 207, "Grey 9"),
        GREY10(79, 230, 230, 230, "Grey 10"),
        LIGHT1(80, 170, 175, 170, "Light 1"),
        LIGHT2(81, 106, 115, 107, "Light 2"),
        LIGHT3(82, 170, 175, 170, "Light 3"),
        LIGHT4(83, 187, 190, 181, "Light 4"),
        LIGHT5(84, 187, 190, 181, "Light 5"),
        LIGHT6(85, 106, 111, 112, "Light 6"),
        LIGHT7(86, 96, 99, 95, "Light 7"),
        LIGHT8(87, 106, 115, 107, "Light 8"),
        LIGHT9(88, 170, 175, 170, "Light 9"),
        LIGHT10(89, 187, 190, 181, "Light 10"),
        DARK1(90, 33, 41, 43, "Dark 1"),
        DARK2(91, 52,56,66, "Dark 2"),
        DARK3(92, 65, 70, 72, "Dark 3"),
        DARK4(93, 78, 89, 96, "Dark 4"),
        DARK5(94, 65, 69, 76, "Dark 5");
        
        public final int id, r, g, b;
        private final String friendlyName;
        
        private CarColor(int id, int r, int g, int b, String friendlyName)
        {
            this.id = id;
            this.r = r;
            this.g = g;
            this.b = b;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
        
        public Color getColor()
        {
            return new Color(r, g, b);
        }
    }
}