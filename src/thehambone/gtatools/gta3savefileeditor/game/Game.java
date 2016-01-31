package thehambone.gtatools.gta3savefileeditor.game;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import thehambone.gtatools.gta3savefileeditor.Main;

/**
 * This class contains methods relating to the Grand Theft Auto III game itself.
 * <p>
 * Created on Mar 5, 2015.
 * 
 * @author thehambone
 */
public class Game
{
    /**
     * Returns the path to the "GTA3 User Files" folder.
     * 
     * @return the path to the "GTA3 User Files" folder
     */
    public static String getUserDirectoryPath()
    {
        FileSystemView fsv = new JFileChooser().getFileSystemView();
        String documentsPath = fsv.getDefaultDirectory().getPath();
        
        String gta3UserFiles = documentsPath + File.separator;
        if (Main.getOperatingSystem() == Main.OperatingSystem.MAC_OS_X) {
            gta3UserFiles += 
                    "Documents" + File.separator + "Rockstar Games"
                    + File.separator + "GTA3 User Files";
        } else {
            gta3UserFiles += "GTA3 User Files";
        }
        
        return gta3UserFiles;
    }
}
