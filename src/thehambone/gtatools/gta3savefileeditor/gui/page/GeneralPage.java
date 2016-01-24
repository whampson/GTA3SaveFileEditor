package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableComponent;
import thehambone.gtatools.gta3savefileeditor.gxt.GXT;
import thehambone.gtatools.gta3savefileeditor.gxt.GXTSelectorDialog;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockSimpleVars;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.Gang;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.PedType;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarDWORD;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * Created on Mar 29, 2015.
 * 
 * @author thehambone
 */
public class GeneralPage extends Page
{
    private static final char GXT_INDICATOR = '\uFFFF';
    private static final int FLAG_TONI_MISSION2_PASSED_MOBILE_OFFSET   = 266;
    private static final int FLAG_TONI_MISSION2_PASSED_PC_OFFSET       = 262;
    private static final int FLAG_DIABLO_MISSION3_PASSED_MOBILE_OFFSET = 284;
    private static final int FLAG_DIABLO_MISSION3_PASSED_PC_OFFSET     = 280;
    private static final int FLAG_ASUKA_MISSION1_PASSED_MOBILE_OFFSET  = 320;
    private static final int FLAG_ASUKA_MISSION1_PASSED_PC_OFFSET      = 316;
    private static final int FLAG_YARDIE_MISSION2_PASSED_MOBILE_OFFSET = 344;
    private static final int FLAG_YARDIE_MISSION2_PASSED_PC_OFFSET     = 340;
    private static final int FLAG_YARDIE_MISSION4_PASSED_MOBILE_OFFSET = 346;
    private static final int FLAG_YARDIE_MISSION4_PASSED_PC_OFFSET     = 342;
    private static final int FLAG_HOOD_MISSION5_PASSED_MOBILE_OFFSET   = 367;
    private static final int FLAG_HOOD_MISSION5_PASSED_PC_OFFSET       = 363;
    
    private Map<String, String> gxt;
    private JComponent[] timestampComponents;
    private BlockSimpleVars simp;
    private String prevTitleGXTKey;
    private String prevTitleText;
    private boolean flagToniMission2Passed;
    private boolean flagDiabloMission3Passed;
    private boolean flagAsukaMission1Passed;
    private boolean flagYardieMission2Passed;
    private boolean flagYardieMission4Passed;
    private boolean flagHoodMission5Passed;
    
    /**
     * Creates a new {@code GeneralPage} object.
     */
    public GeneralPage()
    {
        super("General", Visibility.VISIBLE_WHEN_FILE_LOADED_ONLY);
        
        initComponents();
        initVariableComponentParameters();
        initSaveNameComponents();
        initTimestampComponents();
        initGlitchFixButtons();
        initWeatherComponents();
    }
    
    /*
     * Sets parameters for the various VariableComponents found on the page.
     */
    private void initVariableComponentParameters()
    {
        monthComboBox.setValueOffset(1);
        dayTextField.setUnsigned(true);
        yearTextField.setUnsigned(true);
        hourTextField.setDisplayFormat("%02d");
        hourTextField.setUnsigned(true);
        minuteTextField.setDisplayFormat("%02d");
        minuteTextField.setUnsigned(true);
        secondTextField.setDisplayFormat("%02d");
        secondTextField.setUnsigned(true);
        millisTextField.setUnsigned(true);
        gameTimeHourTextField.setDisplayFormat("%02d");
        gameTimeHourTextField.setUnsigned(true);
        gameTimeMinuteTextField.setDisplayFormat("%02d");
        gameTimeMinuteTextField.setUnsigned(true);
        globalTimerTextField.setUnsigned(true);
        weatherTimerTextField.setUnsigned(true);
        minuteLengthTextField.setUnsigned(true);
        weatherInterpolationSlider.setMinimum(0);
        weatherInterpolationSlider.setMaximum(1000);
        weatherInterpolationSlider.setScale(1000);
        weatherNeverChangesCheckBox.setDeselectedValue(-1);
    }
    
    /*
     * Sets up the components in the "Save Name" pane.
     */
    private void initSaveNameComponents()
    {
        // Place radio buttons in a button group.
        // This allows toggling between buttons.
        ButtonGroup bg = new ButtonGroup();
        bg.add(gxtKeyRadioButton);
        bg.add(textRadioButton);
        
        // Set up "GXT Key" radio button action listener
        gxtKeyRadioButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gxtKeyRadioButtonAction(e);
            }
        });
        
        // Set up "Text" radio button action listener
        textRadioButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textRadioButtonAction(e);
            }
        });
        
        // Set up "Select GXT String" button action listener
        selectGXTStringButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                selectGXTStringButtonAction(e);
            }
        });
    }
    
    /*
     * Sets up the components in the "Timestamp" pane.
     */
    private void initTimestampComponents()
    {
        // This is used to enable/disable all timestamp components
        timestampComponents = new JComponent[] {
            monthComboBox, dayTextField, dayOfWeekComboBox, yearTextField,
            hourTextField, minuteTextField, secondTextField, millisTextField,
            monthLabel, dayLabel, dayOfWeekLabel, yearLabel, hourLabel,
            minuteLabel, secondLabel, millisLabel
        };
    }
    
    private void initGlitchFixButtons()
    {
        purpleNinesGlitchFixButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fixPurpleNinesGlitch();
            }
        });
        
        disableHostilePedsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                fixHostilePeds();
            }
        });
    }
    
    /*
     * Sets up the components in the "Weather" pane.
     */
    @SuppressWarnings("unchecked")
    private void initWeatherComponents()
    {
        DefaultComboBoxModel<String> currentWeatherModel
                = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> previousWeatherModel
                = new DefaultComboBoxModel<>();
        
        // Populate combo boxes with weather types
        for (GameConstants.WeatherType w : GameConstants.WeatherType.values()) {
            currentWeatherModel.addElement(w.getFriendlyName());
            previousWeatherModel.addElement(w.getFriendlyName());
        }
        currentWeatherComboBox.setModel(currentWeatherModel);
        previousWeatherComboBox.setModel(previousWeatherModel);
        
        // Set up current weather combo box action listener
        currentWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentWeatherComboBoxAction(e);
            }
        });
        
        // Set up previous weather combo box action listener
        previousWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                previousWeatherComboBoxAction(e);
            }
        });
    }
    
    /*
     * Updates the save title text field and variable
     */
    private void updateSaveTitle(String str, boolean isGXTKey)
    {
        if (isGXTKey) {
            // Put text field in "GXT" mode
            saveTitleTextField.updateVariableOnChange(false);
            saveTitleTextField.setMaxInputLength(-1);
            saveTitleTextField.setEditable(false);
                
            String gxtValue = gxt.get(str);
            boolean isKeyValid = gxtValue != null;
            
            if (isKeyValid) {
                // Use GXT value as text field text
                saveTitleTextField.setText(gxtValue);
                saveTitleTextField.setToolTipText(gxtValue);
                
                prevTitleGXTKey = str;
                prevTitleText = gxtValue;
                
                // Update variable with GXT key
                simp.szSaveName.setValue(GXT_INDICATOR + str);
                Logger.debug("Variable updated: " + simp.szSaveName);
                notifyChange(simp.szSaveName);
            } else {
                saveTitleTextField.setText("<invalid GXT key>");
                saveTitleTextField.setToolTipText("Invalid GXT key.");
            }
        } else {
            // Put text field in "Text" mode
            int maxLen = simp.szSaveName.getMaxLength() - 1;
            saveTitleTextField.setMaxInputLength(maxLen);
            saveTitleTextField.setEditable(true);
            saveTitleTextField.updateVariableOnChange(true);
            
            prevTitleText = str;
            
            // Update variable with string
            simp.szSaveName.setValue(str);
            Logger.debug("Variable updated: " + simp.szSaveName);
            notifyChange(simp.szSaveName);
            
            // Refresh text field
            saveTitleTextField.refreshComponent();
            saveTitleTextField.setToolTipText(null);
        }
    }
    
    private void detectPurpleNinesGlitch()
    {
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        
        // Get Hoods gang data
        Gang hoods = saveFile.gangs.aGang.getElementAt(6);
        
        if (hoods.nPedModelOverrideIndex.getValue() != -1
                && !flagHoodMission5Passed) {
            purpleNinesGlitchFixButton.setEnabled(true);
            purpleNinesGlitchFixButton.setText("Fix");
        } else {
            purpleNinesGlitchFixButton.setEnabled(false);
            purpleNinesGlitchFixButton.setText("Glitch not present");
        }
    }
    
    private void fixPurpleNinesGlitch()
    {
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        
        Gang hoods = saveFile.gangs.aGang.getElementAt(6);
        hoods.nPedModelOverrideIndex.setValue((byte)-1);
        
        purpleNinesGlitchFixButton.setEnabled(false);
        purpleNinesGlitchFixButton.setText("Fixed");
        
        Logger.debug("Variable updated: " + hoods.nPedModelOverrideIndex);
        notifyChange(hoods.nPedModelOverrideIndex);
    }
    
    private void detectHostilePeds()
    {
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        VarArray<PedType> aPedType = saveFile.pedTypes.aPedType;
        
        // Get CIVMALE data
        int civmaleID = GameConstants.PedType.CIVMALE.getID();
        PedType civmale = aPedType.getElementAt(civmaleID);
        
        int civmaleThreat = civmale.nThreatFlags.getValue();
        int player01Mask = GameConstants.PedType.PLAYER01.getPedTypeMask();
        int civmaleMask = GameConstants.PedType.CIVMALE.getPedTypeMask();
        int civfemaleMask = GameConstants.PedType.CIVFEMALE.getPedTypeMask();
        
        /* If CIVMALE is hostile towards player, CIVMALE, or CIVFEMALE
           then the cheats were most likely enabled. */
        if (((civmaleThreat & player01Mask) != 0)
                || ((civmaleThreat & civmaleMask) != 0)
                || ((civmaleThreat & civfemaleMask) != 0)) {
            disableHostilePedsButton.setEnabled(true);
            disableHostilePedsButton.setText("Fix");
        } else {
            disableHostilePedsButton.setEnabled(false);
            disableHostilePedsButton.setText("Peds not hostile");
        }
    }
    
    private void fixHostilePeds()
    {
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        VarArray<PedType> aPedType = saveFile.pedTypes.aPedType;
        GameConstants.PedType pedTypes[] = GameConstants.PedType.values();
        
        GameConstants.PedType player01 = GameConstants.PedType.PLAYER01;
        GameConstants.PedType mafia = GameConstants.PedType.GANG01;
        GameConstants.PedType triads = GameConstants.PedType.GANG02;
        GameConstants.PedType diablos = GameConstants.PedType.GANG03;
        GameConstants.PedType yakuza = GameConstants.PedType.GANG04;
        GameConstants.PedType yardies = GameConstants.PedType.GANG05;
        GameConstants.PedType hoods = GameConstants.PedType.GANG07;
        
        // Set default threat flags for all ped types
        for (int i = 0; i < aPedType.getElementCount(); i++) {
            // Get ped type data from save file
            PedType storedPT = aPedType.getElementAt(i);
            
            // Get default threat flags for ped type
            int threat = pedTypes[i].getDefaultThreatFlags();
            
            // Update threat flags
            storedPT.nThreatFlags.setValue(threat);
            Logger.debug("Variable updated: " + storedPT.nThreatFlags);
            notifyChange(storedPT.nThreatFlags);
        }
        
        // Set gang hostility towards player based on mission completion status
        
        // Mafia
        if (flagAsukaMission1Passed) {
            PedType mafiaData = aPedType.getElementAt(mafia.getID());
            int threat = mafiaData.nThreatFlags.getValue();
            threat |= player01.getPedTypeMask();
            mafiaData.nThreatFlags.setValue(threat);
        }
        
        // Triads
        if (flagToniMission2Passed || flagDiabloMission3Passed) {
            PedType triadsData = aPedType.getElementAt(triads.getID());
            int threat = triadsData.nThreatFlags.getValue();
            threat |= player01.getPedTypeMask();
            triadsData.nThreatFlags.setValue(threat);
        }
        
        // Diablos
        if (flagYardieMission2Passed) {
            PedType diablosData = aPedType.getElementAt(diablos.getID());
            int threat = diablosData.nThreatFlags.getValue();
            threat |= player01.getPedTypeMask();
            diablosData.nThreatFlags.setValue(threat);
        }
        
        // Yardies
        if (flagYardieMission4Passed) {
            PedType yardiesData = aPedType.getElementAt(yardies.getID());
            int threat = yardiesData.nThreatFlags.getValue();
            threat |= player01.getPedTypeMask();
            yardiesData.nThreatFlags.setValue(threat);
        }
        
        disableHostilePedsButton.setEnabled(false);
        disableHostilePedsButton.setText("Fixed");
    }
    
    /*
     * Defines the action for the "Text" radio button.
     */
    private void textRadioButtonAction(ActionEvent e)
    {
        selectGXTStringButton.setEnabled(false);
        if (prevTitleText != null) {
            updateSaveTitle(prevTitleText, false);
        } else {
            updateSaveTitle(saveTitleTextField.getText(), false);
        }
    }
    
    /*
     * Defines the action for the "GXT Key" radio button.
     */
    private void gxtKeyRadioButtonAction(ActionEvent e)
    {
        selectGXTStringButton.setEnabled(true);
        if (prevTitleGXTKey != null) {
            updateSaveTitle(prevTitleGXTKey, true);
        } else {
            updateSaveTitle(saveTitleTextField.getText(), true);
        }
    }
    
    /*
     * Defines the action for the "Select GXT String" button.
     */
    private void selectGXTStringButtonAction(ActionEvent e)
    {
        // Show GXT Selector dialog
        Window parent = SwingUtilities.getWindowAncestor(this);
        GXTSelectorDialog gxtSelect = new GXTSelectorDialog(parent, gxt);
        String key = gxtSelect.showKeySelectionDialog();
        
        // Update save name if user made a selection
        if (key != null) {
            updateSaveTitle(key, true);
        }
    }
    
    /*
     * Defines the action for the "Current weather" combo box.
     */
    private void currentWeatherComboBoxAction(ActionEvent e)
    {
        // Update rightmost label above weather interpolation slider
        currentWeatherSliderLabel.setText(
                currentWeatherComboBox.getSelectedItem().toString());
        
        /* Set the selected value of the "Never changes" checkbox equal to the
           ID of the current weather selection */
        weatherNeverChangesCheckBox.setSelectedValue(
                currentWeatherComboBox.getSelectedIndex());
        
        /* Write selected value from aboce to the "Never changes" checkbox
        variable if the checkbox is selected */
        if (weatherNeverChangesCheckBox.isSelected()) {
            weatherNeverChangesCheckBox.updateVariable();
        }
    }
    
    /*
     * Defines the action for the "Previous weather" combo box.
     */
    private void previousWeatherComboBoxAction(ActionEvent e)
    {
        // Update leftmost label above weather interpolation slider
        previousWeatherSliderLabel.setText(
                previousWeatherComboBox.getSelectedItem().toString());
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getPageTitle());
        
        // Get most up-to-date GXT table
        gxt = GXT.getGXTTable();
        
        SaveFileNew saveFile = SaveFileNew.getCurrentSaveFile();
        simp = saveFile.simpleVars;
        
        boolean isPC = (saveFile.getPlatform() == SaveFileNew.Platform.PC);
        boolean isMobile
                = (saveFile.getPlatform() == SaveFileNew.Platform.ANDROID)
                        || (saveFile.getPlatform() == SaveFileNew.Platform.IOS);
        
        // Enable or disable timestamp components based on platform
        for (JComponent comp : timestampComponents) {
            comp.setEnabled(isPC);
        }
        
        // Enable GXT-related components for save name (mobile only)
        gxtKeyRadioButton.setVisible(isMobile);
        textRadioButton.setVisible(isMobile);
        selectGXTStringButton.setVisible(isMobile);
        
        // Set up "Save Name" page
        saveTitleTextField.setVariable(simp.szSaveName);
        saveTitleTextField.setMaxInputLength(simp.szSaveName.getMaxLength()- 1);
        if (isMobile) {
            if (gxt != null) {
                // Enable GXT features
                gxtKeyRadioButton.setEnabled(true);
                
                // Check whether save title is a GXT key
                if (simp.szSaveName.getValue().charAt(0) == GXT_INDICATOR) {
                    // Select "GXT Key" button and get GXT string
                    prevTitleGXTKey = simp.szSaveName.getValue().substring(1);
                    gxtKeyRadioButton.doClick();
                } else {
                    // Select "Text" button
                    textRadioButton.doClick();
                }
            } else {
                // GXT failed to load; disable GXT features
                gxtKeyRadioButton.setEnabled(false);
                textRadioButton.doClick();
            }
        }
        
        if (isPC) {
            // Set variables for timestamp components
            monthComboBox.setVariable(simp.timestamp.nMonth);
            dayTextField.setVariable(simp.timestamp.nDay);
            dayOfWeekComboBox.setVariable(simp.timestamp.nDayOfWeek);
            yearTextField.setVariable(simp.timestamp.nYear);
            hourTextField.setVariable(simp.timestamp.nHour);
            minuteTextField.setVariable(simp.timestamp.nMinute);
            secondTextField.setVariable(simp.timestamp.nSecond);
            millisTextField.setVariable(simp.timestamp.nMillisecond);
        }
        
        // Set variables for all other VariableComponents
        gameTimeHourTextField.setVariable(simp.nGameHours);
        gameTimeMinuteTextField.setVariable(simp.nGameMinutes);
        globalTimerTextField.setVariable(simp.nTimeInMillis);
        weatherTimerTextField.setVariable(simp.nLastClockTick);
        minuteLengthTextField.setVariable(simp.nGameMinuteLengthMillis);
        currentWeatherComboBox.setVariable(simp.nCurrentWeatherType);
        previousWeatherComboBox.setVariable(simp.nPreviousWeatherType);
        weatherNeverChangesCheckBox.setVariable(simp.nForcedWeatherType);
        weatherInterpolationSlider.setVariable(simp.fWeatherInterpolationValue);
        
        // Update weather combo boxes to fire action events
        currentWeatherComboBox.updateVariableOnChange(false);
        previousWeatherComboBox.updateVariableOnChange(false);
        currentWeatherComboBox
                .setSelectedIndex(simp.nCurrentWeatherType.getValue());
        previousWeatherComboBox
                .setSelectedIndex(simp.nPreviousWeatherType.getValue());
        currentWeatherComboBox.updateVariableOnChange(true);
        previousWeatherComboBox.updateVariableOnChange(true);
        
        // Set flags from global vars
        VarArray<VarDWORD> globalVars = simp.theScripts.aScriptVariable;
        if (isPC) {
            flagToniMission2Passed = globalVars.getElementAt(
                    FLAG_TONI_MISSION2_PASSED_PC_OFFSET).toBoolean();
            flagDiabloMission3Passed = globalVars.getElementAt(
                    FLAG_DIABLO_MISSION3_PASSED_PC_OFFSET).toBoolean();
            flagAsukaMission1Passed = globalVars.getElementAt(
                    FLAG_ASUKA_MISSION1_PASSED_PC_OFFSET).toBoolean();
            flagYardieMission2Passed = globalVars.getElementAt(
                    FLAG_YARDIE_MISSION2_PASSED_PC_OFFSET).toBoolean();
            flagYardieMission4Passed = globalVars.getElementAt(
                    FLAG_YARDIE_MISSION4_PASSED_PC_OFFSET).toBoolean();
            flagHoodMission5Passed = globalVars.getElementAt(
                    FLAG_HOOD_MISSION5_PASSED_PC_OFFSET).toBoolean();
        } else if (isMobile) {
            flagToniMission2Passed = globalVars.getElementAt(
                    FLAG_TONI_MISSION2_PASSED_MOBILE_OFFSET).toBoolean();
            flagDiabloMission3Passed = globalVars.getElementAt(
                    FLAG_DIABLO_MISSION3_PASSED_MOBILE_OFFSET).toBoolean();
            flagAsukaMission1Passed = globalVars.getElementAt(
                    FLAG_ASUKA_MISSION1_PASSED_MOBILE_OFFSET).toBoolean();
            flagYardieMission2Passed = globalVars.getElementAt(
                    FLAG_YARDIE_MISSION2_PASSED_MOBILE_OFFSET).toBoolean();
            flagYardieMission4Passed = globalVars.getElementAt(
                    FLAG_YARDIE_MISSION4_PASSED_MOBILE_OFFSET).toBoolean();
            flagHoodMission5Passed = globalVars.getElementAt(
                    FLAG_HOOD_MISSION5_PASSED_MOBILE_OFFSET).toBoolean();
        }
        
        // Detect bugs/glitches
        detectPurpleNinesGlitch();
        detectHostilePeds();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        scrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        saveTitlePanel = new javax.swing.JPanel();
        saveTitleTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.StringVariableTextField();
        gxtKeyRadioButton = new javax.swing.JRadioButton();
        textRadioButton = new javax.swing.JRadioButton();
        selectGXTStringButton = new javax.swing.JButton();
        gameTimePanel = new javax.swing.JPanel();
        gameTimeHourLabel = new javax.swing.JLabel();
        gameTimeHourTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        globalTimerLabel = new javax.swing.JLabel();
        globalTimerTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        gameTimeMinuteLabel = new javax.swing.JLabel();
        gameTimeMinuteTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        weatherTimerLabel = new javax.swing.JLabel();
        weatherTimerTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        minuteLengthLabel = new javax.swing.JLabel();
        minuteLengthTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        msLabel = new javax.swing.JLabel();
        timestampPanel = new javax.swing.JPanel();
        monthLabel = new javax.swing.JLabel();
        monthComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        dayLabel = new javax.swing.JLabel();
        dayTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        dayOfWeekLabel = new javax.swing.JLabel();
        dayOfWeekComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        yearLabel = new javax.swing.JLabel();
        yearTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        hourLabel = new javax.swing.JLabel();
        hourTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        minuteLabel = new javax.swing.JLabel();
        minuteTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        secondLabel = new javax.swing.JLabel();
        secondTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        millisLabel = new javax.swing.JLabel();
        millisTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        glitchAndBugFixesPanel = new javax.swing.JPanel();
        purpleNinesGlitchLabel = new javax.swing.JLabel();
        purpleNinesGlitchFixButton = new javax.swing.JButton();
        disableHostilePedsLabel = new javax.swing.JLabel();
        disableHostilePedsButton = new javax.swing.JButton();
        weatherPanel = new javax.swing.JPanel();
        currentWeatherSelectionLabel = new javax.swing.JLabel();
        currentWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        weatherNeverChangesCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox();
        previousWeatherSelectionLabel = new javax.swing.JLabel();
        previousWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        previousWeatherSliderLabel = new javax.swing.JLabel();
        currentWeatherSliderLabel = new javax.swing.JLabel();
        weatherInterpolationSlider = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableSlider();

        saveTitlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Name"));

        saveTitleTextField.setText("a");

        gxtKeyRadioButton.setText("GXT String");

        textRadioButton.setText("Text");

        selectGXTStringButton.setText("Select GXT String");

        javax.swing.GroupLayout saveTitlePanelLayout = new javax.swing.GroupLayout(saveTitlePanel);
        saveTitlePanel.setLayout(saveTitlePanelLayout);
        saveTitlePanelLayout.setHorizontalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveTitlePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(textRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(gxtKeyRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectGXTStringButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(saveTitlePanelLayout.createSequentialGroup()
                        .addComponent(saveTitleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        saveTitlePanelLayout.setVerticalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gxtKeyRadioButton)
                    .addComponent(textRadioButton)
                    .addComponent(selectGXTStringButton))
                .addContainerGap())
        );

        gameTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Time"));

        gameTimeHourLabel.setText("Hour:");

        gameTimeHourTextField.setText("0");

        globalTimerLabel.setText("Global timer:");

        globalTimerTextField.setText("0");

        gameTimeMinuteLabel.setText("Minute:");
        gameTimeMinuteLabel.setToolTipText("");

        gameTimeMinuteTextField.setText("0");
        gameTimeMinuteTextField.setToolTipText("");

        weatherTimerLabel.setText("Weather timer:");

        weatherTimerTextField.setText("0");

        minuteLengthLabel.setText("Minute length:");

        minuteLengthTextField.setText("0");

        msLabel.setText("ms");

        javax.swing.GroupLayout gameTimePanelLayout = new javax.swing.GroupLayout(gameTimePanel);
        gameTimePanel.setLayout(gameTimePanelLayout);
        gameTimePanelLayout.setHorizontalGroup(
            gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gameTimePanelLayout.createSequentialGroup()
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(gameTimeHourLabel)
                            .addComponent(gameTimeMinuteLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gameTimeHourTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(gameTimeMinuteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(weatherTimerLabel)
                            .addComponent(globalTimerLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(globalTimerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(weatherTimerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                    .addGroup(gameTimePanelLayout.createSequentialGroup()
                        .addComponent(minuteLengthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minuteLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(msLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gameTimePanelLayout.setVerticalGroup(
            gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameTimeHourLabel)
                    .addComponent(globalTimerLabel)
                    .addComponent(gameTimeHourTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(globalTimerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameTimeMinuteLabel)
                    .addComponent(weatherTimerLabel)
                    .addComponent(gameTimeMinuteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weatherTimerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gameTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minuteLengthLabel)
                    .addComponent(msLabel)
                    .addComponent(minuteLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        timestampPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Timestamp"));

        monthLabel.setText("Month:");

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec." }));

        dayLabel.setText("Day:");

        dayTextField.setText("0");

        dayOfWeekLabel.setText("Day of week:");

        dayOfWeekComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat." }));

        yearLabel.setText("Year:");

        yearTextField.setText("0");

        hourLabel.setText("Hour:");
        hourLabel.setToolTipText("");

        hourTextField.setText("0");

        minuteLabel.setText("Minute:");
        minuteLabel.setToolTipText("");

        minuteTextField.setText("0");

        secondLabel.setText("Second:");

        secondTextField.setText("0");

        millisLabel.setText("Millis.:");

        millisTextField.setText("0");

        javax.swing.GroupLayout timestampPanelLayout = new javax.swing.GroupLayout(timestampPanel);
        timestampPanel.setLayout(timestampPanelLayout);
        timestampPanelLayout.setHorizontalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timestampPanelLayout.createSequentialGroup()
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, timestampPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(monthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, timestampPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hourLabel)
                            .addComponent(dayOfWeekLabel)
                            .addComponent(secondLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(hourTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(secondTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dayOfWeekComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(yearLabel)
                    .addComponent(dayLabel)
                    .addComponent(minuteLabel)
                    .addComponent(millisLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dayTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(yearTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minuteTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(millisTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        timestampPanelLayout.setVerticalGroup(
            timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timestampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthLabel)
                    .addComponent(dayLabel)
                    .addComponent(dayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(dayOfWeekLabel)
                    .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dayOfWeekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hourLabel)
                    .addComponent(minuteLabel)
                    .addComponent(hourTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timestampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondLabel)
                    .addComponent(millisLabel)
                    .addComponent(secondTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(millisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        glitchAndBugFixesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Glitch and Bug Fixes"));

        purpleNinesGlitchLabel.setText("Purple Nines glitch:");

        purpleNinesGlitchFixButton.setText("Glitch not present");
        purpleNinesGlitchFixButton.setToolTipText("Disable the Purple Nine's glitch allowing you to complete on D-Ice's missions.");
        purpleNinesGlitchFixButton.setEnabled(false);
        purpleNinesGlitchFixButton.setMaximumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setMinimumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setPreferredSize(new java.awt.Dimension(127, 23));

        disableHostilePedsLabel.setText("Disable hostile peds:");

        disableHostilePedsButton.setText("Peds not hostile");
        disableHostilePedsButton.setToolTipText("Reverse the effects of the \"peds riot\" and \"peds attack player\" cheats.");
        disableHostilePedsButton.setEnabled(false);

        javax.swing.GroupLayout glitchAndBugFixesPanelLayout = new javax.swing.GroupLayout(glitchAndBugFixesPanel);
        glitchAndBugFixesPanel.setLayout(glitchAndBugFixesPanelLayout);
        glitchAndBugFixesPanelLayout.setHorizontalGroup(
            glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(glitchAndBugFixesPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purpleNinesGlitchLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(disableHostilePedsLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purpleNinesGlitchFixButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disableHostilePedsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        glitchAndBugFixesPanelLayout.setVerticalGroup(
            glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(glitchAndBugFixesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purpleNinesGlitchLabel)
                    .addComponent(purpleNinesGlitchFixButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(disableHostilePedsButton)
                    .addComponent(disableHostilePedsLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        purpleNinesGlitchFixButton.getAccessibleContext().setAccessibleName("Not Present");

        weatherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weather"));

        currentWeatherSelectionLabel.setText("Current weather:");

        currentWeatherComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<weather_name>" }));

        weatherNeverChangesCheckBox.setText("Never changes");

        previousWeatherSelectionLabel.setText("Previous weather:");

        previousWeatherComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<weather_name>" }));

        previousWeatherSliderLabel.setText("WeatherA");

        currentWeatherSliderLabel.setText("WeatherB");

        javax.swing.GroupLayout weatherPanelLayout = new javax.swing.GroupLayout(weatherPanel);
        weatherPanel.setLayout(weatherPanelLayout);
        weatherPanelLayout.setHorizontalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weatherInterpolationSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addComponent(previousWeatherSliderLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(currentWeatherSliderLabel))
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(weatherNeverChangesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addComponent(currentWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addComponent(previousWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previousWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        weatherPanelLayout.setVerticalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentWeatherSelectionLabel)
                    .addComponent(currentWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherNeverChangesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousWeatherSelectionLabel)
                    .addComponent(previousWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousWeatherSliderLabel)
                    .addComponent(currentWeatherSliderLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherInterpolationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(gameTimePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(glitchAndBugFixesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveTitlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(timestampPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(weatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(timestampPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(saveTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gameTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(glitchAndBugFixesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox currentWeatherComboBox;
    private javax.swing.JLabel currentWeatherSelectionLabel;
    private javax.swing.JLabel currentWeatherSliderLabel;
    private javax.swing.JLabel dayLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox dayOfWeekComboBox;
    private javax.swing.JLabel dayOfWeekLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField dayTextField;
    private javax.swing.JButton disableHostilePedsButton;
    private javax.swing.JLabel disableHostilePedsLabel;
    private javax.swing.JLabel gameTimeHourLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeHourTextField;
    private javax.swing.JLabel gameTimeMinuteLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeMinuteTextField;
    private javax.swing.JPanel gameTimePanel;
    private javax.swing.JPanel glitchAndBugFixesPanel;
    private javax.swing.JLabel globalTimerLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField globalTimerTextField;
    private javax.swing.JRadioButton gxtKeyRadioButton;
    private javax.swing.JLabel hourLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField hourTextField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel millisLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField millisTextField;
    private javax.swing.JLabel minuteLabel;
    private javax.swing.JLabel minuteLengthLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField minuteLengthTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField minuteTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox monthComboBox;
    private javax.swing.JLabel monthLabel;
    private javax.swing.JLabel msLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox previousWeatherComboBox;
    private javax.swing.JLabel previousWeatherSelectionLabel;
    private javax.swing.JLabel previousWeatherSliderLabel;
    private javax.swing.JButton purpleNinesGlitchFixButton;
    private javax.swing.JLabel purpleNinesGlitchLabel;
    private javax.swing.JPanel saveTitlePanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.StringVariableTextField saveTitleTextField;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel secondLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField secondTextField;
    private javax.swing.JButton selectGXTStringButton;
    private javax.swing.JRadioButton textRadioButton;
    private javax.swing.JPanel timestampPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableSlider weatherInterpolationSlider;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox weatherNeverChangesCheckBox;
    private javax.swing.JPanel weatherPanel;
    private javax.swing.JLabel weatherTimerLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField weatherTimerTextField;
    private javax.swing.JLabel yearLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField yearTextField;
    // End of variables declaration//GEN-END:variables
}