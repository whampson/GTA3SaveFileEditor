package thehambone.gtatools.gta3savefileeditor.game;

import java.awt.Color;

/**
 * This class contains a set of enums that reflect various constants found in
 * GTA III, such a weapons, vehicles, pedtypes, etc.
 * <p>
 * Created on Mar 16, 2015.
 * 
 * @author thehambone
 */
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
        LANDSTK (90,  "Landstalker"),
        IDAHO   (91,  "Idaho"),
        STINGER (92,  "Stinger"),
        LINERUN (93,  "Linerunner"),
        PEREN   (94,  "Perennial"),
        SENTINL (95,  "Sentinel"),
        PATRIOT (96,  "Patriot"),
        FIRETRK (97,  "Firetruck"),
        TRASHM  (98,  "Trashmaster"),
        STRETCH (99,  "Stretch"),
        MANANA  (100, "Manana"),
        INFERNS (101, "Infernus"),
        BLISTA  (102, "Blista"),
        PONY    (103, "Pony"),
        MULE    (104, "Mule"),
        CHEETAH (105, "Cheetah"),
        AMBULAN (106, "Ambulance"),
        FBICAR  (107, "Fbi Car"),
        MOONBM  (108, "Moonbeam"),
        ESPERAN (109, "Esperanto"),
        TAXI    (110, "Taxi"),
        KURUMA( 111, "Kuruma"),
        BOBCAT  (112, "Bobcat"),
        WHOOPEE (113, "Mr Whoopee"),
        BFINJC  (114, "BF Injection"),
        MANANA2 (115, "Manana (corpse)"),
        POLICAR (116, "Police"),
        ENFORCR (117, "Enforcer"),
        SECURI  (118, "Securicar"),
        BANSHEE (119, "Banshee"),
        PREDATR (120, "Predator"),
        BUS     (121, "Bus"),
        RHINO   (122, "Rhino"),
        BARRCKS (123, "Barracks OL"),
//        TRAIN   (124, "Train"),
//        HELI    (125, "Helicopter"),
        DODO    (126, "Dodo"),
        COACH   (127, "Coach"),
        CABBIE  (128, "Cabbie"),
        STALION (129, "Stallion"),
        RUMPO   (130, "Rumpo"),
//        RCBANDT (131, "RC Bandit"),
        BELLYUP (132, "Triad Fish Van"),
        MRWONGS (133, "Mr Wongs"),
        MAFIACR (134, "Mafia Sentinel"),
        YARDICR (135, "Yardie Lobo"),
        YAKUZCR (136, "Yakuza Stinger"),
        DIABLCR (137, "Diablo Stallion"),
        COLOMCR (138, "Cartel Cruiser"),
        HOODSCR (139, "Hoods Rumpo XL"),
//        AEROPL  (140, "Aeroplane"),
//        DODO2   (141, "Deaddodo"),
        SPEEDER (142, "Speeder"),
        REEFER  (143, "Reefer"),
        PANLANT (144, "Panlantic"),
        FLATBED (145, "Flatbed"),
        YANKEE  (146, "Yankee"),
//        HELI2   (147, "Escape"),
        BORGNIN (148, "Borgnine"),
        TOYZ    (149, "TOYZ"),
        GHOST   (150, "Ghost");
        
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
    
    public static enum VehicleImmunity
    {
        BULLETPROOF     (1, "Bulletproof"),
        FIREPROOF       (2, "Fireproof"),
        EXPLOSIONPROOF  (4, "Explosionproof"),
        COLLISIONPROOF  (8, "Collisionproof");
        
        private final int mask;
        private final String friendlyName;
        
        private VehicleImmunity(int mask, String friendlyName)
        {
            this.mask = mask;
            this.friendlyName = friendlyName;
        }
        
        public int getMask()
        {
            return mask;
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
        FISTS       (0,  "unarmed"),
        BAT         (1,  "Bat"),
        PISTOL      (2,  "Pistol"),
        UZI         (3,  "Uzi"),
        SHOTGUN     (4,  "Shotgun"),
        AK47        (5,  "AK47"),
        M16         (6,  "M16"),
        SNIPER      (7,  "Sniper Rifle"),
        ROCKET      (8,  "Rocket Launcher"),
        FLAME       (9,  "Flamethrower"),
        MOLOTOV     (10, "Molotov Cocktail"),
        GRENADE     (11, "Grenade"),
        DETONATOR   (12, "Detonator");
        
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
        PORTLAND    (1, "Portland"),
        STAUNTON    (2, "Staunton Island"),
        SHORESIDE   (3, "Shoreside Vale");
        
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
        SUNNY   (0, "Sunny"),
        CLOUDY  (1, "Cloudy"),
        RAINY   (2, "Rainy"),
        FOGGY   (3, "Foggy");
        
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
        PLAYER01    (0,  0x00,       "Player 1"),
        PLAYER02    (1,  0x00,       "Player 2"),
        PLAYER03    (2,  0x00,       "Player 3"),
        PLAYER04    (3,  0x00,       "Player 4"),
        CIVMALE     (4,  0x20900000, "Male Civilian"),
        CIVFEMALE   (5,  0x20900000, "Female Civilian"),
        COP         (6,  0x20900000, "Cop"),
        GANG01      (7,  0x0090FF00, "Mafia"),
        GANG02      (8,  0x0090FE80, "Triads"),
        GANG03      (9,  0x0090FD80, "Diablos"),
        GANG04      (10, 0x0090FB00, "Yakuza"),
        GANG05      (11, 0x0090F700, "Yardies"),
        GANG06      (12, 0x0090EF81, "Colombians"),
        GANG07      (13, 0x0090DF80, "Hoods"),
        GANG08      (14, 0x0090BF80, "(unused gang)"),
        GANG09      (15, 0x00907F80, "(unused gang)"),
        EMERGENCY   (16, 0x00800000, "Paramedic"),
        FIREMAN     (17, 0x00,       "Firefighter"),
        CRIMINAL    (18, 0x00B00040, "Criminal"),
        _SPECIAL    (19, 0x00,       "Additional Peds"),     // unused?
        PROSTITUTE  (20, 0x02900000, "Prostitute"),
        SPECIAL     (21, 0x00,       "Special"),
        _UNUSED     (22, 0x00,       "(unused)");
        
        private final int id;
        private final int defaultThreatFlags;
        private final String friendlyName;
        
        private PedType(int id, int defaultThreatFlags, String friendlyName)
        {
            this.id = id;
            this.defaultThreatFlags = defaultThreatFlags;
            this.friendlyName = friendlyName;
        }
        
        public int getID()
        {
            return id;
        }
        
        public int getDefaultThreatFlags()
        {
            return defaultThreatFlags;
        }
        
        public int getPedTypeMask()
        {
            return (1 << id);
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
    
    public enum CarBomb
    {
        NONE            (0, "(none)"),
        TIMED           (1, "Time Bomb"),
        ONIGNITION      (2, "Ignition Bomb"),
        REMOTE          (3, "Remote Bomb"),
        TIMEDACTIVE     (4, "Time Bomb (armed)"),
        ONIGNITIONACTIVE(5, "Ignition Bomb (armed)");
        
        private final int id;
        private final String friendlyName;
        
        private CarBomb(int id, String friendlyName)
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
        HEAD_RADIO          (0, "Head Radio"),
        DOUBLE_CLEF_FM      (1, "Double Clef FM"),
        JAH_RADIO           (2, "Jah Radio"),
        RISE_FM             (3, "Rise FM"),
        LIPS_106            (4, "Lips 106"),
        GAME_FM             (5, "Game FM"),
        MSX_FM              (6, "MSX FM"),
        FLASHBACK_95_6      (7, "Flashback 95.6"),
        CHATTERBOX_109      (8, "Chatterbox 109"),
        USER_TRACK_PLAYER   (9, "User track player"),
        POLICE_RADIO        (10, "Police radio"),
        RADIO_OFF           (11, "(radio off)");
        
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
        // TODO: Read directly from carcols.dat
        BLACK                   (0, 5, 5, 5),
        WHITE                   (1, 245, 245, 245),
        POLICE_CAR_BLUE         (2, 42, 119, 161),
        CHERRY_RED              (3, 179, 54, 58),
        MIDNIGHT_BLUE           (4, 38, 55, 57),
        TEMPLE_CURTAIN_PURPLE   (5, 134, 68, 110),
        TAXI_YELLOW             (6, 243, 237, 71),
        STRIKING_BLUE           (7, 76, 117, 183),
        LIGHT_BLUE_GREY         (8, 102, 114, 146),
        HOODS                   (9, 94, 112, 114),
        RED1                    (10, 53, 34, 36),
        RED2                    (11, 90, 33, 36),
        RED3                    (12, 102, 43, 43),
        RED4                    (13, 99, 50, 46),
        RED5                    (14, 132, 40, 39),
        RED6                    (15, 138, 58, 66),
        RED7                    (16, 104, 39, 49),
        RED8                    (17, 139, 60, 68),
        RED9                    (18, 158, 47, 43),
        RED10                   (19, 163, 58, 47),
        ORANGE1                 (20, 210, 86, 51),
        ORANGE2                 (21, 146, 86, 53),
        ORANGE3                 (22, 244, 114, 58),
        ORANGE4                 (23, 211, 87, 51),
        ORANGE5                 (24, 226, 90, 89),
        ORANGE6                 (25, 119, 42, 37),
        ORANGE7                 (26, 225, 119, 67),
        ORANGE8                 (27, 196, 70, 54),
        ORANGE9                 (28, 225, 120, 68),
        ORANGE10                (29, 195, 89, 56),
        YELLOW1                 (30, 70, 72, 64),
        YELLOW2                 (31, 116, 119, 97),
        YELLOW3                 (32, 117, 119, 99),
        YELLOW4                 (33, 145, 138, 61),
        YELLOW5                 (34, 148, 140, 102),
        YELLOW6                 (35, 153, 141, 121),
        YELLOW7                 (36, 216, 165, 52),
        YELLOW8                 (37, 201, 189, 125),
        YELLOW9                 (38, 201, 197, 145),
        YELLOW10                (39, 212, 200, 78),
        GREEN1                  (40, 26, 51, 46),
        GREEN2                  (41, 36, 47, 43),
        GREEN3                  (42, 29, 55, 63),
        GREEN4                  (43, 60, 74, 59),
        GREEN5                  (44, 45, 80, 55),
        GREEN6                  (45, 58, 108, 96),
        GREEN7                  (46, 58, 98, 60),
        GREEN8                  (47, 124, 162, 130),
        GREEN9                  (48, 76, 82, 78),
        GREEN10                 (49, 86, 119, 91),
        BLUE1                   (50, 28, 70, 80),
        BLUE2                   (51, 72, 94, 132),
        BLUE3                   (52, 28, 39, 69),
        BLUE4                   (53, 31, 52, 104),
        BLUE5                   (54, 43, 72, 120),
        BLUE6                   (55, 71, 92, 131),
        BLUE7                   (56, 68, 124, 146),
        BLUE8                   (57, 61, 103, 171),
        BLUE9                   (58, 75, 125, 130),
        BLUE10                  (59, 128, 176, 183),
        PURPLE1                 (60, 61, 35, 51),
        PURPLE2                 (61, 28, 41, 72),
        PURPLE3                 (62, 52, 57, 65),
        PURPLE4                 (63, 64, 69, 76),
        PURPLE5                 (64, 74, 45, 43),
        PURPLE6                 (65, 86, 62, 51),
        PURPLE7                 (66, 65, 70, 76),
        PURPLE8                 (67, 103, 39, 49),
        PURPLE9                 (68, 131, 90, 117),
        PURPLE10                (69, 134, 133, 135),
        GREY1                   (70, 23, 23, 23),
        GREY2                   (71, 46, 46, 46),
        GREY3                   (72, 69, 69, 69),
        GREY4                   (73, 92, 92, 92),
        GREY5                   (74, 115, 115, 115),
        GREY6                   (75, 138, 138, 138),
        GREY7                   (76, 161, 161, 161),
        GREY8                   (77, 184, 184, 184),
        GREY9                   (78, 207, 207, 207),
        GREY10                  (79, 230, 230, 230),
        LIGHT1                  (80, 170, 175, 170),
        LIGHT2                  (81, 106, 115, 107),
        LIGHT3                  (82, 170, 175, 170),
        LIGHT4                  (83, 187, 190, 181),
        LIGHT5                  (84, 187, 190, 181),
        LIGHT6                  (85, 106, 111, 112),
        LIGHT7                  (86, 96, 99, 95),
        LIGHT8                  (87, 106, 115, 107),
        LIGHT9                  (88, 170, 175, 170),
        LIGHT10                 (89, 187, 190, 181),
        DARK1                   (90, 33, 41, 43),
        DARK2                   (91, 52,56,66),
        DARK3                   (92, 65, 70, 72),
        DARK4                   (93, 78, 89, 96),
        DARK5                   (94, 65, 69, 76);
        
        private final int id, r, g, b;
        
        private CarColor(int id, int r, int g, int b)
        {
            this.id = id;
            this.r = r;
            this.g = g;
            this.b = b;
        }
        
        public int getID()
        {
            return id;
        }
        
        public Color getColor()
        {
            return new Color(r, g, b);
        }
    }
}
