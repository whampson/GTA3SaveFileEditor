package thehambone.gtatools.gta3savefileeditor.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.Main;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * The "About" window.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, April 08, 2015
 */
public class AboutDialog extends JDialog
{
    private static final String ICON_PATH        = "META-INF/res/logo1.png";
    private static final String COPYRIGHT_STRING = "Copyright " + '\u00A9' + " 2015-2016.";
    private static final String ABOUT_STRING     = "Thanks to OrionSR, Seemann, Silent, and spaceeinstein "
            + "for helping me research and document GTA III saves. You guys rock!";
    
    public AboutDialog(Frame parent)
    {
        super(parent, "About", true);
        initComponents();
    }
    
    private void initComponents()
    {
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        try {
            iconLabel.setIcon(new ImageIcon(IO.loadImageResource(ICON_PATH)));
        } catch (IOException ex) {
            Logger.error("Failed to load image resource.");
            Logger.stackTrace(ex);
        }
        topPanel.add(iconLabel, BorderLayout.CENTER);

        // Center
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel authorLabel1 = new JLabel("Author:");
        authorLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        authorLabel1.setBorder(new EmptyBorder(1, 0, 1, 2));
        JLabel authorLabel2 = new JLabel(String.format("<html><a href='%s'>%s</a></html>", Main.PROGRAM_AUTHOR_URL, Main.PROGRAM_AUTHOR));
        authorLabel2.setBorder(new EmptyBorder(1, 2, 1, 0));
        authorLabel2.setToolTipText(Main.PROGRAM_AUTHOR_URL);
        authorLabel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        authorLabel2.addMouseListener(getOpenURLMouseAdapter(Main.PROGRAM_AUTHOR_URL));
        JLabel versionLabel1 = new JLabel("Version:");
        versionLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        versionLabel1.setBorder(new EmptyBorder(1, 0, 1, 2));
        JLabel versionLabel2 = new JLabel(Main.PROGRAM_VERSION + " build " + Main.getBuildNumber());
        versionLabel2.setBorder(new EmptyBorder(1, 2, 1, 0));
        JLabel buildDate1 = new JLabel("Build date:");
        buildDate1.setHorizontalAlignment(SwingConstants.RIGHT);
        buildDate1.setBorder(new EmptyBorder(1, 0, 1, 2));
        JLabel buildDate2 = new JLabel(new SimpleDateFormat("MMMM dd, yyyy").format(Main.getBuildDate()));
        buildDate2.setBorder(new EmptyBorder(1, 2, 1, 1));
        JLabel aboutTextLabel = new JLabel(GUIUtils.formatHTMLString(ABOUT_STRING, 170, true));
        aboutTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutTextLabel.setBorder(new EmptyBorder(1, 0, 1, 0));
        JLabel bugsLabel1 = new JLabel("Bug reports go to:");
        bugsLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        bugsLabel1.setBorder(new EmptyBorder(1, 0, 1, 2));
        JLabel bugsLabel2 = new JLabel(String.format("<html><a href='mailto:%s'>%s</a></html>", Main.PROGRAM_AUTHOR_EMAIL, Main.PROGRAM_AUTHOR_EMAIL));
        bugsLabel2.setBorder(new EmptyBorder(1, 2, 1, 0));
        bugsLabel2.setToolTipText(String.format("mailto:%s", Main.PROGRAM_AUTHOR_EMAIL));
        bugsLabel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bugsLabel2.addMouseListener(getOpenURLMouseAdapter(String.format("mailto:%s", Main.PROGRAM_AUTHOR_EMAIL)));
        con.gridx = 0;
        con.gridy = 0;
        centerPanel.add(authorLabel1, con);
        con.gridx = 1;
        con.gridy = 0;
        centerPanel.add(authorLabel2, con);
        con.gridx = 0;
        con.gridy = 1;
        centerPanel.add(versionLabel1, con);
        con.gridx = 1;
        con.gridy = 1;
        centerPanel.add(versionLabel2, con);
        con.gridx = 0;
        con.gridy = 2;
        centerPanel.add(buildDate1, con);
        con.gridx = 1;
        con.gridy = 2;
        centerPanel.add(buildDate2, con);
        con.gridwidth = 2;
        con.ipady = 15;
        con.gridx = 0;
        con.gridy = 3;
        centerPanel.add(aboutTextLabel, con);
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 4;
        centerPanel.add(bugsLabel1, con);
        con.gridx = 1;
        con.gridy = 4;
        centerPanel.add(bugsLabel2, con);
        
        //Bottom
        JPanel bottomLeftPanel = new JPanel(new BorderLayout());
        bottomLeftPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        JLabel copyrightLabel = new JLabel(COPYRIGHT_STRING);
        copyrightLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        JButton closeButton = new JButton("Close");
        bottomLeftPanel.add(copyrightLabel, BorderLayout.WEST);
        bottomLeftPanel.add(closeButton, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomLeftPanel, BorderLayout.SOUTH);

        add(mainPanel);

        closeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeDialog();
            }
        });
        getRootPane().registerKeyboardAction(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeDialog();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false); 
    }
    
    private MouseAdapter getOpenURLMouseAdapter(final String url)
    {
        return new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    Logger.error("Failed to open URI.");
                    Logger.stackTrace(ex);
                }
            }
        };
    }
    
    private void closeDialog()
    {
        dispose();
    }
}