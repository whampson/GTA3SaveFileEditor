package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.Gang;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.PedType;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockSimpleVars;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 29, 2015
 */
public class GeneralPage extends Page
{
    public GeneralPage()
    {
        super("General", Visibility.VISIBLE_WHEN_GAMESAVE_LOADED_ONLY);
        initComponents();
        addNotifiersToComponents(mainPanel);
        initComboBoxes();
    }
        
    @SuppressWarnings("unchecked")
    private void initComboBoxes()
    {
        DefaultComboBoxModel<String> currentWeatherComboBoxModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> previousWeatherComboBoxModel = new DefaultComboBoxModel<>();
        for (GameConstants.WeatherType w : GameConstants.WeatherType.values()) {
            currentWeatherComboBoxModel.addElement(w.getFriendlyName());
            previousWeatherComboBoxModel.addElement(w.getFriendlyName());
        }
        currentWeatherComboBox.setModel(currentWeatherComboBoxModel);
        previousWeatherComboBox.setModel(previousWeatherComboBoxModel);
        
        currentWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentWeatherSliderLabel.setText(
                        currentWeatherComboBox.getSelectedItem().toString());
                weatherNeverChangesCheckBox.setSelectedValue(
                        currentWeatherComboBox.getSelectedIndex());
                if (weatherNeverChangesCheckBox.isSelected()) {
                    weatherNeverChangesCheckBox.updateVariable();
                }
            }
        });
        
        previousWeatherComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                previousWeatherSliderLabel.setText(
                        previousWeatherComboBox.getSelectedItem().toString());
            }
        });
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getTitle());
        
        isPageInitializing = true;
        
        BlockSimpleVars simp = SaveFileNew.getCurrentSaveFile().simpleVars;
        
        saveTitleTextField.setMaxInputLength(simp.szSaveName.getMaxLength()- 1);
        saveTitleTextField.setVariable(simp.szSaveName);
        
        gameTimeHourTextField.setDisplayFormat("%02d");
        gameTimeHourTextField.setUnsigned(true);
        gameTimeHourTextField.setVariable(simp.nGameHours);
        
        gameTimeMinuteTextField.setDisplayFormat("%02d");
        gameTimeMinuteTextField.setUnsigned(true);
        gameTimeMinuteTextField.setVariable(simp.nGameMinutes);
        
        globalTimerTextField.setUnsigned(true);
        globalTimerTextField.setVariable(simp.nTimeInMillis);

        weatherTimerTextField.setUnsigned(true);
        weatherTimerTextField.setVariable(simp.nLastClockTick);
        
        minuteLengthTextField.setUnsigned(true);
        minuteLengthTextField.setVariable(simp.nGameMinuteLengthMillis);
        
        monthComboBox.setValueOffset(1);
        monthComboBox.setVariable(simp.timestamp.nMonth);
        
        dayTextField.setUnsigned(true);
        dayTextField.setVariable(simp.timestamp.nDay);
        
        dayOfWeekComboBox.setVariable(simp.timestamp.nDayOfWeek);
        
        yearTextField.setUnsigned(true);
        yearTextField.setVariable(simp.timestamp.nYear);
        
        hourTextField.setDisplayFormat("%02d");
        hourTextField.setUnsigned(true);
        hourTextField.setVariable(simp.timestamp.nHour);
        
        minuteTextField.setDisplayFormat("%02d");
        minuteTextField.setUnsigned(true);
        minuteTextField.setVariable(simp.timestamp.nMinute);
        
        secondTextField.setDisplayFormat("%02d");
        secondTextField.setUnsigned(true);
        secondTextField.setVariable(simp.timestamp.nSecond);
        
        millisTextField.setUnsigned(true);
        millisTextField.setVariable(simp.timestamp.nMillisecond);
        
        weatherInterpolationSlider.setMinimum(0);
        weatherInterpolationSlider.setMaximum(1000);
        weatherInterpolationSlider.setScale(1000);
        weatherInterpolationSlider.setVariable(simp.fWeatherInterpolationValue);
        
        currentWeatherComboBox.setVariable(simp.nCurrentWeatherType);
        
        previousWeatherComboBox.setVariable(simp.nPreviousWeatherType);
        
        weatherNeverChangesCheckBox.setDeselectedValue(-1);
        weatherNeverChangesCheckBox.setVariable(simp.nForcedWeatherType);
        
//        detectPurpleNinesGlitch();
//        detectPedHostilityCheatsEnabled();
        
        isPageInitializing = false;
    }
    
    private void detectPurpleNinesGlitch()
    {
        boolean glitchPresent = false;
        Gang hoods = vars.aGangs.getValueAt(GameConstants.Gang.GANG07.getID());
        if (hoods.getPedModelOverrideFlagAsVariable().getValue().byteValue() != -1) {
            glitchPresent = true;
        }
        purpleNinesGlitchFixButton.setText(glitchPresent ? "Fix" : "Not present");
        purpleNinesGlitchFixButton.setEnabled(glitchPresent);
    }
    
    private void detectPedHostilityCheatsEnabled()
    {
        boolean enabled = false;
        for (GameConstants.PedType pedType : GameConstants.PedType.values()) {
            PedType savedPedType = vars.aPedTypes.getValueAt(pedType.getID());
            int threatValue = savedPedType.getThreat();
            if (pedType.getID() > -1 && pedType.getID() < 4) {
                continue;   // skip players
            }
            if (pedType.getID() > 6 && pedType.getID() < 16) {
                continue;   // skip gangs
            }
            if ((threatValue & GameConstants.PedType.PLAYER01.getThreatNumber()) != 0
                    || (threatValue & GameConstants.PedType.CIVMALE.getThreatNumber()) != 0
                    || (threatValue & GameConstants.PedType.CIVFEMALE.getThreatNumber()) != 0) {
                // if ped type is hostile toward player, civmale, or civfemale...
                // one of the cheats was most likely enabled
                enabled = true;
                break;
            }
        }
        reversePedHostilityCheatsButton.setText(enabled ? "Fix" : "Cheats not enabled");
        reversePedHostilityCheatsButton.setEnabled(enabled);
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
        reversePedHostilityCheatsLabel = new javax.swing.JLabel();
        reversePedHostilityCheatsButton = new javax.swing.JButton();
        weatherPanel = new javax.swing.JPanel();
        currentWeatherSelectionLabel = new javax.swing.JLabel();
        currentWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        previousWeatherSelectionLabel = new javax.swing.JLabel();
        previousWeatherComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        previousWeatherSliderLabel = new javax.swing.JLabel();
        currentWeatherSliderLabel = new javax.swing.JLabel();
        weatherInterpolationSlider = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableSlider();
        weatherNeverChangesCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox();

        saveTitlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Name"));

        saveTitleTextField.setText("a");

        javax.swing.GroupLayout saveTitlePanelLayout = new javax.swing.GroupLayout(saveTitlePanel);
        saveTitlePanel.setLayout(saveTitlePanelLayout);
        saveTitlePanelLayout.setHorizontalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveTitleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        saveTitlePanelLayout.setVerticalGroup(
            saveTitlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, saveTitlePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(14, Short.MAX_VALUE))
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

        purpleNinesGlitchFixButton.setText("Not present");
        purpleNinesGlitchFixButton.setToolTipText("Disable the Purple Nine's glitch, allowing you to progress on D-Ice's missions.");
        purpleNinesGlitchFixButton.setEnabled(false);
        purpleNinesGlitchFixButton.setMaximumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setMinimumSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.setPreferredSize(new java.awt.Dimension(127, 23));
        purpleNinesGlitchFixButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                purpleNinesGlitchFixButtonActionPerformed(evt);
            }
        });

        reversePedHostilityCheatsLabel.setText("Reverse ped hostility cheats:");

        reversePedHostilityCheatsButton.setText("Cheats not enabled");
        reversePedHostilityCheatsButton.setToolTipText("Reverse the effects of the \"peds riot\" and \"peds attack player\" cheats.");
        reversePedHostilityCheatsButton.setEnabled(false);
        reversePedHostilityCheatsButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                reversePedHostilityCheatsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout glitchAndBugFixesPanelLayout = new javax.swing.GroupLayout(glitchAndBugFixesPanel);
        glitchAndBugFixesPanel.setLayout(glitchAndBugFixesPanelLayout);
        glitchAndBugFixesPanelLayout.setHorizontalGroup(
            glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(glitchAndBugFixesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(reversePedHostilityCheatsLabel)
                    .addComponent(purpleNinesGlitchLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(glitchAndBugFixesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purpleNinesGlitchFixButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reversePedHostilityCheatsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(reversePedHostilityCheatsButton)
                    .addComponent(reversePedHostilityCheatsLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        purpleNinesGlitchFixButton.getAccessibleContext().setAccessibleName("Not Present");

        weatherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weather"));

        currentWeatherSelectionLabel.setText("Current weather:");

        previousWeatherSelectionLabel.setText("Previous weather:");

        previousWeatherSliderLabel.setText("WeatherA");

        currentWeatherSliderLabel.setText("WeatherB");

        weatherNeverChangesCheckBox.setText("Never changes");

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
                                .addComponent(currentWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addComponent(previousWeatherSelectionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previousWeatherComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(weatherPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(weatherNeverChangesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousWeatherSliderLabel)
                    .addComponent(currentWeatherSliderLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherInterpolationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                        .addComponent(weatherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(saveTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gameTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(glitchAndBugFixesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void purpleNinesGlitchFixButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_purpleNinesGlitchFixButtonActionPerformed
    {//GEN-HEADEREND:event_purpleNinesGlitchFixButtonActionPerformed
        if (vars == null) {
            return;
        }
        int hoodsID = GameConstants.Gang.GANG07.getID();
        Gang hoods = vars.aGangs.getValueAt(hoodsID);
        hoods.getPedModelOverrideFlagAsVariable().setValue(new GTAByte((byte)-1));
        purpleNinesGlitchFixButton.setText("Fixed");
        purpleNinesGlitchFixButton.setEnabled(false);
    }//GEN-LAST:event_purpleNinesGlitchFixButtonActionPerformed

    private void reversePedHostilityCheatsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_reversePedHostilityCheatsButtonActionPerformed
    {//GEN-HEADEREND:event_reversePedHostilityCheatsButtonActionPerformed
        if (vars == null) {
            return;
        }
        for (GameConstants.PedType pedType : GameConstants.PedType.values()) {
            PedType savedPedType = vars.aPedTypes.getValueAt(pedType.getID());
            int threatValue = savedPedType.getThreat();
            if (pedType.getID() > 6 && pedType.getID() < 16) {
                // If pedtype is a gang...
                threatValue &= ~pedType.getThreatNumber();  // make gang not hostile towards itself
                if (pedType == GameConstants.PedType.GANG04 || pedType == GameConstants.PedType.GANG07) {
                    threatValue &= ~GameConstants.PedType.PLAYER01.getThreatNumber();   // make Yakuza and Hoods not hostile towards the player
                }
            } else {
                // If pedtype is anything else...
                // make pedtype not hostile toward gangs or the player
                threatValue &= ~GameConstants.PedType.PLAYER01.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG01.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG02.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG03.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG04.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG05.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG06.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG07.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG08.getThreatNumber();
                threatValue &= ~GameConstants.PedType.GANG09.getThreatNumber();
            }
            // make pedtype not hostile toward anyone else
            threatValue &= ~GameConstants.PedType.PLAYER02.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PLAYER03.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PLAYER04.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CIVMALE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CIVFEMALE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.COP.getThreatNumber();
            threatValue &= ~GameConstants.PedType.EMERGENCY.getThreatNumber();
            threatValue &= ~GameConstants.PedType.FIREMAN.getThreatNumber();
            threatValue &= ~GameConstants.PedType.CRIMINAL.getThreatNumber();
            threatValue &= ~GameConstants.PedType.SPECIAL01.getThreatNumber();
            threatValue &= ~GameConstants.PedType.PROSTITUTE.getThreatNumber();
            threatValue &= ~GameConstants.PedType.SPECIAL02.getThreatNumber();
            savedPedType.setThreat(threatValue);
            vars.aPedTypes.setValueAt(pedType.getID(), savedPedType);
        }
        reversePedHostilityCheatsButton.setText("Fixed");
        reversePedHostilityCheatsButton.setEnabled(false);
    }//GEN-LAST:event_reversePedHostilityCheatsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox currentWeatherComboBox;
    private javax.swing.JLabel currentWeatherSelectionLabel;
    private javax.swing.JLabel currentWeatherSliderLabel;
    private javax.swing.JLabel dayLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox dayOfWeekComboBox;
    private javax.swing.JLabel dayOfWeekLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField dayTextField;
    private javax.swing.JLabel gameTimeHourLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeHourTextField;
    private javax.swing.JLabel gameTimeMinuteLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField gameTimeMinuteTextField;
    private javax.swing.JPanel gameTimePanel;
    private javax.swing.JPanel glitchAndBugFixesPanel;
    private javax.swing.JLabel globalTimerLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField globalTimerTextField;
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
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox previousWeatherComboBox;
    private javax.swing.JLabel previousWeatherSelectionLabel;
    private javax.swing.JLabel previousWeatherSliderLabel;
    private javax.swing.JButton purpleNinesGlitchFixButton;
    private javax.swing.JLabel purpleNinesGlitchLabel;
    private javax.swing.JButton reversePedHostilityCheatsButton;
    private javax.swing.JLabel reversePedHostilityCheatsLabel;
    private javax.swing.JPanel saveTitlePanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.StringVariableTextField saveTitleTextField;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel secondLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField secondTextField;
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