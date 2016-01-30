package thehambone.gtatools.gta3savefileeditor.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import thehambone.gtatools.gta3savefileeditor.Settings;

/**
 * This class provides several convenience methods for GUI-related tasks, such
 * as showing message boxes and creating HTML-formatted strings for JLabels.
 * <p>
 * Created on Mar 24, 2015.
 * 
 * @author thehambone
 */
public class GUIUtilities
{
    /**
     * The default width in pixels for text.
     */
    public static final int DEFAULT_TEXT_WIDTH = 200;
    
    /**
     * Takes a string and formats it as an HTML paragraph. HTML-formatted
     * strings can be used on JLabels when text on multiple lines is required,
     * as well as bold/italic/underlined text or any other HTML formatting.
     * 
     * @param s the string to format
     * @return the HTML-formatted string
     */
    public static String formatHTMLString(String s)
    {
        return formatHTMLString(s, DEFAULT_TEXT_WIDTH, false, null);
    }
    
    /**
     * Takes a string and formats it as an HTML paragraph. HTML-formatted
     * strings can be used on JLabels when text on multiple lines is required,
     * as well as bold/italic/underlined text or any other HTML formatting. This
     * method can provide some HTML text attributes, namely text width, centered
     * text, and text color. All other attributes should be specified in the
     * format string.
     * 
     * @param s the string to format
     * @param width maximum desired text width in pixels
     * @param textCentered a boolean indicating whether the text should be
     *        centered
     * @param textColor the text color
     * @return the HTML-formatted string
     */
    public static String formatHTMLString(String s, int width,
            boolean textCentered, Color textColor)
    {
        StringBuilder styleBuilder = new StringBuilder();
        
        styleBuilder.append(String.format("width: %dpx;", width));
        if (textCentered) {
            styleBuilder.append("text-align: center;");
        }
        if (textColor != null) {
            styleBuilder.append(String.format("color: #%06x;",
                    textColor.getRGB() & ~0xFF000000)); // Un-set alpha bits
        }
        
        return String.format("<html><p style='%s'>%s</p></html>",
                styleBuilder.toString(), s.replaceAll("\n", "<br>"));
    }
    
    /**
     * Displays a dialog box containing the system's "error" icon, the
     * specified message text, and an OK button. A sound is played when the
     * message is shown. HTML text attributes and newlines may be included in
     * the message string.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown on the dialog
     * @param title the dialog title
     */
    public static void showErrorMessageBox(Component parent, String message,
            String title)
    {
        showErrorMessageBox(parent, message, title, null, DEFAULT_TEXT_WIDTH,
                true);
    }
    
    /**
     * Displays a dialog box containing the system's "error" icon, the
     * specified message text, and an OK button. A sound is played when the
     * message is shown. HTML text attributes and newlines may be included in
     * the message string. Optionally, the exception that caused the error can
     * be provided and details about the exception will be shown in the message.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown on the dialog
     * @param title the dialog title
     * @param cause the exception that caused the error, may be null
     */
    public static void showErrorMessageBox(Component parent, String message,
            String title, Throwable cause)
    {
        showErrorMessageBox(parent, message, title, cause, DEFAULT_TEXT_WIDTH,
                true);
    }
    
    /**
     * Displays a dialog box containing the system's "error" icon, the
     * specified message text, and an OK button. HTML text attributes and
     * newlines may be included in the message string. Optionally, the exception
     * that caused the error can be provided and details about the exception
     * will be shown in the message. Additionally, the maximum text width can be
     * specified, which ultimately affects the dialog size, along the ability to
     * toggle playing a sound with the message.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown on the dialog
     * @param title the dialog title
     * @param cause the exception that caused the error, may be null
     * @param textWidth the maximum desired width of the message text in pixels
     * @param playSound a boolean indicating whether to play the system error
     *        sound with the message
     */
    public static void showErrorMessageBox(Component parent, String message,
            String title, Throwable cause, int textWidth, boolean playSound)
    {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(message);
        
        if (cause != null) {
            messageBuilder.append("\n");
            messageBuilder.append("\n");
            messageBuilder.append("Error details:\n");
            messageBuilder.append(cause.getClass().getSimpleName()).append(": ")
                    .append(cause.getMessage());
        }
        
        message = messageBuilder.toString();
        
        if (playSound) {
            /* We have to use the "exclamation" sound because Java doesn't
               provide access to the "critical" sound (for whatever reason) */
            Runnable winSound = (Runnable)Toolkit.getDefaultToolkit()
                    .getDesktopProperty("win.sound.exclamation");
            if (winSound != null) {
                winSound.run();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
        JOptionPane.showMessageDialog(parent,
                formatHTMLString(message, textWidth, false, null),
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a dialog box containing the system's "information" icon, the
     * specified message text, and an OK button. HTML text attributes and
     * newlines may be included in the message string.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown in the dialog
     * @param title the dialog title
     */
    public static void showInformationMessageBox(Component parent,
            String message, String title)
    {
        showInformationMessageBox(parent, message, title, DEFAULT_TEXT_WIDTH,
                false);
    }
    
    /**
     * Shows a dialog box containing the system's "information" icon, the
     * specified message text, and an OK button. HTML text attributes and
     * newlines may be included in the message string. Additionally, the maximum
     * text width can be specified, which ultimately affects the dialog size.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown in the dialog
     * @param title the dialog title
     * @param textWidth the maximum desired width of the message text in pixels
     */
    public static void showInformationMessageBox(Component parent,
            String message, String title, int textWidth)
    {
        showInformationMessageBox(parent, message, title, textWidth, false);
    }
    
    /**
     * Shows a dialog box containing the system's "information" icon, the
     * specified message text, and an OK button. HTML text attributes and
     * newlines may be included in the message string. Additionally, the maximum
     * text width can be specified, which ultimately affects the dialog size,
     * along the ability to toggle playing a sound with the message.
     * 
     * @param parent the dialog parent
     * @param message the message to be shown in the dialog
     * @param title the dialog title
     * @param textWidth the maximum desired width of the message text in pixels
     * @param playSound a boolean indicating whether to play a sound with the
     *        message
     */
    public static void showInformationMessageBox(Component parent,
            String message, String title, int textWidth, boolean playSound)
    {
        if (playSound) {
            Runnable winSound = (Runnable)Toolkit.getDefaultToolkit()
                    .getDesktopProperty("win.sound.asterisk");
            if (winSound != null) {
                winSound.run();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
        JOptionPane.showMessageDialog(parent,
                formatHTMLString(message, textWidth, false, null),
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays a file selection dialog. The dialog will have a file name filter
     * enabled to only allow for files with the ".b" extension to be selected.
     * However, this can be disabled by the user. The file selection dialog will
     * start in the most recently-visited folder.
     * 
     * @param parent the dialog parent
     * @param title the dialog title
     * @param approveButtonText the text to appear on the approve button
     * @return the selected file, {@code null} if the user cancels
     */
    public static File showFileSelectionDialog(Component parent, String title,
            String approveButtonText)
    {
        String lastLocation = Settings.get(Settings.Key.LAST_LOCATION);
        
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(title);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileFilter(new FileNameExtensionFilter(
                "GTA III-era Save Files (*.b)", "b"));
        if (lastLocation != null) {
            jfc.setCurrentDirectory(new File(lastLocation));
        }
        
        int option = jfc.showDialog(parent, approveButtonText);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        File f = jfc.getSelectedFile();
        
        Settings.set(Settings.Key.LAST_LOCATION, f.getAbsolutePath());
        
        return f;
    }
    
    /**
     * Displays a file selection dialog that only allows folders to be selected.
     * The file selection dialog will start in the most recently-visited folder.
     * 
     * @param parent the dialog parent
     * @param title the dialog title
     * @param approveButtonText the text to appear on the approve button
     * @return the selected file, {@code null} if the user cancels
     */
    public static File showDirectorySelectionDialog(Component parent,
            String title, String approveButtonText)
    {
        String lastLocation = Settings.get(Settings.Key.LAST_LOCATION);
        
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(title);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        if (lastLocation != null) {
            jfc.setCurrentDirectory(new File(lastLocation));
        }
        
        int option = jfc.showDialog(parent, approveButtonText);
        if (option != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        File dir = jfc.getSelectedFile();
        
        Settings.set(Settings.Key.LAST_LOCATION, dir.getAbsolutePath());
        
        return dir;
    }
}
