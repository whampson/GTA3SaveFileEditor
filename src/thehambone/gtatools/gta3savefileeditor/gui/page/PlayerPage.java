package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.CPed;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;
import thehambone.gtatools.gta3savefileeditor.gui.component.old.VariableValueTextField;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.WeaponListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockPlayerInfo;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.BlockPlayerPeds;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.PlayerPed;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.WeaponSlot;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarInt;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 */
public class PlayerPage extends Page
{
    private VarArray<WeaponSlot> weaponSlots;
    
    private PlayerPed playerped;
    
    public PlayerPage()
    {
        super("Player", Visibility.VISIBLE_WHEN_FILE_LOADED_ONLY);
        initComponents();
        addChangeNotifiersToComponents(mainPanel, weaponSlotComboBox);
        
        maxWantedLevelComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateMaxChaosLevel(maxWantedLevelComboBox
                        .getSelectedIndex() + 1);
            }
        });
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getPageTitle());
        
        isPageInitializing = true;
        
        BlockPlayerInfo player = SaveFileNew.getCurrentSaveFile().playerInfo;
        playerped = SaveFileNew.getCurrentSaveFile()
                .playerPeds.aPlayerPed.getElementAt(0);
        
        healthTextField.setVariable(playerped.cPlayerPed.fHealth);
        armorTextField.setVariable(playerped.cPlayerPed.fArmor);
        moneyTextField.setVariable(player.nMoney, player.nMoneyOnScreen);
        
        infiniteSprintCheckBox.setVariable(player.bPlayerNeverGetsTired);
        getOutOfJailFreeCheckBox.setVariable(player.bPlayerGetOutOfJailFree);
        freeHealthCareCheckBox.setVariable(player.bPlayerFreeHealthCare);
        
        boolean wantedLevelEnabled = playerped.nMaxChaosLevel.getValue() >= 40;
        setWantedLevelEnabled(wantedLevelEnabled);
        enableWantedLevelCheckBox.setSelected(wantedLevelEnabled);
        
        maxWantedLevelComboBox.setValueOffset(1);
        maxWantedLevelComboBox.setVariable(playerped.nMaxWantedLevel);
        
        playerXTextField.setVariable(playerped.cPlayerPed.vPosition.fX);
        playerYTextField.setVariable(playerped.cPlayerPed.vPosition.fY);
        playerZTextField.setVariable(playerped.cPlayerPed.vPosition.fZ);
        
        weaponSlots = playerped.cPlayerPed.aWeaponSlot;
        DefaultComboBoxModel<GameConstants.Weapon> weaponComboBoxModel
                = new DefaultComboBoxModel<>();
        for (GameConstants.Weapon w : GameConstants.Weapon.values()) {
            if (w == GameConstants.Weapon.FIST) {
                continue;
            }
            weaponComboBoxModel.addElement(w);
        }
        weaponSlotComboBox.setModel(weaponComboBoxModel);
        weaponSlotComboBox.setRenderer(new WeaponListCellRenderer());
        
        weaponSlotComboBoxActionPerformed(null);
        
        isPageInitializing = false;
    }
    
    private void updateWeaponInInventoryCheckBox(WeaponSlot ws)
    {
        boolean isEmpty = ws.nWeaponID.getValue() == 0;
        weaponInInventoryCheckBox.setSelected(!isEmpty);
    }
    
    private void updateWeaponAmmoTextField(int weaponSlotID, WeaponSlot ws)
    {
        boolean isBat = weaponSlotID == GameConstants.Weapon.BAT.getID();
        boolean isEmpty = ws.nWeaponID.getValue() == 0;
//        weaponAmmoTextField.updateVariable();
        weaponAmmoTextField.setEnabled(!isBat);
        if (isBat) {
            weaponAmmoTextField.setVariable(null);
            weaponAmmoTextField.setText("0");
            return;
        }
        if (!isEmpty) {
            weaponAmmoTextField.setVariable(ws.nWeaponAmmo);
        } else {
            weaponAmmoTextField.setVariable(null);
            weaponAmmoTextField.setText("0");
        }
        weaponAmmoTextField.setEnabled(!isEmpty);
    }
    
    private void setWantedLevelEnabled(boolean isEnabled)
    {
        maxWantedLevelLabel.setEnabled(isEnabled);
        maxWantedLevelComboBox.setEnabled(isEnabled);
        
        if (isEnabled) {
            int maxWantedLevel = playerped.nMaxWantedLevel.getValue();            
            
            maxWantedLevelComboBox.setSelectedIndex(maxWantedLevel - 1);
            updateMaxChaosLevel(maxWantedLevel);
        } else {
            updateMaxChaosLevel(0);
        }
    }
    
    private void updateMaxChaosLevel(int maxWantedLevel)
    {
        int maxChaosLevel = 0;
        switch (maxWantedLevel) {
                case 1:
                    maxChaosLevel = 120;
                    break;
                case 2:
                    maxChaosLevel = 300;
                    break;
                case 3:
                    maxChaosLevel = 600;
                    break;
                case 4:
                    maxChaosLevel = 1200;
                    break;
                case 5:
                    maxChaosLevel = 2400;
                    break;
                case 6:
                    maxChaosLevel = 4800;
                    break;
            }
            playerped.nMaxChaosLevel.setValue(maxChaosLevel);
            Logger.debug("Variable updated: " + playerped.nMaxChaosLevel);
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
        generalPanel = new javax.swing.JPanel();
        healthLabel = new javax.swing.JLabel();
        healthTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField();
        armorLabel = new javax.swing.JLabel();
        armorTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField();
        moneyLabel = new javax.swing.JLabel();
        moneyTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        playerPerksPanel = new javax.swing.JPanel();
        infiniteSprintCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox();
        getOutOfJailFreeCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox();
        freeHealthCareCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox();
        freeBombsCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox();
        freeRespraysCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox();
        weaponsPanel = new javax.swing.JPanel();
        weaponSlotComboBox = new javax.swing.JComboBox<>();
        weaponPropertiesPanel = new javax.swing.JPanel();
        weaponInInventoryCheckBox = new javax.swing.JCheckBox();
        weaponAmmoLabel = new javax.swing.JLabel();
        weaponAmmoTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField();
        playerCoordinatesPanel = new javax.swing.JPanel();
        playerXLabel = new javax.swing.JLabel();
        playerXTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField();
        playerYLabel = new javax.swing.JLabel();
        playerYTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField();
        playerZLabel = new javax.swing.JLabel();
        playerZTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField();
        otherPanel = new javax.swing.JPanel();
        enableWantedLevelCheckBox = new javax.swing.JCheckBox();
        maxWantedLevelLabel = new javax.swing.JLabel();
        maxWantedLevelComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Player Properties"));

        healthLabel.setText("Health:");

        healthTextField.setText("0.0");

        armorLabel.setText("Armor:");

        armorTextField.setText("0.0");

        moneyLabel.setText("Money:");

        moneyTextField.setText("0");

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(moneyLabel)
                    .addComponent(armorLabel)
                    .addComponent(healthLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(healthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(armorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(moneyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(healthLabel)
                    .addComponent(healthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(armorLabel)
                    .addComponent(armorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moneyLabel)
                    .addComponent(moneyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        playerPerksPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Player Perks"));

        infiniteSprintCheckBox.setText("Infinite sprint");

        getOutOfJailFreeCheckBox.setText("Get out of jail free");

        freeHealthCareCheckBox.setText("Free healthcare");

        freeBombsCheckBox.setText("Free bombs");

        freeRespraysCheckBox.setText("Free resprays");

        javax.swing.GroupLayout playerPerksPanelLayout = new javax.swing.GroupLayout(playerPerksPanel);
        playerPerksPanel.setLayout(playerPerksPanelLayout);
        playerPerksPanelLayout.setHorizontalGroup(
            playerPerksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPerksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerPerksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(freeHealthCareCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(getOutOfJailFreeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infiniteSprintCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freeBombsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freeRespraysCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playerPerksPanelLayout.setVerticalGroup(
            playerPerksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPerksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infiniteSprintCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(getOutOfJailFreeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freeHealthCareCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freeBombsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freeRespraysCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        weaponsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weapons"));
        weaponsPanel.setToolTipText("");

        weaponSlotComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                weaponSlotComboBoxActionPerformed(evt);
            }
        });

        weaponPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weapon Properties"));

        weaponInInventoryCheckBox.setText("In inventory");
        weaponInInventoryCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                weaponInInventoryCheckBoxActionPerformed(evt);
            }
        });

        weaponAmmoLabel.setText("Ammo:");

        weaponAmmoTextField.setText("0");
        weaponAmmoTextField.setToolTipText("");

        javax.swing.GroupLayout weaponPropertiesPanelLayout = new javax.swing.GroupLayout(weaponPropertiesPanel);
        weaponPropertiesPanel.setLayout(weaponPropertiesPanelLayout);
        weaponPropertiesPanelLayout.setHorizontalGroup(
            weaponPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weaponPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(weaponInInventoryCheckBox)
                    .addGroup(weaponPropertiesPanelLayout.createSequentialGroup()
                        .addComponent(weaponAmmoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponAmmoTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        weaponPropertiesPanelLayout.setVerticalGroup(
            weaponPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(weaponInInventoryCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(weaponPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weaponAmmoLabel)
                    .addComponent(weaponAmmoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout weaponsPanelLayout = new javax.swing.GroupLayout(weaponsPanel);
        weaponsPanel.setLayout(weaponsPanelLayout);
        weaponsPanelLayout.setHorizontalGroup(
            weaponsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weaponsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weaponPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(weaponSlotComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        weaponsPanelLayout.setVerticalGroup(
            weaponsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponsPanelLayout.createSequentialGroup()
                .addComponent(weaponSlotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weaponPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        playerCoordinatesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Player Coordinates"));

        playerXLabel.setText("X:");

        playerXTextField.setEditable(false);
        playerXTextField.setText("0.0");
        playerXTextField.setToolTipText("Unavailable for editing at this time.");

        playerYLabel.setText("Y:");

        playerYTextField.setEditable(false);
        playerYTextField.setText("0.0");
        playerYTextField.setToolTipText("Unavailable for editing at this time.");

        playerZLabel.setText("Z:");

        playerZTextField.setEditable(false);
        playerZTextField.setText("0.0");
        playerZTextField.setToolTipText("Unavailable for editing at this time.");

        javax.swing.GroupLayout playerCoordinatesPanelLayout = new javax.swing.GroupLayout(playerCoordinatesPanel);
        playerCoordinatesPanel.setLayout(playerCoordinatesPanelLayout);
        playerCoordinatesPanelLayout.setHorizontalGroup(
            playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerCoordinatesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(playerXLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(playerYLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(playerZLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerZTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playerCoordinatesPanelLayout.setVerticalGroup(
            playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerCoordinatesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerXLabel)
                    .addComponent(playerYLabel)
                    .addComponent(playerZLabel)
                    .addComponent(playerXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerZTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        otherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Wanted Level"));

        enableWantedLevelCheckBox.setText("Enable wanted level");
        enableWantedLevelCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                enableWantedLevelCheckBoxActionPerformed(evt);
            }
        });

        maxWantedLevelLabel.setText("Max wanted level:");

        maxWantedLevelComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 star", "2 stars", "3 stars", "4 stars", "5 stars", "6 stars" }));

        javax.swing.GroupLayout otherPanelLayout = new javax.swing.GroupLayout(otherPanel);
        otherPanel.setLayout(otherPanelLayout);
        otherPanelLayout.setHorizontalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enableWantedLevelCheckBox)
                    .addGroup(otherPanelLayout.createSequentialGroup()
                        .addComponent(maxWantedLevelLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maxWantedLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        otherPanelLayout.setVerticalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enableWantedLevelCheckBox)
                .addGap(7, 7, 7)
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxWantedLevelLabel)
                    .addComponent(maxWantedLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(playerCoordinatesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(generalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerPerksPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(weaponsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playerPerksPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerCoordinatesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(otherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void weaponInInventoryCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_weaponInInventoryCheckBoxActionPerformed
    {//GEN-HEADEREND:event_weaponInInventoryCheckBoxActionPerformed
        GameConstants.Weapon w = (GameConstants.Weapon)weaponSlotComboBox.getSelectedItem();
        WeaponSlot ws = weaponSlots.getElementAt(w.getID());
        ws.nWeaponID.setValue(weaponInInventoryCheckBox.isSelected() ? w.getID() : 0);
        updateWeaponAmmoTextField(w.getID(), ws);
    }//GEN-LAST:event_weaponInInventoryCheckBoxActionPerformed

    private void enableWantedLevelCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_enableWantedLevelCheckBoxActionPerformed
    {//GEN-HEADEREND:event_enableWantedLevelCheckBoxActionPerformed
        setWantedLevelEnabled(enableWantedLevelCheckBox.isSelected());
    }//GEN-LAST:event_enableWantedLevelCheckBoxActionPerformed

    private void weaponSlotComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_weaponSlotComboBoxActionPerformed
    {//GEN-HEADEREND:event_weaponSlotComboBoxActionPerformed
        GameConstants.Weapon w = (GameConstants.Weapon)weaponSlotComboBox.getSelectedItem();
        WeaponSlot ws = weaponSlots.getElementAt(w.getID());
        updateWeaponInInventoryCheckBox(ws);
        updateWeaponAmmoTextField(w.getID(), ws);
    }//GEN-LAST:event_weaponSlotComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel armorLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField armorTextField;
    private javax.swing.JCheckBox enableWantedLevelCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox freeBombsCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox freeHealthCareCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox freeRespraysCheckBox;
    private javax.swing.JPanel generalPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox getOutOfJailFreeCheckBox;
    private javax.swing.JLabel healthLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField healthTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BooleanVariableCheckBox infiniteSprintCheckBox;
    private javax.swing.JPanel mainPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox maxWantedLevelComboBox;
    private javax.swing.JLabel maxWantedLevelLabel;
    private javax.swing.JLabel moneyLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField moneyTextField;
    private javax.swing.JPanel otherPanel;
    private javax.swing.JPanel playerCoordinatesPanel;
    private javax.swing.JPanel playerPerksPanel;
    private javax.swing.JLabel playerXLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField playerXTextField;
    private javax.swing.JLabel playerYLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField playerYTextField;
    private javax.swing.JLabel playerZLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.FloatVariableTextField playerZTextField;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel weaponAmmoLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableTextField weaponAmmoTextField;
    private javax.swing.JCheckBox weaponInInventoryCheckBox;
    private javax.swing.JPanel weaponPropertiesPanel;
    private javax.swing.JComboBox<GameConstants.Weapon> weaponSlotComboBox;
    private javax.swing.JPanel weaponsPanel;
    // End of variables declaration//GEN-END:variables
}