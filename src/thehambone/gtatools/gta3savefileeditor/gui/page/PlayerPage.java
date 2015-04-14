package thehambone.gtatools.gta3savefileeditor.gui.page;

import javax.swing.DefaultComboBoxModel;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAFloat;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.CPed;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.WeaponSlot;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;
import thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.WeaponListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.io.IO;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 30, 2015
 */
public class PlayerPage extends Page
{
    private Variable<WeaponSlot> weaponSlots;
    
    public PlayerPage()
    {
        super("Player", Visibility.VISIBLE_WHEN_GAMESAVE_LOADED_ONLY);
        initComponents();
        addNotifiersToComponents(mainPanel, weaponSlotComboBox);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void loadPage()
    {
        IO.debugf("Loading page: %s...\n", getTitle());
        
        isPageInitializing = true;
        vars = SaveFile.getCurrentlyLoadedFile().getVariables();
        
        CPed cPed = vars.aPlayerPed.getValue().getCPed();
        Variable<GTAFloat> playerHealth = cPed.getHealthAsVariable();
        Variable<GTAFloat> playerArmor = cPed.getArmorAsVariable();
        Variable[] playerCoords = { cPed.getPlayerXAsVariable(), cPed.getPlayerYAsVariable(), cPed.getPlayerZAsVariable() };
        weaponSlots = cPed.getWeaponSlotsAsVariable();
        
        DefaultComboBoxModel<GameConstants.Weapon> weaponComboBoxModel = new DefaultComboBoxModel<>();
        for (GameConstants.Weapon w : GameConstants.Weapon.values()) {
            if (w == GameConstants.Weapon.FIST) {
                continue;
            }
            weaponComboBoxModel.addElement(w);
        }
        weaponSlotComboBox.setModel(weaponComboBoxModel);
        weaponSlotComboBox.setRenderer(new WeaponListCellRenderer());
        
        healthTextField.setInputType(VariableValueTextField.InputType.DECIMAL);
        healthTextField.setVariable(playerHealth);
        armorTextField.setInputType(VariableValueTextField.InputType.DECIMAL);
        armorTextField.setVariable(playerArmor);
        moneyTextField.setInputType(VariableValueTextField.InputType.INTEGER);
        moneyTextField.setVariable(vars.iMoney2);
        moneyTextField.addVariableToUpdate(vars.iMoney);
        
        freeHealthcareCheckBox.setVariable(vars.bFreeHealthcare);
        getOutOfJailFreeCheckBox.setVariable(vars.bGetOutOfJailFree);
        infiniteSprintCheckBox.setVariable(vars.bInfiniteSprint);
        freeBombsCheckBox.setVariable(vars.bFreeBombs);
        freeRespraysCheckBox.setVariable(vars.bFreeResprays);
        
        weaponAmmoTextField.setInputType(VariableValueTextField.InputType.INTEGER);
        
        xTextField.setInputType(VariableValueTextField.InputType.DECIMAL);
        yTextField.setInputType(VariableValueTextField.InputType.DECIMAL);
        zTextField.setInputType(VariableValueTextField.InputType.DECIMAL);
        xTextField.setVariable(playerCoords[0]);
        yTextField.setVariable(playerCoords[1]);
        zTextField.setVariable(playerCoords[2]);
        
        // Until I can figure out how to prevent the game from loading player at safehouse coords
        xTextField.setEnabled(false);
        yTextField.setEnabled(false);
        zTextField.setEnabled(false);
        
        maxChaosLevelTextField.setInputType(VariableValueTextField.InputType.INTEGER);
        maxWantedLevelTextField.setInputType(VariableValueTextField.InputType.INTEGER);
        maxChaosLevelTextField.setVariable(vars.aPlayerPed.getValue().getMaxChaosLevelAsVariable());
        maxWantedLevelTextField.setVariable(vars.aPlayerPed.getValue().getMaxWantedLevelAsVariable());
        
        weaponSlotComboBoxActionPerformed(null);
        
        isPageInitializing = false;
    }
    
    private void updateWeaponInInventoryCheckBox(WeaponSlot ws)
    {
        boolean isEmpty = ws.getWeaponID() == 0;
        jCheckBox1.setSelected(!isEmpty);
    }
    
    private void updateWeaponAmmoTextField(int weaponSlotID, WeaponSlot ws)
    {
        boolean isBat = weaponSlotID == GameConstants.Weapon.BAT.getID();
        boolean isEmpty = ws.getWeaponID() == 0;
        weaponAmmoTextField.updateVariable();
        weaponAmmoTextField.setEnabled(!isBat);
        if (isBat) {
            weaponAmmoTextField.setVariable(null);
            weaponAmmoTextField.setText("0");
            return;
        }
        if (!isEmpty) {
            weaponAmmoTextField.setVariable(ws.getBulletsTotalAsVariable());
        } else {
            weaponAmmoTextField.setVariable(null);
            weaponAmmoTextField.setText("0");
        }
        weaponAmmoTextField.setEnabled(!isEmpty);
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
        healthTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        armorLabel = new javax.swing.JLabel();
        armorTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        moneyLabel = new javax.swing.JLabel();
        moneyTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        playerPerksPanel = new javax.swing.JPanel();
        freeHealthcareCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        getOutOfJailFreeCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        infiniteSprintCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        freeBombsCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        freeRespraysCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        weaponsPanel = new javax.swing.JPanel();
        weaponSlotLabel = new javax.swing.JLabel();
        weaponSlotComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        weaponAmmoLabel = new javax.swing.JLabel();
        weaponAmmoTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        playerCoordinatesPanel = new javax.swing.JPanel();
        xLabel = new javax.swing.JLabel();
        xTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        yLabel = new javax.swing.JLabel();
        yTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        zLabel = new javax.swing.JLabel();
        zTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        otherPanel = new javax.swing.JPanel();
        maxChaosLevelLabel = new javax.swing.JLabel();
        maxChaosLevelTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();
        maxWantedLevelLabel = new javax.swing.JLabel();
        maxWantedLevelTextField = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField();

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));

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
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(moneyLabel)
                    .addComponent(armorLabel)
                    .addComponent(healthLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(moneyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(armorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(healthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        freeHealthcareCheckBox.setText("Free healthcare");

        getOutOfJailFreeCheckBox.setText("Get out of jail free");

        infiniteSprintCheckBox.setText("Infinite sprint");

        freeBombsCheckBox.setText("Free bombs");

        freeRespraysCheckBox.setText("Free resprays");

        javax.swing.GroupLayout playerPerksPanelLayout = new javax.swing.GroupLayout(playerPerksPanel);
        playerPerksPanel.setLayout(playerPerksPanelLayout);
        playerPerksPanelLayout.setHorizontalGroup(
            playerPerksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPerksPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerPerksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(freeHealthcareCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(freeHealthcareCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(getOutOfJailFreeCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infiniteSprintCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(freeBombsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(freeRespraysCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        weaponsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weapons"));
        weaponsPanel.setToolTipText("");

        weaponSlotLabel.setText("Slot:");

        weaponSlotComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bat" }));
        weaponSlotComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                weaponSlotComboBoxActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot Properties"));

        weaponAmmoLabel.setText("Ammo:");

        weaponAmmoTextField.setText("0");

        jCheckBox1.setText("In inventory");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(weaponAmmoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponAmmoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(weaponsPanelLayout.createSequentialGroup()
                        .addComponent(weaponSlotLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weaponSlotComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        weaponsPanelLayout.setVerticalGroup(
            weaponsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weaponsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weaponSlotLabel)
                    .addComponent(weaponSlotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        playerCoordinatesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Player Coordinates"));

        xLabel.setText("X:");

        xTextField.setText("0.0");

        yLabel.setText("Y:");

        yTextField.setText("0.0");

        zLabel.setText("Z:");

        zTextField.setText("0.0");

        javax.swing.GroupLayout playerCoordinatesPanelLayout = new javax.swing.GroupLayout(playerCoordinatesPanel);
        playerCoordinatesPanel.setLayout(playerCoordinatesPanelLayout);
        playerCoordinatesPanelLayout.setHorizontalGroup(
            playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerCoordinatesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(yLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(zLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playerCoordinatesPanelLayout.setVerticalGroup(
            playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerCoordinatesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerCoordinatesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabel)
                    .addComponent(yLabel)
                    .addComponent(zLabel)
                    .addComponent(xTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        otherPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Other"));

        maxChaosLevelLabel.setText("Max chaos level:");

        maxChaosLevelTextField.setText("0");

        maxWantedLevelLabel.setText("Max wanted level:");

        maxWantedLevelTextField.setText("0");

        javax.swing.GroupLayout otherPanelLayout = new javax.swing.GroupLayout(otherPanel);
        otherPanel.setLayout(otherPanelLayout);
        otherPanelLayout.setHorizontalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(maxWantedLevelLabel)
                    .addComponent(maxChaosLevelLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maxChaosLevelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(maxWantedLevelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        otherPanelLayout.setVerticalGroup(
            otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxChaosLevelLabel)
                    .addComponent(maxChaosLevelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxWantedLevelLabel)
                    .addComponent(maxWantedLevelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(generalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(playerPerksPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(weaponsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(otherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playerCoordinatesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void weaponSlotComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_weaponSlotComboBoxActionPerformed
    {//GEN-HEADEREND:event_weaponSlotComboBoxActionPerformed
        GameConstants.Weapon w = (GameConstants.Weapon)weaponSlotComboBox.getSelectedItem();
        WeaponSlot ws = weaponSlots.getValueAt(w.getID());
        updateWeaponInInventoryCheckBox(ws);
        updateWeaponAmmoTextField(w.getID(), ws);
    }//GEN-LAST:event_weaponSlotComboBoxActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBox1ActionPerformed
    {//GEN-HEADEREND:event_jCheckBox1ActionPerformed
        GameConstants.Weapon w = (GameConstants.Weapon)weaponSlotComboBox.getSelectedItem();
        WeaponSlot ws = weaponSlots.getValueAt(w.getID());
        ws.setWeaponID(jCheckBox1.isSelected() ? w.getID() : 0);
        updateWeaponAmmoTextField(w.getID(), ws);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel armorLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField armorTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox freeBombsCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox freeHealthcareCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox freeRespraysCheckBox;
    private javax.swing.JPanel generalPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox getOutOfJailFreeCheckBox;
    private javax.swing.JLabel healthLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField healthTextField;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox infiniteSprintCheckBox;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel maxChaosLevelLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField maxChaosLevelTextField;
    private javax.swing.JLabel maxWantedLevelLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField maxWantedLevelTextField;
    private javax.swing.JLabel moneyLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField moneyTextField;
    private javax.swing.JPanel otherPanel;
    private javax.swing.JPanel playerCoordinatesPanel;
    private javax.swing.JPanel playerPerksPanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel weaponAmmoLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField weaponAmmoTextField;
    private javax.swing.JComboBox weaponSlotComboBox;
    private javax.swing.JLabel weaponSlotLabel;
    private javax.swing.JPanel weaponsPanel;
    private javax.swing.JLabel xLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField xTextField;
    private javax.swing.JLabel yLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField yTextField;
    private javax.swing.JLabel zLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueTextField zTextField;
    // End of variables declaration//GEN-END:variables
}