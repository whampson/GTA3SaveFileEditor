package thehambone.gtatools.gta3savefileeditor.game;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import thehambone.gtatools.gta3savefileeditor.Main;

/**
 * Contains methods relating to the Grand Theft Auto III game.
 * 
 * This class may be removed in the future because I'm not sure what else would
 go in here other than getUserDirectoryPath().
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 05, 2015
 */
public class Game
{
    /**
     * Returns the path to the "GTA3 User Files" folder.
     * 
     * @return the path to the "GTa3 User Files" folder
     */
    public static String getUserDirectoryPath()
    {
        FileSystemView fsv = new JFileChooser().getFileSystemView();
        String documentsPath = fsv.getDefaultDirectory().getPath();
        
        String gta3UserFiles = documentsPath + File.separator;
        if (Main.getOperatingSystem() == Main.OperatingSystem.MAC_OS_X) {
            gta3UserFiles += "Rockstar Games" + File.separator
                    + "GTA3 User Files";
        } else {
            gta3UserFiles += "GTA3 User Files";
        }
        
        return gta3UserFiles;
    }
}
