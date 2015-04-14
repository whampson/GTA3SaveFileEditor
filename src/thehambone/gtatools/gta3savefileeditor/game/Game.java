package thehambone.gtatools.gta3savefileeditor.game;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * Contains methods relating to the Grand Theft Auto III game.
 * 
 * This class may be removed in the future because I'm not sure what else would
 * go in here other than getGameUserDirectoryPath().
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 05, 2015
 */
public class Game
{
    public static String getGameUserDirectoryPath()
    {
        // todo: should probably do this through native code - reading the registry and whatnot
        return new JFileChooser().getFileSystemView().getDefaultDirectory().getPath() + File.separator + "GTA3 User Files";
        // getFileSystemView().getDefaultDirectory() essentailly gets the user's "My Documents" folder
    }
}