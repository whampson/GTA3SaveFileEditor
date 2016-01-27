package thehambone.gtatools.gta3savefileeditor.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Convenience class for certain GUI operations.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 24, 2015
 */
public class GUIUtils
{
    public static final int DEFAULT_WIDTH = 200;
    
    public static String formatHTMLString(String s)
    {
        return formatHTMLString(s, DEFAULT_WIDTH, false);
    }
    
    public static String formatHTMLString(String s, int width, boolean center)
    {
        if (center) {
            return String.format("<html><p style='width: %dpx; text-align: center'>%s</p></html>", width, s.replaceAll("\n", "<br>"));
        } else {
            return String.format("<html><p style='width: %dpx;'>%s</p></html>", width, s.replaceAll("\n", "<br>"));
        }
    }
    
    public static void showErrorMessage(Component parent, String message, String title)
    {
        showErrorMessage(parent, message, title, DEFAULT_WIDTH, null);
    }
    
    public static void showErrorMessage(Component parent, String message, String title, Throwable t)
    {
        showErrorMessage(parent, message, title, DEFAULT_WIDTH, t);
    }
    
    public static void showErrorMessage(Component parent,
                                        String message,
                                        String title,
                                        int width,
                                        Throwable t) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(message);
        if (t != null) {
            messageBuilder.append("\n");
            messageBuilder.append("\n");
            messageBuilder.append("Error details:\n");
            messageBuilder.append(t.getClass().getName()).append(": ").append(t.getMessage());
        }
        JOptionPane.showMessageDialog(parent,
                    formatHTMLString(messageBuilder.toString(), width, false),
                    title,
                    JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showInformationMessage(Component parent, String message,
            String title)
    {
        showInformationMessage(parent, message, title, DEFAULT_WIDTH);
    }
    
    public static void showInformationMessage(Component parent, String message,
            String title, int width)
    {
        JOptionPane.showMessageDialog(parent,
                formatHTMLString(message, width, false),
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}